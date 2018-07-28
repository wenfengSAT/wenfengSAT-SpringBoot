package com.mongodb.springbootmongodb.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.springbootmongodb.model.UserEntity;
import com.mongodb.springbootmongodb.service.dao.UserCustomerDao;

/** 
* @author 作者 zhujf: 
* @version 创建时间：2017年10月17日 下午5:14:46 
* 
*/
@RestController
@RequestMapping("userPage")
public class PageController {

	@Autowired
	private UserCustomerDao userCustomerDao;
	
	@RequestMapping("/selectName")
    public List<UserEntity> selectName(){
        //构建分页信息 
		//参数1表示当前第几页,参数2表示每页的大小,参数3表示排序(表示按照哪个字段排序)
        PageRequest pageRequest = buildPageRequest(1,5,"id");
        //查询指定分页的内容
        Iterator<UserEntity> customers =  userCustomerDao.findAll(pageRequest).iterator();
        List<UserEntity> lists = new ArrayList<UserEntity>();
        while(customers.hasNext()){
            lists.add(customers.next());
        }
        return lists;
    }
    /**
     *      * 创建分页请求.      
     */
    private PageRequest buildPageRequest(int pageNumber, int pageSize,String sortType) {
        Sort sort = null;
        if (sortType == null || "".equals(sortType)) {
            sort = new Sort(Direction.DESC, "id");
        } else if ("desc".equals(sortType)) {
            sort = new Sort(Direction.DESC, "id");
        } else if ("asc".equals(sortType)) {
            sort = new Sort(Direction.ASC, "id");
        }
        //参数1表示当前第几页,参数2表示每页的大小,参数3表示排序
        return new PageRequest(pageNumber-1,pageSize,sort);
    }
	
}
