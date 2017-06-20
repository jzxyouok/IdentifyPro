package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.ResumeDetailResponse;
import com.bobao.identifypro.ui.adapter.ResumeRecordAdapter;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.HashMap;

public class UserWalletActivity extends BaseActivity {
    @ViewInject(R.id.tv_back)
    private TextView mTvBack;
    @ViewInject(R.id.tv_title)
    private TextView mTvTitle;
    @ViewInject(R.id.tv_money)
    private TextView mTvMoney;
    @ViewInject(R.id.tv_grade_rule)
    private TextView mTvGradeRule;
    @ViewInject(R.id.my_grade)
    private TextView mTvMyGrade;
    @ViewInject(R.id.tv_my_recharge)
    private TextView mTvMyRecharge;
    @ViewInject(R.id.ll_resume_detail)
    private LinearLayout mLlResumeDetail;
    @ViewInject(R.id.tv_more)
    private TextView mMoreTv;
    @ViewInject(R.id.rcv_resume_record)
    private RecyclerView mRecyclerView;

    private ResumeRecordAdapter mResumeRecordAdapter;

    private String mUserId;

    private ArrayList<String> mNames;
    private ArrayList<String> mTimes;
    private ArrayList<String> mFroms;
    private ArrayList<String> mPrices;

    @Override
    protected void getIntentData() {
        mUserId = getIntent().getStringExtra(IntentConstant.USER_ID);
    }

    @Override
    protected void initData() {
        mResumeRecordAdapter = new ResumeRecordAdapter();
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_wallet;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
        mTvTitle.setText(R.string.my_wallet_money);
        setOnClickListener(mTvBack);
    }

    @Override
    protected void initContent() {
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        setOnClickListener(mTvMyGrade, mTvMyRecharge, mLlResumeDetail);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {
        NetUtils.getInstance(false).get(mContext, NetConstant.getResumeDetailParams(mContext), new AccountListener(mContext));
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        HashMap<String, String> map = new HashMap<>();
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.my_grade:
                intent = new Intent(this, UserIntegrateActivity.class);
                jump(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(this, EventEnum.UserPageScore, map);
                break;
            case R.id.tv_my_recharge:
                intent = new Intent(this, UserRechargeSelectActivity.class);
                intent.putExtra(IntentConstant.USER_ID, mUserId);
                jump(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(this, EventEnum.UserWalletPageRecharge, map);
                break;
            case R.id.ll_resume_detail:
                if (mMoreTv.getVisibility() == View.GONE || mPrices == null) {
                    return;
                }
                intent = new Intent(this, ResumeDetailActivity.class);
                intent.putStringArrayListExtra(IntentConstant.RESUME_DETAIL_NAME, mNames);
                intent.putStringArrayListExtra(IntentConstant.RESUME_DETAIL_TIME, mTimes);
                intent.putStringArrayListExtra(IntentConstant.RESUME_DETAIL_FROM, mFroms);
                intent.putStringArrayListExtra(IntentConstant.RESUME_DETAIL_PRICE, mPrices);
                jump(intent);
                map.clear();
                map.put(UmengConstants.KEY_USER_PAGE_ID, mUserId);
                UmengUtils.onEvent(this, EventEnum.UserWalletPageRechargeRecord, map);
                break;
        }
    }

    private class AccountListener extends NetUtils.Callback<ResumeDetailResponse> {
        public AccountListener(Context context) {
            super(context, ResumeDetailResponse.class);
        }

        @Override
        public void onNetSuccess(ResumeDetailResponse response) {
            if (response.getError() || response.getData() == null || response.getData().getList() == null || response.getData().getList().size() == 0) {
                return;
            }
            mTvMoney.setText(String.valueOf(response.getData().getMoney()));
            mMoreTv.setVisibility(response.getData().getList().size() > 6 ? View.VISIBLE : View.GONE);

            mNames = new ArrayList<>();
            mTimes = new ArrayList<>();
            mFroms = new ArrayList<>();
            mPrices = new ArrayList<>();
            for (ResumeDetailResponse.DataEntity.ListEntity listEntity : response.getData().getList()) {
                mNames.add(listEntity.getType_name());
                mTimes.add(listEntity.getTime());
                mFroms.add(listEntity.getFrom());
                mPrices.add(listEntity.getPrice());
            }
            if (response.getData().getList().size() > 6) {
                mResumeRecordAdapter.setData(mNames.subList(0, 6), mTimes.subList(0, 6), mFroms.subList(0, 6), mPrices.subList(0, 6));
            } else {
                mResumeRecordAdapter.setData(mNames, mTimes, mFroms, mPrices);
            }
            mRecyclerView.setAdapter(mResumeRecordAdapter);
        }
    }
}
