package com.example.main.bean;

import java.util.List;

/**
 * Created by 陈泽宇 on 2020/8/31
 * Describe: 用户信息
 */
public class Subject {
    private String mobile;

    private String avatar;//头像

    private List<Grid> grids;//所属网格，[{id, name}]形式

//    private List<String> modules;//已授权菜单，[{pId, id, name, category, path}]形式，涉及的字段后续接口会有具体说明

    private String name;

    private String id;

    private int state;//状态，1：有效，0：停用

    private String account;//用户账号

    private String email;

    public void setMobile(String mobile){
        this.mobile = mobile;
    }
    public String getMobile(){
        return this.mobile;
    }
    public void setAvatar(String avatar){
        this.avatar = avatar;
    }
    public String getAvatar(){
        return this.avatar;
    }
//    public void setModules(List<String> modules){
//        this.modules = modules;
//    }
//    public List<String> getModules(){
//        return this.modules;
//    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setState(int state){
        this.state = state;
    }
    public int getState(){
        return this.state;
    }
    public void setAccount(String account){
        this.account = account;
    }
    public String getAccount(){
        return this.account;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }

    public List<Grid> getGrids() {
        return grids;
    }

    public void setGrids(List<Grid> grids) {
        this.grids = grids;
    }
}


