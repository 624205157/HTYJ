package com.example.main.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by czy on 2020/8/16 13:24.
 * describe: 任务
 */
public class Task implements Parcelable {

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


    protected Task(Parcel in) {
        id = in.readString();
        name = in.readString();
        startTime = in.readString();
        stopTime = in.readString();
        state = in.readString();
        timeLimit = in.readString();
        count = in.readString();
        hidden = in.readByte() != 0;
        controls = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(startTime);
        dest.writeString(stopTime);
        dest.writeString(state);
        dest.writeString(timeLimit);
        dest.writeString(count);
        dest.writeByte((byte) (hidden ? 1 : 0));
        dest.writeString(controls);
    }


}
