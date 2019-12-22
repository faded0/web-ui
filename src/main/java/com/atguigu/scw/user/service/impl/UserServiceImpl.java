package com.atguigu.scw.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.atguigu.scw.user.bean.TMember;
import com.atguigu.scw.user.bean.TMemberAddress;
import com.atguigu.scw.user.bean.TMemberAddressExample;
import com.atguigu.scw.user.bean.TMemberExample;
import com.atguigu.scw.user.exception.UserException;
import com.atguigu.scw.user.mapper.TMemberAddressMapper;
import com.atguigu.scw.user.mapper.TMemberMapper;
import com.atguigu.scw.user.service.UserService;
import com.atguigu.scw.user.vo.UserAddressVo;
import com.atguigu.scw.user.vo.UserRegisterVo;
import com.atguigu.scw.user.vo.UserRespVo;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	TMemberMapper memberMapper;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	TMemberAddressMapper memberAddressMapper;
   
	@Override
	public void doregister(UserRegisterVo vo) {
		/*
		 * 1、检查账户是否唯一
		 * 2、对密码进行加密
		 * 3、将Vo中的数据封装到member中
		 * 4、调用到Mapper层
		 */
		
		//1、检查账户是否唯一
		TMemberExample example=new TMemberExample();
		example.createCriteria().andLoginacctEqualTo(vo.getLoginacct());
		long count = memberMapper.countByExample(example);
		if(count>0) {
			throw new UserException("账户已被注册");
		}
		//对密码进行加密
		vo.setUserpswd(passwordEncoder.encode(vo.getUserpswd()));
		TMember member=new TMember();
		//将Vo对象转换成为Member对象
		BeanUtils.copyProperties(vo, member);
		
		//设置用户名
		member.setUsername(vo.getLoginacct());
		//设置状态
		member.setAuthstatus("0");
		
		//插入到数据库
		memberMapper.insertSelective(member);
		
		
		
	}

	@Override
	public UserRespVo login(String username, String password) {
		TMemberExample example=new TMemberExample();
		example.createCriteria().andUsernameEqualTo(username);
		List<TMember> memberList = memberMapper.selectByExample(example);
		if(CollectionUtils.isEmpty(memberList)||memberList.size()>1) {
			throw new UserException("登录账户不存在");
		}
		TMember member = memberList.get(0);
		String encodepwd = member.getUserpswd();
		boolean matches = passwordEncoder.matches(password, encodepwd);
		if(!matches) {
			throw new UserException("密码错误");
		}
		//将登录转成为UserRespVo用户
		UserRespVo vo=new UserRespVo();
		BeanUtils.copyProperties(member, vo);
		return vo;
	}

	@Override
	public List<UserAddressVo> getAddressAll(Integer userid) {
		TMemberAddressExample example=new TMemberAddressExample();
		example.createCriteria().andMemberidEqualTo(userid);
		 List<TMemberAddress> memberAddressList = memberAddressMapper.selectByExample(example);
		 //创建一个收货地址的
		 List<UserAddressVo> userAddressList=new ArrayList<UserAddressVo>();
		 for (TMemberAddress memberAddress : memberAddressList) {
			   UserAddressVo userAdd=new UserAddressVo();
			   BeanUtils.copyProperties(memberAddress, userAdd);
			   userAddressList.add(userAdd);	   
		}
		return userAddressList;
	}

}
