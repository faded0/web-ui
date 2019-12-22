package com.atguigu.scw.user.utils;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class SmsTemplate {
	Logger log = LoggerFactory.getLogger(getClass());
	
	@Value("${sms.host}")
	private String host;
	
	@Value("${sms.path}")
	private String path; // 路径

	

	@Value("${sms.method:POST}")  //表示没有匹配默认值为
	private String method; // 请求方法

	@Value("${sms.appcode}")
	private String appcode; // appcode

	// 发送验证码

	public  String sendCode(Map<String, String> querys) {
		
		HttpResponse response = null;
        //请求头
		Map<String, String> headers = new HashMap<String, String>();

		// 授权头
		headers.put("Authorization", "APPCODE " + appcode);
		//POST请求身体部分
		Map<String, String> body = new HashMap<String, String>();
		try {
			if (method.equalsIgnoreCase("get")) {  //如果是Get请求
				response = HttpUtils.doGet(host, path, method, headers, querys);
			} else {  //如果是POST请求
				response = HttpUtils.doPost(host, path, method, headers, querys, body);
			}
			String string = EntityUtils.toString(response.getEntity());   //返回信息
			log.info("短信发送完成；响应数据是：{}", string);
			
			return string;          //信息返回成功
			// 获取返回的响应数据
		} catch (Exception e) {
			log.error("短信发送失败；发送参数是：{}", querys);   //信息发送失败
			return "fail";
		}

	}

	
	
	
}
