package com.example.main.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.RadioGroup;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.blankj.utilcode.util.ServiceUtils;
import com.example.commonlib.Constants;
import com.example.commonlib.base.BaseActivity;
import com.example.commonlib.okhttp.exception.OkHttpException;
import com.example.commonlib.okhttp.listener.DisposeDataListener;
import com.example.commonlib.utils.ShareHelper;
import com.example.main.R;
import com.example.main.R2;
import com.example.main.RequestCenter;
import com.example.main.bean.Subject;
import com.example.main.bean.User;
import com.example.main.fragment.BaseFragment;
import com.example.main.fragment.Fragment1;
import com.example.main.fragment.Fragment2;
import com.example.main.fragment.Fragment3;
import com.example.main.fragment.Fragment4;
import com.example.main.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.example.main.service.CallService;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.tencent.liteav.login.model.ProfileManager;
import com.tencent.liteav.login.model.UserModel;
import com.xiaomi.mipush.sdk.MiPushClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.yokeyword.fragmentation.SupportFragment;


public class MainActivity extends ConfigActivity {

    @BindView(R2.id.rg)
    RadioGroup rg;

    private List<SupportFragment> fragments;
    private int index = 0;
    private ShareHelper shareHelper;


    @Override
    protected int setContentView() {
        return R.layout.activity_my_main;
    }

    @SuppressLint("InvalidR2Usage")
    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        shareHelper = ShareHelper.getInstance();
        getPermission();

        if (TextUtils.equals("LoginActivity", getIntent().getStringExtra("from"))) {
            initData();
            return;
        }

        //判断是否已登录
        String username = (String) shareHelper.query("username", "");
        String password = (String) shareHelper.query("password", "");
        if (!TextUtils.isEmpty(username)) {
            buildDialog("登录中，请稍后...");
            login(username, password);

        } else {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }

//        RequestParams params = new RequestParams();
//        params.put("key","49e0db5b3745d3fce02a4292ad7ce212");
//        params.put("name","轨迹测试");
//        {"data":{"name":"轨迹测试","sid":184215},"errcode":10000,"errdetail":null,"errmsg":"OK"}
//        RequestCenter.getServiceId(params, new DisposeDataListener() {
//            @Override
//            public void onSuccess(Object responseObj) {
//                Log.e("轨迹测试",responseObj.toString());
//            }
//
//            @Override
//            public void onFailure(OkHttpException responseObj) {
//
//            }
//        });
    }


    private void initData() {
        fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new Fragment3());
        fragments.add(new Fragment4());

        loadMultipleRootFragment(R.id.frag, 0,
                fragments.get(0),
                fragments.get(1),
                fragments.get(2),
                fragments.get(3)
        );


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @SuppressLint("InvalidR2Usage")
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.tab_1) {
                    index = 0;
                    showHideFragment(fragments.get(0));
                } else if (i == R.id.tab_2) {
                    index = 1;
                    showHideFragment(fragments.get(1));
                } else if (i == R.id.tab_3) {
                    index = 2;
                    showHideFragment(fragments.get(2));
                } else if (i == R.id.tab_4) {
                    index = 3;
                    showHideFragment(fragments.get(3));
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
        AllenVersionChecker.getInstance().cancelAllMission();
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


    private void getPermission() {
        PermissionX.init(this)
                .permissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, List<String> grantedList, List<String> deniedList) {
                        if (allGranted) {

                        } else {
                            String text = "";
                            for (String a : deniedList) {
                                text = text + a + ";";
                            }
                            showToast("您拒绝了" + text);
//                            Toast.makeText(MainActivity.this, "您拒绝了定位权限", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void login(String username, String password) {
        User user = new User(username, Utils.encryption(password, "SHA-1"));
        Gson gson = new Gson();
        String jsonStr = gson.toJson(user);
        RequestCenter.requestLogin(jsonStr, new DisposeDataListener() {
            @Override
            public void onSuccess(Object o) {
                cancelDialog();
                try {
                    JSONObject jsonObject = new JSONObject(o.toString());
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("msg");
                    showToast(message);
                    if (TextUtils.equals("0", code)) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        shareHelper
                                .save("username", username)
                                .save("password", password)
                                .save("token", data.getString("token"))
                                .save("userSig",data.getJSONObject("signature").getString("userSig"))
                                .save("subject", data.getString("subject")).commit();
                        Constants.TAKEN = data.getString("token");
                        Constants.SERVICE_ID = data.getInt("serviceId");
                        Gson gson = new Gson();
                        Subject personInfo = gson.fromJson(data.getString("subject"), new TypeToken<Subject>() {
                        }.getType());
                        /**
                         * 腾讯云登录
                         */

                        loginTRTC(MainActivity.this,username, data.getJSONObject("signature").getString("userSig"));

                        ProfileManager.getInstance().login(
                                new UserModel(
                                        personInfo.getMobile(),
                                        personInfo.getAccount(),
                                        personInfo.getName(),
                                        personInfo.getAvatar()));
                        //小米推送登录
                        MiPushClient.setUserAccount(MainActivity.this, username, null);

                        initData();


                    } else {
                        showToast("登录信息失效，请重新登录");
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(OkHttpException e) {
                showToast("登录信息失效，请重新登录");
                e.printStackTrace();
                cancelDialog();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


}
