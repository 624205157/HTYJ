package com.example.main.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.StringUtils;
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
            String nowDate = getDateStr(today, "yyyy-MM-dd");
            String timeDate = getDateStr(time, "yyyy-MM-dd");
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


    /**
     * 获取版本号
     *
     * @param context 上下文
     *
     * @return 版本号
     */
    public static String getVersionName(Context context) {

        //获取包管理器
        PackageManager pm = context.getPackageManager();
        //获取包信息
        try {
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            //返回版本号
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return "";

    }

    /**
     * 手机号验证
     * @param  phone
     * @return 验证通过返回true
     */
    public static boolean isMobile(String phone) {
        if(StringUtils.isEmpty(phone)) {
            return false;
        }

        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        if(phone.length() != 11) {
            return false;
        }else {
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(phone);
            boolean isMatch = m.matches();
            return isMatch;
        }
    }

    /**
     * 电话号码验证
     * @param phone
     * @return 验证通过返回true
     */
    public static boolean isPhone(String phone) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][0-9]{2,3}-?[0-9]{5,10}$");  // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$");         // 验证没有区号的
        if (phone.length() > 9) {
            m = p1.matcher(phone);
            b = m.matches();
        } else {
            m = p2.matcher(phone);
            b = m.matches();
        }
        return b;
    }

    /**
     * 我国公民的身份证号码特点如下
     * 1.长度18位
     * 2.第1-17号只能为数字
     * 3.第18位只能是数字或者x
     * 4.第7-14位表示特有人的年月日信息
     * 请实现身份证号码合法性判断的函数，函数返回值：
     * 1.如果身份证合法返回0
     * 2.如果身份证长度不合法返回1
     * 3.如果第1-17位含有非数字的字符返回2
     * 4.如果第18位不是数字也不是x返回3
     * 5.如果身份证号的出生日期非法返回4
     */
    public static boolean validator(String id) {
        String str = "[1-9]{2}[0-9]{4}(19|20)[0-9]{2}"
                + "((0[1-9]{1})|(1[1-2]{1}))((0[1-9]{1})|([1-2]{1}[0-9]{1}|(3[0-1]{1})))"
                + "[0-9]{3}[0-9x]{1}";
        Pattern pattern = Pattern.compile(str);
        return pattern.matcher(id).matches();
    }

    /**
     * 营业执照 统一社会信用代码（15位）
     * @param license
     * @return
     */
    public static boolean isLicense15(String license) {
        if(StringUtils.isEmpty(license)) {
            return false;
        }
        if(license.length() != 15) {
            return false;
        }

        String businesslicensePrex14 = license.substring(0,14);// 获取营业执照注册号前14位数字用来计算校验码
        String businesslicense15 = license.substring(14, license.length());// 获取营业执照号的校验码
        char[] chars = businesslicensePrex14.toCharArray();
        int[] ints = new int[chars.length];
        for(int i=0; i<chars.length;i++) {
            ints[i] = Integer.parseInt(String.valueOf(chars[i]));
        }
        getCheckCode(ints);
        if(businesslicense15.equals(getCheckCode(ints)+"")) {// 比较 填写的营业执照注册号的校验码和计算的校验码是否一致
            return true;
        }
        return false;
    }

    /**
     * 获取 营业执照注册号的校验码
     * @param ints
     * @return
     */
    private static int getCheckCode(int[] ints) {
        if(null != ints && ints.length > 1) {
            int ti = 0;
            int si = 0;// pi|11+ti
            int cj = 0;// （si||10==0？10：si||10）*2
            int pj = 10;// pj=cj|11==0?10:cj|11
            for (int i=0;i<ints.length;i++) {
                ti = ints[i];
                pj = (cj % 11) == 0 ? 10 : (cj % 11);
                si = pj + ti;
                cj = (0 == si % 10 ? 10 : si % 10) * 2;
                if (i == ints.length-1) {
                    pj = (cj % 11) == 0 ? 10 : (cj % 11);
                    return pj == 1 ? 1 : 11 - pj;
                }
            }
        }// end if
        return -1;
    }

    /**
     * 营业执照 统一社会信用代码（18位）
     * @param license
     * @return
     */
    public static boolean isLicense18(String license) {
        if(StringUtils.isEmpty(license)) {
            return false;
        }
        if(license.length() != 18) {
            return false;
        }

        String regex = "^([159Y]{1})([1239]{1})([0-9ABCDEFGHJKLMNPQRTUWXY]{6})([0-9ABCDEFGHJKLMNPQRTUWXY]{9})([0-90-9ABCDEFGHJKLMNPQRTUWXY])$";
        if (!license.matches(regex)) {
            return false;
        }
        String str = "0123456789ABCDEFGHJKLMNPQRTUWXY";
        int[] ws = { 1, 3, 9, 27, 19, 26, 16, 17, 20, 29, 25, 13, 8, 24, 10, 30, 28 };
        String[] codes = new String[2];
        codes[0] = license.substring(0, license.length() - 1);
        codes[1] = license.substring(license.length() - 1, license.length());
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += str.indexOf(codes[0].charAt(i)) * ws[i];
        }
        int c18 = 31 - (sum % 31);
        if (c18 == 31) {
            c18 = 'Y';
        } else if (c18 == 30) {
            c18 = '0';
        }
        if (str.charAt(c18) != codes[1].charAt(0)) {
            return false;
        }
        return true;
    }

}
