package com.bobao.identifypro.constant;

/**
 * Created by you on 2015/6/1.
 */
public class NetWorkRequestConstants {
    public final static String KEY_BASE_REQUEST_PARAM_MODULE = "module";
    public final static String KEY_USERINFO_COMMIT_ACTION = "act";
    public final static String KEY_USERINFO_COMMIT_API = "api";
    public final static String KEY_USERINFO_COMMIT_OPERATION = "op";

    public final static String KEY_USERINFO_COMMIT_MOBILE = "mobile";
    public final static String KEY_USERINFO_COMMIT_NICKNAME = "name";
    public final static String KEY_USERINFO_COMMIT_MAIL = "mail";

    /**
     * 发送FeedBack请求
     * type ： 0 android 1ios?
     * model：
     * 手机型号 如：MI 3
     * ?android_version ：安卓版本 如：4.2.1
     * ?app_version ： app版本 如：xiaobao1.3.9?
     * package ：包名 如： com.bobaoo.xiaobao
     * ?content ： 反馈内容
     */
    public final static String KEY_USER_FEEDBACK_TYPE = "type";
    public final static String KEY_USER_FEEDBACK_MODEL = "model";
    public final static String KEY_USER_FEEDBACK_OS_VERSION = "android_version";
    public final static String KEY_USER_FEEDBACK_APP_VERSION = "app_version";
    public final static String KEY_USER_FEEDBACK_APP_PCKNAME = "package";
    public final static String KEY_USER_FEEDBACK_APP_CONTENT = "content";

    public final static String KEY_USER_FEEDBACK_APP_EXPERT_ID = "id";

    public final static String VALUE_BASE_REQUEST_PARAM_MODULE = "jbapp";
    public final static String VALUE_BASE_REQUEST_PARAM_ACTION = "jb";

    public final static String VALUE_BASE_REQUEST_PARAM_API_USER = "user";
    public final static String VALUE_BASE_REQUEST_PARAM_API_COMMENT = "comment";

    public final static String VALUE_BASE_REQUEST_PARAM_API_EXPERT = "expert";
    public final static String VALUE_BASE_REQUEST_PARAM_API_GOODS = "goods";

    public final static String VALUE_BASE_REQUEST_PARAM_OPERATION = "edit";
    public final static String VALUE_BASE_REQUEST_PARAM_COLLECTION = "collect";
    public final static String VALUE_BASE_REQUEST_PARAM_FANS = "fans";
    public final static String VALUE_BASE_REQUEST_PARAM_FORGET_PSWD = "ForgotPassword";
    public final static String VALUE_BASE_REQUEST_PARAM_COMMENTS = "comment";
    public final static String VALUE_BASE_REQUEST_PARAM_COMMENTS_GET = "get";
    public final static String VALUE_BASE_REQUEST_PARAM_DELETE = "delete";
    public final static String VALUE_BASE_REQUEST_PARAM_FEEDBACK = "feedback";
    public final static String VALUE_BASE_REQUEST_PARAM_GOODS = "goods";
    public final static String VALUE_BASE_REQUEST_PARAM_DETAIL = "detail";
    public final static String VALUE_BASE_REQUEST_PARAM_PAYINFO = "sun";

    public final static String KEY_IDENTIFY_PAY_TYPE = "type";
    public final static String VALUE_IDENTIFY_PAY_TYPE = "pay";

    public final static String VALUE_CHANGE_PSWD_OPERATION = "ChangePassword";
    public final static String KEY_OLD_PASSWORD = "oldpwd";
    public final static String KEY_NEW_PASSWORD = "newpwd";

    public final static String VALUE_BASE_REQUEST_PARAM_API_MSG = "message";
    public final static String VALUE_BASE_REQUEST_PARAM_ASKLIST = "asklist";

    public final static String VALUE_BASE_REQUEST_PARAM_ASK_CONTENT = "askspeak";
    public final static String VALUE_BASE_REQUEST_PARAM_ASK_GOODS = "askgoods";
    public final static String KEY_PRICE_QUERY_FROM = "from";
    public final static String KEY_PRICE_QUERY_TO  = "to";
    public final static String KEY_PRICE_QUERY_GOODSID = "gid";

    public final static String VALUE_CHANGE_PASSWORD_AUTH_CODE = "code";
    public final static String KEY_PASSWORD = "password";

    public final static String GET_SCORE_METHOD_SHARE = "share";
    public final static String GET_SCORE_METHOD_COMMENT = "comment";
}
