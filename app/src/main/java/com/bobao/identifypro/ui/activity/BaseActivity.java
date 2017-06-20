package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.bobao.identifypro.R;
import com.bobao.identifypro.ui.dialog.NetworkUnConnectedDialog;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.DialogUtils;

import java.util.HashMap;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class BaseActivity extends SwipeBackActivity implements OnClickListener {

    private static final int DoubleBackTime = 2000;
    protected String TAG = BaseActivity.class.getSimpleName();

    // 页面根节点
    protected View mRootView;
    // 点击按钮的时间
    private long mKeyTime;
    // 点击按钮的次数
    private int mKeyCount;
    //是否双击
    protected boolean isNeedDoubleClick;
    private long mClickTime;

    protected Context mContext;
    protected BaseActivity mActivity;
    private GestureDetector mGestureDetector;

    private NetworkUnConnectedDialog mNetworkUnConnectedDialog;

    private View.OnClickListener mTryAgainClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            refreshData();
            if (mNetworkUnConnectedDialog != null) {
                mNetworkUnConnectedDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getIntentData();
        initBaseData();
        setContentView();
        initTitle();
        initContent();
        initFooter();
        attachData();
        //添加Activity到堆栈
        ActivityUtils.getInstance().addActivity(this);
//        if (!NetUtils.isNetworkConnected(mContext)) {
//            mNetworkUnConnectedDialog = DialogUtils.showNetworkUnConnectedDialog(mContext, mTryAgainClickListener);
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //结束Activity&从栈中移除该Activity
        ActivityUtils.getInstance().finishActivity(this);
    }

    private void initBaseData() {
        mContext = this;
        mActivity = this;
        isNeedDoubleClick = false;
        initData();
    }

    private void setContentView() {
        // 初始化页面布局
        int res = setLayoutViewId();
        if (res != 0) {
            mRootView = LayoutInflater.from(this).inflate(res, null);
            setContentView(mRootView);
        }
    }

    @Override
    public void onClick(View view) {
    }

    /**
     * 获取intent数据
     */
    protected abstract void getIntentData();

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 设置页面布局
     */
    protected abstract int setLayoutViewId();

    /**
     * 初始化title
     */
    protected abstract void initTitle();

    /**
     * 初始化内容布局
     */
    protected abstract void initContent();

    /**
     * 初始化footer
     */
    protected abstract void initFooter();

    /**
     * 为view控件绑定数据
     */
    protected abstract void attachData();

    /**
     * 刷新数据
     */
    protected abstract void refreshData();

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
     * 统一为各种view确定可以点击
     */
    protected void setClickEnable(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(true);
            }
        }
    }

    /**
     * 统一为各种view确定不可以点击
     */
    protected void setClickDisable(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setEnabled(false);
            }
        }
    }

    /**
     * 统一为各种view确定可见状态
     */
    protected void setViewVisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 统一为各种view确定不可见状态
     */
    protected void setViewInvisible(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.INVISIBLE);
            }
        }
    }

    /**
     * 统一为各种view确定不可见状态
     */
    protected void setViewGone(View... views) {
        for (View view : views) {
            if (view != null) {
                view.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 跳转到activity
     */
    protected void jump(Intent intent) {
        startActivity(intent);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Context context, Class<?> targetClass) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        jump(intent);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Context context, Class<?> targetClass, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转到activity
     */
    protected void jump(Context context, Class<?> targetClass, HashMap<String, Integer> params, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(context, targetClass);
        if (params != null) {
            for (String key : params.keySet()) {
                intent.putExtra(key, params.get(key));
            }
        }
        startActivityForResult(intent, requestCode);
    }

    /**
     * 用于显示调试信息
     */
    protected void showDebugInfo(Context context) {
        if ((System.currentTimeMillis() - mKeyTime) > 500) {
            mKeyCount = 0;
        } else {
            mKeyCount++;
            if (mKeyCount > 10) {
                DialogUtils.showDebugInfoDialog(context);
            }
        }
        mKeyTime = System.currentTimeMillis();
    }

    /**
     * 向右滑动时返回上一级页面
     */
    protected void onScroll() {

        mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if ((e2.getX() - e1.getX()) > 250) {
                    finish();
                    overridePendingTransition(R.anim.in_from_left, R.anim.out_from_right);
                }
                return super.onScroll(e1, e2, distanceX, distanceY);
            }
        });
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });

    }

    @Override
    public void onBackPressed() {
        if (isNeedDoubleClick) {
            long current = System.currentTimeMillis();
            if (current - mClickTime > DoubleBackTime) {
                DialogUtils.showShortPromptToast(mContext, R.string.press_back_again);
            } else {
                //结束Activity&从栈中移除所有Activity
                ActivityUtils.getInstance().AppExit();
            }
            mClickTime = current;
        } else {
            super.onBackPressed();
        }
    }
}