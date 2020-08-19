package com.example.main.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.List;

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
}
