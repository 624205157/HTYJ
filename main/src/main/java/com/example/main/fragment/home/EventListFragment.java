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
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.activity.EventDetailsActivity;
import com.example.main.adapter.EventAdapter;
import com.example.main.bean.Event;
import com.example.main.bean.Resources;
import com.example.main.fragment.BaseFragment;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 11:32.
 * describe: 事件列表
 */
public class EventListFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
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

        mAdapter = new EventAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
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
                    Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                    intent.putExtra("id",mData.get(position).getId());
                    intent.putExtra("state",mData.get(position).getState());
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
            String searchStr = search.getText().toString();
            if (!TextUtils.isEmpty(searchStr)) {
                RequestParams params = new RequestParams();
                if (Utils.isContainChinese(searchStr)) {
                    params.put("name", searchStr);
                } else {
                    params.put("sc_code", searchStr);
                }
                params.put("pageable", "n");
                getSearch(params);
            } else {
                pageNum = 0;
                mData.clear();
                getData();
            }
        }
    }

    private int pages = 1;

    private void getData(){

        RequestParams params = new RequestParams();
        try {
            params.put("pageNum", ++pageNum);
            params.put("pageable", "y");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pages < pageNum) {
            mAdapter.getLoadMoreModule().setEnableLoadMore(false);
            return;
        }
        params.put("state",state);

        RequestCenter.getDataList(UrlService.EVENT, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(data.getString("list"), new TypeToken<List<Event>>() {
                        }.getType()));
                        mAdapter.setList(mData);

                        mAdapter.notifyDataSetChanged();

                    }else {
                        showToast(data.getString("msg"));
                    }

                    pages = data.getInt("pages");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });


    }

    private void getSearch(RequestParams params) {
        mData.clear();
        RequestCenter.getDataList(UrlService.EVENT, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(result.getString("data"), new TypeToken<List<Event>>() {
                        }.getType()));
                        mAdapter.setList(mData);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.getLoadMoreModule().setEnableLoadMore(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });
    }


}
