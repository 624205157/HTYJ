package debug;

import androidx.multidex.MultiDex;

import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.utils.ShareHelper;
import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by 陈泽宇 on 2020/6/17
 * Describe:
 */
public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareHelper.init(this);
        // 初始化MultiDex
        MultiDex.install(this);
        Fresco.initialize(this);

    }
}
