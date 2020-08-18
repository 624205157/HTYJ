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
import com.example.main.bean.Enterprise;
import com.example.main.fragment.BaseFragment;
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
                    intent.putExtra("id",mData.get(position).getId());
                    startActivity(intent);
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

    private void getData() {
        RequestParams params = new RequestParams();
        try {
            params.put("pageNum",++pageNum);
            params.put("pageable","y");
        }catch (Exception e){
            e.printStackTrace();
        }


        RequestCenter.getEnterpriseList(params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    JSONObject data = result.getJSONObject("data");

                    if (TextUtils.equals(result.getString("code"),"0")){
                        mData.addAll(new Gson().fromJson(data.getString("list"), new TypeToken<List<Enterprise>>() {
                        }.getType()));
                        mAdapter.setList(mData);

                        mAdapter.notifyDataSetChanged();

                    }

                    if (data.getInt("pages") == pageNum){
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

//        for (int i = 1;i<9;i++) {
//            Enterprise enterprise = new Enterprise();
//            enterprise.setName("高大上企业" + i);
//            enterprise.setAddress("海南省海口市华龙区奥术大师大大所大所奥术大师大所"+ i);
//            enterprise.setSocialCreditCode(""+ i+ i+ i+ i+ i+ i);
//            enterprise.setLegalPerson("dasc是"+ i);
//            enterprise.setIsStart("是"+ i);
//            enterprise.setLatitude(43.888824);
//            enterprise.setLongitude(125.300985);
//            mData.add(enterprise);
//        }
//

    }

}
