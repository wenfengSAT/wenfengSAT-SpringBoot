package com.xuwc.simpo.controller;

import com.xuwc.simpo.entity.User;
import com.xuwc.simpo.service.UserService;
import com.xuwc.simpo.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/** 
 * redis 缓存
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    //首页
    @RequestMapping("index")
    public List<List<User>> index(String userName){
        //测试查询所有
        List<User> userList = userService.getAll();
        //测试用户名查询
        List<User> userList1 = userService.findByUserName("系统管理员");
        //测试性别查询
        List<User> userList2 = userService.findBySex("男");
        //测试用户名加性别查询
        List<User> userList3 = userService.findByUserNameAndSex("系统管理员","男");
        List<List<User>> list = new ArrayList<List<User>>();
        list.add(userList);
        list.add(userList1);
        list.add(userList2);
        list.add(userList3);

        //插入当前时间戳  验证@CachePut  不检查缓存是否存在 总是执行
        userService.saveTime();

        //将数据存入redis
        String str = "xuwc";
        JedisUtils.set("xuwc",str,0);
        return list;
    }

    /**
     * 测试从redis 读取数据
     * @return
     */
    @RequestMapping("redis")
    public String redisValue(){
        return "从redis中取到的值为："+JedisUtils.get("xuwc");
    }

    /**
     * 测试从redis 删除数据
     * @return
     */
    @RequestMapping("removeRedis")
    public String removeRedis(){
        return userService.removeCache();
    }


    /**
     *  获取用户信息
     * @param id
     * @return
     */
    @RequestMapping("getUser")
    public User getUser(String id){
        return userService.getUserInfo(id);
    }
}
