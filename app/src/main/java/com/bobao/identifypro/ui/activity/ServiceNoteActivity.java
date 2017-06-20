package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.ServiceNoteResponse;
import com.bobao.identifypro.ui.adapter.ServiceProcessAdapter;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;

public class ServiceNoteActivity extends BaseActivity {
    private TextView mServiceNoteTv;
    private TextView mIdentifyTimeTv;
    private TextView mResponseTimeTv;
    private TextView mHelpLineTv;
    private RecyclerView mServiceProcessRecyclerView;
    private TextView mImmediateAppointmentTv;
    private LinearLayout mLlMeetDateContainer;
    private TextView mTvMeetDate;

    private ServiceProcessAdapter mServiceProcessAdapter;
    private String mExpertId;
    private String mExpertName = "";
    private String mIdentifyType = "";
    private String mIdentifyName = "";
    private String mServiceType = "";
    private String mServiceTypeName = "";
    private String mAppraisalCost = "";

    @Override
    protected void getIntentData() {
        mExpertId = getIntent().getStringExtra(IntentConstant.EXPERT_ID);
        mExpertName = getIntent().getStringExtra(IntentConstant.EXPERT_NAME);
        mServiceType = getIntent().getStringExtra(IntentConstant.SERVICE_TYPE);
        mServiceTypeName = getIntent().getStringExtra(IntentConstant.SERVICE_TYPE_NAME);
        mIdentifyType = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE);
        mIdentifyName = getIntent().getStringExtra(IntentConstant.IDENTIFY_TYPE_NAME);
        mAppraisalCost = getIntent().getStringExtra(IntentConstant.APPRAISAL_COST);
    }

    @Override
    protected void initData() {
        mServiceProcessAdapter = new ServiceProcessAdapter(mContext,mServiceTypeName);
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_service_note;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(mServiceTypeName);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mLlMeetDateContainer = (LinearLayout) findViewById(R.id.ll_meet_date);
        mTvMeetDate = (TextView) findViewById(R.id.tv_meet_date);
        mServiceNoteTv = (TextView) findViewById(R.id.tv_service_note);
        mIdentifyTimeTv = (TextView) findViewById(R.id.tv_identify_time);
        mResponseTimeTv = (TextView) findViewById(R.id.tv_response_time);
        mHelpLineTv = (TextView) findViewById(R.id.tv_helpline);
        mImmediateAppointmentTv = (TextView) findViewById(R.id.tv_immediate_appointment);
        mServiceProcessRecyclerView = (RecyclerView) findViewById(R.id.rv_service_process);
        GridLayoutManager gridLayoutManagerDecisionService = new GridLayoutManager(mContext, IntentConstant.RECYCLERVIEW_SIZE_GRIDLAYOUT_FOUR);
        mServiceProcessRecyclerView.setLayoutManager(gridLayoutManagerDecisionService);
        mServiceProcessRecyclerView.setAdapter(mServiceProcessAdapter);
        if ((AppConstant.SERVICE_TYPE_ID[3] + "").equals(mServiceType)){
            mLlMeetDateContainer.setVisibility(View.VISIBLE);
        }
        setOnClickListener(mImmediateAppointmentTv, mHelpLineTv);
    }

    @Override
    protected void initFooter() {

    }

    @Override
    protected void attachData() {

    }

    @Override
    protected void refreshData() {
        NetUtils.getInstance(false).get(mContext, NetConstant.getServiceNoteParams(mContext, mServiceType), new ServiceNoteListener(mContext));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_helpline:
                ActivityUtils.makeCallIntent(mContext, mHelpLineTv.getText().toString().trim());
                break;
            case R.id.tv_immediate_appointment:
                if (!TextUtils.isEmpty(mIdentifyType)) {//从专家详情入口进入，已确定鉴定类型与专家
                    Intent intent = new Intent(mContext, ServiceAppointmentActivity.class);
                    intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY_FULL);
                    intent.putExtra(IntentConstant.IDENTIFY_TYPE, mIdentifyType);
                    intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, mIdentifyName);
                    intent.putExtra(IntentConstant.SERVICE_TYPE, mServiceType);
                    intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, mServiceTypeName);
                    intent.putExtra(IntentConstant.EXPERT_ID, mExpertId);
                    intent.putExtra(IntentConstant.EXPERT_NAME, mExpertName);
                    intent.putExtra(IntentConstant.APPRAISAL_COST, mAppraisalCost);
                    jump(intent);
                } else {//从首页服务进入，选择确定鉴定类型
                    DialogUtils.showChooseCollectionDialog(mContext, mServiceType, mServiceTypeName);
                }
                break;
            default:
                super.onClick(view);
                break;
        }
    }

    private class ServiceNoteListener extends NetUtils.Callback<ServiceNoteResponse> {

        public ServiceNoteListener(Context context) {
            super(context, ServiceNoteResponse.class);
        }

        @Override
        public void onNetSuccess(ServiceNoteResponse response) {
            ServiceNoteResponse.DataEntity dataEntity = response.getData();
            if (dataEntity != null) {
                mServiceNoteTv.setText(dataEntity.getIntro());
                mIdentifyTimeTv.setText(dataEntity.getDuration());
                mResponseTimeTv.setText(dataEntity.getResponse());
                mHelpLineTv.setText(dataEntity.getPhone());
                mTvMeetDate.setText(dataEntity.getMeet_time());
            }
        }
    }
}
