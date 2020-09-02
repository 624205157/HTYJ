package com.example.main.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;

import butterknife.BindView;

/**
 * Created by 陈泽宇 on 2020/9/2
 * Describe: 预案详情
 */
public class PlanDetailsActivity extends BaseActivity {


    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.type)
    TextView type;
    @BindView(R2.id.create_time)
    TextView createTime;
    @BindView(R2.id.count)
    TextView count;
    @BindView(R2.id.photo_recycler1)
    RecyclerView photoRecycler1;

    @Override
    protected int setContentView() {
        return R.layout.activity_plan_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("预案详情");
    }
}
