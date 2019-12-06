package com.sxw.elasticsearch.model;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 创建时间
     */
    private Date createTime;
}
