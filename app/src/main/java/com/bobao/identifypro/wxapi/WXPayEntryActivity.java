package com.bobao.identifypro.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.UserPayData;
import com.bobao.identifypro.manager.WXDealManager;
import com.bobao.identifypro.ui.activity.UserPaySuccessActivity;
import com.bobao.identifypro.ui.activity.UserRechargeRecordActivity;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, "wx88acfde81a58d106");
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq request) {
        // ...
    }

    @Override
    public void onResp(BaseResp response) {
        if (response.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (response.errCode == -1) {
                finish();
            } else if (response.errCode == -2) {
                finish();
            } else {
                String action = WXDealManager.getInstance().getWXCashAction().name();
                if (WXDealManager.getInstance().getWXCashAction() == WXDealManager.WXCashAction.PAY) {
                    startUserPayRequest(WXDealManager.getInstance().getGoodsId(), WXDealManager.getInstance().getPayMethod(),
                            WXDealManager.getInstance().getmCashCouponId());
                    Intent intent = new Intent(WXPayEntryActivity.this, UserPaySuccessActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    //充值成功后 跳转充值记录
                    String userId = WXDealManager.getInstance().getmUserId();
                    Intent intent = new Intent(WXPayEntryActivity.this, UserRechargeRecordActivity.class);
                    intent.putExtra(IntentConstant.USER_ID, userId);
                    startActivity(intent);
                    new HttpUtils().send(
                            HttpRequest.HttpMethod.GET,
                            NetConstant.HOST,
                            NetConstant.getRechargeScoreParams(WXPayEntryActivity.this,
                                    WXDealManager.getInstance().getRechargeAmount()), null);
                    finish();
                }
            }
        }

    }

    private void startUserPayRequest(String goodsId, String payMethodFlg, String cashCouponId) {
        NetUtils.getInstance(false).get(WXPayEntryActivity.this, NetConstant.getPayParams(WXPayEntryActivity.this, goodsId, payMethodFlg, cashCouponId),
                new WXPayEntryListener(WXPayEntryActivity.this));
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

    private class WXPayEntryListener extends NetUtils.Callback<UserPayData> {

        public WXPayEntryListener(Context context) {
            super(context, UserPayData.class);
        }

        @Override
        public void onConvertFailed(String json) {
            Toast.makeText(WXPayEntryActivity.this, "失败", Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onNetSuccess(UserPayData response) {
            if (response != null) {
                if (!response.getError()) {
                    finish();
                }
            }
            Toast.makeText(WXPayEntryActivity.this, response.getData(), Toast.LENGTH_SHORT).show();
            finish();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Toast.makeText(WXPayEntryActivity.this, "失败", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
