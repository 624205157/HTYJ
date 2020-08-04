package com.example.main.fragment;

import com.zhhl.openlock.R;
import com.zhhl.openlock.fragment.qiye.FirmFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment2 extends GroupPopFragment {

    @Override
    protected void lazyLoad() {
        if (findChildFragment(FirmFragment.class) == null) {
            loadRootFragment(R.id.home, new FirmFragment());
        }
    }

}
