package com.example.main.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 陈泽宇 on 2020/8/18
 * Describe:
 */
public class Utils {

    public static String getText(TextView v){
        return TextUtils.isEmpty(v.getText()) ? "" : v.getText() + "";
    }

    public static File getFile(List<LocalMedia> list){
        try {
            return new File(list.get(0).getPath());
        }catch (Exception e){
            return null;
        }
    }

    public static List<File> getFileList(List<LocalMedia> list){
        try {
            List<File> fileList = new ArrayList<>();
            for (LocalMedia localMedia:list){
                fileList.add(new File(localMedia.getPath()));
            }
            return fileList;
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 判断字符串中是否包含中文
     * @param str
     * 待校验字符串
     * @return 是否为中文
     * @warn 不能校验是否为中文标点符号
     */
    public static boolean isContainChinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean isMultiSim(Context context){
        boolean result = false;
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if(telecomManager != null){
            @SuppressLint("MissingPermission") List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            int simNum = 0;
            for (int i = 0;i<phoneAccountHandleList.size();i++ ){
                if (!TextUtils.isEmpty(phoneAccountHandleList.get(i).getId())){
                    simNum++;
                }
            }
            result = simNum >= 2;
        }
        return result;
    }

    public static void call(Context context, int id, String telNum){
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        if(telecomManager != null){
            @SuppressLint("MissingPermission") List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telNum));
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(id));
            context.startActivity(intent);
        }
    }
}
