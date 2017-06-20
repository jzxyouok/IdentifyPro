package com.bobao.identifypro.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.PayResult;
import com.bobao.identifypro.domain.UserPayData;
import com.bobao.identifypro.ui.activity.UserPaySuccessActivity;
import com.bobao.identifypro.ui.activity.UserRechargeRecordActivity;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.SignUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by you on 2015/7/16.
 */
public class AliPayManager {
    private static AliPayManager sInstance = new AliPayManager();
    private Context mContext;
    private String mUserId;
    private String mRechargeValue;
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAOJrwakZlpyHUUSVe1ROV+TrOG4/9IlClRj/lcDKkk7GkiLouFi0DmTRFc2T9gOISS8yU0xnkBBrLpcegxooyhg0yh8Q1Es7xZKJQVzX448dM6s2niDUBHt+cnFvHQ/CWM9jaFvZCxepqByWcQZKTxTSQWHILbB4BYVu61SJBlmRAgMBAAECgYAv/5HlTBReiF0VAe9MFvORBsBGtu4a7u92hi/z172eT4AJQHZb74ehnaVWmEgtxYVmKO/5oXar1FzjEkfOktkMVF8u8czUoqFWKWA8ipw1ZL5UQ6i2GZh3VA6HMSG2OW2bX9gT4JYhaCS7D+2ZbPdWbxKll5SEvJykjV2//frRsQJBAP7GCGocq9cPjJDwZfMXorHwRElxJyAbuaU0xL1Gdsh0aeDLcw0vNYkOCLBzzggVDlS4HS6fqTtXnjYq74wL5O0CQQDjgsiYwQyLNGDDS7bh+18uLzTX5Lq3FilgsnUbqegY3sxqBgm2WuESbefPWWIAuYLhY17oo7XhpSp/MM7osba1AkEA7tu1Wd7FkNyIGf74Zh+brh2nt/85AlZcB7JgXV/pz2etOE8l7496LqOUq/H2kQdEp1LyMRsJa5RqxTgd2/vuxQJBAMwgfrzXkNqhPyRanZb6g/abMk12krRMtQlmdL5CXtVZqYyDKFmuBn0TkUYwC0ddKvnwv6n5oOn42D1QXITZVnUCQQC+CNrdF9kKqnteTSyK5pSK9TPfaFWvreS5ZRPHgRIt0Lcb78dIjXzp5TB+H7Xp9dja/at29BpcvzaZClBCgU4i";
    private static final int SDK_PAY_FLAG = 1;

    //9000 订单支付成功
    //8000处理中
    //4000订单支付失败
    //6001用户中途取消
    //6002网络链接出错
    private static final String ALIPAY_SUCCESS_FLG = "9000";
    private static final String ALIPAY_WAITING_FLG = "8000";
    private static final String ALIPAY_FAILED_FLG = "4000";
    private static final String ALIPAY_CANCLE_FLG = "6001";
    private static final String ALIPAY_NETWORD_ERRO_FLG = "6002";

    private static final String SIGN_TYPE = "sign_type=\"RSA\"";

    //充值/支付
    private boolean mIsRechargeFlg;
    private String mGoodsId;
    private String mPayMethod;
    private String mCashCouponId;

