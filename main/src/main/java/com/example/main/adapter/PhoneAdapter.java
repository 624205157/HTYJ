package com.example.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.People;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by czy on 2020/8/8 20:43.
 * describe:
 */
public class PhoneAdapter extends BaseQuickAdapter<People, BaseViewHolder> {


    public PhoneAdapter(@Nullable List<People> data) {
        super(R.layout.item_phone, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, People people) {
        holder.setText(R.id.name, people.getName());
        holder.setText(R.id.address, people.getAddress());
        holder.setText(R.id.tel, people.getTel());
    }
}
