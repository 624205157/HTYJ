package com.example.main.activity.my;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.bean.Subject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by 陈泽宇 on 2020/8/31
 * Describe: 更新个人信息
 */
public class UpdateMyActivity extends BaseActivity {
    @BindView(R2.id.head_img)
    ImageView headImg;
    @BindView(R2.id.name)
    EditText name;
    @BindView(R2.id.tel)
    EditText tel;

    @Override
    protected int setContentView() {
        return R.layout.activity_update_my;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {

        String subject = (String) shareHelper.query("subject", "");
        if (TextUtils.isEmpty(subject)) {
            return;
        }
        Gson gson = new Gson();
        Subject personInfo = gson.fromJson(subject, new TypeToken<Subject>() {
        }.getType());
        name.setText(personInfo.getName());
        tel.setText(personInfo.getMobile());

        Glide.with(this).load(personInfo.getAvatar()).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into(headImg);



    }

    @OnClick({R2.id.head_img, R2.id.submit})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.head_img) {

        } else if (id == R.id.submit) {

        }
    }
}
