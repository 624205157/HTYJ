package com.example.commonlib.view;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.commonlib.R;
import com.example.commonlib.listener.OnSelectClickListener;


/**
 * 文本提示窗
 * Created by liyanjun on 2017/6/17.
 */

public class TextHintDialog extends CustomDialog implements View.OnClickListener {

    private TextView titleTxt;//标题
    private TextView messageTxt;//内容
    private Button confirmBtn;//确定按钮
    private CharSequence titleStr;//标题
    private CharSequence messageStr;//内容
    private OnSelectClickListener clickListener;//监听
    private int gravity;
    /**
     * 构造器
     *
     * @param context
     */
    public TextHintDialog(Context context) {
        this(context, true);
    }

    private TextHintDialog(Context context, boolean dimEnabled) {
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
        setContentView(R.layout.dialog_text_hint);
        setCanceledOnTouchOutside(false);
        initView();//初始化控件
        setView();//设置控件
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
    }

    /**
     * 初始化控件
     */
    private void initView() {
        titleTxt = (TextView) findViewById(R.id.title);
        messageTxt = (TextView) findViewById(R.id.message);
        confirmBtn = (Button) findViewById(R.id.confirm);
    }

    /**
     * 设置控件
     */
    private void setView() {
        confirmBtn.setOnClickListener(this);//设置点击监听
    }

    /**
     * 设置显示内容
     *
     * @param messageStr
     */
    public TextHintDialog setMessageStr(CharSequence messageStr) {
        this.messageStr = messageStr;
        return this;
    }
    public TextHintDialog setMessageStr(CharSequence messageStr,int gravity) {
        this.messageStr = messageStr;
        this.gravity = gravity;
        return this;
    }
    /**
     * 设置显示标题
     *
     * @param titleStr
     */
    public TextHintDialog setTitleStr(CharSequence titleStr) {
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
        if (v.getId() == R.id.confirm) {//确定
            if (null != clickListener) {
                clickListener.onChecked(true);
            }
            dismiss();
        }
    }

    /**
     * 设置监听
     * @param clickListener
     */
    public TextHintDialog setClickListener(OnSelectClickListener clickListener) {
        this.clickListener = clickListener;
        return this;
    }
}
