package com.example.main.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by 陈泽宇 on 2020/8/18
 * Describe:
 */
public class Utils {

    public static String getText(TextView v){
        return TextUtils.isEmpty(v.getText()) ? "" : v.getText() + "";
    }
}
