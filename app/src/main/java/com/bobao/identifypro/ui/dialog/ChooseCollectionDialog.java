package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.activity.ServiceAppointmentActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by Administrator on 2015/10/19.
 */
public class ChooseCollectionDialog extends BaseCustomerDialog {
    private RecyclerView mRecyclerView;
    private Context mContext;
    private String mServiceType;
    private String mServiceTypeName;
    @ViewInject(R.id.tv_paint)
    private TextView mTvPaint;
    @ViewInject(R.id.tv_china)
    private TextView mTvChina;
    @ViewInject(R.id.tv_jade)
    private TextView mTvJade;
    @ViewInject(R.id.tv_sundry)
    private TextView mTvSundry;
    @ViewInject(R.id.tv_money)
    private TextView mTvMoney;
    @ViewInject(R.id.tv_bronze)
    private TextView mTvBronze;
    @ViewInject(R.id.ll_collect)
    private LinearLayout mLlCollection;

    public ChooseCollectionDialog(Context context, String serviceType, String serviceTypeName) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mServiceType = serviceType;
        mServiceTypeName = serviceTypeName;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_choose_collection_layout;
    }

    @Override
    protected void initTitle() {
        ViewUtils.inject(this, mRootView);
    }

    @Override
    protected void initView() {
        if ((AppConstant.SERVICE_TYPE_ID[3] + "").equals(mServiceType)){
            mTvPaint.setVisibility(View.GONE);
            mLlCollection.setVisibility(View.GONE);
        }
        setOnClickListener(mTvBronze,mTvMoney,mTvChina,mTvJade,mTvPaint,mTvSundry);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View v) {
        String selectCollection = "";
        String selectCollectionName = "";
        switch (v.getId()) {
            case R.id.tv_paint:
                selectCollection = AppConstant.IDENTIFY_KIND_TABLE_ID[2];
                selectCollectionName = AppConstant.IDENTIFY_KIND_TABLE[2];
                break;
            case R.id.tv_china:
                selectCollection = AppConstant.IDENTIFY_KIND_TABLE_ID[0];
                selectCollectionName = AppConstant.IDENTIFY_KIND_TABLE[0];
                break;
            case R.id.tv_jade:
                selectCollection = AppConstant.IDENTIFY_KIND_TABLE_ID[1];
                selectCollectionName = AppConstant.IDENTIFY_KIND_TABLE[1];
                break;
            case R.id.tv_bronze:
                selectCollection = AppConstant.IDENTIFY_KIND_TABLE_ID[3];
                selectCollectionName = AppConstant.IDENTIFY_KIND_TABLE[3];
                break;
            case R.id.tv_sundry:
                selectCollection = AppConstant.IDENTIFY_KIND_TABLE_ID[5];
                selectCollectionName = AppConstant.IDENTIFY_KIND_TABLE[5];
                break;
            case R.id.tv_money:
                selectCollection = AppConstant.IDENTIFY_KIND_TABLE_ID[4];
                selectCollectionName = AppConstant.IDENTIFY_KIND_TABLE[4];
                break;

            default:
                dismiss();
                break;
        }
        if (TextUtils.isEmpty(selectCollection)) {
            DialogUtils.showShortPromptToast(mContext, R.string.select_collection_failed);
        } else {
            Intent intent = new Intent(mContext, ServiceAppointmentActivity.class);
            intent.putExtra(IntentConstant.TARGET_ACTIVITY, IntentConstant.ACTIVITY_FROM_SERVICE_NOTE_ACTIVITY);
            intent.putExtra(IntentConstant.IDENTIFY_TYPE, selectCollection);
            intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, selectCollectionName);
            intent.putExtra(IntentConstant.SERVICE_TYPE, mServiceType);
            intent.putExtra(IntentConstant.SERVICE_TYPE_NAME, mServiceTypeName);
            ActivityUtils.jump(mContext, intent);
        }
        dismiss();
    }
}
