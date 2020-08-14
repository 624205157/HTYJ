package com.example.main.fragment.home;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.EventAdapter;
import com.example.main.bean.Event;
import com.example.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 11:32.
 * describe: 事件列表
 */
public class EventListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private String state;

    public static EventListFragment newInstance(String state){
        EventListFragment fragment = new EventListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state",state);
        fragment.setArguments(bundle);
        return fragment;
    }

    private EventAdapter mAdapter;
    private List<Event> mData = new ArrayList<>();

    Handler handler = new Handler();

    @Override
    protected int setContentView() {
        return R.layout.fragment_update_enterprise;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

        state = getArguments().getString("state");
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    handler.removeCallbacks(mSearchTesk);
                    handler.postDelayed(mSearchTesk, 500);
                } else {
                    handler.removeCallbacks(mSearchTesk);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter = new EventAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.addChildClickViewIds(R.id.navigation,R.id.check_v);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.navigation) {
                    showToast("导航");
                    ARouter.getInstance().build("/map/navigation")
                            .withParcelable("address",mData.get(position).getPoint())
                            .navigation();

                }else if (view.getId() == R.id.check_v) {

                }
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerview.setAdapter(mAdapter);


        refresh.setOnRefreshListener(this);


    }

    SearchTask mSearchTesk = new SearchTask();

    @Override
    public void onRefresh() {
        getData();
        refresh.setRefreshing(false);
    }

    /**
     * 搜索任务
     */
    class SearchTask implements Runnable {

        @Override
        public void run() {
            showToast("搜索" + search.getText().toString());
        }
    }

    private void getData(){
        for (int i = 1;i<9;i++) {
            Event enterprise = new Event();
            enterprise.setName("突发事件" + i);
            enterprise.setAddress("海南省海口市华龙区奥术大师大大所大所奥术大师大所"+ i);
            enterprise.setType("自然灾害"+ i);
            enterprise.setLevel("一般"+ i);
            enterprise.setState(state);
            enterprise.setLatitude(43.888824);
            enterprise.setLongitude(125.300985);
            mData.add(enterprise);
        }

        mAdapter.setList(mData);

        mAdapter.notifyDataSetChanged();
    }

}
