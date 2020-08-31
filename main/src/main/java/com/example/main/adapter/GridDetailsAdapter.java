package com.example.main.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by 陈泽宇 on 2020/8/24
 * Describe:
 */
public class GridDetailsAdapter extends BaseQuickAdapter<User, BaseViewHolder> {

    public GridDetailsAdapter(@Nullable List<User> data) {
        super(R.layout.item_grid_details, data);
    }
    @Override
    protected void convert(@NotNull BaseViewHolder holder, User user) {

        holder.setText(R.id.name,user.getSubject().getName())
                .setText(R.id.tel,user.getSubject().getMobile());

    }
}
