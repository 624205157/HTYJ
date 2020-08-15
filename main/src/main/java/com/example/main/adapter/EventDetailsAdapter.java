package com.example.main.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Event;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/15 10:33.
 * describe:
 */
public class EventDetailsAdapter extends BaseQuickAdapter<Event, BaseViewHolder> {

    public EventDetailsAdapter (List<Event> datas) {
        super(R.layout.item_event_details, datas);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Event event) {
        holder.setText(R.id.state,event.getState())
                .setText(R.id.transactor,event.getTransactor())
                .setText(R.id.time,event.getTime());
    }
}
