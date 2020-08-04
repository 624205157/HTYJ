package com.example.main.fragment;

import com.zhhl.openlock.R;
import com.zhhl.openlock.fragment.beian.RecordFragment;

/**
 * Created by czy on 2019/6/25 16:23.
 */
public class Fragment1 extends GroupPopFragment {

    @Override
    protected void lazyLoad() {
        if (findChildFragment(RecordFragment.class) == null) {
            loadRootFragment(R.id.home, new RecordFragment());
        }
//        if (findChildFragment(RecordListFragment.class) == null) {
//            loadRootFragment(R.id.home, new RecordListFragment());
//        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        BaseFragment fragment =  findChildFragment(RecordFragment.class);
//        fragment.onActivityResult(requestCode, resultCode, data);
//    }

}
