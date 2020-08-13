package com.example.main.fragment.grid;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.GridAdapter;
import com.example.main.bean.Grid;
import com.example.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GridFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {


    @BindView(R2.id.list)
    RecyclerView list;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private GridAdapter adapter;
    private List<Grid> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_grid;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        adapter = new GridAdapter(mData);
        adapter.setAnimationEnable(true);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {

            }
        });
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        list.setAdapter(adapter);

        refresh.setOnRefreshListener(this);

        getData();
    }

    @OnClick(R2.id.search)
    public void onViewClicked() {


    }

    private void getData() {
        Grid grid = new Grid();
        grid.setName("qweqweqweqweqe");
        grid.setAddress("发沙发沙发沙发沙发");
        mData.add(grid);
        mData.add(grid);
        mData.add(grid);
        mData.add(grid);
        mData.add(grid);
        mData.add(grid);

        adapter.setList(mData);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        getData();
        refresh.setRefreshing(false);
    }
}
