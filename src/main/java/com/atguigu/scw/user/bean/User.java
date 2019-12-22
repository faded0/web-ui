package com.atguigu.scw.user.bean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class User {
	  
	  @ApiModelProperty(value="主键")
	  private Integer id;
	  
	  @ApiModelProperty(value="姓名")
	  private String name;
	  
	  @ApiModelProperty(value="电子邮件")
	  private String email;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
	  
	  

}
