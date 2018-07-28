package com.xuwc.simpo.service;

import com.xuwc.simpo.entity.Redis;
import com.xuwc.simpo.entity.User;

import java.util.List;

/** 
 * 用户service
 */
public interface UserService {
    //获取用户信息  findById(同理)
    User getUserInfo(String id);
    //获取所有用户
    List<User> getAll();
    //查询按照方法名
    List<User> findByUserName(String userName);
    //按照性别查询
    List<User> findBySex(String sex);
    //测试用户名和性别查询
    List<User> findByUserNameAndSex(String userName,String sex);
    //插入当前时间戳
    long saveTime();
    //清除缓存中的值并返回
    String removeCache();
    //保存信息
    void saveRedis(Redis redis);
}
