package com.example.main.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.commonlib.utils.ShareHelper;
import com.example.main.R;
import com.example.main.fragment.BaseFragment;
import com.example.main.fragment.Fragment1;
import com.example.main.fragment.Fragment2;
import com.example.main.fragment.Fragment3;
import com.example.main.fragment.Fragment4;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;

import java.util.ArrayList;
import java.util.List;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;


public class MainActivity extends SupportActivity {


    private RadioGroup rg;
    private List<SupportFragment> fragments;
    private int index = 0;
    private ShareHelper shareHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(com.example.commonlib.R.color.white));//设置状态栏颜色
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_main);
        shareHelper = ShareHelper.getInstance();
        rg =  findViewById(R.id.rg);
        getPermission();
        initData();

    }


    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());

        loadMultipleRootFragment(R.id.frag,0,
                fragments.get(0),
                fragments.get(1),
                fragments.get(2),
                fragments.get(3)
                );


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
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
                        case R.id.tab_4:
                        index = 3;
                        showHideFragment(fragments.get(3));
                        break;
                }
            }
        });
        rg.check(R.id.tab_1);
    }


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

//    public void currentItem(int item){
//        showHideFragment(fragments.get(item));
//        switch (item){
//            case 0:
//                rg.check(R.id.tab_1);
//                break;
//            case 1:
//                rg.check(R.id.tab_2);
//                break;
//            case 2:
//                rg.check(R.id.tab_3);
//                break;
//        }
//
//    }
//
//    public void disableRadioGroup(RadioGroup testRadioGroup) {
//        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
//            testRadioGroup.getChildAt(i).setEnabled(false);
//        }
//    }
//
//    public void enableRadioGroup(RadioGroup testRadioGroup) {
//        for (int i = 0; i < testRadioGroup.getChildCount(); i++) {
//            testRadioGroup.getChildAt(i).setEnabled(true);
//        }
//    }


    private void getPermission(){
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {

                        } else {
//                            Toast.makeText(MainActivity.this, "您拒绝了定位权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



}
