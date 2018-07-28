package com.mongodb.springbootmongodb.model;

import java.io.Serializable;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午1:49:53 
* 
*/
public class UserEntity implements Serializable{

	 private static final long serialVersionUID = -3258839839160856613L;
     private Long id;
     private String userName;
     private String passWord;
     
     
     
	public UserEntity() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
     
}
