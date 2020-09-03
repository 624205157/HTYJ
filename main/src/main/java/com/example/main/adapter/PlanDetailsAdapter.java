package com.example.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.MyFiles;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/9/3 16:02.
 * describe:
 */
public class PlanDetailsAdapter extends BaseQuickAdapter<MyFiles, BaseViewHolder> {

    public PlanDetailsAdapter(List<MyFiles> mData){
        super(R.layout.item_plan_details,mData);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, MyFiles myFiles) {
        holder.setText(R.id.name,myFiles.getName());
    }
}
