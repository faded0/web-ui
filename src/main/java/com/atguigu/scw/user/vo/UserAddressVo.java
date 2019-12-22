package com.atguigu.scw.user.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserAddressVo {
	  @ApiModelProperty("地址ID")
      private Integer id=1;
	  
	  @ApiModelProperty("会员地址")
      private String address="西双版纳";
      
}
