package com.atguigu.scw.user.service;

import java.util.List;

import com.atguigu.scw.user.vo.UserAddressVo;
import com.atguigu.scw.user.vo.UserRegisterVo;
import com.atguigu.scw.user.vo.UserRespVo;

public interface UserService {

	public void doregister(UserRegisterVo userRegisterVo);

	public UserRespVo login(String username, String password);

	public List<UserAddressVo> getAddressAll(Integer userid);
    
}
