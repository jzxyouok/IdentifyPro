package com.bobao.identifypro.constant;

import android.content.Context;
import android.util.Log;

import com.bobao.identifypro.utils.AppUtils;
import com.bobao.identifypro.utils.DeviceUtil;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.lidroid.xutils.http.RequestParams;

import java.io.File;

/**
 * Created by star on 15/6/4.
 */
public class NetConstant {
    public static final String LOGIN = "http://user.artxun.com/mobile/user/login.jsp";
    public static final String HOST = "http://jianbao.artxun.com/index.php";
    public static final String ALIPAY_HOST = "http://user.artxun.com/mobile/finance/malipay/charge.jsp";
    public static final String UPDATE_HOST = "http://artist.app.artxun.com/recomment/op_version.jsp?app=xiaobao";
    public static final String SCORE_RULES_HOST = "http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=h5&op=rules";
    public static final String IDENTIFY_TIP = "http://jianbao.artxun.com/index.php?module=jbapp&act=jb&api=h5&op=notice";
    public static final String IDENTIFY_FAQ = "http://jianbao.artxun.com/index.php?module=jbapp&act=pro&api=h5&op=help";

    private static final String COOKIE_KEY = "jucu=";

    /**
     * 获得带基本版本信息和设备信息的请求参数
     */
    private static RequestParams getBaseParamsWithHeader(Context context) {
        RequestParams params = new RequestParams();
        params.addHeader("app-version-code", String.valueOf(AppUtils.getVersionCode(context)));
        params.addHeader("device-model", DeviceUtil.getPhoneModel());
        return params;
    }

