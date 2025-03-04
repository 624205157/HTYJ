package com.example.main.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.Constants;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.bean.Subject;
import com.example.main.bean.User;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.liteav.login.model.ProfileManager;
import com.tencent.liteav.login.model.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.model.TRTCAudioCallImpl;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.example.commonlib.trtc.GenerateTestUserSig;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 陈泽宇 on 2020/5/18
 * Describe: 登录页
 */
public class LoginActivity extends ConfigActivity {
    @BindView(R2.id.user_name)
    EditText userName;
    @BindView(R2.id.password)
    EditText password;

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {

    }


    private void login(String username,String password) {
        User user = new User(username, Utils.encryption(password, "SHA-1"));
        Gson gson = new Gson();
        String jsonStr = gson.toJson(user);
        RequestCenter.requestLogin(jsonStr, new DisposeDataListener() {
            @Override
            public void onSuccess(Object o) {
                cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("msg");
                    showToast(message);
                    if (TextUtils.equals("0", code)) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        shareHelper
                                .save("username", username)
                                .save("password", password)
                                .save("token", data.getString("token"))
                                .save("userSig",data.getJSONObject("signature").getString("userSig"))
                                .save("subject", data.getString("subject")).commit();
                        Constants.TAKEN = data.getString("token");
                        Constants.SERVICE_ID = data.getInt("serviceId");
                        Gson gson = new Gson();
                        Subject personInfo = gson.fromJson(data.getString("subject"), new TypeToken<Subject>() {
                        }.getType());


                        /**
                         * 腾讯云登录
                         */

                        loginTRTC(LoginActivity.this,username, data.getJSONObject("signature").getString("userSig"));

                        ProfileManager.getInstance().login(
                                new UserModel(
                                        personInfo.getMobile(),
                                        personInfo.getAccount(),
                                        personInfo.getName(),
                                        personInfo.getAvatar()));

                        //小米推送登录
                        MiPushClient.setUserAccount(LoginActivity.this, username, null);

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("from","LoginActivity");
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException e) {
                showToast("网络连接失败,请稍后重试");
                e.printStackTrace();
                cancelDialog();
            }
        });
    }


    @OnClick({R2.id.forget_password, R2.id.login})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.forget_password) {
        } else if (id == R.id.login) {

            if (TextUtils.isEmpty(userName.getText())) {
                showToast("用户名不可为空");
                return;
            }
            if (TextUtils.isEmpty(password.getText())) {
                showToast("密码不可为空");
//                return;
            }
            buildDialog("登录中，请稍后...");
            login(userName.getText() +"",password.getText() + "");


        }
    }
}
