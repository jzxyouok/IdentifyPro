package com.bobao.identifypro.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.UserPayData;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.exception.HttpException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by you on 2015/6/12.
 */
public class BaifuBaoPayActivity extends BaseActivity {
    private WebView mWebView;
    private String mUserId;
    private double mPrice;
    private String mGoodsId;
    private String mPayMethod;
    private String mCashCouponId;
    private boolean mPayFlg;

    @Override
    protected void getIntentData() {
        Intent intent = getIntent();
        mUserId = intent.getStringExtra("userid");
        mPrice = intent.getDoubleExtra("amount", 0);
        mGoodsId = intent.getStringExtra("goodsid");
        mPayMethod = intent.getStringExtra("paymethod");
        mCashCouponId = intent.getStringExtra("CashCouponId");
        mPayFlg = intent.getBooleanExtra("payflg", false);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_baifubaopay_layout;
    }

    @Override
    protected void initTitle() {

    }

    @SuppressLint("JavascriptInterface")
    @Override
    protected void initContent() {
        mWebView = (WebView) findViewById(R.id.wb_baifubao_pay);
        Log.e("UserRecharge", "加载充值页面");
        String BaseUrl = "http://user.artxun.com/mobile/finance/gateway.jsp?gateway=baifubao&version=2";
        BaseUrl = StringUtils.getString(BaseUrl, "&app=", getPackageName());
        BaseUrl = StringUtils.getString(BaseUrl, "&uid=", mUserId);
        BaseUrl = StringUtils.getString(BaseUrl, "&amount=", mPrice);
        Log.e("UserPay", "BaseUrl == " + BaseUrl);

        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(this, "history");

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("success") && url.startsWith("http://user.artxun.com/mobile/finance/")) {
                    Log.e("UserPay", "充值成功");
                    UmengUtils.onEvent(mContext, EventEnum.User_Bfb_Recharge_Success);
                    if (mPayFlg) {
                        Log.e("UserPay", "开始扣费请求");
                        startUserPayRequest(mGoodsId, mPayMethod, mCashCouponId);
                    } else {
                        //充值成功，跳转充值记录页面
                        Intent intent = new Intent(BaifuBaoPayActivity.this, UserRechargeRecordActivity.class);
                        intent.putExtra(IntentConstant.USER_ID, mUserId);
                        startActivity(intent);
                        //充值送积分
                        NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getRechargeScoreParams(mContext, StringUtils.getString(mPrice)), null);
                        finish();
                    }
                    return false;
                } else {
                    Log.e("UserPay", "充值失败 url=" + url);
                    UmengUtils.onEvent(mContext, EventEnum.User_Bfb_ReCharge_Failed);
                    view.loadUrl(url);
                    return true;
                }
            }
        });
        mWebView.loadUrl(BaseUrl);
//        setContentView(mWebView);
    }

    private void startUserPayRequest(String goodsId, String payMethodFlg, String cashCouponId) {
        //TODO 扣费请求
        NetUtils.getInstance(false).get(mContext, NetConstant.getPayParams(mContext, goodsId, payMethodFlg, cashCouponId), new UserPayListener(mContext));
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

    @Override
    protected void onResume() {
        super.onResume();
        UmengUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    private class UserPayListener extends NetUtils.Callback<UserPayData> {

        public UserPayListener(Context context) {
            super(context, UserPayData.class);
        }

        @Override
        public void onNetSuccess(UserPayData response) {
            if (response != null) {
                if (!response.getError()) {
                    Log.e("UserPay", "扣费成功！");
                    Intent intent = new Intent(mContext, UserPaySuccessActivity.class);
                    startActivity(intent);
                    finish();
                    Map<String, String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_SUCCESS, response.getData().toString());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Success, payFailedMap);
                } else {
                    Log.e("UserPay", response.getData() + "");
                }
            } else {
                Log.e("UserPay", "data == null");
            }
            DialogUtils.showShortPromptToast(mContext, response.getData());
            finish();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(BaifuBaoPayActivity.this, "失败", Toast.LENGTH_SHORT).show();
            finish();
            Log.e("UserPay", "扣费失败");
            Map<String, String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.failed);
            finish();
            Log.e("UserPay", "扣费失败");
        }
    }
}
