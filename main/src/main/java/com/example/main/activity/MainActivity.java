package com.example.main.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.example.toollibrary.Utils.ShareHelper;
import com.zhhl.openlock.fragment.BaseFragment;
import com.zhhl.openlock.fragment.Fragment1;
import com.zhhl.openlock.fragment.Fragment2;
import com.zhhl.openlock.fragment.Fragment3;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

import static com.zhhl.openlock.MyLocationClientOption.getMyOption;

public class MainActivity extends SupportActivity {


    private RadioGroup rg;
    private List<SupportFragment> fragments;
    private int index = 0;
    private ShareHelper shareHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shareHelper = ShareHelper.getInstance();
        rg =  findViewById(R.id.rg);

        initData();
//        if (TextUtils.isEmpty((String)shareHelper.query("firmID",""))){
//            disableRadioGroup(rg);
//        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_DENIED) {
            initLocationOption();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION
            }, 100);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (100 == requestCode && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            initLocationOption();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());

        loadMultipleRootFragment(R.id.frag,0,
                fragments.get(0),
                fragments.get(1),
                fragments.get(2)
                );


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                String firmID = (String)shareHelper.query("firmID","");
                if (TextUtils.isEmpty(firmID)){
                    Toast.makeText(MainActivity.this,"请先通过备案申请!", Toast.LENGTH_SHORT).show();
                    rg.check(R.id.tab_1);
                    return;
                }
                switch (i){
                    case R.id.tab_1:
                        index = 0;
                        showHideFragment(fragments.get(0));
                        break;
                    case R.id.tab_2:
                        index = 1;
                        showHideFragment(fragments.get(1));
                        break;
                    case R.id.tab_3:
                        index = 2;
                        showHideFragment(fragments.get(2));
                        break;
                }
            }
        });
        rg.check(R.id.tab_1);
    }

    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseFragment fragment = (BaseFragment) fragments.get(index);
        fragment.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//
//            imageUri = Uri.fromFile(fileUri);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
//                //通过FileProvider创建一个content类型的Uri
//                imageUri = FileProvider.getUriForFile(MainActivity.this, "com.zhhl.openlock.provider", fileUri);
//
//            switch (requestCode) {
//                case CODE_CAMERA_REQUEST://拍照完成回调
//                    cropImageUri = Uri.fromFile(fileCropUri);
//                    PhotoUtils.cropImageUri(MainActivity.this, imageUri, cropImageUri, 1, 1, 480, 480, CODE_RESULT_REQUEST);
//
//
//                    break;
//                case CODE_RESULT_REQUEST://裁剪返回的图片
//                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
//                    if (bitmap != null) {
////                        showImages(bitmap);
//                        bitmap = PhotoUtils.getCompressedImage(fileUri.getPath(), 50,bitmap);
//                        EventBus.getDefault().post(bitmap);
//                    }
//                    break;
//            }
//        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);
    }

    public void currentItem(int item){
        showHideFragment(fragments.get(item));
        switch (item){
            case 0:
                rg.check(R.id.tab_1);
                break;
            case 1:
                rg.check(R.id.tab_2);
                break;
            case 2:
                rg.check(R.id.tab_3);
                break;
        }

    }

    public void disableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(false);
        }
    }

    public void enableRadioGroup(RadioGroup testRadioGroup) {
        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
            testRadioGroup.getChildAt(i).setEnabled(true);
        }
    }

    private LocationClient locationClient;

    /**
     * 初始化定位参数配置
     */
    private void initLocationOption() {
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = new LocationClient(getApplicationContext());
        //声明LocationClient类实例并配置定位参数
        MyLocationListener myLocationListener = new MyLocationListener();
        //注册监听函数
        locationClient.registerLocationListener(myLocationListener);

        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        locationClient.setLocOption(getMyOption());
        //开始定位
        locationClient.start();
    }

    private double latitude;
    private double longitude;
    /**
     * 实现定位回调
     */
    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (latitude == location.getLatitude()&& longitude == location.getLongitude()){
                return;
            }
            String addressStr = location.getAddrStr();//省市县
            String b = location.getLocationDescribe();//在xxx附近
            String d = b.substring(1, b.length() - 2);//(在)xxx(附近)
            //获取纬度信息
            latitude = location.getLatitude();
            //获取经度信息
            longitude = location.getLongitude();

            //获取定位精度，默认值为0.0f
//            float radius = location.getRadius();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
//            String coorType = location.getCoorType();
            //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
//            int errorCode = location.getLocType();
            Log.e("addressStr",addressStr);
            shareHelper.save("latitude",latitude + "").commit();
            shareHelper.save("longitude",longitude + "").commit();

        }
    }
}
