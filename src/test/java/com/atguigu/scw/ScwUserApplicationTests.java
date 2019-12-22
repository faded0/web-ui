package com.atguigu.scw;

import java.util.HashMap;
import java.util.Map;


import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import com.atguigu.scw.user.utils.HttpUtils;
import com.atguigu.scw.user.utils.SmsTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ScwUserApplicationTests {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	//redis测试
	@Autowired
	RedisTemplate<Object, Object> redisTemplate;
	
	@Autowired
	StringRedisTemplate  stringRedisTemplate;
	//短信验证
	@Autowired
	SmsTemplate  smsTemplate;

	@Test
	public void contextLoads() {
		System.out.println(redisTemplate);
		redisTemplate.opsForValue().set("k1", "v1");
		System.out.println(redisTemplate.opsForValue().get("k1"));
		stringRedisTemplate.opsForValue().set("phoonue:code", "周三");
		logger.debug("操作成功");
	}
	
	@Test
	public void SendVerification() {
		Map<String, String> querys = new HashMap<String, String>();
	    querys.put("mobile", "18652865027");
	    querys.put("param", "code:33333");
	    querys.put("tpl_id", "TP1711063");
	    String sendCode = smsTemplate.sendCode(querys);
	    System.out.println(sendCode);
	}
	

}
