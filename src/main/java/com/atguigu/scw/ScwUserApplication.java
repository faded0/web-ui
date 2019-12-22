package com.atguigu.scw;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@MapperScan(basePackages="com.atguigu.scw.user.mapper")    //加载Mapper接口包
@EnableHystrix                //开启熔断服务
@EnableFeignClients           //开启远程调用接口
@EnableDiscoveryClient        //开启eureka客户端服务
@SpringBootApplication
public class ScwUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(ScwUserApplication.class, args);
	}

}
