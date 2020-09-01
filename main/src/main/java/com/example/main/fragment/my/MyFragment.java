package com.example.main.fragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.example.commonlib.Constants;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.activity.LoginActivity;
import com.example.main.activity.my.UpdateMyActivity;
import com.example.main.bean.Grid;
import com.example.main.bean.Subject;
import com.example.main.fragment.BaseFragment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

        if (!TextUtils.isEmpty(personInfo.getAvatar())) {
            GlideUrl glideUrl = new GlideUrl(personInfo.getAvatar(), new LazyHeaders.Builder()
                    .addHeader("Authorization", Constants.TAKEN)
                    .build());
            Glide.with(this).load(glideUrl).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                    .into(headImg);
        }

        List<Grid> grids = new ArrayList<>();
        grids.addAll(personInfo.getGrids());
        String departmentStr = "";
        for (Grid grid : grids) {
            departmentStr = departmentStr + "-" + grid.getName();
        }
        department.setText(departmentStr.substring(1));
    }

    @OnClick({R2.id.update_personal, R2.id.update_version, R2.id.cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.update_personal) {
            startActivity(new Intent(getActivity(), UpdateMyActivity.class));
        } else if (id == R.id.update_version) {

        } else if (id == R.id.cancel) {
            shareHelper.save("username", "")
                    .save("password", "")
                    .save("token", "")
                    .save("subject","").commit();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            mActivity.finish();
        }
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
