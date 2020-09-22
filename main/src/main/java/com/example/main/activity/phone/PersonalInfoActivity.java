package com.example.main.activity.phone;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.view.TextHintDialog;
import com.example.commonlib.view.TwoChangeDialog;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.activity.GridDetailsActivity;
import com.example.main.bean.Grid;
import com.example.main.bean.Subject;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.tencent.liteav.login.model.ProfileManager;
import com.tencent.liteav.login.model.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.main.utils.Utils.isMultiSim;

/**
 * Created by czy on 2020/8/9 20:42.
 * describe: 人员详情
 */
public class PersonalInfoActivity extends BaseActivity {


    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.grid)
    TextView grid;
    @BindView(R2.id.tel)
    TextView tel;

    List<UserModel> list = new ArrayList<>();
    @BindView(R2.id.iv)
    CircleImageView iv;

    @Override
    protected int setContentView() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("人员详情");
        Gson gson = new Gson();
        Subject user = gson.fromJson(getIntent().getStringExtra("user"), new TypeToken<Subject>() {
        }.getType());
        name.setText(user.getName());
        tel.setText(user.getMobile());

        List<Grid> grids = new ArrayList<>();
        grids.addAll(user.getGrids());
        String departmentStr = "";
        for (Grid grid : grids) {
            departmentStr = departmentStr + "-" + grid.getName();
        }
        if (!TextUtils.isEmpty(departmentStr)) {
            grid.setText(departmentStr.substring(1));
        }

        Glide.with(this).load(user.getAvatar()).placeholder(R.mipmap.my_head).error(R.mipmap.my_head)
                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭Glide的硬盘缓存机制
                .into(iv);

        ProfileManager.getInstance().getUserInfoByUserId(user.getAccount(), new ProfileManager.GetUserInfoCallback() {
            @Override
            public void onSuccess(UserModel model) {
                list.add(model);
            }

            @Override
            public void onFailed(int code, String msg) {

            }
        });

    }

    @OnClick({R2.id.voice, R2.id.video,R2.id.tel})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.voice) {

            TRTCAudioCallActivity.startCallSomeone(PersonalInfoActivity.this, list);
        } else if (view.getId() == R.id.video) {
            TRTCVideoCallActivity.startCallSomeone(PersonalInfoActivity.this, list);
        }else if(view.getId() == R.id.tel){
            callPhone(tel.getText() + "");
        }
    }

    private void callPhone(String callNumber){
        if (!isMultiSim(this)) {
            new TextHintDialog(this).setMessageStr("拨打电话：" + callNumber)
                    .setClickListener(isConfirm -> {

                    })
                    .show();
        } else {
            new TwoChangeDialog(this).setMessageStr("拨打电话：" + callNumber)
                    .setButtonText("电话卡1", "电话卡2")
                    .setClickListener(tag -> {
                        getPermission(tag,callNumber);
                    }).show();
        }
    }

    private void getPermission(int id ,String callNumber) {
        PermissionX.init(this)
                .permissions(Manifest.permission.CALL_PHONE
                )
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {
                            Utils.call(PersonalInfoActivity.this,id,callNumber);
                        } else {
                            String text = "";
                            for (String a : deniedList) {
                                text = text + a + ";";
                            }
                            showToast("您拒绝了" +text);
//                            Toast.makeText(MainActivity.this, "您拒绝了定位权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
