package com.xuwc.simpo.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/** 
 * 用户
 */
@Entity
@Table(name = "sys_user")
public class User implements Serializable {

    private static final long serialVersionUID = 6478021448802033208L;

    //id
    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;
    //登录名
    @Column(name = "loginname")
    private String loginName;
    //用户名
    @Column(name = "username")
    private String userName;
    //性别
    @Column(name = "sex")
    private String sex;
    //密码
    @Column(name = "password")
    private String password;
    //删除标识(0:有效,1:删除)
    @Column(name = "delflag")
    private String delFlag;
    //创建时间
    @Column(name = "addtime")
    @Temporal(TemporalType.DATE)
    private Date addTime;
    //创建者
    @Column(name = "adduserid")
    private String addUserId;
    //更新时间
    @Column(name = "updtime")
    @Temporal(TemporalType.DATE)
    private Date updTime;
    //更新者
    @Column(name = "upduserid")
    private String updUserId;

    //    @JsonIgnore
//    @ManyToOne(targetEntity = Article.class)
//    @JoinColumn(name = "userid")
//    @Transient
//    private List<Article> articleList = new ArrayList<Article>();


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddUserId() {
        return addUserId;
    }

    public void setAddUserId(String addUserId) {
        this.addUserId = addUserId;
    }

    public Date getUpdTime() {
        return updTime;
    }

    public void setUpdTime(Date updTime) {
        this.updTime = updTime;
    }

    public String getUpdUserId() {
        return updUserId;
    }

    public void setUpdUserId(String updUserId) {
        this.updUserId = updUserId;
    }


    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        User user = (User) obj;

        return id != null ? id.equals(user.id) : user.id == null;
    }

    @Override
    public String toString() {
        String str = "{id=" + id + ",loginName="+loginName+",userName="+userName+",sex="+sex+
                ",password="+password+",delFlag="+delFlag+",addTime="+addTime+",addUserId="+addUserId
                +",updTime="+updTime+",updUserId="+updUserId+"}\n";
        return str;
    }
}
