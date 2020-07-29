package com.example.main;




import com.example.commonlib.okhttp.CommonOkHttpClient;
import com.example.commonlib.okhttp.listener.DisposeDataHandle;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.CommonRequest;

import static com.example.main.UrlService.LOGIN;
import static com.example.main.UrlService.REGISTER;

/**
 * 描述:     统一管理所有的请求
 */

public class RequestCenter {


    /**
     * 登录账号
     * @param params
     * @param listener
     */
    public static void requestLogin(String jsonStr, DisposeDataListener listener){
        CommonOkHttpClient.post(CommonRequest.createPostJSONRequest(LOGIN, jsonStr),
                new DisposeDataHandle(listener));
    }


    /**
     * 注册账号
     * @param params
     * @param listener
     */
    public static void requestRegister(String jsonStr, DisposeDataListener listener){
        CommonOkHttpClient.post(CommonRequest.createPostJSONRequest(REGISTER, jsonStr),
                new DisposeDataHandle(listener));
    }


}
