package com.mongodb.springbootmongodb.service.dao;

import java.util.List;


import com.mongodb.springbootmongodb.model.UserEntity;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午1:52:15 
* 
*/

public interface UserDao {

	 public void saveUser(UserEntity user);
	 
	 
	 public UserEntity findUserByUserName(String userName);
	 
	 
	 public void updateUser(UserEntity user);
	 
	 
	 
	 public void deleteUserById(Long id) ;
	
	 
	 public List<UserEntity> listUser();
	
}
