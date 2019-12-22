package com.atguigu.scw.user.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.atguigu.scw.common.bean.AppResponse;
import com.atguigu.scw.common.utils.ScwAppUtils;
import com.atguigu.scw.user.consts.UserAppConsts;
import com.atguigu.scw.user.exception.UserException;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.utils.SmsTemplate;
import com.atguigu.scw.user.vo.UserAddressVo;
import com.atguigu.scw.user.vo.UserRegisterVo;
import com.atguigu.scw.user.vo.UserRespVo;
import com.google.gson.Gson;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

@Api(tags="用户登录/注册模块")       //描述当前类
@RequestMapping(value="/user")
@Slf4j
@RestController 
public class UserLoginController {
	
	//Service层
	@Autowired
	UserService userService;
	
	//验证码
	@Autowired
	SmsTemplate  smsTemplate;
	
	//Redis
	@Autowired
	StringRedisTemplate strRedisTemplate;
	
	//4、查询用户的地址
	@ApiOperation(value="查询用户地址")
	@GetMapping(value="/address")
	public AppResponse<List<UserAddressVo>> getAddress(@RequestParam("accessToken")String accessToken){
		//验证是否登录
		UserRespVo userResp = ScwAppUtils.toFromObject(strRedisTemplate, UserRespVo.class, accessToken);
		if(userResp==null) {
			return AppResponse.fail("登录超时",null);
		}
		//如果已经登录，查看当前登录的ID
		Integer userid = userResp.getId();
		//根据用户ID去查询地址
		List<UserAddressVo> userAddressList=userService.getAddressAll(userid);
		return AppResponse.ok(userAddressList);
	}
	
	//3、用户登录
	
	@ApiOperation(value="用户登录方法")
	@PostMapping(value="/doLogin")
	public AppResponse<Object> dologin(String loginacct,String userpswd){
		/*
		 * 1、验证用户名和密码是否正确
		 * 2、登录成功生成令牌
		 */
		try {
			log.info("开始执行。。。");
			log.info("username,userpswd值:{},{}",loginacct,userpswd);
			UserRespVo vo=userService.login(loginacct,userpswd);
			log.info("值:{}",vo);
			String token=UUID.randomUUID().toString().replace("-", "");
			token=UserAppConsts.LOGIN_SUCCESS_PRIFIX+token;
            vo.setAccessToken(token);
            //将VO对象转换成为GSON
            Gson gson=new Gson();
            String voStr=gson.toJson(vo);
            //将Vo对象存在Redis中
            strRedisTemplate.opsForValue().set(token, voStr);
            return AppResponse.ok(vo);
		} catch (UserException e) {
			
			return AppResponse.fail("登录失败", e.getMessage());
		}
	
		
	}
	
	//2、用户注册
	@ApiOperation(value="用户注册方法")
	@PostMapping(value="/doRegister")
	public AppResponse<String> register(UserRegisterVo userRegisterVo){
		/*
		 * 用户注册的步骤：
		 * 1、如Redis中校验验证码是否存在，如果存在就对比
		 * 2、将Vo类转换成为member类
		 * 3、调用Service层（验证用户名是否存在，存在该用户已被注册）
		 * 4、注册成功，删除Redis中的验证码
		 */
		
		//1、如Redis中校验验证码是否存在，如果存在就对比
		String codeKey=UserAppConsts.PHONENUM_CODE_PRIFIX+userRegisterVo.getLoginacct()+UserAppConsts.PHONENUM_CODE_SUFFIX;
		log.debug("验证码信息:{}",codeKey);
		String codeStr = strRedisTemplate.opsForValue().get(codeKey);
		if(StringUtils.isEmpty(codeStr)) { //说明不存在
			return AppResponse.fail("验证码过期", "error");
		}
		if(!codeStr.equalsIgnoreCase(userRegisterVo.getCode())) {
			return AppResponse.fail("验证码错误", "error");
		}
		
		//2、将Vo类转换成为member类
		try {
			userService.doregister(userRegisterVo);
			//3、注册成功，删除Redis中验证码
			strRedisTemplate.delete(codeKey);
			
			return AppResponse.ok("注册成功");
		} catch (UserException e) {
			return AppResponse.fail("注册失败",e.getMessage());
		}
		
	}
	
	
	//发送验证码
	@ApiOperation("获取注册的验证码")
	@ApiImplicitParams({@ApiImplicitParam(name="phoneNo",value="手机号码",required=true)})
	@PostMapping(value="/sendCode")
	public AppResponse<String> sendCode(String phoneNo){
		
		/*
		 * 发送验证码的流程分析：
		 * 验证手机号码格式是否构造正确
		 * 1、查询Redis内存中对应得手机号的验证码还未过期，如果还未过期提示用户
		 * 2、查询该手机的验证码发送验证码的次数，如果超过三次，则不允许再发送验证码
		 * 3、如果以上条件都成立，就发送验证码
		 *   将验证码保存至内存中，并设置过期时间
		 */
		//1、验证手机格式是否正确
		boolean isphone = ScwAppUtils.isphone(phoneNo);
		if(!isphone) {
			return  AppResponse.fail("手机号码格式不正确", phoneNo);
		}
		// 存储一个手机号码获取的键
		String countkey=UserAppConsts.PHONENUM_COUNT_PRIFIX+phoneNo+UserAppConsts.PHONENUM_CODE_SUFFIX;
		//2查询Redis存在的该手机的验证码是否超过了范围了
		
		String countStr = strRedisTemplate.opsForValue().get(phoneNo);
		int count=0;
		if(!StringUtils.isEmpty(countStr)) {
			count=Integer.parseInt(countStr);
		}
		if(count>=3) {
			return AppResponse.fail("获取验证码的次数已经用尽", "请求失败");
		}
		//3、查看该手机的验证码是否已经过期
		String codeKey=UserAppConsts.PHONENUM_CODE_PRIFIX+phoneNo+UserAppConsts.PHONENUM_CODE_SUFFIX;
		String code = strRedisTemplate.opsForValue().get(codeKey);
		if(!StringUtils.isEmpty(code)) {
			 return AppResponse.fail("请不要频繁发送验证码", "请求失败");
		}
		//4、生成验证码发送短信
		code = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
		Map<String, String> querys = new HashMap<String, String>();
	    querys.put("mobile", phoneNo);
	    querys.put("param", "code:"+code);
	    querys.put("tpl_id", "TP1711063");
	    String sendCode = smsTemplate.sendCode(querys);
	    
	    //将该验证码信息保存到Redis中
	    //设置验证码的过去时间为60秒
	    strRedisTemplate.opsForValue().set(codeKey, code, 60, TimeUnit.SECONDS);
	    
	    //更新验证码的过去次数
	    strRedisTemplate.opsForValue().increment(countkey);
	    
	    //响应结果
	    return AppResponse.ok("短信发送成功");
		
	}

}
