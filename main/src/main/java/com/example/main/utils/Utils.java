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
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 陈泽宇 on 2020/8/18
 * Describe:
 */
public class Utils {

    public static String getText(TextView v) {
        return TextUtils.isEmpty(v.getText()) ? "" : v.getText() + "";
    }

    public static File getFile(List<LocalMedia> list) {
        try {
            return new File(list.get(0).getPath());
        } catch (Exception e) {
            return null;
        }
    }

    public static List<File> getFileList(List<LocalMedia> list) {
        try {
            List<File> fileList = new ArrayList<>();
            for (LocalMedia localMedia : list) {
                fileList.add(new File(localMedia.getPath()));
            }
            return fileList;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 判断字符串中是否包含中文
     *
     * @param str 待校验字符串
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

    public static boolean isMultiSim(Context context) {
        boolean result = false;
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);
        if (telecomManager != null) {
            @SuppressLint("MissingPermission") List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();
            int simNum = 0;
            for (int i = 0; i < phoneAccountHandleList.size(); i++) {
                if (!TextUtils.isEmpty(phoneAccountHandleList.get(i).getId())) {
                    simNum++;
                }
            }
            result = simNum >= 2;
        }
        return result;
    }

    public static void call(Context context, int id, String telNum) {
        TelecomManager telecomManager = (TelecomManager) context.getSystemService(Context.TELECOM_SERVICE);

        if (telecomManager != null) {
            @SuppressLint("MissingPermission") List<PhoneAccountHandle> phoneAccountHandleList = telecomManager.getCallCapablePhoneAccounts();

            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + telNum));
            intent.putExtra(TelecomManager.EXTRA_PHONE_ACCOUNT_HANDLE, phoneAccountHandleList.get(id));
            context.startActivity(intent);
        }
    }

    /**
     * Date对象获取时间字符串
     *
     * @param date
     * @param format
     * @return
     */
    public static String getDateStr(Date date, String format) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    /**
     * 将字符串转位日期类型
     *
     * @return
     */
    public static Date parseServerTime(String serverTime, String format) {
        if (TextUtils.isEmpty(format)) {
            format = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINESE);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        Date date = null;
        try {
            date = sdf.parse(serverTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期格式字符串转换时间戳
     *
     * @param date
     * @param format
     * @return
     */
    public static long date2TimeStamp(String date, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(date).getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

//    public static String stamp2String(long time, String format) {
//        if (TextUtils.isEmpty(format)) {
//            format = "yyyy-MM-dd HH:mm:ss";
//        }
//        SimpleDateFormat sdf = new SimpleDateFormat(format); //设置格式
//        return sdf.format(time);
//    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @return boolean
     */
    public static boolean isToday(Date time) {
        boolean b = false;
//        Date time = parseServerTime(sdate,null);
        Date today = new Date();
        if (time != null) {
            String nowDate = getDateStr(today, null);
            String timeDate = getDateStr(time, null);
            if (nowDate.equals(timeDate)) {
                b = true;
            }
        }
        return b;
    }


    /**
     * MD5、SHA1等加密算法(不可逆) <br>
     * 1、生成一个指定加密方式的加密计算摘要 <br>
     * 2、计算(指定加密方式)函数 <br>
     * 3、digest()最后确定返回md5hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符<br>
     * 4、BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值<br>
     *
     * @param value 需要加密的内容
     * @param type  加密方式 SHA-1
     * @return 加密后的字符串(MD5 ： 32 、 SHA1 ： 40)
     */
    public static String encryption(String value, String type){
        try {
            MessageDigest md5 = null;
            md5 = MessageDigest.getInstance(type);
            byte[] byteArray = value.getBytes("UTF-8");
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuffer hexValue = new StringBuffer();
            for (int i = 0; i < md5Bytes.length; i++) {
                int val = md5Bytes[i] & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
