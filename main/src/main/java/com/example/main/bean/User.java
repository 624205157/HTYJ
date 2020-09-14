package com.example.main.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈泽宇 on 2020/5/19
 * Describe: 用户
 */
public class User {
    @SerializedName("account")
    private String username;
    private String password;

//    private String headUrl;//头像
    private String name;
//    private String tel;
//    private String isLeader;//是否为负责人

    private Subject subject;//用户信息
    private String token;//调用系统接口的认证令牌，有效期24小时

    public User(){
    }

    public User(String username, String password){
        this.username = username;
        this.password = password;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

//    public String getHeadUrl() {
//        return headUrl;
//    }
//
//    public void setHeadUrl(String headUrl) {
//        this.headUrl = headUrl;
//    }
//
//    public String getTel() {
//        return tel;
//    }
//
//    public void setTel(String tel) {
//        this.tel = tel;
//    }
//
//    public String getIsLeader() {
//        return isLeader;
//    }
//
//    public void setIsLeader(String isLeader) {
//        this.isLeader = isLeader;
//    }
//
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
