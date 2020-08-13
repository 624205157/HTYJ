package com.czy.htyj;

import androidx.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.utils.ShareHelper;
import com.facebook.drawee.backends.pipeline.Fresco;

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
    }
}
