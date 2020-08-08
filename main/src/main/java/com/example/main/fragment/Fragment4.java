package com.example.main.fragment;


import android.os.Bundle;

import com.example.main.R;
import com.example.main.fragment.my.MyFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment4 extends GroupPopFragment {


    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        if (findChildFragment(MyFragment.class) == null) {
            loadRootFragment(R.id.home, new MyFragment());
        }
    }

}
