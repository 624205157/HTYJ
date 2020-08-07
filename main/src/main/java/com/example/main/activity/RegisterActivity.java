package com.example.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

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
 * Created by 陈泽宇 on 2020/5/19
 * Describe: 注册账号
 */
public class RegisterActivity extends BaseActivity {
    @BindView(R2.id.user_name)
    EditText userName;
    @BindView(R2.id.password)
    EditText password;
    @BindView(R2.id.password2)
    EditText password2;

    @Override
    protected int setContentView() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("注册账号");
    }

    @OnClick(R2.id.register)
    public void onViewClicked() {
        if (TextUtils.isEmpty(userName.getText())){
            showToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(password.getText())){
            showToast("密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password2.getText())){
            showToast("确认密码不能为空");
            return;
        }
        if (!TextUtils.equals(password.getText(),password2.getText())){
            showToast("两次密码不一致");
            return;
        }
        register();

    }

    private void register(){
        buildDialog("注册中，请稍后...");
        User user = new User(userName.getText() + "",password.getText() + "");
        Gson gson = new Gson();
        String jsonStr = gson.toJson(user);
        RequestCenter.requestRegister(jsonStr, new DisposeDataListener() {
            @Override
            public void onSuccess(Object o) {
                cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String code = jsonObject.getString("code");

                    String message = jsonObject.getString("message");
                    showToast(message);
                    if (TextUtils.equals("200",code)){
                        finish();
                    }

                }catch (Exception e){
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
}
