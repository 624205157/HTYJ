package com.example.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czy on 2020/8/12 12:50.
 * describe: 企业信息
 */
public class Enterprise {

    private String id;
    private String name;
    private String address;

    @SerializedName("contactPhone")
    private String tel;//企业电话
    @SerializedName("faxNumber")
    private String fax;//传真号
    @SerializedName("creditCode")
    private String socialCreditCode;//社会信用代码
    @SerializedName("legalPerson")
    private String legalPerson;//法人
    @SerializedName("legalPhone")
    private String legalPersonTel;//法人电话
    @SerializedName("star")
    private int isStart;//是否星标企业
    @SerializedName("gridName")
    private String grid;//网格名称
    private double longitude;//经度
    private double latitude;//纬度
    private Address point = new Address();
    private List<ImageList> license;//营业执照图片
    private List<ImageList> identity;//法人图片
    private String [] exist;//删除图片

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

    public String getSocialCreditCode() {
        return socialCreditCode;
    }

    public void setSocialCreditCode(String socialCreditCode) {
        this.socialCreditCode = socialCreditCode;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLegalPersonTel() {
        return legalPersonTel;
    }

    public void setLegalPersonTel(String legalPersonTel) {
        this.legalPersonTel = legalPersonTel;
    }

    public String getGrid() {
        return grid;
    }

    public void setGrid(String grid) {
        this.grid = grid;
    }

    public int getIsStart() {
        return isStart;
    }

    public void setIsStart(int isStart) {
        this.isStart = isStart;
    }

    public String[] getExist() {
        return exist;
    }

    public void setExist(String[] exist) {
        this.exist = exist;
    }

    public List<ImageList> getLicense() {
        return license;
    }

    public void setLicense(List<ImageList> license) {
        this.license = license;
    }

    public List<ImageList> getIdentity() {
        return identity;
    }

    public void setIdentity(List<ImageList> identity) {
        this.identity = identity;
    }
}
