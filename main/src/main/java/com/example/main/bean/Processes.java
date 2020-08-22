package com.example.main.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 陈泽宇 on 2020/8/21
 * Describe:处理进程
 */
public class Processes {

    private String id;
    @SerializedName("event_id")
    private String eventId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("user_name")
    private String userName;//处理人姓名
    private String state;//处理进程，1=已处理，0=未处理
    @SerializedName("settle_time")
    private String settleTime;//处理时间
    private String content;//描述

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSettleTime() {
        return settleTime;
    }

    public void setSettleTime(String settleTime) {
        this.settleTime = settleTime;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
