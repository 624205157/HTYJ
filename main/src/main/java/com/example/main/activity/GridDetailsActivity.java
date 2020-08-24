package com.example.main.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.GridDetailsAdapter;
import com.example.main.bean.Grid;
import com.example.main.bean.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 陈泽宇 on 2020/8/24
 * Describe: 网格详情
 */
public class GridDetailsActivity extends RightTitleActivity {
    @BindView(R2.id.list)
    RecyclerView list;

    private Grid grid;
    private GridDetailsAdapter adapter;
    private List<User> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_grid_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        grid = getIntent().getParcelableExtra("grid");

        addBack();
        setTitleText(grid.getName());
        rightTitle("中心点", new RightClickListener() {
            @Override
            public void callBack() {
//                ARouter.getInstance().build("/map/navigation")
//                        .withParcelable("address", )
//                        .navigation();
            }
        });

        mData.addAll(grid.getUsers());

        adapter = new GridDetailsAdapter(mData);
        adapter.setAnimationEnable(true);

        adapter.addChildClickViewIds(R.id.tel);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.tel) {

                }
            }
        });

        list.setLayoutManager(new GridLayoutManager(this,2));

        list.setAdapter(adapter);

    }
}
