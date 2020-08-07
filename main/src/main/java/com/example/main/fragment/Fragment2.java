package com.example.main.fragment;


import com.example.main.R;
import com.example.main.fragment.grid.GridFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment2 extends GroupPopFragment {

    @Override
    protected void lazyLoad() {
        if (findChildFragment(GridFragment.class) == null) {
            loadRootFragment(R.id.home, new GridFragment());
        }
    }

}
