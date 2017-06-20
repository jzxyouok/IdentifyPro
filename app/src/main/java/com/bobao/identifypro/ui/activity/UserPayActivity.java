package com.bobao.identifypro.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.EventEnum;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.constant.UmengConstants;
import com.bobao.identifypro.domain.CashCoupon;
import com.bobao.identifypro.domain.NormalMessageResponse;
import com.bobao.identifypro.domain.UserIdentifyPayInfoResponse;
import com.bobao.identifypro.domain.UserPayData;
import com.bobao.identifypro.domain.WXPayStatusResponse;
import com.bobao.identifypro.manager.AliPayManager;
import com.bobao.identifypro.manager.WXDealManager;
import com.bobao.identifypro.task.StringToBeanTask;
import com.bobao.identifypro.ui.dialog.CommonDialog;
import com.bobao.identifypro.ui.dialog.EditInfoDialog;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.bobao.identifypro.utils.BigDecimalUtils;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.NetUtils;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UmengUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zcw.togglebutton.ToggleButton;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by you on 2015/6/5.
 */
public class UserPayActivity extends BaseActivity implements EditInfoDialog.OnConfirmListener {
    private String mIdentifyId;
    private String mUserId;
    private double mGoodsPrice;//鉴定的原价
    private double mPayPrice;//可能扣除积分的价格
    private double mUserAmount;//用户账户余额
    private double mCashCouponAmount;//现金券金额
    private String mGoodsId;
    private String mIdentifyType;
    private String mCashCouponId;

    final private String USE_BALANCE_PAY = "0";
    final private String USE_WX_PAY = "1";
    final private String USE_BFB_PAY = "2";
    final private String USE_BALANCE_METHOD = "4";
    final private String NOT_USE_BALANCE_METHOD = "2";
    private String mPayMethod = NOT_USE_BALANCE_METHOD;

    private TextView mIdentifyTypeTV;//鉴定类型
    private TextView mPriceTV;//商品价格
    private TextView mCashCouponDesView;
    private TextView mCashCouponView;
    private TextView mUserAccountTV;
    private TextView mPayPriceTV;
    private ToggleButton mBalancePayBtn;
    private RadioButton mWeChatPayBtn;
    private RadioButton mBfbPayBtn;
    private RadioButton mAliPayBtn;
    private TextView mUserPayTo;
    private View mRlAliPay;
    private View mRlBalancePay;
    private View mRlWeChatPay;
    private View mRlBfbPay;

    private ProgressDialog mGetWxPayStateProgressDialog;//获取微信支付状态进度框
    private ProgressDialog mGetChargeProgressDialog;//生成支付订单进度框
    private ProgressDialog mGetPayInfoProgressDialog;//获取支付页面数据进度框

    private boolean mIsWeixinPaySuccess;//微信是否支付成功

    private CommonDialog mCommonDialog;

