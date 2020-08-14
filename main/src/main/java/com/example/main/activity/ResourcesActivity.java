package com.example.main.activity;

import android.os.Bundle;

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
    @BindView(R2.id.tab)
    TabLayout tabView;
    @BindView(R2.id.fragment)
    ViewPagerSlide pager;

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

        titleList.add("新增应急资源");
        titleList.add("维护应急资源");
        for (int i = 0; i < titleList.size(); i++) {
            tabView.addTab(tabView.newTab().setText(titleList.get(i)));
        }
        PagerAdapter myPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        myPagerAdapter.setFragments(fragments);
        myPagerAdapter.setTitles(titleList);
        pager.setAdapter(myPagerAdapter);
        // 使用 TabLayout 和 ViewPager 相关联
        tabView.setupWithViewPager(pager);

    }

}
