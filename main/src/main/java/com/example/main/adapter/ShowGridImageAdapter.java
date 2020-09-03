package com.example.main.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.main.R;
import com.facebook.drawee.view.SimpleDraweeView;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.List;

public class ShowGridImageAdapter extends RecyclerView.Adapter<ShowGridImageAdapter.ViewHolder> {
    private LayoutInflater mInflater;
    private List<LocalMedia> list = new ArrayList<>();
    private Context context;


    public ShowGridImageAdapter(Context context) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }


    public void setList(List<LocalMedia> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        SimpleDraweeView mImg;
        ImageView del;

        public ViewHolder(View view) {
            super(view);
            mImg = (SimpleDraweeView) view.findViewById(R.id.fiv);
            del = (ImageView) view.findViewById(R.id.iv_del);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    /**
     * 创建ViewHolder
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.gv_filter_image,
                viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    /**
     * 设置值
     */
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.del.setVisibility(View.GONE);
        String path = "";
        LocalMedia media = list.get(position);
        if (media.isCompressed()) {
            // 压缩过
            path = media.getCompressPath();
        } else {
            // 原图
            path = media.getPath();
        }
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.color.color_f6)
                .diskCacheStrategy(DiskCacheStrategy.ALL);
        Glide.with(viewHolder.itemView.getContext())
                .load(path)
                .apply(options)
                .into(viewHolder.mImg);
        //itemView 的点击事件
        if (mItemClickListener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = viewHolder.getAdapterPosition();
                    mItemClickListener.onItemClick(adapterPosition, v);
                }
            });
        }
    }

    protected OnItemClickListener mItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mItemClickListener = listener;
    }
}
