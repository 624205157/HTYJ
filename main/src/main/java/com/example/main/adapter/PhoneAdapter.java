package com.example.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Grid;
import com.example.main.bean.Subject;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by czy on 2020/8/8 20:43.
 * describe:
 */
public class PhoneAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    private Context context;

    public PhoneAdapter(@Nullable List<Subject> data, Context context) {
        super(R.layout.item_phone, data);
        this.context = context;
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Subject user) {
        holder.setText(R.id.name, user.getName());

        holder.setText(R.id.tel, user.getMobile());

        List<Grid> grids = new ArrayList<>();
        grids.addAll(user.getGrids());
        String departmentStr = "";
        for (Grid grid : grids) {
            departmentStr = departmentStr + "-" + grid.getName();
        }
        if (!TextUtils.isEmpty(departmentStr)) {
            holder.setText(R.id.address, "(" + departmentStr.substring(1) + ")");
        }else {
            holder.setText(R.id.address,"");
        }
        Glide.with(context).load(user.getAvatar()).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into((ImageView) holder.getView(R.id.iv));
    }
}
