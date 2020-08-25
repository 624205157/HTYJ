package com.example.main.fragment.grid;

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
import com.chad.library.adapter.base.listener.OnItemClickListener;
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
import com.example.main.activity.GridDetailsActivity;
import com.example.main.adapter.GridAdapter2;
import com.example.main.bean.Grid;
import com.example.main.fragment.BaseFragment;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 网格 列表
 */
public class GridFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.list)
    RecyclerView list;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private GridAdapter2 adapter;
    private List<Grid> mData = new ArrayList<>();

    Handler handler = new Handler();


    @Override
    protected int setContentView() {
        return R.layout.fragment_grid;
    }

    public static GridFragment newInstance(String pid) {
        GridFragment fragment = new GridFragment();
        Bundle bundle = new Bundle();
        bundle.putString("pid", pid);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(mSearchTesk);
                handler.postDelayed(mSearchTesk, 500);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




        adapter = new GridAdapter2(mData,getArguments().getString("pid"));
        adapter.setAnimationEnable(true);

        adapter.addChildClickViewIds(R.id.next, R.id.details);
        adapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.next) {
                    start(GridFragment.newInstance(mData.get(position).getId()));
                } else if (view.getId() == R.id.details) {
                    Intent intent = new Intent(getActivity(), GridDetailsActivity.class);
                    intent.putExtra("id",mData.get(position).getId());
                    intent.putExtra("name",mData.get(position).getName());
                    intent.putExtra("polygon",mData.get(position).getPolygon());
                    startActivity(intent);
                }
            }
        });

        list.setLayoutManager(new LinearLayoutManager(getContext()));

        list.setAdapter(adapter);

        refresh.setOnRefreshListener(this);

        getData();
    }

    SearchTask mSearchTesk = new SearchTask();

    /**
     * 搜索任务
     */
    class SearchTask implements Runnable {

        @Override
        public void run() {
            String searchStr = search.getText().toString();
            if (!TextUtils.isEmpty(searchStr)) {
                RequestParams params = new RequestParams();
                params.put("name", searchStr);
                getSearch(params);
            } else {
                mData.clear();
                getData();
            }
        }
    }

    private void getData() {

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(getArguments().getString("pid"))) {
            params.put("pid", getArguments().getString("pid"));
        }else {
            params.put("pid", "root");
        }

        RequestCenter.getDataList(UrlService.GRID, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(result.getString("data"), new TypeToken<List<Grid>>() {
                        }.getType()));
                        adapter.setList(mData);

                        adapter.notifyDataSetChanged();

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
        if (!TextUtils.isEmpty(getArguments().getString("pid"))) {
            params.put("pid", getArguments().getString("pid"));
        }else {
            params.put("pid", "root");
        }
        mData.clear();
        RequestCenter.getDataList(UrlService.GRID, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(result.getString("data"), new TypeToken<List<Grid>>() {
                        }.getType()));
                        adapter.setList(mData);
                        adapter.notifyDataSetChanged();
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

    @Override
    public void onRefresh() {
        mData.clear();
        getData();
        refresh.setRefreshing(false);
    }


}
