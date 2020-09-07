package com.example.commonlib.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.RadioButton;


import androidx.core.content.ContextCompat;

import com.example.commonlib.R;
import com.example.commonlib.bean.AddCustomValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * 只给GridLayout 加子控件
 * 添加选择按钮的帮助类
 * Created by liyanjun on 2017/8/8.
 */
@SuppressWarnings("unchecked")
public class DynamicAddCompoundHelper<B extends CompoundButton> implements CompoundButton.OnCheckedChangeListener {
    /**
     * 接口回调
     */
    public interface OnCheckedChangeListener {
        void onCheckedChanged(int tag, int parentID, String values);
    }

    public interface OnGroupIdChangeListener {
        void onGroupChanged(int tag, String groupId);
    }

    /**
     * 属性
     */
    private OnCheckedChangeListener checkedChangeListener;//接口回调
    private OnGroupIdChangeListener groupIdChangeListener;//接口回调
    private Context mContext;//上下文
    private List<AddCustomValues> list;//给子控件赋值
    private final List<AddCustomValues> values = new ArrayList<>();//给子控件赋值
    private final List<AddCustomValues> childValues = new ArrayList<>();//给子控件赋值
    private GridLayout parent;//父容器
    private final List<B> saveViewList = new ArrayList<>();//储存单选按钮的容器
    private String setStyleName;//类型名
    private int tag;//标识符
    private String selectValues;//选中的内容
    private boolean isReset;//是否重置中
    private boolean isChangeStyle;//更改样式
    private String showGroupID;

    public static int TYPE_SCREEN = 1;

    private int type = 0;

    private int screenWidth;

    /**
     * 构造器
     *
     * @param mContext
     * @param list
     * @param parent
     */
    public DynamicAddCompoundHelper(Context mContext, List<AddCustomValues> list, GridLayout parent) {
        this(mContext, list, parent, false);
    }

    public DynamicAddCompoundHelper(Context mContext, List<AddCustomValues> list, GridLayout parent, boolean isToChange) {
        this.mContext = mContext;
        this.list = list;
        this.parent = parent;
        this.isChangeStyle = isToChange;
        WindowManager wm1 = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        screenWidth = wm1.getDefaultDisplay().getWidth();
    }

    /**
     * 设置监听
     *
     * @param checkedChangeListener
     */
    public DynamicAddCompoundHelper<B> addCheckedChangeListener(OnCheckedChangeListener checkedChangeListener) {
        this.checkedChangeListener = checkedChangeListener;
        return this;
    }

    public DynamicAddCompoundHelper<B> addGroupIdChangeListener(OnGroupIdChangeListener groupIdChangeListener) {
        this.groupIdChangeListener = groupIdChangeListener;
        return this;
    }

    /**
     * 管理容器类
     */
    public DynamicAddCompoundHelper<B> manageGridLayoutView(Class<B> compoundButtonClass) {
        createViewFromAddValuesData(compoundButtonClass);
        addView();
        return this;
    }

    /**
     * 创建一行N个
     */
    public DynamicAddCompoundHelper<B> manageGridLayoutView(Class<B> compoundButtonClass, int num) {
        createViewFromAddValuesData(compoundButtonClass);
//        createNewViewFromAddValuesData(compoundButtonClass);
        if (showGroupID == null) {
            setViewTValues(list, num);
        } else {
            setViewTValues(childValues, num);
        }
        return this;
    }


    /**
     * 显示添加添加控件
     */
    private void addView() {
        if (showGroupID == null) {
            if (list != null) {
                setViewTValues(list);
            }
        } else {
            setViewTValues(childValues);
        }
    }

    private void setViewTValues(List<AddCustomValues> list) {
        for (int i = 0; i < list.size(); i++) {
            saveViewList.get(i).setOnCheckedChangeListener(this);
            //设置添加的子控件边距
            GridLayout.Spec rowSpec = GridLayout.spec(i / 2, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % 2, 1f);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_2);
            layoutParams.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_2);


