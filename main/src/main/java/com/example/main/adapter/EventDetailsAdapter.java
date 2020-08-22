package com.example.main.adapter;

import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Event;
import com.example.main.bean.Processes;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/15 10:33.
 * describe:
 */
public class EventDetailsAdapter extends BaseQuickAdapter<Processes, BaseViewHolder> {

    public EventDetailsAdapter(List<Processes> datas) {
        super(R.layout.item_event_details, datas);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Processes processes) {
        if (TextUtils.isEmpty(processes.getContent())) {
        }
        if (TextUtils.equals(processes.getState(), "0")) {
            holder.setTextColor(R.id.state, R.color.background_solid_2);
            holder.setTextColor(R.id.transactor, R.color.background_solid_2);
            holder.setTextColor(R.id.time, R.color.background_solid_2);
        }
        holder.setText(R.id.state, processes.getContent());
        holder.setText(R.id.transactor, "(处理人: " + processes.getUserName() + ")");
        if (TextUtils.isEmpty(processes.getSettleTime())) {
            holder.setText(R.id.time, "待处理");
        } else {
            holder.setText(R.id.time, processes.getSettleTime());
        }


    }
}
