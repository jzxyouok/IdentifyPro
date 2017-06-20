package com.bobao.identifypro.ui.dialog;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

public class ProgressDialog extends BaseCustomerDialog {
    private String mTitle;

    private CircleProgressBar mProgress;

    public ProgressDialog(Context context) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
    }

    public ProgressDialog(Context context, String title) {
        super(context, R.style.CustomDialog);
        setCanceledOnTouchOutside(false);
        mTitle = title;
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.dialog_progress;
    }

    @Override
    protected void initTitle() {
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setText(mTitle);
    }

    @Override
    protected void initView() {
        mProgress = (CircleProgressBar) findViewById(R.id.progressBar);
        mProgress.setColorSchemeResources(android.R.color.holo_orange_light, android.R.color.holo_blue_light,
                android.R.color.holo_green_light, android.R.color.holo_red_light);
    }

    @Override
    protected void attachData() {
    }

    @Override
    public void setOnKeyListener(OnKeyListener onKeyListener) {
        super.setOnKeyListener(onKeyListener);
    }

    @Override
    public void onClick(View view) {
    }

    public void setProgress(long current, long total) {
        mProgress.setShowProgressText(true);
        if (current < total) {
            mProgress.setProgress((int) (current * 100 / total));
        } else {
            mProgress.setProgress(100);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK){
//            HttpUtils httpUtils = new HttpUtils();
//            HttpClient httpClient = httpUtils.getHttpClient();
//            if (httpClient != null && httpClient.getConnectionManager() != null){
//                httpClient.getConnectionManager().shutdown();
//                DialogUtils.showLongPromptToast(getContext(), "BACK键停止上传");
//            }
//        }
//        if (keyCode == KeyEvent.KEYCODE_MOVE_HOME){
//            HttpUtils httpUtils = new HttpUtils();
//            HttpClient httpClient = httpUtils.getHttpClient();
//            if (httpClient != null && httpClient.getConnectionManager() != null){
//                httpClient.getConnectionManager().shutdown();
////                DialogUtils.showLongPromptToast(mContext, "home停止上传");
//            }
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