    /**
     * 获取基本的参数
     */
    private static RequestParams getBaseParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "pro");
        return params;
    }

    /**
     * 获取banner列表的参数
     */
    public static RequestParams getBannerListParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "index");
        params.addQueryStringParameter("op", "index");
        params.addQueryStringParameter("type", "1");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取首页服务参数
     */
    public static RequestParams getHomeServiceParams(Context context, String type) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "index");
        params.addQueryStringParameter("op", "index");
        params.addQueryStringParameter("type", type);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取服务说明参数
     */
    public static RequestParams getServiceNoteParams(Context context, String sid) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "serve");
        params.addQueryStringParameter("op", "explain");
        params.addQueryStringParameter("sid", sid);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取服务说明参数
     */
    public static RequestParams getServiceAppointmentParams(Context context, String sid, String kind, double lat, double lon,int meet) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "serve");
        params.addQueryStringParameter("op", "meet");
        params.addQueryStringParameter("sid", sid);
        params.addQueryStringParameter("kind", kind);
        params.addQueryStringParameter("lat", String.valueOf(lat));
        params.addQueryStringParameter("lon", String.valueOf(lon));
        params.addQueryStringParameter("meet", String.valueOf(meet));
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取修改预约鉴定参数
     */
    public static RequestParams getEditUserServiceAppointmentParams(Context context, String id, String st) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "view");
        params.addQueryStringParameter("id", id);
        params.addQueryStringParameter("st", st);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取未支付订单数
     */
    public static RequestParams getUnPayCountParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "nocharg");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取订单Id的参数
     */
    public static RequestParams getOrderIdParams(Context context, String tid, String sid, String kid, String size, String id, String note, double lat, double lon) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "serve");
        params.addQueryStringParameter("op", "addgoods");
        params.addBodyParameter("tid", tid);
        params.addBodyParameter("sid", sid);
        params.addBodyParameter("kid", kid);
        params.addBodyParameter("size", size);
        params.addBodyParameter("id", id);
        params.addBodyParameter("note", note);
        params.addBodyParameter("lat", String.valueOf(lat));
        params.addBodyParameter("lon", String.valueOf(lon));
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户支付参数
     */
    public static RequestParams getUserPayParams(Context context, String mIdentifyId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "sun");
        params.addQueryStringParameter("id", mIdentifyId);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 查询微信支付接口
     */
    public static RequestParams getQureyPaySatusParams(Context context, String goodsId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "goods");
        params.addQueryStringParameter("op", "wxorder");
        params.addQueryStringParameter("id", goodsId);
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 充值获取积分
     */
    public static RequestParams getRechargeScoreParams(Context context, String amount) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("api", "integral");
        params.addQueryStringParameter("op", "send");
        params.addQueryStringParameter("amount", amount);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取支付参数
     */
    public static RequestParams getPayParams(Context context, String goodsId, String payMethodFlg, String coupon) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "charge");
        params.addQueryStringParameter("op", "index");
        params.addQueryStringParameter("id", goodsId);
        params.addQueryStringParameter("jflag", payMethodFlg);
        params.addQueryStringParameter("rid", coupon);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取支付宝充值订单数据
     */
    public static RequestParams getAliRechargeInfoParams(Context context, String userId, String amount) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("app", context.getPackageName());
        params.addQueryStringParameter("uid", userId);
        params.addQueryStringParameter("amount", amount);
        params.addQueryStringParameter("gateway", "malipay");
        return params;
    }

    /**
     * 获取用户充值记录参数
     */
    public static RequestParams getUserRechargeRecordParams(Context context) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "logs");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取现金券参数
     */
    public static RequestParams getCashCouponParams(Context context, String number) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "charge");
        params.addQueryStringParameter("op", "roll");
        params.addQueryStringParameter("rid", number);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取专家详情数据
     */
    public static RequestParams getExpertDetailParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("op", "detail");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取专家列表的参数
     */
    private static RequestParams getExpertListParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("op", "list");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", String.valueOf(page));
        return params;
    }

    /**
     * 获取机构专家列表的参数
     */
    public static RequestParams getOrganizationExpertListParams(Context context, int page, int identifyType) {
        RequestParams params = getExpertListParams(context, page);
        params.addQueryStringParameter("org", "1,2,3,4,5,6,7,8");
        params.addQueryStringParameter("kind", String.valueOf(identifyType));
        Log.e("ExpertPage", page + "");
        return params;
    }

    /**
     * 获取机构专家列表的参数
     */
    public static RequestParams getOrganizationExpertListParams(Context context, int page, int organization, int identifyType) {
        RequestParams params = getExpertListParams(context, page);
        params.addQueryStringParameter("org", String.valueOf(organization));
        params.addQueryStringParameter("kind", String.valueOf(identifyType));
        return params;
    }

    /**
     * 获取关注专家列表的参数
     */
    public static RequestParams getAttentionExpertListParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "fans");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取专家服务和价格的参数
     */
    public static RequestParams getExpertServiceInfoParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("op", "serve");
        params.addQueryStringParameter("id", id);
        return params;
    }

    /**
     * 评论,分享获取积分请求
     */
    public static RequestParams getScoreParams(Context context, String operation, String type, String id) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "api");
        params.addQueryStringParameter("api", "integral");
        params.addQueryStringParameter("op", operation);
        params.addQueryStringParameter("type", type);
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 关注请求
     */
    public static RequestParams getExpertAttentionParams(Context context, String expertId) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "expert");
        params.addQueryStringParameter("op", "fans");
        params.addQueryStringParameter("id", expertId);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取登陆数据
     */
    public static RequestParams getLoginParams(Context context, String name, String password) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "center");
        params.addQueryStringParameter("op", "bobao");
        params.addQueryStringParameter("username", name);
        params.addQueryStringParameter("app", context.getPackageName());
        params.addQueryStringParameter("password", password);
        return params;
    }

    /**
     * 获取验证码的参数
     */
    public static RequestParams getAuthCodeParams(Context context, String tel) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "center");
        params.addQueryStringParameter("op", "register");
        params.addQueryStringParameter("mobile", tel);
        return params;
    }

    /**
     * 注册的参数
     */
    public static RequestParams getRegisterParams(Context context, String tel, String authCode, String password) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "center");
        params.addQueryStringParameter("op", "register");
        params.addQueryStringParameter("mobile", tel);
        params.addQueryStringParameter("authcode", authCode);
        params.addQueryStringParameter("password", password);
        params.addQueryStringParameter("app", context.getPackageName());
        return params;
    }

    /**
     * 获取忘记密码验证码参数
     */
    public static RequestParams getAuthCodeRequestParams(Context context, String tel) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "ForgotPassword");
        params.addQueryStringParameter("mobile", tel);
        return params;
    }

    /**
     * 获取忘记密码参数
     */
    public static RequestParams getChangePasswdRequestParams(Context context, String tel, String authCode, String newPswd) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "ForgotPassword");
        params.addQueryStringParameter("mobile", tel);
        params.addQueryStringParameter("code", authCode);
        params.addQueryStringParameter("password", newPswd);
        return params;
    }

    /**
     * 获得用户个人信息参数
     */
    public static RequestParams getUserPrivateInfoParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "edit");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取修改用户头像参数
     */
    public static RequestParams getUpLoadHeadImgParams(Context context, File headImg) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "headimg");
        params.addBodyParameter("headimg", headImg);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        params.addHeader("Content-Type", "multipart/form-data;charset=UTF-8");
        return params;
    }

    /**
     * 获取修改昵称参数
     */
    public static RequestParams getCommitEditNicknameParams(Context context, String nickname) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "editnike");
        params.addQueryStringParameter("nikename", nickname);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户信息参数
     */
    public static RequestParams getUserInfoParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "datainfo");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取认证手机号验证码的参数
     */
    public static RequestParams getCheckPhoneAuthCodeParams(Context context, String tel) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "checkmobile");
        params.addQueryStringParameter("mobile", tel);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取认证手机的参数
     */
    public static RequestParams getCheckPhoneSubmitParams(Context context, String tel, String authCode) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "checkmobile");
        params.addQueryStringParameter("mobile", tel);
        params.addQueryStringParameter("code", authCode);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取修改密码参数
     */
    public static RequestParams getEditPasswordParams(Context context, String oldPwd, String newPwd) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "ChangePassword");
        params.addQueryStringParameter("oldpwd", oldPwd);
        params.addQueryStringParameter("newpwd", newPwd);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获得用户发消息参数
     */
    public static RequestParams getSendFeedBackParams(Context context, String content) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "feedback");
        params.addQueryStringParameter("type", "0");
        params.addQueryStringParameter("model", DeviceUtil.getPhoneModel());
        params.addQueryStringParameter("android_version", DeviceUtil.getAndroidOSVersion());
        params.addQueryStringParameter("app_version", StringUtils.getString("identifypro", AppUtils.getAppVersionName(context)));
        params.addQueryStringParameter("package", context.getPackageName());
        params.addQueryStringParameter("content", content);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取发送用户消息参数
     */
    public static RequestParams getSendFeedBackParams(Context context, String content, String mTo, String mGoodsId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "message");
        params.addQueryStringParameter("op", "askgoods");
        params.addQueryStringParameter("to", mTo);
        params.addQueryStringParameter("content", content);
        params.addQueryStringParameter("gid", mGoodsId);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户反馈参数
     */
    public static RequestParams getUserFeedBackParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "feedback_get");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取用户预约专家参数
     */
    public static RequestParams getUserAppointmentExpertsParams(Context context, String mType) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "list");
        params.addQueryStringParameter("sp", String.valueOf(10));
        params.addQueryStringParameter("st", mType);
        params.addQueryStringParameter("page", "1");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取删除未支付预约参数
     */
    public static RequestParams getDeleteNoPayAppointmentTreasureParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "del");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取删除已完成预约参数
     */
    public static RequestParams getDeleteFinishAppointmentTreasureParams(Context context, String id) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "overdel");
        params.addQueryStringParameter("id", id);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 对专家进行评价
     */
    public static RequestParams getEvaluateParams(Context context, String id, String content, int score) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "pay");
        params.addQueryStringParameter("op", "comment");
        params.addBodyParameter("id", id);
        params.addBodyParameter("context", content);
        params.addBodyParameter("stars", String.valueOf(score));
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取询价参数
     */
    public static RequestParams getPriceQueryContentParams(Context context, String mFrom, String mTo, String mGoodsId) {
        RequestParams params = getBaseParamsWithHeader(context);
        params.addQueryStringParameter("module", "jbapp");
        params.addQueryStringParameter("act", "jb");
        params.addQueryStringParameter("api", "message");
        params.addQueryStringParameter("op", "askspeak");
        params.addQueryStringParameter("from", mFrom);
        params.addQueryStringParameter("to", mTo);
        params.addQueryStringParameter("gid", mGoodsId);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取消费明细数据
     */
    public static RequestParams getResumeDetailParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "logs");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取我的积分数据
     */
    public static RequestParams getMyIntegrateParams(Context context) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "myintegral");
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取我的积分数据
     */
    public static RequestParams getIntegrateDetailParams(Context context, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "integral");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", String.valueOf(page));
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 提交邀请码
     */
    public static RequestParams getInviteCodeParams(Context context, String code) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "user");
        params.addQueryStringParameter("op", "invite");
        params.addQueryStringParameter("code", code);
        params.addHeader("Cookie", StringUtils.getString(COOKIE_KEY, UserInfoUtils.getUserToken(context)));
        return params;
    }

    /**
     * 获取我的询价数据
     */
    public static RequestParams getUserPriceQueryParams(Context context, int type, int page) {
        RequestParams params = getBaseParams(context);
        params.addQueryStringParameter("api", "message");
        params.addQueryStringParameter("op", "asklist");
        params.addQueryStringParameter("sp", AppConstant.DefaultPageItemNum);
        params.addQueryStringParameter("page", StringUtils.getString(page));
        params.addQueryStringParameter("type", StringUtils.getString(type));
        params.addHeader("Cookie", StringUtils.getString("jucu=", UserInfoUtils.getUserToken(context)));
        return params;
    }
}
