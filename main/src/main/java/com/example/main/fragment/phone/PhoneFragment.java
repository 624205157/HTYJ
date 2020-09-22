package com.example.main.fragment.phone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.activity.CheckOtherTraActivity;
import com.example.main.activity.phone.PersonalInfoActivity;
import com.example.main.adapter.PhoneAdapter;
import com.example.main.bean.People;
import com.example.main.bean.Subject;
import com.example.main.bean.User;
import com.example.main.fragment.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.liteav.login.model.ProfileManager;

import org.json.JSONObject;

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
    private List<Subject> userList = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.fragment_phone;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {
        adapter = new PhoneAdapter(userList,mContext);
        adapter.setAnimationEnable(true);

        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                Intent intent = new Intent(getActivity(), PersonalInfoActivity.class);
                Gson gson = new Gson();
                intent.putExtra("user",gson.toJson(userList.get(position)));
                startActivity(intent);
            }
        });
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        list.setAdapter(adapter);

        refresh.setOnRefreshListener(this);

        getUserList();
    }



    private void getUserList(){
        userList.clear();
        RequestCenter.getDataList(UrlService.USERLIST, null, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                try {
                    JSONObject data = new JSONObject(responseObj.toString());
                    String code = data.getString("code");
                    if (TextUtils.equals(code,"0")){
                        Gson gson = new Gson();
                        shareHelper.save("userList",data.getJSONObject("data").getString("list")).commit();
                        ProfileManager.getInstance().getUserList();//初始化用户数据列表
                        userList.addAll(gson.fromJson(data.getJSONObject("data").getString("list"),new TypeToken<List<Subject>>(){}.getType()));
                        String my = (String) shareHelper.query("username","");
                        for (Subject s : userList){
                            if (TextUtils.equals(s.getAccount(),my)){
                                userList.remove(s);
                                break;
                            }
                        }
                        adapter.setList(userList);
                        adapter.notifyDataSetChanged();
                    }
                }catch (Exception e){
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
        getUserList();
        refresh.setRefreshing(false);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getUserList();
    }
}
