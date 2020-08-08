package com.example.main.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.example.commonlib.utils.ShareHelper;
import com.example.main.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by czy on 2019/6/24 10:02.
 */
public abstract class BaseFragment extends SupportFragment {
    /**
     * 视图是否已经初初始化
     */
    protected boolean isInit = false;
    protected boolean isLoad = false;
    protected final String TAG = "BaseFragment";
    private View view;
    protected ShareHelper shareHelper;
    protected String firmID = "";

    protected Context mContext;//上下文
    protected Activity mActivity;//界面

    private Unbinder unbinder;

    /**
     * 当视图被加载到Activity中时候调用
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        this.mActivity = (Activity) context;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(setContentView(), container, false);
        isInit = true;
        title = view.findViewById(R.id.title);
        unbinder = ButterKnife.bind(this, view);
        /**初始化的时候去加载数据**/
        isCanLoadData(savedInstanceState);

        return view;
    }

    /**
     * 视图是否已经对用户可见，系统的方法
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData(null);
        refreshData();
    }

    /**
     * 是否可以加载数据
     * 可以加载数据的条件：
     * 1.视图已经初始化
     * 2.视图对用户可见
     */
    private void isCanLoadData(Bundle savedInstanceState) {

        if (shareHelper == null)
            shareHelper = ShareHelper.getInstance();

        if (!isInit) {
            return;
        }
        if (getUserVisibleHint()) {
            firmID = (String) shareHelper.query("firmID","");
            lazyLoad(savedInstanceState);
            isLoad = true;
        } else {
            if (isLoad) {
                stopLoad();
            }
        }
    }

    /**
     * 视图销毁的时候讲Fragment是否初始化的状态变为false
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isInit = false;
        isLoad = false;
        unbinder.unbind();
    }

    protected void showToast(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * 设置Fragment要显示的布局
     *
     * @return 布局的layoutId
     */
    protected abstract int setContentView();

    /**
     * 获取设置的布局
     *
     * @return
     */
    protected View getContentView() {
        return view;
    }

    /**
     * 找出对应的控件
     *
     * @param id
     * @param <T>
     * @return
     */
    protected <T extends View> T findViewById(int id) {

        return (T) getContentView().findViewById(id);
    }

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract void lazyLoad(Bundle savedInstanceState);

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以调用此方法
     */
    protected void stopLoad() {
    }

    protected boolean isRefresh;//是否刷新


    @Override
    public void onPause() {
        super.onPause();
        isRefresh = false;
    }

//    @Override
//    public void onSupportVisible() {
//        super.onSupportVisible();
//        if (isInit && isRefresh) refreshData();
//    }

    @Override
    public void onResume() {
        super.onResume();
        isRefresh = true;
    }





    @Override
    public void onEnterAnimationEnd(Bundle savedInstanceState) {
        super.onEnterAnimationEnd(savedInstanceState);
        isInit = true;
    }


    protected void refreshData() {
        firmID = (String) shareHelper.query("firmID","");
    }

    private TextView title;

    protected void setTitleText(String titleStr) {
        title.setText(titleStr);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BaseFragment fragment = (BaseFragment) getTopChildFragment();
        fragment.onActivityResult(requestCode, resultCode, data);
    }

}