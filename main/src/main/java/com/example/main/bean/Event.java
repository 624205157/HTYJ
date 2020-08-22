package com.example.main.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czy on 2020/8/13 20:18.
 * describe: 事件
 */
public class Event {

    private String id;
    private String name;
    private String address;
    @SerializedName("category_name")
    private String type;//事件类型
    @SerializedName("degree_name")
    private String level;//紧急程度
    private String state;//1=已办结，0=处理中
    private String content;//事件内容

    private double longitude;//经度
    private double latitude;//纬度
    private Address point = new Address();

    private List<ImageList> attachments;

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

    private List<Processes> processes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Address getPoint() {
        point.setName(name);
        point.setLatitude(latitude);
        point.setLongitude(longitude);
        return point;
    }

    public void setPoint(Address point) {
        this.point = point;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Processes> getProcesses() {
        return processes;
    }

    public void setProcesses(List<Processes> processes) {
        this.processes = processes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ImageList> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ImageList> attachments) {
        this.attachments = attachments;
    }
}
