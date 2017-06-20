package com.bobao.identifypro.manager;

/**
 * Created by chenming on 2015/6/8.
 * 微信
 */
public class WXDealManager {
    public enum WXCashAction{
        Recharge,
        PAY
    }

    private WXCashAction  mWXCashAction = WXCashAction.Recharge;
    private String mGoodsId;
    private String mPayMethod;
    private String mUserId;
    private String mRechargeAmount;//充值金额
    private String mCashCouponId;

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    private static WXDealManager sInstance = new WXDealManager();

    public static WXDealManager getInstance(){
        return sInstance;
    }

    public WXCashAction getWXCashAction() {
        return mWXCashAction;
    }

    public void setWXCashAction(WXCashAction mWXCashAction) {
        this.mWXCashAction = mWXCashAction;
    }

    public String getGoodsId() {
        return mGoodsId;
    }

    public void setGoodsId(String mGoodsId) {
        this.mGoodsId = mGoodsId;
    }

    public String getPayMethod() {
        return mPayMethod;
    }

    public void setPayMethod(String mPayMethod) {
        this.mPayMethod = mPayMethod;
    }

    public String getRechargeAmount() {
        return mRechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.mRechargeAmount = rechargeAmount;
    }

    public String getmCashCouponId() {
        return mCashCouponId;
    }

    public void setmCashCouponId(String mCashCouponId) {
        this.mCashCouponId = mCashCouponId;
    }

    public void clearWXDealInfo(){
        mGoodsId = "";
        mWXCashAction = WXCashAction.Recharge;
        mUserId = "";
        mRechargeAmount = "";
    }

}
