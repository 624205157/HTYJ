package com.example.main.activity;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.bean.Plan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

    private Plan plan;

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
        }


    }
}
