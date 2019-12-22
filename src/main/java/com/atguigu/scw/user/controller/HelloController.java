package com.atguigu.scw.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
//使用swagger注解说明

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(tags="这是一个swagger 测试")  //描述当前类用来做什么
@RestController
public class HelloController {
	
	@ApiOperation(value="这是一个hello的Get请求方法")    //用来描述方法的
	//用来方法参数的,参数的名称，参数额含义,以及参数是否是必须，多个参数使用逗号隔开
	@ApiImplicitParams(value={@ApiImplicitParam(name="name",value="姓名",required=false ),@ApiImplicitParam(name="id",value="学号",required=true)}) 
	@GetMapping("/hello")
	public String hello(String name,Integer id) {
		return "ok"+name;
	}
	
	//测试方法二
	@ApiOperation(value="这是一个POST请求")
	@ApiImplicitParams(value={@ApiImplicitParam(name="address",value="地址",required=false)})
	@PostMapping("hello2")
	public String helloPOST(String address) {
		return address;
	}

}
