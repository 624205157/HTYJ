package com.example.main.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Task;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/12 12:49.
 * describe:
 */
public class TaskAdapter extends BaseQuickAdapter<Task,BaseViewHolder> implements LoadMoreModule {

    public TaskAdapter(List<Task> data) {
        super(R.layout.item_task, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Task data) {
        holder.setText(R.id.name,data.getName());
        holder.setText(R.id.time_limit,data.getName());
        holder.setText(R.id.time,"开始时间: "+data.getStartTime());
        if (TextUtils.equals(data.getState(),"3")){
            holder.setText(R.id.level,"未开始");
            holder.setGone(R.id.finish,true);
        }else if (TextUtils.equals(data.getState(),"2")){
            holder.setText(R.id.level,"进行中");
            holder.setGone(R.id.finish,false);
        }else if (TextUtils.equals(data.getState(),"1")){
            holder.setText(R.id.level,"已完成");
            holder.setGone(R.id.finish,false);
        }

        View view = holder.getView(R.id.cl_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();

                if (data.isHidden()){
                    data.setHidden(false);
                }else {
                    data.setHidden(true);
                }
                notifyDataSetChanged();
            }
        });

        if (data.isHidden()){
            holder.setGone(R.id.cl_2,false);
            view.setBackgroundColor(Color.parseColor("#eeeeee"));
            holder.setImageResource(R.id.hidden,R.mipmap.up_img);
        }else {
            holder.setGone(R.id.cl_2,true);
            view.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.setImageResource(R.id.hidden,R.mipmap.down_img);

        }

    }

    private void clear(){
        for (Task data:getData()){
            data.setHidden(false);
        }
    }

}
