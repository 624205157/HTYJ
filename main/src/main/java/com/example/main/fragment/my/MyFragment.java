package com.example.main.fragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.okhttp.request.RequestParams;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.UrlService;
import com.example.main.activity.LoginActivity;
import com.example.main.activity.my.UpdateMyActivity;
import com.example.main.bean.Grid;
import com.example.main.bean.Subject;
import com.example.main.fragment.BaseFragment;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by czy on 2020/8/5 10:56.
 * describe: 我的
 */
public class MyFragment extends BaseFragment {
    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.department)
    TextView department;
    @BindView(R2.id.head_img)
    CircleImageView headImg;


    @Override
    protected int setContentView() {
        return R.layout.fragment_my;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

    }

    private void initData() {
        String subject = (String) shareHelper.query("subject", "");
        if (TextUtils.isEmpty(subject)) {
            return;
        }
        Gson gson = new Gson();
        Subject personInfo = gson.fromJson(subject, new TypeToken<Subject>() {
        }.getType());
        name.setText(personInfo.getName());

        Glide.with(this).load(personInfo.getAvatar()).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into(headImg);

        List<Grid> grids = new ArrayList<>();
        grids.addAll(personInfo.getGrids());
        String departmentStr = "";
        for (Grid grid : grids) {
            departmentStr = departmentStr + "-" + grid.getName();
        }
        if (!TextUtils.isEmpty(departmentStr))
            department.setText(departmentStr.substring(1));
    }

    @OnClick({R2.id.update_personal, R2.id.update_version, R2.id.cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.update_personal) {
            startActivity(new Intent(getActivity(), UpdateMyActivity.class));
        } else if (id == R.id.update_version) {
            getNewVersion();
        } else if (id == R.id.cancel) {
            shareHelper.save("username", "")
                    .save("password", "")
                    .save("token", "")
                    .save("subject", "").commit();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            mActivity.finish();
        }
    }

    private void getNewVersion(){
        buildDialog("");
        RequestParams params = new RequestParams();
        params.put("platform","android");
        params.put("version", Utils.getVersionName(getActivity()) + "");
//        params.put("version", "0.0.1");
        RequestCenter.getDataList(UrlService.VERSION, params, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                cancelDialog();
                try {
                    JSONObject result = new JSONObject(responseObj.toString());
                    if (TextUtils.equals(result.getString("code"),"0")){
                        JSONObject data = result.getJSONObject("data");
                        if (TextUtils.equals(data.getString("needUpdate"),"0")){
                            showToast("当前为最新版本");
                        }else {
                            updateApp(data.getString("fileUrl"),data.getString("content"));
                        }
                    }else {
                        showToast(result.getString("msg"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(OkHttpException responseObj) {
                cancelDialog();
            }
        });

    }
//    private String apkUrl = "http://118.24.148.250:8080/yk/update_signed.apk";
//    private String updateContent = "1、Kotlin重构版\n2、支持自定义UI\n3、增加md5校验\n4、更多功能等你探索";

    private void updateApp(String url,String content) {
        AllenVersionChecker
                .getInstance()
                .downloadOnly(
                        UIData.create().setDownloadUrl(url).setContent(content).setTitle("发现新版本")
                )
                .executeMission(mContext);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        initData();
    }
}
