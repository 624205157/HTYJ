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
import com.example.main.adapter.UpdateEnterpriseAdapter;
import com.example.main.adapter.UpdateResourcesAdapter;
import com.example.main.bean.Enterprise;
import com.example.main.bean.Resources;
import com.example.main.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by czy on 2020/8/10 11:32.
 * describe: 维护应急资源
 */
public class UpdateResourcesFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R2.id.search)
    EditText search;
    @BindView(R2.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R2.id.refresh)
    SwipeRefreshLayout refresh;

    private UpdateResourcesAdapter mAdapter;
    private List<Resources> mData = new ArrayList<>();

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

        mAdapter = new UpdateResourcesAdapter(mData);
        mAdapter.setAnimationEnable(true);
        mAdapter.addChildClickViewIds(R.id.navigation,R.id.update,R.id.del);
        mAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                if (view.getId() == R.id.navigation) {
                    showToast("导航");
                    ARouter.getInstance().build("/map/navigation")
                            .withParcelable("address",mData.get(position).getPoint())
                            .navigation();

                }else if (view.getId() == R.id.update) {
                    showToast("修改");
                }if (view.getId() == R.id.del) {
                    new MyDialog(mContext)
                            .setTitleStr("提示")
                            .setMessageStr("您确定要删除吗?")
                            .setButtonText("取消","确定")
                            .setClickListener(new CallPhoneListener() {
                                @Override
                                public void onClick(int var1) {
                                    if (var1 == 2){
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

    private void getData(){
        for (int i = 1;i<9;i++) {
            Resources enterprise = new Resources();
            enterprise.setName("高大上企业" + i);
            enterprise.setAddress("海南省海口市华龙区奥术大师大大所大所奥术大师大所"+ i);
            enterprise.setType("防护用品"+ i);
            enterprise.setTotal("18"+ i);
            enterprise.setLatitude(43.888824);
            enterprise.setLongitude(125.300985);
            mData.add(enterprise);
        }

        mAdapter.setList(mData);

        mAdapter.notifyDataSetChanged();
    }

}
