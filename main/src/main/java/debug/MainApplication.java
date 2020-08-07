package debug;

import com.example.commonlib.base.BaseApplication;
import com.example.commonlib.utils.ShareHelper;

/**
 * Created by 陈泽宇 on 2020/6/17
 * Describe:
 */
public class MainApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        ShareHelper.init(this);
    }
}
