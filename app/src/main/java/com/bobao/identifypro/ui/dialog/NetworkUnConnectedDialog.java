package com.bobao.identifypro.ui.dialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.bobao.identifypro.R;
import com.bobao.identifypro.utils.ActivityUtils;


/**
 * Created by you on 2015/6/1.
 */
public class NetworkUnConnectedDialog extends BaseCustomerDialog {
    private Context mContext;
    private View.OnClickListener mTryAgainListener;

    public NetworkUnConnectedDialog(Context context, View.OnClickListener tryAgainListener) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mTryAgainListener = tryAgainListener;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_network_unconnected;
    }

    @Override
    protected void initTitle() {
    }

    @Override
    protected void initView() {
        findViewById(R.id.tv_configure_network).setOnClickListener(this);
        findViewById(R.id.tv_try_again).setOnClickListener(mTryAgainListener);
    }

    @Override
    protected void attachData() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_configure_network:
                Intent intent ;
                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                if (android.os.Build.VERSION.SDK_INT > 10) {
                    intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                } else {
                    intent = new Intent();
                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
                    intent.setComponent(component);
                    intent.setAction("android.intent.action.VIEW");
                }
                ActivityUtils.jump(mContext, intent);
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }
}
