package com.example.main.activity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈泽宇 on 2020/9/1
 * Describe: 下发任务
 */
public class IssueTaskActivity extends BaseActivity {
    @BindView(R2.id.name)
    EditText name;
    @BindView(R2.id.address)
    EditText address;
    @BindView(R2.id.safety)
    EditText safety;
    @BindView(R2.id.change)
    EditText change;
    @BindView(R2.id.time)
    TextView time;
    @BindView(R2.id.intact)
    TextView intact;
    @BindView(R2.id.is_intact)
    Switch isIntact;
    @BindView(R2.id.photo_recycler1)
    RecyclerView photoRecycler1;

    @Override
    protected int setContentView() {
        return R.layout.activity_issue_task;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {

    }

    @OnClick(R2.id.time)
    public void onViewClicked() {

    }
}
