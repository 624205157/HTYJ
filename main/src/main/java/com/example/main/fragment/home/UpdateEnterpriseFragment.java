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
import com.example.main.activity.UpdateEnterpriseActivity;
import com.example.main.adapter.UpdateEnterpriseAdapter;
import com.example.main.bean.DeleteData;
import com.example.main.bean.Enterprise;
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
 * describe: 企业信息维护
 */
public class UpdateEnterpriseFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, OnLoadMoreListener {
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private UpdateEnterpriseAdapter mAdapter;
    private List<Enterprise> mData = new ArrayList<>();

    Handler handler = new Handler();

    private int pageNum = 0;

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
                handler.removeCallbacks(mSearchTesk);
                handler.postDelayed(mSearchTesk, 1000);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mAdapter = new UpdateEnterpriseAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.getLoadMoreModule().setOnLoadMoreListener(this);
        mAdapter.addChildClickViewIds(R.id.navigation, R.id.update, R.id.del);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.navigation) {
                    showToast("导航");
                    ARouter.getInstance().build("/map/navigation")
                            .withParcelable("address", mData.get(position).getPoint())
                            .navigation();

                } else if (view.getId() == R.id.update) {
                    Intent intent = new Intent(getActivity(), UpdateEnterpriseActivity.class);
                    intent.putExtra("id", mData.get(position).getId());
                    startActivity(intent);
                }
                if (view.getId() == R.id.del) {
                    MyDialog dialog = new MyDialog(mContext);
                    dialog.setTitleStr("提示");
                    dialog.setMessageStr("您确定要删除吗?");
                    dialog.setButtonText("取消", "确定");
                    dialog.setClickListener(new CallPhoneListener() {
                        @Override
                        public void onClick(int var1) {
                            if (var1 == 2) {
                                deleteData(mData.get(position).getId(), adapter, position);
//                                        adapter.notifyDataSetChanged();
                            }
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            }
        });

        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerview.setAdapter(mAdapter);


        refresh.setOnRefreshListener(this);
        getData();

    }

    private void deleteData(String id, BaseQuickAdapter adapter, int position) {
        DeleteData deleteData = new DeleteData(id);
        Gson gson = new Gson();
        String jsonStr = gson.toJson(deleteData);


        RequestCenter.deleteData(UrlService.ENTERPRISE, jsonStr, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                showToast("删除成功");
                adapter.remove(mData.get(position));
            }

            @Override
            public void onFailure(OkHttpException responseObj) {

            }
        });
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

    private void getData() {
        RequestParams params = new RequestParams();
        try {
            params.put("current", ++pageNum);
            params.put("pageable", "y");
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pages < pageNum) {
            mAdapter.getLoadMoreModule().setEnableLoadMore(false);
            return;
        }

        RequestCenter.getDataList(UrlService.ENTERPRISE, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");

                    if (TextUtils.equals(result.getString("code"), "0")) {
                    /*    mData.removeAll(mData);
                        mData.clear();*/
                        mData.addAll(new Gson().fromJson(data.getString("list"), new TypeToken<List<Enterprise>>() {
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

            }
        });


    }

    private void getSearch(RequestParams params) {
        mData.clear();
        RequestCenter.getDataList(UrlService.ENTERPRISE, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());

                    if (TextUtils.equals(result.getString("code"), "0")) {
                        mData.addAll(new Gson().fromJson(result.getString("data"), new TypeToken<List<Enterprise>>() {
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
