package com.xuwc.simpo.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.xuwc.simpo.entity.Redis;
import com.xuwc.simpo.service.UserService;
import com.xuwc.simpo.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;

/** 
 * 模拟并发线程Controller
 */
@RestController
public class ThreadController {

    @Autowired
    private UserService userService;

    @RequestMapping("thread")
    public void index(){
        //自定义线程名称
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("xuwc-pool-%d").build();
        //定义线程池
        final ExecutorService executor = Executors.newFixedThreadPool(100,threadFactory);
        System.out.println("=====================>> 开始执行 <<==========================");
        long start=System.currentTimeMillis();   //获取开始时间
        // 构建完成服务
        CompletionService<Integer> completionService = new ExecutorCompletionService<java.lang.Integer>(executor);
        //线程加入
        for (int index = 0; index < 500; index++) {
            completionService.submit(new com.xuwc.simpo.entity.Thread(userService,index));
        }
        for (int index = 0; index < 500; index++){
            try {
                //等待返回结果
                System.err.println(completionService.take().get());
            }catch (InterruptedException e){
                e.printStackTrace();
            }catch (ExecutionException e){
                e.printStackTrace();
            }
        }
        long end=System.currentTimeMillis(); //获取结束时间
        System.out.println("================>> 程序运行时间： "+(end-start)+"ms <<=================");
        executor.shutdown();
    }

    @RequestMapping("test")
    public void test(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final CountDownLatch downLatch = new CountDownLatch(1000);
        //自定义线程名称
        final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("xuwc-pool-%d").build();
        //定义线程池
        final ExecutorService executor = Executors.newFixedThreadPool(80,threadFactory);
        for (int index = 0; index < 1000; index++){
            final int NO = index;
            Runnable run = new Runnable()
            {
                @Override
                public void run() {
                    try {
                        // 等待，所有一起执行
                        downLatch.await();
                        //开始模拟登录等待。。。
                        Thread.sleep(1000);
                        System.out.println(sdf.format(new Date()) + Thread.currentThread().getName()+"第" + NO + "条 开始执行");

                        String time = System.currentTimeMillis()+"";
                        Redis redis = new Redis();
                        redis.setTitle(java.lang.Thread.currentThread().getName() +"||"+ time +"==>" + NO);
                        redis.setSort(NO);
                        redis.setAddTime(new Date());
                        try {
                            userService.saveRedis(redis);
                            //将数据插入到redis
                            JedisUtils.set(time,NO+"",0);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    } catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    finally {
                    }
                }
            };
            executor.submit(run);
        }
        // begin减一，开始并发执行
        for(int i=1;i <= 1000;i++){
            downLatch.countDown();
        }
        //关闭执行
        executor.shutdown();
    }
}
