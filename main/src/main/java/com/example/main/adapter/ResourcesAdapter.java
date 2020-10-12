package com.example.main.adapter;

import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Resources;
import com.example.main.listener.ClickCallBack;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by 陈泽宇 on 2020/10/11
 * Describe:
 */
public class ResourcesAdapter extends BaseQuickAdapter<Resources, BaseViewHolder> implements LoadMoreModule {

    private ClickCallBack clickCallBack;

    public ResourcesAdapter(List<Resources> data, ClickCallBack clickCallBack) {
        super(R.layout.item_resourses, data);
        this.clickCallBack = clickCallBack;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Resources resources) {
        holder.setText(R.id.name, resources.getName());
        holder.setText(R.id.type,  resources.getType() );
        holder.setText(R.id.total, resources.getTotal());
        holder.setText(R.id.surplus,  resources.getSurplus());

        TextView slide1 = holder.getView(R.id.slide_1);
        TextView slide2 = holder.getView(R.id.slide_2);


        slide1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.update(holder.getLayoutPosition());
            }
        });

        slide2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickCallBack.delete(holder.getLayoutPosition());
            }
        });


    }
}
