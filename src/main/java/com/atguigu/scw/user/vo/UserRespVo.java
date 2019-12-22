package com.atguigu.scw.user.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/*
 * 登录成功以后响应给用户的信息
 */
@Data
@ApiModel
@ToString
public class UserRespVo {
	
	private Integer id;
	@ApiModelProperty("访问令牌，请妥善保管，以后每次请求都给带上")
	private String accessToken;// 访问令牌
    private String loginacct;

    private String username;

    private String email;

    private String authstatus;

    private String usertype;

    private String realname;

    private String cardnum;

    private String accttype;

}
