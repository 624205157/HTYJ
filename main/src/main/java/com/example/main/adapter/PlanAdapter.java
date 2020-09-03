package com.example.main.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Plan;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/12 12:49.
 * describe:
 */
public class PlanAdapter extends BaseQuickAdapter<Plan, BaseViewHolder> implements LoadMoreModule {

    public PlanAdapter(List<Plan> data) {
        super(R.layout.item_plan, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Plan plan) {
        holder.setText(R.id.name, plan.getName());
        holder.setText(R.id.create_time, "创建时间: " + plan.getCreateTime());
        String type = "";
        for (int i = plan.getCategory().size()-1; i >= 0; i--) {
            type = type  + "-" + plan.getCategory().get(i).getName() ;
        }

        holder.setText(R.id.type, type.substring(1));


        View view = holder.getView(R.id.cl_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(plan);

                if (plan.isHidden()) {
                    plan.setHidden(false);
                } else {
                    plan.setHidden(true);
                }
                notifyDataSetChanged();
            }
        });

        if (plan.isHidden()) {
            holder.setGone(R.id.cl_2, false);
            view.setBackgroundColor(Color.parseColor("#eeeeee"));
            holder.setImageResource(R.id.hidden, R.mipmap.up_img);
        } else {
            holder.setGone(R.id.cl_2, true);
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.setImageResource(R.id.hidden, R.mipmap.down_img);

        }

    }

    private void clear(Plan data) {
        for (Plan plan : getData()) {
            if (plan != data)
                plan.setHidden(false);
        }
    }

}
