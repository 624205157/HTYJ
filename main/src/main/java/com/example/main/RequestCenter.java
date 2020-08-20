package com.example.main;




import com.example.commonlib.okhttp.CommonOkHttpClient;
import com.example.commonlib.okhttp.listener.DisposeDataHandle;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.CommonRequest;
import com.example.commonlib.okhttp.request.RequestParams;

import java.io.File;
import java.util.List;

import static com.example.main.UrlService.LOGIN;
import static com.example.main.UrlService.REGISTER;

/**
 * 描述:     统一管理所有的请求
 */

public class RequestCenter {


    /**
     * 登录账号
     * @param listener
     */
    public static void requestLogin(String jsonStr, DisposeDataListener listener){
        CommonOkHttpClient.post(CommonRequest.createPostJSONRequest(LOGIN, jsonStr),
                new DisposeDataHandle(listener));
    }


    /**
     * 注册账号
     * @param listener
     */
    public static void requestRegister(String jsonStr, DisposeDataListener listener){
        CommonOkHttpClient.post(CommonRequest.createPostJSONRequest(REGISTER, jsonStr),
                new DisposeDataHandle(listener));
    }

    /**
     * 获取企业列表
     * @param listener
     */

    public static void getDataList(String url, RequestParams params, DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createGetRequest(url,params),new DisposeDataHandle(listener));
    }

    /**
     * 获取字典
     * @param listener
     */

    public static void getDictionary(RequestParams params,DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createGetRequest(UrlService.DICTIONARY,params),new DisposeDataHandle(listener));
    }
  /**
     * 获取字典 网格
     * @param listener
     */

    public static void getGrid(RequestParams params,DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createGetRequest(UrlService.GRID,params),new DisposeDataHandle(listener));
    }

    /**
     * 获取字典 类别
     * @param listener
     */

    public static void getType(RequestParams params,DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createGetRequest(UrlService.TYPE,params),new DisposeDataHandle(listener));
    }
 /**
     * 删除企业
     * @param listener
     */

    public static void deleteData(String url,String jsonStr,DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createDeleteRequest(url,jsonStr),new DisposeDataHandle(listener));
    }



    /**
     * 新增/修改 企业
     * @param params
     * @param file1
     * @param file2
     * @param listener
     */
    public static void addEnterprise(RequestParams params, File file1,File file2, DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createMultipartRequest2(UrlService.ENTERPRISE,params,file1,file2),new DisposeDataHandle(listener));
    }

    /**
     * 新增/修改 应急资源
     * @param params
     * @param file1
     * @param file2
     * @param listener
     */
    public static void addResources(RequestParams params, List<File> files, DisposeDataListener listener){
        CommonOkHttpClient.get(CommonRequest.createMultipartRequest(UrlService.RESOURCE,params,files),new DisposeDataHandle(listener));
    }
}
