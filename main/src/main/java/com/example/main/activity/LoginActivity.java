package com.example.main.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;


import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.bean.User;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by 陈泽宇 on 2020/5/18
 * Describe: 登录页
 */
public class LoginActivity extends BaseActivity {
    @BindView(R2.id.user_name)
    EditText userName;
    @BindView(R2.id.password)
    EditText password;
    @BindView(R2.id.automatic_login)
    CheckBox automaticLogin;

    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("登录");
    }


    private void login(){
        buildDialog("登录中，请稍后...");
        User user = new User(userName.getText() + "",password.getText() + "");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(user);
        RequestCenter.requestLogin(jsonStr, new DisposeDataListener() {
            @Override
            public void onSuccess(Object o) {
                cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");
                    showToast(message);
                    if (TextUtils.equals("200", code)) {
                        ARouter.getInstance().build("/show/img")
                                .withString("type","公司简介")
                                .navigation();
//                        finish();
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


    @OnClick({R2.id.register, R2.id.forget_password, R2.id.login, R2.id.wechat, R2.id.qq, R2.id.weibo})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.register) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else if (id == R.id.forget_password) {
        } else if (id == R.id.login) {
            if (TextUtils.isEmpty(userName.getText())) {
                showToast("用户名不可为空");
                return;
            }
            if (TextUtils.isEmpty(password.getText())) {
                showToast("密码不可为空");
                return;
            }
//            login();
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        } else if (id == R.id.wechat) {
        } else if (id == R.id.qq) {
        } else if (id == R.id.weibo) {
        }
    }
}
