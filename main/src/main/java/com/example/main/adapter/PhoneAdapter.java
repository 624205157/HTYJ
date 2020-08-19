package com.example.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.ImageList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by czy on 2020/8/8 20:43.
 * describe:
 */
public class PhoneAdapter extends BaseQuickAdapter<ImageList.People, BaseViewHolder> {


    public PhoneAdapter(@Nullable List<ImageList.People> data) {
        super(R.layout.item_phone, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, ImageList.People people) {
        holder.setText(R.id.name, people.getName());
        holder.setText(R.id.address, people.getAddress());
        holder.setText(R.id.tel, people.getTel());
    }
}
