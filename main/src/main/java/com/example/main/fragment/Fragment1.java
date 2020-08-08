package com.example.main.fragment;


import android.os.Bundle;

import com.example.main.R;
import com.example.main.fragment.home.HomeFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment1 extends GroupPopFragment {

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        if (findChildFragment(HomeFragment.class) == null) {
            loadRootFragment(R.id.home, new HomeFragment());
        }
    }


}
