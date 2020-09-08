package com.tencent.liteav.login.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {
    @SerializedName("mobile")
    public String phone;
    @SerializedName("account")
    public String userId;
    public String userSig;
    @SerializedName("name")
    public String userName;
    @SerializedName("avatar")
    public String userAvatar;

    @java.lang.Override
    public java.lang.String toString() {
        return "UserModel{" +
                "phone='" + phone + '\'' +
                ", userId='" + userId + '\'' +
                ", userSig='" + userSig + '\'' +
                ", userName='" + userName + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                '}';
    }

    public UserModel() {

    }

    public UserModel(String phone, String userId, String userName, String userAvatar) {
        this.phone = phone;
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = userAvatar;
    }
}