    private View.OnClickListener mCancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCommonDialog.dismiss();
        }
    };
    private View.OnClickListener mSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_CancelPay_Onclick);
            Intent intent = new Intent(mContext, MainActivity.class);
            jump(intent);
            finish();
        }
    };

    @Override
    protected void getIntentData() {
        mIdentifyId = getIntent().getStringExtra(IntentConstant.USER_PAY_GOODS_ID);
    }

    @Override
    protected void initData() {
    }

    @Override
    protected int setLayoutViewId() {
        return R.layout.activity_user_identify_payinfo;
    }

    @Override
    protected void initTitle() {
        TextView backView = (TextView) findViewById(R.id.tv_back);
        TextView titleView = (TextView) findViewById(R.id.tv_title);
        titleView.setText(R.string.payment);
        setOnClickListener(backView);
    }

    @Override
    protected void initContent() {
        View couponView = findViewById(R.id.rl_coupon);
        mIdentifyTypeTV = (TextView) findViewById(R.id.user_identify_type);//鉴定类型
        mPriceTV = (TextView) findViewById(R.id.user_identify_price);//鉴定价格
        mCashCouponDesView = (TextView) findViewById(R.id.tv_cash_coupon_des);
        mCashCouponView = (TextView) findViewById(R.id.tv_cash_coupon);
        mUserAccountTV = (TextView) findViewById(R.id.use_account_available);//账户余额
        mBalancePayBtn = (ToggleButton) findViewById(R.id.use_balance_checkbox);
        mWeChatPayBtn = (RadioButton) findViewById(R.id.user_wx_pay_radioBTN);
        mBfbPayBtn = (RadioButton) findViewById(R.id.user_bfb_pay_radioBTN);
        mAliPayBtn = (RadioButton) findViewById(R.id.user_zfb_pay_radioBTN);
        mPayPriceTV = (TextView) findViewById(R.id.user_actual_pay);
        mUserPayTo = (TextView) findViewById(R.id.user_pay_to);
        mRlAliPay = findViewById(R.id.rl_AliPay);
        mRlBalancePay = findViewById(R.id.rl_balance_pay);
        mRlBfbPay = findViewById(R.id.rl_bfb_pay);
        mRlWeChatPay = findViewById(R.id.rl_wechat_pay);

        setOnClickListener(couponView, mUserPayTo, mRlAliPay, mRlBalancePay, mRlBfbPay, mRlWeChatPay);
        setSwipeBackEnable(false);
    }

    @Override
    protected void initFooter() {
    }

    @Override
    protected void attachData() {
        if (mGetPayInfoProgressDialog == null) {
            mGetPayInfoProgressDialog = DialogUtils.showProgressDialog(mContext, getString(R.string.dialog_loading));
        }
        //获取支付信息
        NetUtils.getInstance(false).get(mContext, NetConstant.getUserPayParams(mContext, mIdentifyId), new UserPayListener(mContext));
    }

    @Override
    protected void refreshData() {
    }

    private void checkedPayMethod(RadioButton radioButton, ToggleButton toggleButton, final boolean Alipay, final boolean balance, final boolean WeChat,
                                  final boolean bfb) {
        if (radioButton != null) {
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mAliPayBtn.setChecked(Alipay);
                        mWeChatPayBtn.setChecked(WeChat);
                        mBfbPayBtn.setChecked(bfb);
                        mBalancePayBtn.setToggleOff(balance);
                    }
                }
            });
        } else if (toggleButton != null) {
            toggleButton.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
                @Override
                public void onToggle(boolean on) {
                    if (on) {
                        mAliPayBtn.setChecked(Alipay);
                        mWeChatPayBtn.setChecked(WeChat);
                        mBfbPayBtn.setChecked(bfb);
                        mBalancePayBtn.setToggleOn(balance);
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkedPayMethod(mAliPayBtn, null, true, true, false, false);
        checkedPayMethod(mBfbPayBtn, null, false, true, false, true);
        checkedPayMethod(null, mBalancePayBtn, false, true, false, false);
        checkedPayMethod(mWeChatPayBtn, null, false, true, true, false);
        UmengUtils.onResume(this);
        //查询微信支付接口
        if (mWeChatPayBtn.isChecked()) {
            if (mGetWxPayStateProgressDialog == null) {
                mGetWxPayStateProgressDialog = DialogUtils.showProgressDialog(mContext, "获取支付信息");
            }
            NetUtils.getInstance(false).getNoCache(mContext, NetConstant.getQureyPaySatusParams(mContext, mGoodsId), new WXPayStatusListener(mContext));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                showPickDialog();
                break;
            case R.id.rl_coupon:
                if (!TextUtils.isEmpty(mCashCouponView.getText().toString().trim())) {
                    return;
                }
                DialogUtils.showEditDialog(mContext, this, R.string.user_cash_coupon, R.string.input_cash_coupon);
                UmengUtils.onEvent(mContext, EventEnum.User_Pay_Coupon);
                break;
            case R.id.user_pay_to:
                doPay();
                break;
            case R.id.rl_AliPay:
                mAliPayBtn.setChecked(true);
                break;
            case R.id.rl_balance_pay:
                mBalancePayBtn.setToggleOn(true);
                break;
            case R.id.rl_wechat_pay:
                mWeChatPayBtn.setChecked(true);
                break;
            case R.id.rl_bfb_pay:
                mBfbPayBtn.setChecked(true);
                break;
            default:
                break;
        }
    }

    private void doPay() {
        if (mBalancePayBtn.isToggleOn()) {
            if (mUserAmount < mPayPrice) {
                DialogUtils.showShortPromptToast(mContext, R.string.balance_not_enough);
                return;
            }
            UmengUtils.onEvent(mContext, EventEnum.BoBao_Pay);
            doBobaoPay(mGoodsId, mPayMethod);
        }
        //百付宝支付
        if (mBfbPayBtn.isChecked()) {
            UmengUtils.onEvent(mContext, EventEnum.Bfb_Pay);
            if (mPayPrice < 0.000001d && USE_BALANCE_METHOD.equals(mPayMethod)) {
                doBobaoPay(mGoodsId, mPayMethod);
                return;
            }
            doBaifubao();
        }
        //支付宝支付
        if (mAliPayBtn.isChecked()) {
            UmengUtils.onEvent(UserPayActivity.this, EventEnum.Zfb_Pay);
            if (mPayPrice < 0.000001d && USE_BALANCE_METHOD.equals(mPayMethod)) {
                doBobaoPay(mGoodsId, mPayMethod);
                return;
            }
            AliPayManager.getsInstance().doAliPay(mContext, mUserId, StringUtils.getString(mPayPrice), mGoodsId, mPayMethod, mCashCouponId);
        }
        //微信支付
        if (mWeChatPayBtn.isChecked()) {
            if (mPayPrice < 0.000001d && USE_BALANCE_METHOD.equals(mPayMethod)) {
                doBobaoPay(mGoodsId, mPayMethod);
                return;
            }
            WXDealManager.getInstance().clearWXDealInfo();
            WXDealManager.getInstance().setGoodsId(mGoodsId);
            WXDealManager.getInstance().setWXCashAction(WXDealManager.WXCashAction.PAY);
            WXDealManager.getInstance().setPayMethod(mPayMethod);
            WXDealManager.getInstance().setmCashCouponId(mCashCouponId);
            //充值统计跟踪
            Map<String, String> map = new HashMap<>();
            map.put(UmengConstants.KEY_PAY_ORDERID, WXDealManager.getInstance().getGoodsId());
            UmengUtils.onEvent(this, EventEnum.User_Recharge_WX_Start, map);
            if (mGetChargeProgressDialog == null) {
                mGetChargeProgressDialog = DialogUtils.showProgressDialog(mContext, "正在支付");
            }
            getWxParameter();
            UmengUtils.onEvent(mContext, EventEnum.WX_Pay);
        }
    }

    /**
     * 微信支付
     */
    protected void getWxParameter() {
        final Map<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_ORDERID, mGoodsId);
        UmengUtils.onEvent(mContext, EventEnum.User_WX_Request_Pramas_Start, map);
        final WebView nbView = new WebView(this);
        nbView.getSettings().getUserAgentString();

        String baseUrl = "http://jianbao.artxun.com/index.php?module=jbapp&act=api&api=pay&op=wx";
        baseUrl = StringUtils.getString(baseUrl, "&app=", getPackageName());
        baseUrl = StringUtils.getString(baseUrl, "&uid=", mUserId);
        baseUrl = StringUtils.getString(baseUrl, "&amount=", mPayPrice);
        baseUrl = StringUtils.getString(baseUrl, "&id=", mGoodsId);
        baseUrl = StringUtils.getString(baseUrl, "&jflag=", mPayMethod);
        //现金券
        baseUrl = StringUtils.getString(baseUrl, "&rid=", mCashCouponId);//现金券id
        baseUrl = StringUtils.getString(baseUrl, "&type=", "2");
        Log.e("WXPayTest", "开始请求微信支付参数" + baseUrl);
        new HttpUtils().send(HttpRequest.HttpMethod.GET, baseUrl, new WXPayRequestCallback());
    }

    //获取微信支付对象回调
    private class WXPayRequestCallback extends RequestCallBack<String> implements StringToBeanTask.ConvertListener<NormalMessageResponse> {
        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            StringToBeanTask<NormalMessageResponse> task = new StringToBeanTask<>(NormalMessageResponse.class, this);
            task.execute(responseInfo.result);
            Log.e("WXPayTest", responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            Log.e("WXPayTest", "onFailure");
            if (mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
        }

        @Override
        public void onConvertSuccess(NormalMessageResponse response) {
            Log.e("WXPayTest", response.getData());
            if (mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
            if (response.getError()) {
                DialogUtils.showShortPromptToast(mContext, response.getMessage());
                return;
            }
            doWxpayRequest(response.getData());
        }

        @Override
        public void onConvertFailed(String json) {
            Log.e("WXPayTest", "onConvertFailed");
            if (mGetChargeProgressDialog != null) {
                mGetChargeProgressDialog.dismiss();
                mGetChargeProgressDialog = null;
            }
        }
    }

    public void doWxpayRequest(String url) {
        final Map<String, String> map = new HashMap<>();
        map.put(UmengConstants.KEY_PAY_ORDERID, mGoodsId);

        HashMap<String, String> result = new HashMap<>();
        String[] arr = url.substring(9).split("&");
        for (int i = 0; i < arr.length; i++) {
            int idx = arr[i].indexOf('=');
            if (idx == -1) continue;
            result.put(arr[i].substring(0, idx), arr[i].substring(idx + 1));
        }

        IWXAPI wxApi = WXAPIFactory.createWXAPI(this, null);
        wxApi.registerApp(result.get("appid"));

        if (wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()) {
            PayReq pay = new PayReq();
            pay.appId = result.get("appid");
            pay.partnerId = result.get("mch_id");
            pay.prepayId = result.get("prepay_id");
            pay.timeStamp = result.get("timestamp");
            pay.packageValue = "prepay_id=" + result.get("prepay_id");
            pay.nonceStr = result.get("nonce_str");
            pay.sign = result.get("sign");
            wxApi.sendReq(pay);
        } else {
            Toast.makeText(mContext, R.string.request_install_wx, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 百付宝支付
     */
    protected void doBaifubao() {
        Intent intent = new Intent(this, BaifuBaoPayActivity.class);
        intent.putExtra("userid", mUserId);
        intent.putExtra("amount", mPayPrice);
        intent.putExtra("goodsid", mGoodsId);
        intent.putExtra("paymethod", mPayMethod);
        intent.putExtra("CashCouponId", mCashCouponId);
        intent.putExtra("payflg", true);
        startActivity(intent);
    }

    /**
     * 账户扣费请求
     */
    private void doBobaoPay(String goodsId, String payMethodFlg) {
        NetUtils.getInstance(false).get(mContext, NetConstant.getPayParams(mContext, goodsId, payMethodFlg, mCashCouponId), new BobaoPayListener(mContext));
    }

    @Override
    protected void onPause() {
        super.onPause();
        UmengUtils.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mWeChatPayBtn.isChecked() && mIsWeixinPaySuccess) {//如果微信支付成功,则直接返回
                return super.onKeyDown(keyCode, event);
            }
            showPickDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onConfirm(String text) {
        mCashCouponId = text;
        NetUtils.getInstance(false).get(mContext, NetConstant.getCashCouponParams(mContext, text), new CashCouponListener(mContext));
    }

    private class UserPayListener extends NetUtils.Callback<UserIdentifyPayInfoResponse> {

        public UserPayListener(Context context) {
            super(context, UserIdentifyPayInfoResponse.class);
        }

        @Override
        public void onNetSuccess(UserIdentifyPayInfoResponse response) {
            if (mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
            if (response == null) {
                DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
                return;
            }
            if (response.getError()) {
                DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            } else {
                UserIdentifyPayInfoResponse.DataEntity dataEntity = response.getData();
                UserIdentifyPayInfoResponse.DataEntity.GoodsEntity goods = dataEntity.getGoods();
                UserIdentifyPayInfoResponse.DataEntity.PaymentTypeEntity payment_types = dataEntity.getPayment_type();
                //0为老用户，1为新用户
                if (dataEntity.getIs_new_user() == 1) {
                    mWeChatPayBtn.setChecked(true);
                } else if (dataEntity.getIs_new_user() == 0) {
                    int pay_max = Math.max(Math.max(payment_types.getBAIFUBAO_WAP(), payment_types.getMALIPAY()),
                            payment_types.getWXPAY());
                    if (pay_max == payment_types.getWXPAY()) {
                        mWeChatPayBtn.setChecked(true);
                    } else if (pay_max == payment_types.getMALIPAY()) {
                        mAliPayBtn.setChecked(true);
                    } else {
                        mBfbPayBtn.setChecked(true);
                    }
                }
                List<UserIdentifyPayInfoResponse.DataEntity.PaylistEntity> paylist = dataEntity.getPaylist();
                if (goods != null) {
                    mUserId = goods.getUser_id();
                    mIdentifyId = goods.getId();
                    mIdentifyType = goods.getServe();
                    mIdentifyTypeTV.setText(goods.getServe_name().getServe());
                    mPriceTV.setText(StringUtils.getString("¥ ", goods.getCharge_price()));
                    mGoodsId = goods.getId();//支付的扣费请求需要goodsid
                    mGoodsPrice = !TextUtils.isEmpty(goods.getCharge_price()) ? Double.parseDouble(goods.getCharge_price()) : 0;

                    mPayMethod = NOT_USE_BALANCE_METHOD;
                    mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
                    mPayPriceTV.setText(StringUtils.getString("¥ ", Math.max(mPayPrice, 0)));
                    mPayMethod = NOT_USE_BALANCE_METHOD;
                    mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
                    mUserAmount = dataEntity.getMoney();
                    mUserAccountTV.setText(StringUtils.getString("¥ ", mUserAmount));
                    mPayPriceTV.setText(StringUtils.getString("¥ ", Math.max(mPayPrice, 0)));
                    mUserPayTo.setText(StringUtils.getString(getString(R.string.confirm_pay), getString(R.string.with_money, Math.max(mPayPrice, 0))));
                }
                if (paylist != null) {
                    for (int i = 0; i < paylist.size(); i++) {
                        UserIdentifyPayInfoResponse.DataEntity.PaylistEntity paylistEntity = paylist.get(i);
                        String id = paylistEntity.getId();
                        if (USE_BALANCE_PAY.equals(id)) {
                            mBalancePayBtn.setVisibility(View.VISIBLE);
                        } else if (USE_WX_PAY.equals(id)) {
                            mWeChatPayBtn.setVisibility(View.VISIBLE);
                        } else if (USE_BFB_PAY.equals(id)) {
                            mBfbPayBtn.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            if (mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_pay_info_failed);
            if (mGetPayInfoProgressDialog != null) {
                mGetPayInfoProgressDialog.dismiss();
                mGetPayInfoProgressDialog = null;
            }
        }
    }

    private class BobaoPayListener extends NetUtils.Callback<UserPayData> {

        public BobaoPayListener(Context context) {
            super(context, UserPayData.class);
        }

        @Override
        public void onConvertFailed(String json) {
            finish();
            DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
        }

        @Override
        public void onNetSuccess(UserPayData response) {
            if (response != null) {
                if (!response.getError()) {
                    DialogUtils.showShortPromptToast(mContext, R.string.pay_success);
                    Intent intent = new Intent(mContext, UserPaySuccessActivity.class);
                    jump(intent);
                    finish();
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Bobao_Success);
                } else {
                    DialogUtils.showShortPromptToast(mContext, response.getMessage());
                    Map<String, String> payFailedMap = new HashMap<>();
                    payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, response.getMessage());
                    UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
                }
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            finish();
            DialogUtils.showShortPromptToast(mContext, R.string.pay_failed);
            Map<String, String> payFailedMap = new HashMap<>();
            payFailedMap.put(UmengConstants.KEY_USER_PAY_FAIL, s);
            UmengUtils.onEvent(mContext, EventEnum.User_Pay_Failed, payFailedMap);
        }
    }

    private class CashCouponListener extends NetUtils.Callback<CashCoupon> {
        public CashCouponListener(Context context) {
            super(context, CashCoupon.class);
        }

        @Override
        public void onConvertFailed(String json) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_info_failed);
            UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
        }

        @Override
        public void onNetSuccess(CashCoupon response) {
            if (response != null) {
                if (!response.getError()) {
                    mCashCouponDesView.setVisibility(View.GONE);
                    mCashCouponAmount = !TextUtils.isEmpty(response.getData().getAmount()) ? Double.valueOf(response.getData().getAmount()) : 0;
                    mCashCouponView.setText(StringUtils.getString("¥ ", mCashCouponAmount));
                    mPayPrice = BigDecimalUtils.sub(mGoodsPrice, mCashCouponAmount);
                    mPayPriceTV.setText(StringUtils.getString("¥ ", Math.max(mPayPrice, 0)));
                    mUserPayTo.setText(StringUtils.getString(getString(R.string.confirm_pay), getString(R.string.with_money, Math.max(mPayPrice, 0))));
                    UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Success);
                } else {
                    DialogUtils.showShortPromptToast(mContext, response.getMessage());
                    UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
                }
            } else {
                DialogUtils.showShortPromptToast(mContext, R.string.get_info_failed);
                UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            DialogUtils.showShortPromptToast(mContext, R.string.get_info_failed);
            UmengUtils.onEvent(mContext, EventEnum.Use_Cash_Coupon_Failed);
        }
    }

    private class WXPayStatusListener extends NetUtils.Callback<WXPayStatusResponse> {

        public WXPayStatusListener(Context context) {
            super(context, WXPayStatusResponse.class);
        }

        @Override
        public void onNetSuccess(WXPayStatusResponse response) {
            if (mGetWxPayStateProgressDialog != null) {
                mGetWxPayStateProgressDialog.dismiss();
                mGetWxPayStateProgressDialog = null;
            }
            if (!response.getError()) {
                //如果微信没有回调，则走下面的补丁提示
                if (TextUtils.equals("1", response.getData().getCharged())) {
                    DialogUtils.showConfirmDialog(mContext, getString(R.string.dialog_pay_result_title), "订单已支付", new OnFinishPayBtnClickListener());
                    mIsWeixinPaySuccess = true;
                } else {
                    if (response.getData().getIsPay() == 1) {
                        DialogUtils.showConfirmDialog(mContext, getString(R.string.dialog_pay_result_title), "订单已支付", new OnFinishPayBtnClickListener());
                        mIsWeixinPaySuccess = true;
                    } else {
                        DialogUtils.showConfirmDialog(mContext, getString(R.string.dialog_pay_result_title), "订单未支付");
                    }
                }
            } else {
                DialogUtils.showConfirmDialog(mContext, getString(R.string.dialog_pay_result_title), response.getMessage());
            }
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mGetWxPayStateProgressDialog != null) {
                mGetWxPayStateProgressDialog.dismiss();
                mGetWxPayStateProgressDialog = null;
            }
            DialogUtils.showConfirmDialog(mContext, getString(R.string.dialog_pay_result_title), getString(R.string.dialog_pay_result_failed));
        }

        @Override
        public void onConvertFailed(String json) {
            if (mGetWxPayStateProgressDialog != null) {
                mGetWxPayStateProgressDialog.dismiss();
                mGetWxPayStateProgressDialog = null;
            }
        }
    }

    //用户订单支付完成,点击事件
    private class OnFinishPayBtnClickListener implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            finish();
        }
    }

    /**
     * 选择提示对话框
     */
    private void showPickDialog() {
        mCommonDialog = DialogUtils.showCommonDialog(mContext, getString(R.string.submit_quit), getString(R.string.user_pay_quit),
                getString(R.string.continue_modify), getString(R.string.confirm_cancel), mSubmitListener, mCancelListener);
    }
}
