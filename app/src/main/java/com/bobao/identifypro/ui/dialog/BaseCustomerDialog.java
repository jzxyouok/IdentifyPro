package com.bobao.identifypro.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

public abstract class BaseCustomerDialog extends Dialog implements View.OnClickListener {

    private Context context;
    // 页面根节点
    protected View mRootView;

    public BaseCustomerDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    protected abstract int setLayoutViewId();

    protected abstract void initTitle();

    protected abstract void initView();

    protected abstract void attachData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        setCanceledOnTouchOutside(false);
        initTitle();
        initView();
        attachData();
    }

    private void setContentView() {
        // 初始化页面布局
        int res = setLayoutViewId();
        if (res != 0) {
            mRootView = LayoutInflater.from(context).inflate(res, null);
            setContentView(mRootView);
        }
    }

    /**
     * 统一为各种view添加点击事件
     */
    protected void setOnClickListener(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setOnClickListener(this);
            }
        }
    }

    /**
     * 跳转到activity
     */
    protected void jump(Intent intent) {
        context.startActivity(intent);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Class<?> targetClass) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        jump(intent);
    }

}
