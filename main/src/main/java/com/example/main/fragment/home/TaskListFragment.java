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
import com.example.commonlib.listener.CallPhoneListener;
import com.example.commonlib.view.MyDialog;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.adapter.TaskAdapter;
import com.example.main.bean.Task;
import com.example.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 11:32.
 * describe: 事件列表
 */
public class TaskListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private String state;

    public static TaskListFragment newInstance(String state) {
        TaskListFragment fragment = new TaskListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("state", state);
        fragment.setArguments(bundle);
        return fragment;
    }

    private TaskAdapter mAdapter;
    private List<Task> mData = new ArrayList<>();

    Handler handler = new Handler();

    @Override
    protected int setContentView() {
        return R.layout.fragment_update_enterprise;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

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

        mAdapter = new TaskAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.addChildClickViewIds(R.id.update, R.id.del);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.update) {
                    showToast("修改");
                }
                if (view.getId() == R.id.del) {
                    new MyDialog(mContext)
                            .setTitleStr("提示")
                            .setMessageStr("您确定要删除吗?")
                            .setButtonText("取消", "确定")
                            .setClickListener(new CallPhoneListener() {
                                @Override
                                public void onClick(int var1) {
                                    if (var1 == 2) {
                                        showToast("确定删除");
                                        adapter.remove(mData.get(position));
//                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            }).show();
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

    private void getData() {
        for (int i = 1; i < 9; i++) {
            Task data = new Task();
            data.setName("检查XXX" + i);
            data.setTime("2020年8月16日13:35:19");
            data.setState(state);
            mData.add(data);
        }

        mAdapter.setList(mData);

        mAdapter.notifyDataSetChanged();
    }

}
