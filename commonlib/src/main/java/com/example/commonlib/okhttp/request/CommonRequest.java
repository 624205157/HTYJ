package com.example.commonlib.okhttp.request;


import android.text.TextUtils;
import android.util.Log;

import com.example.commonlib.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 文件名:   CommonRequest
 * 描述:     接收请求参数，为我们生成Request对象
 */

public class CommonRequest {

    /**
     * 创建Get请求的Request
     *
     * @param url
     * @param params
     * @return 通过传入的参数返回一个Get类型的请求
     */
    public static Request createGetRequest(String url, RequestParams params) {
        StringBuilder urlBuilder = new StringBuilder(url).append("?");

        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                urlBuilder
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue())
                        .append("&");
            }
        }

        return new Request.Builder().url(urlBuilder.substring(0, urlBuilder.length() - 1))
                .addHeader("Authorization", Constants.TAKEN)
                .get().build();
    }

    /**
     * 创建Post请求的Request
     *
     * @param url
     * @param params
     * @return 返回一个创建好的Request对象
     */
    public static Request createPostRequest(String url, RequestParams params) {
        FormBody.Builder mFromBodyBuilder = new FormBody.Builder();

        if (params != null) {
            for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                //将请求参数逐一遍历添加到我们的请求构建类中
                mFromBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }

        //通过请求构建类的build方法获取到真正的请求体对象
        FormBody mFormBody = mFromBodyBuilder.build();
        return new Request.Builder().url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .post(mFormBody).build();
    }

    /**
     * 创建Post请求的Request
     * 传json
     *
     * @param url
     * @param jsonStr
     * @return 返回一个创建好的Request对象
     */
    public static Request createPostJSONRequest(String url, String jsonStr) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        return new Request.Builder().url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .post(requestBody).build();
    }

    /**
     * 创建delete请求的Request
     *
     * @param url
     * @param jsonStr
     * @return 通过传入的参数返回一个Get类型的请求
     */
    public static Request createDeleteRequest(String url, String jsonStr) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        return new Request.Builder().url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .delete(requestBody).build();

    }

    /**
     * 创建 patch 请求的Request
     *
     * @param url
     * @param jsonStr
     * @return 通过传入的参数返回一个Get类型的请求
     */
    public static Request createPatchRequest(String url, String jsonStr) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonStr);
        return new Request.Builder()
                .url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .patch(requestBody).build();

    }


    /**
     * 混合form和图片
     *
     * @return 返回一个创建好的Request对象
     */
    public static Request createMultipartRequest(String url, RequestParams params, List<File> files) {

        //构建多部件builder
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //获取参数并放到请求体中
        try {
            if (params != null) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                    //将请求参数逐一遍历添加到我们的请求构建类中
                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                Log.e("TAG", "入参:   " + jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //添加图片集合放到请求体中
        if (files != null) {
            for (File f : files) {
                bodyBuilder.addFormDataPart("attachments", f.getName(),
                        RequestBody.create(MediaType.parse("image/png"), f));
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .put(bodyBuilder.build())
                .build();

        return request;
    }

    /**
     * 混合form和图片
     *
     * @return 返回一个创建好的Request对象
     */
    public static Request createMultipartRequest(String url, RequestParams params,String fileName,File files) {

        //构建多部件builder
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //获取参数并放到请求体中
        try {
            if (params != null) {
                JSONObject jsonObject = new JSONObject();
                for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                    //将请求参数逐一遍历添加到我们的请求构建类中
                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                Log.e("TAG", "入参:   " + jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (files != null) {
                bodyBuilder.addFormDataPart(fileName, files.getName(),RequestBody.create(MediaType.parse("image/png"), files));
        }

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .put(bodyBuilder.build())
                .build();

        return request;
    }


    /**
     * 混合form和图片
     *
     * @return 返回一个创建好的Request对象
     */
    public static Request createMultipartRequest2(String url, RequestParams params, File license, File identity) {

        //构建多部件builder
        MultipartBody.Builder bodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        //获取参数并放到请求体中
        try {
            if (params != null) {
                JSONObject jsonObject = new JSONObject();

                for (Map.Entry<String, String> entry : params.urlParams.entrySet()) {
                    //将请求参数逐一遍历添加到我们的请求构建类中
                    bodyBuilder.addFormDataPart(entry.getKey(), entry.getValue());
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
                Log.e("TAG", "入参:   " + jsonObject.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (license != null) {
            bodyBuilder.addFormDataPart("license", license.getName(),
                    RequestBody.create(MediaType.parse("image/png"), license));
        }
        if (identity != null) {
            bodyBuilder.addFormDataPart("identity", identity.getName(),
                    RequestBody.create(MediaType.parse("image/png"), identity));
        }
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", Constants.TAKEN)
                .put(bodyBuilder.build())
                .build();

        return request;
    }

}
