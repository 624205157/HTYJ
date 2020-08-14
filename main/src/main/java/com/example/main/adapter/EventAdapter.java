package com.example.main.adapter;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Event;
import com.example.main.bean.Resources;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/12 12:49.
 * describe:
 */
public class EventAdapter extends BaseQuickAdapter<Event,BaseViewHolder> {

    public EventAdapter(List<Event> datas) {
        super(R.layout.item_event, datas);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Event data) {
        holder.setText(R.id.name,data.getName());
        holder.setText(R.id.address,"发生地址: "+data.getAddress());
        holder.setText(R.id.type,"事件类别: "+data.getType());
        holder.setText(R.id.level,"紧急程度: "+data.getLevel());
        if (TextUtils.equals(data.getState(),"0")){
            holder.setText(R.id.level,"进展状态: 处理中");
            holder.setText(R.id.check,"详情及处理");
            holder.setGone(R.id.wait,false);
            holder.setGone(R.id.finish,true);
        }else if (TextUtils.equals(data.getState(),"1")){
            holder.setText(R.id.level,"进展状态: 已办结");
            holder.setText(R.id.check,"查看详情");
            holder.setGone(R.id.wait,true);
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
        for (Event event:getData()){
            event.setHidden(false);
        }
    }

}
