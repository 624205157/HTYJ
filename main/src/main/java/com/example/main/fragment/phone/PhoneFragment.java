package com.example.main.fragment.phone;

import android.content.Intent;
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
import com.example.main.activity.phone.PersonalInfoActivity;
import com.example.main.adapter.PhoneAdapter;
import com.example.main.bean.People;
import com.example.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/5 10:55.
 * describe: 通讯录
 */
public class PhoneFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R2.id.list)
    RecyclerView list;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    public static PhoneFragment newInstance(String department){
        PhoneFragment fragment = new PhoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString("department",department);
        fragment.setArguments(bundle);
        return fragment;
    }

    private PhoneAdapter adapter;
    private List<People> mData = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_phone;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        adapter = new PhoneAdapter(mData);
        adapter.setAnimationEnable(true);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                startActivity(new Intent(getActivity(), PersonalInfoActivity.class));
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
        People people = new People();
        people.setName("莎啦啦");
        people.setAddress("(前进街道)");
        people.setTel("13524564182");
        mData.add(people);
        mData.add(people);
        mData.add(people);
        mData.add(people);
        mData.add(people);
        mData.add(people);

        adapter.setList(mData);

        adapter.notifyDataSetChanged();

    }

    @Override
    public void onRefresh() {
        getData();
        refresh.setRefreshing(false);
    }
}
