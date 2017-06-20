package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.ui.activity.ExpertListActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.UmengUtils;

public class ChooseDialog extends BaseCustomerDialog {
    private Context mContext;

    public ChooseDialog(Context context) {
        super(context, R.style.CustomDialog);
        mContext = context;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_choose;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText("请选择您要鉴定的钱币");
    }

    @Override
    protected void initView() {
        View paperView = findViewById(R.id.iv_money_paper);
        View silverView = findViewById(R.id.iv_money_silver);
        View bronzeView = findViewById(R.id.iv_money_bronze);
        setCanceledOnTouchOutside(true);
        setOnClickListener(paperView, silverView, bronzeView);
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(mContext, ExpertListActivity.class);
        switch (view.getId()) {
            case R.id.iv_money_paper:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeMoneyPaper);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[3]);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyTypeMoneyPaper);
                break;
            case R.id.iv_money_silver:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeMoneySilver);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[3]);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyTypeMoneySilver);
                break;
            case R.id.iv_money_bronze:
                intent.putExtra(IntentConstant.IDENTIFY_TYPE, AppConstant.IdentifyTypeMoneyBronze);
                intent.putExtra(IntentConstant.IDENTIFY_TYPE_NAME, AppConstant.IDENTIFY_KIND_TABLE[3]);
                UmengUtils.onEvent(mContext, EventEnum.IdentifyTypeMoneyBronze);
                break;
            default:
                break;
        }
        dismiss();
        ActivityUtils.jump(mContext, intent);
    }

}
