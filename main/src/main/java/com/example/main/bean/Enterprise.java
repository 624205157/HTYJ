package com.example.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by czy on 2020/8/12 12:50.
 * describe: 企业信息
 */
public class Enterprise implements Parcelable {

    private String id;
    private String name;
    private String address;
    private String socialCreditCode;//社会信用代码
    private String legalPerson;//法人
    private String isStart;//是否星标企业
    private double  longitude;//经度
    private double  latitude;//纬度

    private boolean hidden = false;//是否局部隐藏 true 显示 false隐藏

    public Enterprise(){

    }

    public Enterprise(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        socialCreditCode = in.readString();
        legalPerson = in.readString();
        isStart = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
        hidden = in.readByte() != 0;
    }

    public static final Creator<Enterprise> CREATOR = new Creator<Enterprise>() {
        @Override
        public Enterprise createFromParcel(Parcel in) {
            return new Enterprise(in);
        }

        @Override
        public Enterprise[] newArray(int size) {
            return new Enterprise[size];
        }
    };

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

    public String getIsStart() {
        return isStart;
    }

    public void setIsStart(String isStart) {
        this.isStart = isStart;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(socialCreditCode);
        dest.writeString(legalPerson);
        dest.writeString(isStart);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeByte((byte) (hidden ? 1 : 0));
    }
}
