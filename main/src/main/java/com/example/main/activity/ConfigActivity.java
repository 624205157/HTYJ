package com.example.main.activity;

import android.content.Context;
import android.util.Log;

import com.example.commonlib.Constants;
import com.example.commonlib.base.BaseActivity;
import com.example.main.service.CallService;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMOfflinePushConfig;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;
import com.example.commonlib.trtc.GenerateTestUserSig;
import com.tencent.liteav.trtcaudiocalldemo.model.ITRTCAudioCall;
import com.tencent.liteav.trtcaudiocalldemo.model.TRTCAudioCallImpl;
import com.tencent.liteav.trtcvideocalldemo.model.ITRTCVideoCall;
import com.tencent.liteav.trtcvideocalldemo.model.TRTCVideoCallImpl;

/**
 * Created by 陈泽宇 on 2020/9/15
 * Describe:
 */
public abstract class ConfigActivity extends BaseActivity {

    /**
     * 初始化 腾讯云
     */
    protected void initTRTC(V2TIMSDKListener listener) {
        // 1. 从 IM 控制台获取应用 SDKAppID，详情请参考 SDKAppID。
        // 2. 初始化 config 对象
        V2TIMSDKConfig config = new V2TIMSDKConfig();
        // 3. 指定 log 输出级别，详情请参考 SDKConfig。
        config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_NONE);
        // 4. 初始化 SDK 并设置 V2TIMSDKListener 的监听对象。
        // initSDK 后 SDK 会自动连接网络，网络连接状态可以在 V2TIMSDKListener 回调里面监听。
        V2TIMManager.getInstance().initSDK(this, GenerateTestUserSig.SDKAPPID, config, listener);
//            // 5. 监听 V2TIMSDKListener 回调
//            @Override
//            public void onConnecting() {
//                // 正在连接到腾讯云服务器
//            }
//            @Override
//            public void onConnectSuccess() {
//                // 已经成功连接到腾讯云服务器
//            }
//            @Override
//            public void onConnectFailed(int code, String error) {
//                // 连接腾讯云服务器失败
//            }
//        });
    }

    protected void loginTRTC(Context context, String userID, String userSig) {
        V2TIMManager.getInstance().login(userID, userSig, new V2TIMCallback() {
            @Override
            public void onError(int i, String s) {
                showToast("腾讯云登录失败，音视频功能失效，请联系管理员");
                Log.e("腾讯云登录失败", i + " " + s);
            }

            @Override
            public void onSuccess() {
                loginAudioAndVideo(userID, userSig);
                String mRegId = (String) shareHelper.query("regId", "");
                CallService.start(context);
                V2TIMManager.getInstance().getOfflinePushManager()
                        .setOfflinePushConfig(new V2TIMOfflinePushConfig(Constants.PUSH_MI_ID, mRegId), new V2TIMCallback() {
                            @Override
                            public void onError(int i, String s) {
                                Log.e("小米推送添加", s);
                            }

                            @Override
                            public void onSuccess() {
                                Log.e("小米推送添加", "成功");
                            }
                        });
            }
        });
    }

    protected void loginAudioAndVideo(String userId, String userSig) {
        TRTCAudioCallImpl.sharedInstance(this).login(GenerateTestUserSig.SDKAPPID, userId, userSig, new ITRTCAudioCall.ActionCallBack() {
            @Override
            public void onError(int code, String msg) {
                Log.e("音频登录",code + " " + msg);
            }

            @Override
            public void onSuccess() {
                Log.e("音频登录", "成功" );
            }
        });
        TRTCVideoCallImpl.sharedInstance(this).login(GenerateTestUserSig.SDKAPPID, userId, userSig, new ITRTCVideoCall.ActionCallBack() {
            @Override
            public void onError(int code, String msg) {
                Log.e("视频登录",code + " " + msg);
            }

            @Override
            public void onSuccess() {
                Log.e("视频登录", "成功" );
            }
        });
    }
}
