package com.xuwc.simpo.service.impl;

import com.xuwc.simpo.entity.Redis;
import com.xuwc.simpo.entity.User;
import com.xuwc.simpo.respository.RedisRepository;
import com.xuwc.simpo.respository.UserRepository;
import com.xuwc.simpo.service.UserService;
import com.xuwc.simpo.utils.JedisUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** 
 * 用户方法实现
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    // 用户 repository
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RedisRepository redisRepository;

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = "getUserInfo",keyGenerator = "wiselyKeyGenerator")
    public User getUserInfo(String id) {
        return userRepository.getUserInfo(id);
    }

    /**
     * 调用内置方法
     * @return
     */
    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    /***
     * 根据用户名查询
     * @param userName
     * @return
     */
    @Override
    @Cacheable(cacheNames = "findByUserName",keyGenerator = "nameKeyGenerator")
    public List<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    /**
     * 根据性别查询
     * @param sex
     * @return
     */
    @Override
    @Cacheable(value = "findBySex",keyGenerator = "wiselyKeyGenerator")
    public List<User> findBySex(String sex) {
        return userRepository.findBySex(sex);
    }

    /**
     * 根据用户名和性别查询
     * @param userName
     * @param sex
     * @return
     */
    @Override
    @Cacheable(value = "findByUserNameAndSex",keyGenerator = "wiselyKeyGenerator")
    public List<User> findByUserNameAndSex(String userName, String sex) {
        return userRepository.findByUserNameAndSex(userName, sex);
    }

    /**
     * 插入当前时间戳到缓存
     */
    @Override
    @CachePut(value = "lastTime",keyGenerator = "nameKeyGenerator")
    public long saveTime() {
        //当前时间戳
        return System.currentTimeMillis();
    }

    //清除缓存中的值并返回
    @Override
    @CacheEvict(value = "lastTime",beforeInvocation = true,allEntries = true,keyGenerator = "nameKeyGenerator")
    public String removeCache() {
        if(StringUtils.isNotEmpty(JedisUtils.get("saveTime"))){
            return "未清除缓存信息";
        }
        return "已清除缓存信息";
    }

    /**
     * 插入数据到redis表
     * @param redis
     */
    @Override
    public void saveRedis(Redis redis){
        redisRepository.save(redis);
    }
}
