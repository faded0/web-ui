package com.atguigu.scw.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
 * 用户注册Vo类
 */
@ApiModel(value="这是用户注册的Vo类型")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class UserRegisterVo {
	    @ApiModelProperty(value="手机号")
	    private String loginacct;
        
	    @ApiModelProperty(value="密码")
	    private String userpswd;

	    
	    @ApiModelProperty(value="邮箱")
	    private String email;

	    @ApiModelProperty(value="账户类型")
	    private String usertype;
        
	    @ApiModelProperty(value="验证码")
	    private String code; //验证码

}
