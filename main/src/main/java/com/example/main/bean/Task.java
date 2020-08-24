package com.example.main.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czy on 2020/8/16 13:24.
 * describe: 任务
 */
public class Task {

    private String id;
    private String name;
    @SerializedName("start_time")
    private String time;//下发时间
    private String state;
    private String type;//任务类型
    @SerializedName("remark")
    private String count;//任务描述
    private String people;//下发人
    private List<Grid> gridList;//下发网格

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public List<Grid> getGridList() {
        return gridList;
    }

    public void setGridList(List<Grid> gridList) {
        this.gridList = gridList;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
