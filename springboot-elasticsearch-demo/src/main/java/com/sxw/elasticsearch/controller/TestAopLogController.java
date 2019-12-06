package com.sxw.elasticsearch.controller;

import com.sxw.elasticsearch.aspect.WebLog;
import com.sxw.elasticsearch.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 使用自定义注解，AOP 切面统一打印出入参日志
 */
@RestController
@Slf4j
public class TestAopLogController {
    /**
     * POST 方式接口测试
     * @param user
     * @return
     */
    @PostMapping("/user/login")
    @WebLog(description = "请求了用户登录接口")
    public User userLogin(@RequestBody User user) {
        log.info("user login ...");
        return user;
    }

    /**
     * GET 方式接口测试
     * @return
     */
    @GetMapping("/user/{id}")
    @WebLog(description = "请求了用户登录接口")
    public String findUserInfo(@PathVariable("id") String userId) {
        log.info("find user info ...");
        return "success";
    }

    /**
     * GET 方式接口测试
     * @return
     */
    @GetMapping("/test")
    public String test() {
        log.info("testGet ...");
        return "success";
    }

    /**
     * 单文件上传接口测试
     * @return
     */
    @PostMapping("/file/upload")
    public String testFileUpload(@RequestParam("file") MultipartFile file) {
        log.info("testFileUpload ...");
        return "success";
    }

    /**
     * 多文件上传接口测试
     * @return
     */
    @PostMapping("/multiFile/upload")
    public String testMultiFileUpload(@RequestParam("file") MultipartFile[] file) {
        log.info("testMultiFileUpload ...");
        return "success";
    }
}
