package com.bobao.identifypro.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.ui.activity.FeedBackActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RateDialog extends BaseCustomerDialog {

    private Context mContext;
    @ViewInject(R.id.tv_good)
    private View mGoodView;
    @ViewInject(R.id.tv_bad)
    private View mBadView;
    @ViewInject(R.id.tv_cancel)
    private View mCancelView;


    public RateDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        mContext = context;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_rate;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this,mRootView);
    }

    @Override
    protected void initView() {
        setOnClickListener(mGoodView, mBadView, mCancelView);
    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_bad) {
            jump(FeedBackActivity.class);
            // 记录到UserInfoUtils
            UserInfoUtils.saveUserBadRate(mContext, true);
            // 添加统计
            UmengUtils.onEvent(mContext, EventEnum.ClickSendBad);
            dismiss();
        } else if (view.getId() == R.id.tv_good) {
            ActivityUtils.jumpToMarketByOrder((Activity) mContext, mContext.getPackageName());
            // 记录到UserInfoUtils
            UserInfoUtils.saveUserGoodRate(mContext, true);
            // 添加统计
            UmengUtils.onEvent(mContext, EventEnum.ClickSendGood);
            dismiss();
        } else if (view.getId() == R.id.tv_cancel) {
            dismiss();
        }
    }
}
