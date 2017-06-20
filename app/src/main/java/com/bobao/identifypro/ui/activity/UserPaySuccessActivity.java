package com.bobao.identifypro.ui.activity;


import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.utils.StringUtils;

public class UserPaySuccessActivity extends BaseActivity {
    private TextView mEarnPointsTv;
    private View mIdentifyView;
    private View mInviteFriendView;

    @Override
    protected void getIntentData() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_pay_success;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.pay_success);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mEarnPointsTv = (TextView) findViewById(R.id.tv_earn_points);
        String earnPoint = String.format("您本次支付获得%d鉴定积分", 5);
        mEarnPointsTv.setText(StringUtils.getMultiColorString(mContext.getResources().getColor(R.color.orange_yellow), earnPoint, "5"));
        mIdentifyView = findViewById(R.id.tv_continue_identify);
        mInviteFriendView = findViewById(R.id.tv_invite_friend);
        setOnClickListener(mIdentifyView, mInviteFriendView);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_continue_identify:
                intent = new Intent(mContext, MainActivity.class);
                jump(intent);
                finish();
                break;
            default:
                break;

        }
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {

    }
}
