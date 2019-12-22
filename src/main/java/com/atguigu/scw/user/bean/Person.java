package com.atguigu.scw.user.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString                 // 重写ToString方法
@EqualsAndHashCode        //重写equals和HashCode方法
@AllArgsConstructor      //有参构造
@NoArgsConstructor    //无参构造
@Data   //描述Get和Set方法
public class Person {
	
	private Integer id;
	private String name;
	private String address;
    
	
	//测试方式
	public void test() {
		
	}
}
