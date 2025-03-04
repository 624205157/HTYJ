package com.example.main.activity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.view.ViewPagerSlide;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.PagerAdapter;
import com.example.main.fragment.home.AddEnterpriseFragment;
import com.example.main.fragment.home.AddResourcesFragment;
import com.example.main.fragment.home.UpdateEnterpriseFragment;
import com.example.main.fragment.home.UpdateResourcesFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 10:35.
 * describe: 资源
 */
public class ResourcesActivity extends BaseActivity {
    @BindView(R2.id.fragment)
    ViewPagerSlide pager;
    @BindView(R2.id.tab)
    RadioGroup tab;
    @BindView(R2.id.tab_1)
    RadioButton tab1;
    @BindView(R2.id.tab_2)
    RadioButton tab2;
    @Override
    protected int setContentView() {
        return R.layout.activity_enterprise;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("应急资源管理");
        initView();
    }

    private void initView() {

        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        fragments.add(new AddResourcesFragment());
        fragments.add(new UpdateResourcesFragment());

        tab1.setText("新增应急资源");
        tab2.setText("维护应急资源");

        PagerAdapter myPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        myPagerAdapter.setFragments(fragments);
        myPagerAdapter.setTitles(titleList);
        pager.setAdapter(myPagerAdapter);

        tab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.tab_1) {
                    pager.setCurrentItem(0);
                } else if (checkedId == R.id.tab_2) {
                    pager.setCurrentItem(1);
                }
            }
        });
    }

}
