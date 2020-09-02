package com.example.main.activity;

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
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.listener.CallPhoneListener;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.commonlib.view.MyDialog;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.adapter.PlanAdapter;
import com.example.main.adapter.UpdateEnterpriseAdapter;
import com.example.main.bean.DeleteData;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Plan;
import com.example.main.fragment.home.UpdateEnterpriseFragment;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by 陈泽宇 on 2020/8/24
 * Describe: 预案列表
 */
public class PlanListActivity extends BaseActivity implements OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private PlanAdapter mAdapter;
    private List<Plan> mData = new ArrayList<>();

    Handler handler = new Handler();

    private int pageNum = 0;


    @Override
    protected int setContentView() {
        return R.layout.activity_plan_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(mSearchTesk);
                handler.postDelayed(mSearchTesk, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter = new PlanAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.addChildClickViewIds(R.id.look);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.look) {
//                    Intent intent = new Intent(PlanListActivity.this, UpdateEnterpriseActivity.class);
//                    intent.putExtra("id", mData.get(position).getId());
//                    startActivity(intent);
                }
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(this));

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

    private void getData() {
        RequestParams params = new RequestParams();
        try {
            params.put("pageNum", ++pageNum);
            params.put("pageable", "y");
        } catch (Exception e) {
            e.printStackTrace();
        }


        RequestCenter.getDataList(UrlService.PLAN, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(data.getString("list"), new TypeToken<List<Plan>>() {
                        }.getType()));
                        mAdapter.setList(mData);

                        mAdapter.notifyDataSetChanged();

                    }

                    if (data.getInt("pages") == pageNum) {
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

    private void getSearch(RequestParams params) {
        mData.clear();
        RequestCenter.getDataList(UrlService.PLAN, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(result.getString("data"), new TypeToken<List<Plan>>() {
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
