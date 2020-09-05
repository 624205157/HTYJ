package com.example.main.fragment.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
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
import com.chad.library.adapter.base.listener.OnLoadMoreListener;
import com.example.commonlib.listener.CallPhoneListener;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.commonlib.view.MyDialog;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.activity.TaskDetailsActivity;
import com.example.main.adapter.TaskAdapter;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Task;
import com.example.main.fragment.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 11:32.
 * describe: 任务列表
 */
public class TaskListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
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

    private int pageNum = 0;

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

        mAdapter = new TaskAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.addChildClickViewIds(R.id.update);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.update) {
                    showToast("修改");
                    Intent intent = new Intent(getActivity(), TaskDetailsActivity.class);
                    Gson gson = new Gson();
                    intent.putExtra("task",gson.toJson(mData.get(position)));
                    startActivity(intent);
                }
            }
        });
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerview.setAdapter(mAdapter);


        refresh.setOnRefreshListener(this);

        getData();

    }

    SearchTask mSearchTesk = new SearchTask();

    @Override
    public void onRefresh() {
        pageNum = 0;
        mData.clear();
        getData();
        refresh.setRefreshing(false);
    }

    @Override
    public void onLoadMore() {
        getData();
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

    private int pages = 1;

    private void getData() {

        RequestParams params = new RequestParams();
        try {
            params.put("current", ++pageNum);
            params.put("pageable", "y");

            if (pages < pageNum) {
                mAdapter.getLoadMoreModule().setEnableLoadMore(false);
                return;
            }

            params.put("states", URLEncoder.encode(state, "UTF-8"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestCenter.getDataList(UrlService.TASK, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(data.getString("list"), new TypeToken<List<Task>>() {
                        }.getType()));
                        mAdapter.setList(mData);

                        mAdapter.notifyDataSetChanged();

                    }

                    pages = data.getInt("pages");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {
                showToast(responseObj.getMessage());
            }
        });

    }

}
