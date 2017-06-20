package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.MyIntegrateResponse;
import com.bobao.identifypro.manager.UserBaseInfoManager;
import com.bobao.identifypro.utils.NetUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Properties;

public class UserIntegrateActivity extends BaseActivity {
    @ViewInject(R.id.tv_back)
    private TextView mTvBack;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.tv_integrate)
    private TextView mTvIntegrate;
    @ViewInject(R.id.tv_grade_rule)
    private TextView mTvGradeRule;
    @ViewInject(R.id.my_grade)
    private TextView mTvGrade;
    @ViewInject(R.id.tv_my_recharge)
    private TextView mTvRecharge;
    @ViewInject(R.id.tv_rule)
    private TextView mTvRule;
    @ViewInject(R.id.tv_mode)
    private TextView mTvMode;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_my_integrate;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
        mTvTitle.setText(R.string.my_grade);
        setOnClickListener(mTvBack);
    }

    @Override
    protected void initContent() {
        setOnClickListener(mTvGrade, mTvRecharge);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        if (NetUtils.isNetworkConnected(mContext)) {
            NetUtils.getInstance(false).get(mContext, NetConstant.getMyIntegrateParams(mContext), new MyIntegrateListener(mContext));
        } else if (!TextUtils.isEmpty(UserBaseInfoManager.getIntegrateVersion(mContext))) {
            mTvIntegrate.setText(UserBaseInfoManager.getIntegrateIntegral(mContext));
            mTvGradeRule.setText(UserBaseInfoManager.getIntegrateSlogan(mContext));
            mTvRule.setText(UserBaseInfoManager.getIntegrateRule(mContext));
            mTvMode.setText(UserBaseInfoManager.getIntegrateMode(mContext));
        }
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.my_grade:
                jump(mContext, IntegrateDetailActivity.class);
                break;
            case R.id.tv_my_recharge:
                jump(mContext, InviteCodeActivity.class);
                break;
            default:
                break;
        }
    }

    private class MyIntegrateListener extends NetUtils.Callback<MyIntegrateResponse> {
        public MyIntegrateListener(Context context) {
            super(context, MyIntegrateResponse.class);
        }

        @Override
        public void onNetSuccess(MyIntegrateResponse response) {
            if (response.getError() || response.getData() == null) {
                return;
            }
            mTvIntegrate.setText(response.getData().getIntegral());
            mTvGradeRule.setText(response.getData().getSlogan());
            mTvRule.setText(response.getData().getRule());
            mTvMode.setText(response.getData().getMode());

            if (TextUtils.isEmpty(UserBaseInfoManager.getIntegrateVersion(mContext))) {
                Properties properties = new Properties();
                properties.put(IntentConstant.SP_KEY_USER_INTEGRATE_VERSION, String.valueOf(response.getData().getEdition()));
                properties.put(IntentConstant.SP_KEY_USER_INTEGRATE_INTEGRAL, response.getData().getIntegral());
                properties.put(IntentConstant.SP_KEY_USER_INTEGRATE_SLOGAN, response.getData().getSlogan());
                properties.put(IntentConstant.SP_KEY_USER_INTEGRATE_RULE, response.getData().getRule());
                properties.put(IntentConstant.SP_KEY_USER_INTEGRATE_MODE, response.getData().getMode());
                UserBaseInfoManager.getsInstance().saveBaseInfo(mContext, properties);
            } else if (!TextUtils.equals(String.valueOf(response.getData().getEdition()), UserBaseInfoManager.getIntegrateVersion(mContext))) {
                UserBaseInfoManager.updateIntegrateVersion(mContext, String.valueOf(response.getData().getEdition()));
                UserBaseInfoManager.updateIntegrateIntegral(mContext, response.getData().getIntegral());
                UserBaseInfoManager.updateIntegrateSlogan(mContext, response.getData().getSlogan());
                UserBaseInfoManager.updateIntegrateRule(mContext, response.getData().getRule());
                UserBaseInfoManager.updateIntegrateMode(mContext, response.getData().getMode());
            }
        }
    }
}
