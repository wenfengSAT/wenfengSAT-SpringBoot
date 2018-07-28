package com.mongodb.springbootmongodb.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mongodb.springbootmongodb.model.UserEntity;
import com.mongodb.springbootmongodb.service.dao.UserDao;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午1:59:14 
* 
*/
@Component
public class UserDaoImpl implements UserDao{

	@Autowired
    private MongoTemplate mongoTemplate;
	
	  /**
     * 创建对象
     * @param user
     */
    @Override
    public void saveUser(UserEntity user) {
        mongoTemplate.save(user);
    }

    /**
     * 根据用户名查询对象
     * @param userName
     * @return
     */
    @Override
    public UserEntity findUserByUserName(String userName) {
        Query query=new Query(Criteria.where("userName").is(userName));
        UserEntity user =  mongoTemplate.findOne(query , UserEntity.class);
        return user;
    }

    /**
     * 更新对象
     * @param user
     */
    @Override
    public void updateUser(UserEntity user) {
        Query query=new Query(Criteria.where("id").is(user.getId()));
        Update update= new Update().set("userName", user.getUserName()).set("passWord", user.getPassWord());
        //更新查询返回结果集的第一条
        mongoTemplate.updateFirst(query,update,UserEntity.class);
        //更新查询返回结果集的所有
        // mongoTemplate.updateMulti(query,update,UserEntity.class);
    }

    /**
     * 删除对象
     * @param id
     */
    @Override
    public void deleteUserById(Long id) {
        Query query=new Query(Criteria.where("id").is(id));
        mongoTemplate.remove(query,UserEntity.class);
    }

    /**
     * 查询所有
     * @return
     */
    public List<UserEntity> listUser() {
		return mongoTemplate.findAll(UserEntity.class);
	}
	
    /**
     * 一. 常用查询:

		1. 查询一条数据:(多用于保存时判断db中是否已有当前数据,这里 is  精确匹配,模糊匹配 使用 regex...)
     * @param url
     * @return
     */
    public UserEntity getByUrl(long id) {  
        return mongoTemplate.findOne(new Query(Criteria.where("id").is(id)),UserEntity.class);  
    }

    /**
	 * 查询多条数据:linkUrl.id 属于分级查询
	 * @param begin
	 * @param end
	 * @param linkUrlid
	 * @return
	 */
	
	public List<UserEntity> getPageUrlsByUrl(int begin, int end,String linkUrlid) {          
        Query query = new Query();  
        query.addCriteria(Criteria.where("linkUrl.id").is(linkUrlid));  
        return mongoTemplate.find(query.limit(end - begin).skip(begin), UserEntity.class);          
    }  
	
	/**
	 * 模糊查询
	 * @param name
	 * @return
	 */
	public long getProcessLandLogsCount(String name)  
    {  
        Query query = new Query();  
          
       query.addCriteria(Criteria.where("userName").regex(".*?\\" +name+ ".*"));  
       
        return mongoTemplate.count(query, UserEntity.class);  
    }  
	
	/**
	 * gte: 大于等于,lte小于等于...注意查询的时候各个字段的类型要和mongodb中数据类型一致
	 * @param begin
	 * @param end
	 * @param conditions
	 * @param orderField
	 * @param direction
	 * @return
	 */
	
	public List<UserEntity> getProcessLandLogs(long id)  
    {  
        Query query = new Query();  
        query.addCriteria(Criteria.where("id").gte(id)); //gte: 大于等于  
   
        //query.addCriteria(Criteria.where("insertTime").gte(condition.getValue()));  
 
        //query.addCriteria(Criteria.where(condition.getKey()).is(condition.getValue()));  
        return mongoTemplate.find(query, UserEntity.class);  
    }  
	
	/**
	 * 查询字段不存在的数据
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<UserEntity> getGoodsDetails2(int begin, int end) {  
        Query query = new Query();  
        //Criteria.where("key1").ne("").ne(null)  查询不为空的数据
        //Criteria.where("userName").not()
        //查询或语句：a || b
       /* Criteria criteria = new Criteria();  
        criteria.orOperator(Criteria.where("key1").is("0"),Criteria.where("key1").is(null)); */ 
        //查询且语句：a && b
       /* Criteria criteria = new Criteria();  
        criteria.and("key1").is(false);  
        criteria.and("key2").is(type);  
        Query query = new Query(criteria);  
        long totalCount = this.mongoTemplate.count(query, Xxx.class);  */
        
        query.addCriteria(Criteria.where("userName").ne("").ne(null));  
        return mongoTemplate.find(query.limit(end - begin).skip(begin),UserEntity.class);  
    }

	
    
}
