package com.example.main.adapter;

import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.module.LoadMoreModule;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.example.main.R;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Grid;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by czy on 2020/8/12 12:49.
 * describe:
 */
public class UpdateEnterpriseAdapter extends BaseQuickAdapter<Enterprise,BaseViewHolder>  implements LoadMoreModule {

    public UpdateEnterpriseAdapter(List<Enterprise> data) {
        super(R.layout.item_enterprise, data);
    }

    @Override
    protected void convert(@NotNull BaseViewHolder holder, Enterprise enterprise) {
        holder.setText(R.id.name,enterprise.getName());
        holder.setText(R.id.address,"地址: "+enterprise.getAddress());
        holder.setText(R.id.social_credit_code,"社会信用代码: "+enterprise.getSocialCreditCode());
        holder.setText(R.id.legal_person,"法人: "+enterprise.getLegalPerson());
        if (enterprise.getIsStart() == 0){
            holder.setText(R.id.is_start,"否");
        }else {
            holder.setText(R.id.is_start,"是");
        }


        View view = holder.getView(R.id.cl_1);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();

                if (enterprise.isHidden()){
                    enterprise.setHidden(false);
                }else {
                    enterprise.setHidden(true);
                }
                notifyDataSetChanged();
            }
        });

        if (enterprise.isHidden()){
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
        for (Enterprise enterprise:getData()){
            enterprise.setHidden(false);
        }
    }

}
