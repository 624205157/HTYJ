package com.example.main.activity.phone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;
import com.example.main.R2;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/9 20:42.
 * describe: 人员详情
 */
public class PersonalInfoActivity extends BaseActivity {


    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.address)
    TextView address;
    @BindView(R2.id.department)
    TextView department;
    @BindView(R2.id.tel)
    TextView tel;

    @Override
    protected int setContentView() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("人员详情");
    }

    @OnClick({R2.id.voice, R2.id.video})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.voice){

        }else  if (view.getId() == R.id.video){

        }
    }
}
