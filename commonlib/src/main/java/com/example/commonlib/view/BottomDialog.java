package com.example.commonlib.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.commonlib.R;
import com.example.commonlib.listener.CallPhoneListener;


/**
 * Created by 陈泽宇 on 2018/4/3.
 * 底部弹框
 */

public class BottomDialog extends CustomDialog {

    private View addView;


    private int contentView = 0;
    private LinearLayout group;
    private float width;

    /**
     * 构造器
     *
     * @param context
     */
    public BottomDialog(Context context) {
        this(context, true);
    }

    public BottomDialog(Context context, int contentView, boolean dimEnabled, float width) {
        super(context, dimEnabled);
        this.contentView = contentView;
        this.width = width;
    }

    private BottomDialog(Context context, boolean dimEnabled) {
        super(context, dimEnabled);
    }

    /**
     * 设置弹窗显示效果
     *
     * @param windowManager
     */
    @Override
    protected void onCreateView(WindowManager windowManager) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        setDialogWidth((int) (outMetrics.widthPixels * 1f));
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setDialogGravity(Gravity.BOTTOM);
        setAnim(R.anim.bottom_in);
        setContentView(contentView);
//        setCanceledOnTouchOutside(false);
        initView();//初始化控件
        setShow();//设置显示内容
    }


    /**
     * 设置显示的内容
     */
    private void setShow() {
        if (null != addView) {
            group.addView(addView, 0);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {

        group = findViewById(R.id.group);
    }



    public BottomDialog setCanceledOnTouch(boolean cancel) {
        this.setCanceledOnTouchOutside(cancel);
        this.setCancelable(cancel);
        return this;
    }







    /**
     * 添加弹窗布局
     *
     * @param addView
     */
    public BottomDialog setAddView(View addView) {
        this.addView = addView;
        return this;
    }
}
