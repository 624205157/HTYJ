package com.example.main.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Grid;
import com.example.main.bean.Resources;
import com.example.main.bean.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Created by czy on 2020/8/8 20:43.
 * describe:
 */
public class GridAdapter2 extends BaseQuickAdapter<Grid, BaseViewHolder>  {

    private String pid;

    public GridAdapter2(@Nullable List<Grid> data,String pid) {
        super(R.layout.item_grid2, data);
        this.pid = pid;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Grid grid) {

        holder.setText(R.id.name,grid.getName());
        String users = "";
        for (User user : grid.getUsers()){
            users = users + user.getSubject().getName() + "、";
        }
        if (!TextUtils.isEmpty(users)){
            users = users.substring(0, users.length() - 1);
        }else {
            users = "暂无";
        }
        holder.setText(R.id.users,"负责人: " +users);

        if (!TextUtils.isEmpty(pid)){
            holder.setGone(R.id.next,true);
        }else {
            holder.setGone(R.id.next,false);
        }

        View view = holder.getView(R.id.cl_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear(grid);

                if (grid.isHidden()) {
                    grid.setHidden(false);
                } else {
                    grid.setHidden(true);
                }
                notifyDataSetChanged();
            }
        });

        if (grid.isHidden()) {
            holder.setGone(R.id.cl_2, false);
            view.setBackgroundColor(Color.parseColor("#eeeeee"));
            holder.setImageResource(R.id.hidden, R.mipmap.up_img);
        } else {
            holder.setGone(R.id.cl_2, true);
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.setImageResource(R.id.hidden, R.mipmap.down_img);

        }

    }

    private void clear(Grid data) {
        for (Grid grid : getData()) {
            if (grid != data)
                grid.setHidden(false);
        }
    }

}
