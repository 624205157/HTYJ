package com.example.main.activity.phone;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.commonlib.base.BaseActivity;
import com.tencent.liteav.login.model.ProfileManager;
import com.tencent.liteav.login.model.UserModel;
import com.tencent.liteav.trtcaudiocalldemo.ui.TRTCAudioCallActivity;
import com.example.main.R;
import com.example.main.R2;
import com.tencent.liteav.trtcvideocalldemo.ui.TRTCVideoCallActivity;
import com.tencent.trtc.TRTCCloud;

import java.util.ArrayList;
import java.util.List;

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

    List<UserModel> list = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_personal_info;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState, String a) {
        addBack();
        setTitleText("人员详情");
        String userID = ProfileManager.getInstance().getUserModel().userId;

        UserModel userModel = new UserModel();
        if (TextUtils.equals(userID,"123456")) {
            userModel.userId = "654321";
        }else {
            userModel.userId = "123456";
        }
        list.add(userModel);
    }

    @OnClick({R2.id.voice, R2.id.video})
    public void onViewClicked(View view) {
        if (view.getId() == R.id.voice) {

            TRTCAudioCallActivity.startCallSomeone(PersonalInfoActivity.this, list);
        } else if (view.getId() == R.id.video) {
            TRTCVideoCallActivity.startCallSomeone(PersonalInfoActivity.this, list);
        }
    }


//    private void startAudioCall(){
//        //1. 初始化组件
//        ITRTCAudioCall sCall =  TRTCAudioCallImpl.sharedInstance(this);
//        sCall.init();
////2. 注册监听器
//        sCall.addListener(new TRTCAudioCallListener() {
//            @Override
//            public void onError(int code, String msg) {
//                showToast(code + " " + msg);
//            }
//
//            //...省略一些监听代码
//            public void onInvited(String sponsor, final List<String> userIdList, boolean isFromGroup, int callType) {
//                // 收到来自 sponsor 发过来的通话请求，此处代码选择接听，您也可以调用 reject() 拒绝之。
//                sCall.accept();
//            }
//
//            @Override
//            public void onGroupCallInviteeListUpdate(List<String> userIdList) {
//
//            }
//
//            @Override
//            public void onUserEnter(String userId) {
//
//            }
//
//            @Override
//            public void onUserLeave(String userId) {
//
//            }
//
//            @Override
//            public void onReject(String userId) {
//
//            }
//
//            @Override
//            public void onNoResp(String userId) {
//
//            }
//
//            @Override
//            public void onLineBusy(String userId) {
//
//            }
//
//            @Override
//            public void onCallingCancel() {
//
//            }
//
//            @Override
//            public void onCallingTimeout() {
//
//            }
//
//            @Override
//            public void onCallEnd() {
//
//            }
//
//            @Override
//            public void onUserAudioAvailable(String userId, boolean isVideoAvailable) {
//
//            }
//
//            @Override
//            public void onUserVoiceVolume(Map<String, Integer> volumeMap) {
//
//            }
//        });
//        String userID = (String) shareHelper.query("userID","");
////3. 完成组件的登录，登录成功后才可以调用组件的其他功能函数
//        sCall.login(GenerateTestUserSig.SDKAPPID,userID , GenerateTestUserSig.genTestUserSig(userID), new ITRTCAudioCall.ActionCallBack() {
//            @Override
//            public void onError(int code, String msg) {
//
//            }
//
//            public void onSuccess() {
//                //4. 此处为实例代码：我们在组件登录成功后呼叫用户“aaa”
//                if (TextUtils.equals(userID,"123456")) {
//                    sCall.call("654321");
//                }else {
//                    sCall.call("123456");
//                }
//            }
//        });
//    }

}
