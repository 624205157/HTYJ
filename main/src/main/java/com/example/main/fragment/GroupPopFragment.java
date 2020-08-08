package com.example.main.fragment;

import android.os.Bundle;
import android.widget.Toast;

import com.example.main.R;


/**
 * Created by czy on 2019/6/26 13:01.
 */
public class GroupPopFragment extends BaseFragment {

    // 再点一次退出程序时间设置
    private static final long WAIT_TIME = 2000L;
    private long TOUCH_TIME = 0;
    /**
     * 处理回退事件
     *
     * @return
     */
    @Override
    public boolean onBackPressedSupport() {
        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            popChild();
        }else if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
//            SangforAuthManager.getInstance().vpnLogout();
            mActivity.finish();
//            System.exit(0);
        } else {
            TOUCH_TIME = System.currentTimeMillis();
            Toast.makeText(getContext(),"再按一次退出程序", Toast.LENGTH_SHORT).show();
        }
        return true;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_custom;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

    }
}