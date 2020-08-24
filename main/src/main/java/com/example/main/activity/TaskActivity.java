package com.example.main.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.example.commonlib.view.ViewPagerSlide;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.PagerAdapter;
import com.example.main.fragment.home.EventListFragment;
import com.example.main.fragment.home.TaskListFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 10:35.
 * describe: 任务
 */
public class TaskActivity extends RightTitleActivity {
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
        setTitleText("任务管理");
        rightTitle("下发任务", new RightClickListener() {
            @Override
            public void callBack() {
//                startActivity(new Intent(TaskActivity.this,ReportEventActivity.class));
            }
        });
        initView();
    }

    private void initView() {

        // 创建一个集合,装填Fragment
        ArrayList<Fragment> fragments = new ArrayList<>();
        List<String> titleList = new ArrayList<>();
        fragments.add(TaskListFragment.newInstance("0"));
        fragments.add(TaskListFragment.newInstance("1"));

        titleList.add("待办事件");
        titleList.add("已办事件");

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
