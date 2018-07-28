package com.mongodb.springbootmongodb.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.springbootmongodb.model.UserEntity;
import com.mongodb.springbootmongodb.service.impl.UserDaoImpl;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午2:01:14 
* 
*/
@RestController
@RequestMapping("userentity")
public class UserEntityController {

	@Autowired
	private UserDaoImpl userDaoImpl;
	
	@RequestMapping("save")
	public void save(){
		UserEntity user = new UserEntity();
		user.setId(8L);
		user.setPassWord("123456");
		userDaoImpl.saveUser(user);
		
	}
	
	@RequestMapping("query")
	public UserEntity queryByname(){
		UserEntity user = userDaoImpl.findUserByUserName("hahahah");
		System.out.println(user.getUserName()+"  "+user.getId());
		
		return user;
		
	}
	
	@RequestMapping("update")
	public void update(){
		
		UserEntity user = new UserEntity();
		user.setId(6L);
		user.setUserName("aaawangwuaaa");
		user.setPassWord("456789");
		userDaoImpl.updateUser(user);
		
	}
	
	@RequestMapping("findall")
	public List<UserEntity> findAll(){
		List<UserEntity> list = userDaoImpl.listUser();
		System.out.println(list.get(0).toString());
		
		return list;
		
	}
	
	@RequestMapping("delete")
	public void delete(){
		userDaoImpl.deleteUserById(1L);
		
	}
	/**
	 * 查询符合条件的第一条数据
	 */
	@RequestMapping("findone")
	public UserEntity findone(){
		return userDaoImpl.getByUrl(1L);
		
	}
	/**
	 * 模糊查询
	 * @return
	 */
	@RequestMapping("mohu")
	public long mohu(){
		return userDaoImpl.getProcessLandLogsCount("wangwu");
		
	}
	/**
	 * 使用转义符
	 * @return
	 */
	@RequestMapping("zifu")
	public List<UserEntity> zifu(){
		return userDaoImpl.getProcessLandLogs(4L);
	}
	/**
	 * 查询字段不存在的数据
	 * @return
	 */
	@RequestMapping("notexists")
	public List<UserEntity> notexist(){
		return userDaoImpl.getGoodsDetails2(1, 8);
	}
	
	
}
