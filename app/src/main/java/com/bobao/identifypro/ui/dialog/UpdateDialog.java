package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.utils.UmengUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateResponse;

import java.io.File;

public class UpdateDialog extends BaseCustomerDialog {
    private Context mContext;
    private TextView mNewVersionView;
    private TextView mDesView;
    private TextView mOkView;
    private TextView mCancelView;
    private UpdateResponse mUpdateInfo;

    private String mNewVersion;
    private String mUpdateDes;

    public UpdateDialog(Context context, UpdateResponse updateInfo, String newView, String updateDes) {
        super(context, R.style.CustomDialog);
        mContext = context;
        mUpdateInfo = updateInfo;
        mNewVersion = newView;
        mUpdateDes = updateDes;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ok:
                File file = UmengUpdateAgent.downloadedFile(mContext, mUpdateInfo);
                if (file == null) {
                    UmengUpdateAgent.startDownload(mContext, mUpdateInfo);
                } else {
                    UmengUpdateAgent.startInstall(mContext, file);
                }
                UmengUtils.onEvent(mContext, EventEnum.UserUmengUpdateOK);
                dismiss();
                break;
            case R.id.tv_cancel:
                UmengUpdateAgent.ignoreUpdate(mContext, mUpdateInfo);
                UmengUtils.onEvent(mContext, EventEnum.UserUmengUpdateNo);
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_umeng_update;
    }

    @Override
    protected void initTitle() {
        TextView mTitleView = (TextView) findViewById(R.id.tv_title);
        mTitleView.setText(R.string.find_new_version);
    }

    @Override
    protected void initView() {
        mNewVersionView = (TextView) findViewById(R.id.tv_new_version);
        mDesView = (TextView) findViewById(R.id.tv_umeng_update_content);
        mCancelView = (TextView) findViewById(R.id.tv_cancel);
        mOkView = (TextView) findViewById(R.id.tv_ok);
        setOnClickListener(mCancelView, mOkView);
        setCancelable(false);
    }

    @Override
    protected void attachData() {
        mNewVersionView.setText(mNewVersion);
        mDesView.setText(mUpdateDes);
    }
}
