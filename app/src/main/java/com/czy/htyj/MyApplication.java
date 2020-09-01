package com.czy.htyj;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.ServiceUtils;
import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.utils.ShareHelper;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.liteav.login.model.ProfileManager;

import update.UpdateAppUtils;

/**
 * Created by czy on 2020/8/12 21:15.
 * describe:
 */
public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG){
            ARouter.openDebug();
            ARouter.openLog();
        }
        ARouter.init(this);
        ShareHelper.init(this);
        // 初始化MultiDex
        MultiDex.install(this);
        Fresco.initialize(this);

        //app更新初始化
        UpdateAppUtils.init(this);

        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {
                if (ProfileManager.getInstance().isLogin()
                        && !ServiceUtils.isServiceRunning(CallService.class)) {
                    startCallService();
                }
            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }

    private void startCallService() {
        Intent intent = new Intent(this, CallService.class);
        startService(intent);
    }
}
