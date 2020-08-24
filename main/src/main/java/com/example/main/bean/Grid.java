package com.example.main.bean;

import java.util.List;

/**
 * Created by czy on 2020/8/8 20:45.
 * describe: 网格
 */
public class Grid {
    private String id;
    private String pid;
    private String name;
    private String address;

    private List<User> users;
    private String polygon;//json字符串

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public String getPolygon() {
        return polygon;
    }

    public void setPolygon(String polygon) {
        this.polygon = polygon;
    }



}
