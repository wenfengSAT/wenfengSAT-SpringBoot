package com.forezp.dao;

import com.forezp.entity.Account;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by fangzhipeng on 2017/4/20.
 */
public interface AccountDao  extends JpaRepository<Account,Integer> {
	
	//查找方法名必须满足   findBy属性字段名
	public Page<Account> findByNameLike(String name,Pageable pageable);
	
	public Page<Account> findByName(String name,Pageable pageable);
	
	public Page<Account> findBymoney(double money,Pageable pageable);
	
	@Query("select a from Account a where name = ?1")
	public List<Account> findByName(String name);
	
	@Modifying 
	@Query("update Account a set a.name = ?2 where a.id = ?1")
	@Transactional
	public void updateName(int id,String name);
}
