package com.example.main.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.view.NoScrollRecyclerView;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.bean.Subject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by 陈泽宇 on 2020/11/12
 * Describe:
 */
public class SignInActivity extends BaseActivity {
    @BindView(R2.id.iv)
    CircleImageView iv;
    @BindView(R2.id.name)
    TextView name;
    @BindView(R2.id.office_time)
    TextView officeTime;
    @BindView(R2.id.office_state)
    TextView officeState;
    @BindView(R2.id.no_office_time)
    TextView noOfficeTime;
    @BindView(R2.id.no_office_state)
    TextView noOfficeState;
    @BindView(R2.id.sign_state)
    TextView signState;
    @BindView(R2.id.location)
    TextView location;
//    @BindView(R2.id.time)
//    TextClock time;
    @BindView(R2.id.rank_list)
    NoScrollRecyclerView rankList;
    @BindView(R2.id.rank_click)
    ImageView rankClick;


    private boolean isHidden = true;

    @Override
    protected int setContentView() {
        return R.layout.activity_sign_in;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("考勤管理");

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
                .into(iv);

        getData();
    }

    private void getData() {
        officeTime.setText("上班" + "08:30");
        noOfficeTime.setText("下班" + "17:00");
        officeState.setText("未打卡");
        noOfficeState.setText("未打卡");

        signState.setText("上班打卡");

    }

    @OnClick({R2.id.rank_click,R2.id.sign})
    public void onViewClicked(View view) {
        int id = view.getId();
        if (id == R.id.rank_click) {
            if (!isHidden) {
                isHidden = true;
                rankList.setVisibility(View.GONE);
                rankClick.setImageResource(R.mipmap.up_img);
            } else {
                isHidden = false;
                rankList.setVisibility(View.VISIBLE);
                rankClick.setImageResource(R.mipmap.down_img);

            }
        }else if (id == R.id.sign){

        }

    }

    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            cancelDialog();
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
//                    latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
//                    locationText.setText("经度:" + aMapLocation.getLongitude() + " 纬度:" + aMapLocation.getLatitude());

                    location.setText(aMapLocation.getAddress());
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };

    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

}
