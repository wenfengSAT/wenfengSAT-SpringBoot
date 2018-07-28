package com.xuwc.simpo.entity;

import com.xuwc.simpo.service.UserService;
import com.xuwc.simpo.utils.JedisUtils;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;

/** 
 * 线程实例
 */
public class Thread<Integer> implements Callable<String>{

    private UserService userService;

    private int num;

    public Thread(UserService userService,int num){
        this.userService = userService;
        this.num = num;
    }

    @Override
    public synchronized String call() throws Exception {
        java.lang.Thread.sleep(new Random().nextInt(2000));
        String time = System.currentTimeMillis()+"";
        Redis redis = new Redis();
        redis.setSort(num);
        redis.setTitle(java.lang.Thread.currentThread().getName() + time +"==>"+ num);
        redis.setAddTime(new Date());
        try {
            userService.saveRedis(redis);
        }catch (Exception e){
            e.printStackTrace();
        }
        //将数据插入到redis
        JedisUtils.set(time,num+"",0);
        String message =  java.lang.Thread.currentThread().getName() + "==>"+time;
        return message;
    }
}
