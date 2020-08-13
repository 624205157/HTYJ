package com.example.main.fragment;


import android.os.Bundle;

import com.example.main.R;
import com.example.main.fragment.phone.DepartmentFragment;
import com.example.main.fragment.phone.PhoneFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment3 extends GroupPopFragment {


    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        if (findChildFragment(DepartmentFragment.class) == null) {
            loadRootFragment(R.id.home, new DepartmentFragment());
        }
    }

}
