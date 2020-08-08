package com.example.commonlib.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.example.commonlib.R;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import butterknife.ButterKnife;


/**
 * Created by 陈泽宇 on 2020/5/13
 * Describe:
 */
public abstract class BaseActivity extends Activity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(setContentView());
        ButterKnife.bind(this);
        onCreate(savedInstanceState,"");

    }

    protected void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String text, int type) {
        switch (type) {
            case 0:
                Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
                break;
            case 1:
                Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                break;
        }

    }

    protected abstract int setContentView();

    protected abstract void onCreate(Bundle savedInstanceState,String a);

    protected void addBack(){
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void setTitleText(String title){ 
        TextView tv = findViewById(R.id.title);
        tv.setText(title);
    }

    /**
     * 加载框
     */
    public void buildDialog(String msg) {
        if (TextUtils.isEmpty(msg)){
            msg = "加载中...";
        }
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        }
        progressDialog.setMessage(msg);
        progressDialog.setCancelable(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    /**
     * @Description: TODO 取消加载框
     * @author Sunday
     * @date 2015年12月25日
     */
    public void cancelDialog() {
        new Thread() {
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (Exception e) {
// TODO: handle exception
                } finally {
                    if (progressDialog != null)
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                }
            }
        }.start();

    }

    // 两次点击按钮之间的点击间隔不能少于1000毫秒
    private static final int MIN_CLICK_DELAY_TIME = 1000;
    private static long lastClickTime;

    /**
     * 是否是第一次点击
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = false;
        long curClickTime = System.currentTimeMillis();
        if ((curClickTime - lastClickTime) >= MIN_CLICK_DELAY_TIME) {
            flag = true;
        }
        lastClickTime = curClickTime;
        return flag;
    }


}
