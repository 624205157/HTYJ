package com.example.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by czy on 2020/8/16 13:24.
 * describe: 任务
 */
public class Task {

    private String id;
    private String name;
    private String startTime;//开始时间
    private String stopTime;//结束时间
    private String state;//任务状态（1=已完成，2=进行中，3=未开始）
    @SerializedName("deadline")
    private String timeLimit;//任务时限
    @SerializedName("remark")
    private String count;//任务描述

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

//    private List<Control> controls;//表单控件的JSON配置
    private String controls;//表单控件的JSON配置

    private String values ;//表单反显

    private List<MyFiles> attachments;


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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }


    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(String timeLimit) {
        this.timeLimit = timeLimit;
    }

    public String getStopTime() {
        return stopTime;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
    }





    public List<MyFiles> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<MyFiles> attachments) {
        this.attachments = attachments;
    }



    public String getControls() {
        return controls;
    }

    public void setControls(String controls) {
        this.controls = controls;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }
}
