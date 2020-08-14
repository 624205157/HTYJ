package com.example.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by czy on 2020/8/13 17:32.
 * describe:
 */
public class Address implements Parcelable {
    private String name;
    private double longitude;//经度
    private double latitude;//纬度

    public Address(){

    }

    protected Address(Parcel in) {
        name = in.readString();
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