            parent.addView(saveViewList.get(i), layoutParams);//添加控件
        }
    }

    private void setViewTValues(List<AddCustomValues> list, int num) {
        parent.setColumnCount(num);
        for (int i = 0; i < list.size(); i++) {
            saveViewList.get(i).setOnCheckedChangeListener(this);
            //设置添加的子控件边距
            GridLayout.Spec rowSpec = GridLayout.spec(i / num, 1f);
            GridLayout.Spec columnSpec = GridLayout.spec(i % num, 1f);
            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(rowSpec, columnSpec);
            layoutParams.topMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_8);
            layoutParams.bottomMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_8);

            switch (type) {
                case 1:
                    layoutParams.rightMargin = mContext.getResources().getDimensionPixelOffset(R.dimen.dp_15);//标签页面需求
                    break;
                case 2:
                    layoutParams.width = screenWidth / (num + 1);
                    break;
            }
            parent.addView(saveViewList.get(i), layoutParams);//添加控件
        }
    }

    /**
     * 设置标识
     */
    public DynamicAddCompoundHelper<B> setTag(int tag) {
        this.tag = tag;
        return this;
    }

    /**
     * 根据返回数据创建控件
     */
    @SuppressWarnings({"unchecked", "ConstantConditions"})
    private void createViewFromAddValuesData(Class<B> compoundButtonClass) {
        if (null == list || list.isEmpty()) return;
        setStyleName = compoundButtonClass.getSimpleName();
        int styleID = ("CheckBox".equals(setStyleName) && !isChangeStyle) ? R.style.CheckItemCheckBoxStyleWithMargin : R.style.CheckItemRadioButtonStyleWithMargin;
        //遍历返回的数据集合将生成的控件保存在控件集合中
        try {
            AddCustomValues values;
            for (int i = 0; i < list.size(); i++) {
                values = list.get(i);//获取指定索引的对象
                B t;
                if (null != showGroupID && !TextUtils.equals(showGroupID, values.getParamGroup())) {
                    continue;
                } else {
                    childValues.add(values);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Class[] classes = {Context.class, AttributeSet.class, int.class, int.class};//构造参数类型
                    Constructor constructor = compoundButtonClass.getDeclaredConstructor(classes);
                    t = (B) constructor.newInstance(mContext, null, 0, styleID);//获取市里
                } else {
                    Class[] classes = {Context.class};//构造参数类型
                    Constructor constructor = compoundButtonClass.getDeclaredConstructor(classes);
                    t = (B) constructor.newInstance(mContext);//获取市里
                    Drawable drawable;
                    if ("CheckBox".equals(setStyleName) && !isChangeStyle) {
                        drawable = ContextCompat.getDrawable(mContext, R.drawable.more_checked);
                    } else {
                        drawable = ContextCompat.getDrawable(mContext, R.drawable.select_checked);
                    }
                    t.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
                    Field field = null;
                    try {
                        field = t.getClass().getSuperclass().getDeclaredField("mButtonDrawable");
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    field.setAccessible(true);
                    field.set(t, null);
                }
                t.setBackground(null);
                t.setText(values.getParamDesc());//设置控件显示的文本
                t.setTag(values);//设置控件的标签
                saveViewList.add(t);//添加到控件中
            }
        } catch (InstantiationException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    /**
     * 按钮选择事件
     *
     * @param buttonView
     * @param isChecked
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isReset) return;
        if (isChangeStyle) {//复选框当单选框用
            selectValues = setCheckBoxRadioButtonValues(buttonView, isChecked, selectValues);
            if (null != checkedChangeListener) {
                checkedChangeListener.onCheckedChanged(tag, parent.getId(), selectValues == null ? "" : selectValues);
            }
            return;
        }
        if (TextUtils.equals(setStyleName, "CheckBox")) {//复选框
            selectValues = setCheckBoxValues(buttonView, isChecked);
            if (null != checkedChangeListener) {
                checkedChangeListener.onCheckedChanged(tag, parent.getId(), selectValues);
            }
        } else {//单选框
            setRadioButtonValues(buttonView, isChecked);
        }
    }

    /**
     * 获取选中的内容
     *
     * @param buttonView
     * @param isChecked
     * @return
     */
    private String setCheckBoxValues(CompoundButton buttonView, boolean isChecked) {

        String commitStr = "";
        AddCustomValues value = (AddCustomValues) buttonView.getTag();
        if (isChecked) {//增加
            values.add(value);
        } else {//移除
            values.remove(value);
        }
        if (values.size() <= 0) return commitStr;
        //拼接字符串
        for (int i = 0; i < values.size(); i++) {
            commitStr += values.get(i).getParamValue() + ",";
        }
        return commitStr.substring(0, commitStr.length() - 1);
    }

    /**
     * 重置状态
     */
    public void resetChecked() {
        if (saveViewList.isEmpty()) return;
        isReset = true;
        for (int i = 0; i < saveViewList.size(); i++) {
            saveViewList.get(i).setChecked(false);
        }
        checkedChangeListener.onCheckedChanged(tag, parent.getId(), "");
        if (values.size() > 0) values.clear();
        isReset = false;
    }

    /**
     * 选择最后一位是"1" 清空之前选项
     */
    public void resetCheckedLast() {
        if (saveViewList.isEmpty()) return;
        isReset = true;
        for (int i = 1; i < saveViewList.size(); i++) {
            saveViewList.get(i).setChecked(false);
        }
        checkedChangeListener.onCheckedChanged(tag, parent.getId(), "1");
        AddCustomValues value = null;
        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getParamValue().equals("1")) {
                value = values.get(i);
            }
        }
        if (values.size() > 0) values.clear();
        values.add(value);
        isReset = false;
    }

    /**
     * 选择第一位是"1" 清空"1"
     */
    public void resetCheckedFirst() {
        if (saveViewList.isEmpty()) return;
        isReset = true;
        saveViewList.get(0).setChecked(false);

        for (int i = 0; i < values.size(); i++) {
            if (values.get(i).getParamValue().equals("1")) {
                values.remove(values.get(i));
            }
        }
        checkedChangeListener.onCheckedChanged(tag, parent.getId(), values.get(0).getParamValue());
        isReset = false;
    }

    /**
     * 获取单选的值
     *
     * @param buttonView
     * @param isChecked
     */
    private void setRadioButtonValues(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            return;
        } else {
            AddCustomValues value = (AddCustomValues) buttonView.getTag();
            if (null != checkedChangeListener) {
                checkedChangeListener.onCheckedChanged(tag, parent.getId(), value.getParamValue());
            }
            if (null != groupIdChangeListener) {
                groupIdChangeListener.onGroupChanged(tag, value.getParamGroup());
            }
        }
        //单选
        for (int i = 0; i < saveViewList.size(); i++) {
            if (buttonView != saveViewList.get(i)) {
                saveViewList.get(i).setChecked(false);
            }
        }
    }

    /**
     * 单选
     *
     * @param buttonView
     * @param isChecked
     * @param values
     * @return
     */
    private String setCheckBoxRadioButtonValues(CompoundButton buttonView, boolean isChecked, String values) {
        if (values != null && values.equals(((AddCustomValues) buttonView.getTag()).getParamValue())) {
            values = null;
        }
        if (!isChecked) {
            return values;
        }
        //单选
        for (int i = 0; i < saveViewList.size(); i++) {
            if (buttonView == saveViewList.get(i)) {
                values = ((AddCustomValues) buttonView.getTag()).getParamValue();
                continue;
            }
            saveViewList.get(i).setChecked(false);
        }
        return values;
    }

    /**
     * 根据字符选中对应的checkBox
     *
     * @param message
     */
    public void checkedBoxFromInfo(String message) {
        if (null == message) return;
        if (message.isEmpty()) return;
        if (message.charAt(0) == ',') message = message.substring(1, message.length());
        if (message.contains(",")) {
            String tags[] = message.split(",");
            List<String> viewTag = new ArrayList<>();
            for (int i = 0; i < saveViewList.size(); i++) {
                AddCustomValues values = (AddCustomValues) saveViewList.get(i).getTag();
                viewTag.add((values.getParamValue()));
            }
            //开始比对
            for (int i = 0; i < tags.length; i++) {
                int position = viewTag.indexOf(tags[i]);
                saveViewList.get(position).setChecked(true);
            }
        } else {
            for (int i = 0; i < saveViewList.size(); i++) {
                CheckBox box = (CheckBox) saveViewList.get(i);
                AddCustomValues values = (AddCustomValues) box.getTag();
                if (message.equals(values.getParamValue())) {
                    box.setChecked(true);//选中
                    return;
                }
            }
        }
    }

    /**
     * 单选
     *
     * @param radioValues
     */
    public void radioBoxFromInfo(String radioValues) {
        if (null == radioValues) return;
        if (radioValues.isEmpty()) return;
        if (radioValues.charAt(0) == ';')
            radioValues = radioValues.substring(1, radioValues.length());
        for (int i = 0; i < saveViewList.size(); i++) {
            RadioButton radio = (RadioButton) saveViewList.get(i);
            AddCustomValues values = (AddCustomValues) radio.getTag();
            if (radioValues.equals(values.getParamValue())) {
                radio.setChecked(true);//选中
                return;
            }
        }
    }

    /**
     * 获取数据值
     *
     * @param checkValues
     * @return
     */
    @Deprecated
    public String getMessage(String checkValues) {
        System.out.println(values.size());
        System.out.println(list.size());
        for (int i = 0; i < values.size(); i++) {
            System.out.println(values.get(i).toString());
        }
        if (checkValues.isEmpty()) return "";
        else {
            String message = null;
            if (checkValues.contains(",")) {
                StringBuilder builder = new StringBuilder();
                String tags[] = checkValues.split(",");
                List<String> viewTag = new ArrayList<>();
                for (int i = 0; i < values.size(); i++) {
                    viewTag.add((values.get(i).getParamValue()));
                }
                //开始比对
                for (String tag1 : tags) {
                    int position = viewTag.indexOf(tag1);
                    builder.append(values.get(position).getParamDesc()).append(" ");
                }
                message = builder.toString().trim();
                return message;
            } else {
                for (int i = 0; i < values.size(); i++) {
                    if (values.get(i).getParamValue().equals(checkValues)) {
                        message = values.get(i).getParamDesc();
                        break;
                    }
                }
            }
            return message;
        }
    }

    public String getSelectValues(String checkKey) {
        if (null == checkKey) return "";
        if (TextUtils.isEmpty(checkKey)) return "";
        for (AddCustomValues values : list) {
            if (values.getParamValue().equals(checkKey)) {
                return values.getParamDesc();
            }
        }
        return "";
    }

    /**
     * 获取第一个
     *
     * @return
     */
    public String getFirstGroupID() {
        return list.get(0).getParamValue();
    }

    /**
     * 根据groupID显示控件
     *
     * @param showGroupID
     * @param compoundButtonClass
     */
    public void setShowGroupID(String showGroupID, Class<B> compoundButtonClass) {
        this.showGroupID = showGroupID;
        parent.removeAllViews();
        saveViewList.clear();
        childValues.clear();
        manageGridLayoutView(compoundButtonClass);
    }
}
