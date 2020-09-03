package com.example.main.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.PlanAdapter;
import com.example.main.adapter.PlanDetailsAdapter;
import com.example.main.bean.MyFiles;
import com.example.main.bean.Plan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

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
    RecyclerView recyclerview;

    private Plan plan;

    private PlanDetailsAdapter mAdapter;
    private List<MyFiles> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_plan_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("预案详情");

        String json = getIntent().getStringExtra("json");
        Gson gson = new Gson();
        plan = gson.fromJson(json,new TypeToken<Plan>(){}.getType());
        if (plan != null) {
            name.setText("预案名称: "+plan.getName());
            createTime.setText("创建时间: "+plan.getCreateTime());
            count.setText(Html.fromHtml(plan.getContent()));

            String typeStr = "";
            for (int i = plan.getCategory().size() - 1; i >= 0; i--) {
                typeStr =typeStr + "-" + plan.getCategory().get(i).getName() ;
            }

            type.setText(typeStr.substring(1));
            mData.addAll(plan.getAttachments());
        }

        mAdapter = new PlanDetailsAdapter(mData);

        mAdapter.setAnimationEnable(true);

        mAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

                Uri uri = Uri.parse(mData.get(position).getUrl());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        recyclerview.setAdapter(mAdapter);


    }
}
