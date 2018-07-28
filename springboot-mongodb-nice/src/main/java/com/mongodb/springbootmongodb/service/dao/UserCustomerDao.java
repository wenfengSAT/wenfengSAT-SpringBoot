package com.mongodb.springbootmongodb.service.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.mongodb.springbootmongodb.model.UserEntity;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午5:13:27 
* 
*/
@Repository
public interface UserCustomerDao extends PagingAndSortingRepository<UserEntity,Integer>{

}
