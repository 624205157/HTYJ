package com.tencent.liteav.login.model;

import android.text.TextUtils;
import android.app.Activity;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.SPUtils;
import com.example.commonlib.utils.ShareHelper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;


public class ProfileManager {
    private static final ProfileManager ourInstance = new ProfileManager();


    private final static String PER_DATA = "per_profile_manager";
    private final static String PER_USER_MODEL = "per_user_model";
    private static final String PER_USER_ID = "per_user_id";
    private static final String PER_TOKEN = "per_user_token";
    private static final String PER_USER_DATE = "per_user_publish_video_date";
    private static final String TAG = ProfileManager.class.getName();

    private UserModel mUserModel;
    private String mUserId;
    private String mToken;
    private String mUserPubishVideoDate;
    private boolean isLogin = false;

    private static ShareHelper shareHelper;
    private static List<UserModel> userList;

    public static ProfileManager getInstance() {
        shareHelper = ShareHelper.getInstance();
        return ourInstance;
    }

    public void getUserList() {
        Gson gson = new Gson();
        String userListStr = (String) shareHelper.query("userList", "");
        userList = gson.fromJson(userListStr, new TypeToken<List<UserModel>>() {
        }.getType());

    }

    private ProfileManager() {
    }

    public boolean isLogin() {
        return isLogin;
    }

    public UserModel getUserModel() {
        if (mUserModel == null) {
            loadUserModel();
        }
        return mUserModel;
    }

    public String getUserId() {
        if (mUserId == null) {
            mUserId = SPUtils.getInstance(PER_DATA).getString(PER_USER_ID, "");
        }
        return mUserId;
    }

    private void setUserId(String userId) {
        mUserId = userId;
        SPUtils.getInstance(PER_DATA).put(PER_USER_ID, userId);
    }

    private void setUserModel(UserModel model) {
        mUserModel = model;
        saveUserModel();
    }

    public String getToken() {
        if (mToken == null) {
            loadToken();
        }
        return mToken;
    }

    public void setToken(String token) {
        mToken = token;
        SPUtils.getInstance(PER_DATA).put(PER_TOKEN, mToken);
    }

    private void loadToken() {
        mToken = SPUtils.getInstance(PER_DATA).getString(PER_TOKEN, "");
    }


    public String getUserPublishVideoDate() {
        if (mUserPubishVideoDate == null) {
            mUserPubishVideoDate = SPUtils.getInstance(PER_DATA).getString(PER_USER_DATE, "");
        }
        return mUserPubishVideoDate;
    }

    public void setUserPublishVideoDate(String date) {
        mUserPubishVideoDate = date;
        try {
            SPUtils.getInstance(PER_DATA).put(PER_USER_DATE, mUserPubishVideoDate);
        } catch (Exception e) {
        }
    }

    public void getSms(String phone, final ActionCallback callback) {
        callback.onSuccess();
    }

    public void logout(final ActionCallback callback) {
        setUserId("");
        isLogin = false;
        callback.onSuccess();
    }


    public void login(UserModel userModel) {
        isLogin = true;
        setUserId(userModel.userId);
//        userModel.userAvatar = getAvatarUrl(userId);
//        userModel.userName = userId;
//        userModel.phone = userId;
//        userModel.userId = userId;
        userModel.userSig = (String) shareHelper.query("userSig","");
        setUserModel(userModel);
    }

    public void login(String userId, String sms, final ActionCallback callback) {
        isLogin = true;
        setUserId(userId);
        UserModel userModel = new UserModel();
//        userModel.userAvatar = getAvatarUrl(userId);
        userModel.userName = userId;
        userModel.phone = userId;
        userModel.userId = userId;
        userModel.userSig =(String) shareHelper.query("userSig","");
        setUserModel(userModel);
        callback.onSuccess();
    }

    public void autoLogin(String userId, String token, final ActionCallback callback) {
        isLogin = true;
        setUserId(userId);
        UserModel userModel = new UserModel();
//        userModel.userAvatar = getAvatarUrl(userId);
        userModel.userName = userId;
        userModel.phone = userId;
        userModel.userId = userId;
        userModel.userSig = (String) shareHelper.query("userSig","");
        setUserModel(userModel);
        callback.onSuccess();
    }

    public NetworkAction getUserInfoByUserId(String userId, final GetUserInfoCallback callback) {
//        UserModel userModel = new UserModel();
////        userModel.userAvatar = getAvatarUrl(userId);
//        userModel.phone = userId;
//        userModel.userId = userId;
//        userModel.userName = userId;
        getUserList();
        callback.onSuccess(getUserModel(userId));
        return new NetworkAction();
    }

    public NetworkAction getUserInfoByPhone(String phone, final GetUserInfoCallback callback) {
        UserModel userModel = new UserModel();
//        userModel.userAvatar = getAvatarUrl(phone);
        userModel.phone = phone;
        userModel.userId = phone;
        userModel.userName = phone;
        callback.onSuccess(userModel);
        return new NetworkAction();
    }

    public void getUserInfoBatch(List<String> userIdList, final GetUserInfoBatchCallback callback) {
        if (userIdList == null) {
            return;
        }

        List<UserModel> userModelList = new ArrayList<>();
        for (String userId : userIdList) {
//            UserModel userModel = new UserModel();
////            userModel.userAvatar = getAvatarUrl(userId);
//            userModel.phone = userId;
//            userModel.userId = userId;
//            userModel.userName = userId;
            userModelList.add(getUserModel(userId));
        }
        callback.onSuccess(userModelList);
    }

//    private String getAvatarUrl(String userId) {
//        if (TextUtils.isEmpty(userId)) {
//            return null;
//        }
//        byte[] bytes = userId.getBytes();
//        int    index = bytes[bytes.length - 1] % 10;
//        String avatarName = "avatar" + index + "_100";
//        return "https://imgcache.qq.com/qcloud/public/static//" + avatarName + ".20191230.png";
//    }

    private void saveUserModel() {
        try {
            SPUtils.getInstance(PER_DATA).put(PER_USER_MODEL, GsonUtils.toJson(mUserModel));
        } catch (Exception e) {
        }
    }

    private void loadUserModel() {
        try {
            String json = SPUtils.getInstance(PER_DATA).getString(PER_USER_MODEL);
            mUserModel = GsonUtils.fromJson(json, UserModel.class);
        } catch (Exception e) {
        }
    }

    public static class NetworkAction {

        public NetworkAction() {
        }

        public void cancel() {
        }
    }

    // 操作回调
    public interface ActionCallback {
        void onSuccess();

        void onFailed(int code, String msg);
    }

    // 通过userid/phone获取用户信息回调
    public interface GetUserInfoCallback {
        void onSuccess(UserModel model);

        void onFailed(int code, String msg);
    }

    // 通过userId批量获取用户信息回调
    public interface GetUserInfoBatchCallback {
        void onSuccess(List<UserModel> model);

        void onFailed(int code, String msg);
    }

    public void checkNeedShowSecurityTips(Activity activity) {

    }

    private UserModel getUserModel(String mUserId) {
        for (UserModel userModel : userList){
            if (TextUtils.equals(userModel.userId,mUserId)){
                return userModel;
            }
        }
        return null;
    }
}
