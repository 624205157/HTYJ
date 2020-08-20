package com.example.main.bean;

/**
 * Created by 陈泽宇 on 2020/8/20
 * Describe:
 */
public class DeleteData {

    public DeleteData(String str){
        data[0] = str;
    }

    private String [] data = new String[1];

    public String[] getData() {
        return data;
    }

    public void setData(String[] data) {
        this.data = data;
    }
}
