package com.bobao.identifypro.ui.activity;

import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.utils.DateUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.SharedPreferencesUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

public class WebViewActivity extends BaseActivity {
    private String mUrl;
    private String mTitle;
    private WebView mWebView;
    private String mSavePath;

    @Override
    protected void getIntentData() {
        mUrl = getIntent().getStringExtra(IntentConstant.WEB_URL);
        mTitle = getIntent().getStringExtra(IntentConstant.WEB_TITLE);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activiy_webview;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        backView.setText(mTitle);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        mSavePath = StringUtils.getString(DateUtils.getTimestampStr(), ".htm");
        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
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
        if (getExternalCacheDir() == null) {
            return;
        }
        boolean isOneCome = SharedPreferencesUtils.getSharedPreferencesBoolean(this, IntentConstant.LEGALDOCUMENT_ISONECOME);
        if (!NetUtils.isNetworkConnected(this)) {
            if (!isOneCome) {
                mWebView.loadUrl("file:///android_asset/user_help.htm");
            } else {
                File file = new File(getExternalCacheDir().getAbsolutePath(), mSavePath);
                if (file.exists() && getExternalCacheDir() != null) {
                    mWebView.loadUrl(StringUtils.getString("file://", getExternalCacheDir().getAbsolutePath(), mSavePath));
                    SharedPreferencesUtils.setSharedPreferencesBoolean(this, IntentConstant.LEGALDOCUMENT_ISONECOME, true);
                }
            }
        } else {
            new HttpUtils().download(mUrl, StringUtils.getString(getExternalCacheDir().getAbsolutePath(), "/", mSavePath), true, true, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {

                }

                @Override
                public void onFailure(HttpException e, String s) {
                    File file = new File(getExternalCacheDir().getAbsolutePath(), mSavePath);
                    if (file.exists()) {
                        file.delete();
                    }
                }
            });
            mWebView.loadUrl(mUrl);
        }
    }

    @Override
    protected void refreshData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
