package com.example.main.activity.phone;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.commonlib.base.BaseActivity;
import com.example.main.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/9 20:42.
 * describe: 人员详情
 */
public class PersonalInfoActivity extends BaseActivity {


    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.department)
    TextView department;
    @BindView(R.id.tel)
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

    @OnClick({R.id.voice, R.id.video})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.voice:
                break;
            case R.id.video:
                break;
        }
    }
}