    private ProgressDialog mProgressDialog;
    private ProgressDialog mGetChargeProgressDialog;//生成charge对象的进度框

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, ALIPAY_SUCCESS_FLG)) {
                        //充值送积分
                        new HttpUtils().send(
                                HttpRequest.HttpMethod.GET,
                                NetConstant.HOST,
                                NetConstant.getRechargeScoreParams(mContext, mRechargeValue), null);
                        if (mIsRechargeFlg) {
                            //充值成功，跳转充值记录页面
                            Intent intent = new Intent(mContext, UserRechargeRecordActivity.class);
                            intent.putExtra(IntentConstant.USER_ID, mUserId);
                            ActivityUtils.jump(mContext, intent);
                        } else {
                            //开始扣费请求
                            Map<String, String>  map = new HashMap<>();
                            map.put(UmengConstants.KEY_PAY_ORDERID, mGoodsId);
                            UmengUtils.onEvent(mContext, EventEnum.User_Consume_Ali_Start, map);
                            //扣费请求
                            mProgressDialog = DialogUtils.showProgressDialog(mContext, mContext.getString(R.string.ali_paying));
                            NetUtils.getInstance(false).get(mContext, NetConstant.getPayParams(mContext, mGoodsId, mPayMethod, mCashCouponId), new AliPayListener(mContext));
                        }
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, ALIPAY_WAITING_FLG)) {
                            DialogUtils.showShortPromptToast(mContext, R.string.pay_waiting);
                        } else if (TextUtils.equals(resultStatus, ALIPAY_CANCLE_FLG)) {
                            DialogUtils.showShortPromptToast(mContext, R.string.pay_cancel);
                        } else if (TextUtils.equals(resultStatus, ALIPAY_NETWORD_ERRO_FLG)) {
                            DialogUtils.showShortPromptToast(mContext, R.string.cannot_connect_network);
                        } else if (TextUtils.equals(resultStatus, ALIPAY_FAILED_FLG)) {
                            DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    private AliPayManager() {
    }

    public static AliPayManager getsInstance() {
        return sInstance;
    }

    public void doAliPayRecharge(Context context, String userId, String rechargePay) {
        mIsRechargeFlg = true;
        mContext = context;
        mUserId = userId;
        mRechargeValue = rechargePay;
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.ALIPAY_HOST, NetConstant.getAliRechargeInfoParams(mContext, userId, StringUtils.getString(rechargePay)),
                new AliPayInfoListener());
    }

    /**
     * 支付宝支付
     */
    public void doAliPay(Context context, String userId, String price, String goodsId, String payMethodFlg, String cashCouponId) {
        mIsRechargeFlg = false;
        mContext = context;
        mUserId = userId;
        mRechargeValue = price;
        mGoodsId = goodsId;
        mPayMethod = payMethodFlg;
        mCashCouponId = cashCouponId;
        //先做充值请求
        Map<String, String>  map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_ORDERID, mGoodsId);
        UmengUtils.onEvent(mContext, EventEnum.User_Recharge_Ali_Start, map);
        if(mGetChargeProgressDialog == null) {
            mGetChargeProgressDialog = DialogUtils.showProgressDialog(mContext, "正在支付");
        }
        //先做充值请求
        new HttpUtils().send(HttpRequest.HttpMethod.GET, NetConstant.ALIPAY_HOST, NetConstant.getAliRechargeInfoParams(mContext, userId, StringUtils.getString(price)),
                new AliPayInfoListener());
    }

    /**
     * 支付宝充值请求回调
     */
    private class AliPayInfoListener extends RequestCallBack<String> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            if(mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
            String s = responseInfo.result.trim();
            UmengUtils.onEvent(mContext, EventEnum.User_Zfb_Recharge_Success);
            pay(s);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if(mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
            UmengUtils.onEvent(mContext, EventEnum.User_Zfb_Recharge_Failed);
        }
    }

    private class AliPayListener extends NetUtils.Callback<UserPayData> {

        public AliPayListener(Context context) {
            super(context, UserPayData.class);
        }

        @Override
        public void onNetSuccess(UserPayData response) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            if (response != null) {
                if (!response.getError()) {
                    //跳转到支付成页面
                    Intent intent = new Intent(mContext, UserPaySuccessActivity.class);
                    mContext.startActivity(intent);
                    Map<String, String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_SUCCESS, response.getData().toString());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Success, payFailedMap);
                }
            }
            DialogUtils.showShortPromptToast(mContext, response.getData());
            ((Activity) mContext).finish();
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mProgressDialog != null) {
                mProgressDialog.dismiss();
            }
            Map<String, String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }
    }

    /**
     * (支付宝)
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay(String orderInfo) {
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = StringUtils.getString(orderInfo, "&sign=\"", sign, "\"&", SIGN_TYPE);
        //alert(payInfo);
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) mContext);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * (支付宝)
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }
}
