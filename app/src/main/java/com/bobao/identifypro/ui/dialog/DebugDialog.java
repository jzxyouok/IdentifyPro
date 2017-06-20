package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.application.IdentifyProApplication;
import com.bobao.identifypro.utils.DeviceUtil;
import com.bobao.identifypro.utils.StringUtils;

public class DebugDialog extends BaseCustomerDialog {
    private Context mContext;
    private TextView mContentView;
    private String mExtraInfo;

    public DebugDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        mContext = context;
    }

    public DebugDialog(Context context, String extraInfo) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        mContext = context;
        mExtraInfo = extraInfo;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_law;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(R.string.debug);
    }

    @Override
    protected void initView() {

        mContentView = (TextView) findViewById(R.id.tv_content);
        TextView view = (TextView) findViewById(R.id.tv_button);

        mContentView.setText(R.string.debug);
        mContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        view.setText(R.string.ok);

        setOnClickListener(view);
    }

    @Override
    protected void attachData() {
        String debugInfo = "Debug info:\n";
        debugInfo = StringUtils.getString(debugInfo, "PackageName:", StringUtils.getPackageInfo(mContext).packageName, "\n");
        debugInfo = StringUtils.getString(debugInfo, "AppName:", mContext.getString(R.string.app_name, "\n"));
        debugInfo = StringUtils.getString(debugInfo, "PackageVersionCode:", StringUtils.getPackageInfo(mContext).versionCode, "\n");
        debugInfo = StringUtils.getString(debugInfo, "PackageVersionName:", StringUtils.getPackageInfo(mContext).versionName, "\n");
        debugInfo = StringUtils.getString(debugInfo, "UMENG_APPKEY:", StringUtils.getMetaData(mContext, "UMENG_APPKEY"), "\n");
        debugInfo = StringUtils.getString(debugInfo, "FLURRY_API_KEY:", StringUtils.getMetaData(mContext, "FLURRY_API_KEY"), "\n");
        debugInfo = StringUtils.getString(debugInfo, "GA_TRACK_ID:", StringUtils.getMetaData(mContext, "GA_TRACK_ID"), "\n");
        debugInfo = StringUtils.getString(debugInfo, "UMENG_CHANNEL:", StringUtils.getMetaData(mContext, "UMENG_CHANNEL"), "\n");
        debugInfo = StringUtils.getString(debugInfo, "PushName:", IdentifyProApplication.getPushName(mContext), "\n");
        debugInfo = StringUtils.getString(debugInfo, "debug:", DeviceUtil.isApkDebugable(mContext), "\n");
        if (!StringUtils.isNullOrEmpty(mExtraInfo)) {
            debugInfo = StringUtils.getString(debugInfo, mExtraInfo);
        }
        mContentView.setText(debugInfo);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_button:
                dismiss();
                break;
            default:
                break;
        }
    }

}
