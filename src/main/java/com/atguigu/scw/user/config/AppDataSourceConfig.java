package com.atguigu.scw.user.config;

import java.sql.SQLException;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class AppDataSourceConfig {
	
	 /*@ConfigurationProperties(prefix="spring.datasource")
	 @Bean
	 public DruidDataSource getDruidDataSource() throws SQLException {
		 DruidDataSource dataSource=new DruidDataSource();
		 dataSource.setFilters("stat");     //实时监控
		 return dataSource;
	}*/

}
