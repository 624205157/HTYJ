package com.example.main.fragment;

import com.zhhl.openlock.R;
import com.zhhl.openlock.fragment.paidan.OrdersFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment3 extends GroupPopFragment {


    @Override
    protected void lazyLoad() {
        if (findChildFragment(OrdersFragment.class) == null) {
            loadRootFragment(R.id.home, new OrdersFragment());
        }
    }

}
