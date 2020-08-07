package com.example.main.fragment;


import com.example.main.R;
import com.example.main.fragment.phone.PhoneFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment3 extends GroupPopFragment {


    @Override
    protected void lazyLoad() {
        if (findChildFragment(PhoneFragment.class) == null) {
            loadRootFragment(R.id.home, new PhoneFragment());
        }
    }

}
