package com.example.main.bean;

/**
 * Created by czy on 2020/8/13 20:18.
 * describe: 事件
 */
public class Event {

    private String name;
    private String address;
    private String type;
    private String level;
    private String state;

    private double longitude;//经度
    private double latitude;//纬度
    private Address point = new Address();

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

    private String transactor;//处理人
    private String time;//处理时间

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

    public String getTransactor() {
        return transactor;
    }

    public void setTransactor(String transactor) {
        this.transactor = transactor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
