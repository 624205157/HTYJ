package com.example.main.fragment.my;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.main.R;
import com.example.main.R2;
import com.example.main.activity.LoginActivity;
import com.example.main.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by czy on 2020/8/5 10:56.
 * describe: 我的
 */
public class MyFragment extends BaseFragment {
    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.department)
    TextView department;

    @Override
    protected int setContentView() {
        return R.layout.fragment_my;
    }

    @Override
    protected void lazyLoad(Bundle savedInstanceState) {

    }

    @OnClick({R2.id.update_personal, R2.id.update_version, R2.id.cancel})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.update_personal) {
        } else if (id == R.id.update_version) {
        } else if (id == R.id.cancel) {
            shareHelper.save("username","");
            shareHelper.save("password","");
            startActivity(new Intent(getActivity(), LoginActivity.class));
            mActivity.finish();
        }
    }
}
