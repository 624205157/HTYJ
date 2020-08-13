package com.example.main.fragment.phone;

import android.os.Bundle;
import android.view.View;

import com.example.main.R;
import com.example.main.R2;
import com.example.main.fragment.BaseFragment;

import butterknife.OnClick;

/**
 * Created by czy on 2020/8/13 11:02.
 * describe:
 */
public class DepartmentFragment extends BaseFragment {
    @Override
    protected int setContentView() {
        return R.layout.fragment_department;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

    }

    @OnClick({R2.id.search, R2.id.department_1, R2.id.department_2, R2.id.department_3, R2.id.department_4})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.search) {
        } else if (id == R.id.department_1) {
            start(PhoneFragment.newInstance("1"));
        } else if (id == R.id.department_2) {
            start(PhoneFragment.newInstance("2"));
        } else if (id == R.id.department_3) {
            start(PhoneFragment.newInstance("3"));
        } else if (id == R.id.department_4) {
            start(PhoneFragment.newInstance("4"));
        }
    }
}
