package com.example.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czy on 2020/8/13 17:27.
 * describe: 资源
 */
public class Resources {

    private String id;
    private String name;
    private String address;
    @SerializedName("category_name")
    private String type;
    private String total;
    private double longitude;//经度
    private double latitude;//纬度
    private String surplus;//剩余量
    @SerializedName("grid_name")
    private String grid;

    private List<ImageList> attachments;

    private Address point = new Address();

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
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


    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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

    public String getSurplus() {
        return surplus;
    }

    public void setSurplus(String surplus) {
        this.surplus = surplus;
    }

    public List<ImageList> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<ImageList> attachments) {
        this.attachments = attachments;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }
}
