package com.example.commonlib.okhttp.listener;


import com.example.commonlib.okhttp.exception.OkHttpException;

/**
 * 文件名:   DisposeDataListener
 * 描述:     自定义事件监听回调，处理请求成功和失败时的回调函数
 */

public interface DisposeDataListener {

    //请求成功回调事件处理
    public void onSuccess(Object responseObj);

    //请求失败回调事件处理
    public void onFailure(OkHttpException responseObj);

}
