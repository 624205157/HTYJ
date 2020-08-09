package com.example.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Grid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by czy on 2020/8/8 20:43.
 * describe:
 */
public class GridAdapter extends BaseQuickAdapter<Grid, BaseViewHolder> {


    public GridAdapter(@Nullable List<Grid> data) {
        super(R.layout.item_grid, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Grid grid) {
        holder.setText(R.id.name,grid.getName());
        holder.setText(R.id.address,grid.getAddress());
    }
}
