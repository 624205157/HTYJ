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
 */

public class TwoChangeDialog extends CustomDialog implements View.OnClickListener {

    private TextView titleTxt;//标题
    private TextView messageTxt;//内容
    private Button confirmBtn;//按钮1
    private Button cancel;//按钮2
    private CharSequence titleStr;//标题
    private CharSequence messageStr;//内容
    private CallPhoneListener clickListener;//监听
    private int gravity;
    private View addView;

    private String text1;//按钮1内容
    private String text2;//按钮2内容

    private int contentView = 0;
    private LinearLayout group;
    /**
     * 构造器
     *
     * @param context
     */
    public TwoChangeDialog(Context context) {
        this(context, true);
    }

    public TwoChangeDialog(Context context,int contentView,boolean dimEnabled) {
        super(context, dimEnabled);
        this.contentView = contentView;
    }

    private TwoChangeDialog(Context context, boolean dimEnabled) {
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
        setDialogWidth((int) (outMetrics.widthPixels * 0.8f));
        setDialogHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        if (contentView == 0) {
            setContentView(R.layout.dialog_text_hint2);
        }else {
            setContentView(contentView);
        }
//        setCanceledOnTouchOutside(false);
        initView();//初始化控件
        if (!TextUtils.isEmpty(text1)||!TextUtils.isEmpty(text2)) {
            setView();//设置控件
        }
        setShow();//设置显示内容
    }


    /**
     * 设置显示的内容
     */
    private void setShow() {
        if (null != titleStr) {
            titleTxt.setVisibility(View.VISIBLE);//显示控件
            titleTxt.setText(titleStr);//显示内容
        }
        if (null != messageStr) {
            messageTxt.setVisibility(View.VISIBLE);//显示控件
            messageTxt.setText(messageStr);//显示内容
        }
        if (gravity != 0){
            messageTxt.setGravity(Gravity.LEFT);
        }

        if (!TextUtils.isEmpty(text1)){
            confirmBtn.setText(text1);
        }
        if (!TextUtils.isEmpty(text2)){
            cancel.setText(text2);
        }
        if (null != addView){
            group.addView(addView,0);
        }
    }

    /**
     * 初始化控件
     */
    private void initView() {
        titleTxt = findViewById(R.id.title);
        messageTxt = findViewById(R.id.message);
        confirmBtn = findViewById(R.id.confirm);
        cancel = findViewById(R.id.cancel);

        group = findViewById(R.id.group);
    }

    /**
     * 设置控件
     */
    private void setView() {
        confirmBtn.setOnClickListener(this);//设置点击监听
        cancel.setOnClickListener(this);//设置点击监听
    }

    /**
     * 设置按钮vaule
     * */
    public TwoChangeDialog setButtonText(String text1,String text2){
        this.text1 = text1;
        this.text2 = text2;
        return this;
    }

    public TwoChangeDialog setCanceledOnTouch(boolean cancel){
        this.setCanceledOnTouchOutside(cancel);
        this.setCancelable(cancel);
        return this;
    }


    /**
     * 设置显示内容
     *
     * @param messageStr
     */
    public TwoChangeDialog setMessageStr(CharSequence messageStr) {
        this.messageStr = messageStr;
        return this;
    }
    public TwoChangeDialog setMessageStr(CharSequence messageStr, int gravity) {
        this.messageStr = messageStr;
        this.gravity = gravity;
        return this;
    }
    /**
     * 设置显示标题
     *
     * @param titleStr
     */
    public TwoChangeDialog setTitleStr(CharSequence titleStr) {
        this.titleStr = titleStr;
        return this;
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.confirm) {//按钮1
            if (null != clickListener) {
                clickListener.onClick(0);
            }
            dismiss();
        } else if (id == R.id.cancel) {//按钮2
            if (null != clickListener) {
                clickListener.onClick(1);
            }
            dismiss();
        }
    }

    /**
     * 设置监听
     * @param clickListener
     */
    public TwoChangeDialog setClickListener(CallPhoneListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }

    /**
     * 添加弹窗布局
     * @param addView
     */
    public TwoChangeDialog setAddView(View addView) {
        this.addView = addView;
        return this;
    }
}
