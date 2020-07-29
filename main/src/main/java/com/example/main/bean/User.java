package com.example.main.bean;

/**
 * Created by 陈泽宇 on 2020/5/19
 * Describe: 用户
 */
public class User {
    private String username;
    private String password;

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
}
