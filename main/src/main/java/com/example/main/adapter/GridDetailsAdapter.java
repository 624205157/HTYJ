package com.example.main.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Subject;
import com.example.main.bean.User;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 陈泽宇 on 2020/8/24
 * Describe:
 */
public class GridDetailsAdapter extends BaseQuickAdapter<Subject, BaseViewHolder> {

    private Context context;

    public GridDetailsAdapter(Context context, @Nullable List<Subject> data) {
        super(R.layout.item_grid_details, data);
        this.context = context;
    }
    @Override
    protected void convert(@NotNull BaseViewHolder holder, Subject user) {

        holder.setText(R.id.name,user.getName())
                .setText(R.id.tel,user.getMobile());

        CircleImageView headImg = holder.getView(R.id.head_img);
        Glide.with(context).load(user.getAvatar()).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into(headImg);

    }
}
