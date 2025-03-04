package com.example.main.adapter;

import android.graphics.Color;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Resources;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/12 12:49.
 * describe:
 */
public class UpdateResourcesAdapter extends BaseQuickAdapter<Resources, BaseViewHolder> implements LoadMoreModule {

    public UpdateResourcesAdapter(List<Resources> data) {
        super(R.layout.item_resources, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Resources resources) {
        holder.setText(R.id.address,  resources.getAddress());
        holder.setText(R.id.type, "种类: " + resources.getCategories() + "种");
        holder.setText(R.id.total_num, "总数量: " + resources.getTotal());
        holder.setText(R.id.surplus, "剩余数量: " + resources.getSurplus());

        View view = holder.getView(R.id.cl_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(resources);

                if (resources.isHidden()) {
                    resources.setHidden(false);
                } else {
                    resources.setHidden(true);
                }
                notifyDataSetChanged();
            }
        });

        if (resources.isHidden()) {
            holder.setGone(R.id.cl_2, false);
            view.setBackgroundColor(Color.parseColor("#eeeeee"));
            holder.setImageResource(R.id.hidden, R.mipmap.up_img);
        } else {
            holder.setGone(R.id.cl_2, true);
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.setImageResource(R.id.hidden, R.mipmap.down_img);

        }

    }

    private void clear(Resources data) {
        for (Resources resources : getData()) {
            if (resources != data)
                resources.setHidden(false);
        }
    }

}
