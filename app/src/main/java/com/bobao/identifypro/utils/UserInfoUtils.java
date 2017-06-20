package com.bobao.identifypro.utils;

/**
 * Created by chenming on 2015/5/22.
 */

import android.content.Context;
import android.text.TextUtils;

import com.bobao.identifypro.constant.AppConstant;
import com.bobao.identifypro.domain.LoginStepResponse;
import com.bobao.identifypro.domain.RegisterResponse;
import com.bobao.identifypro.domain.UserLoginInfo;

public class UserInfoUtils {
    //鉴宝登陆或者注册成功后的信息
    private static final String SP_KEY_USER_ID = "user_id";
    private static final String SP_KEY_USER_USERNAME = "username";
    private static final String SP_KEY_USER_NICKNAME = "nickname";
    private static final String SP_KEY_USER_TOKEN = "token";
    private static final String SP_KEY_USER_HEAD_URL = "head";
    private static final String SP_KEY_USER_PHONE = "phone";
    private static final String SP_KEY_SOCIAL_LOGIN_FLG = "is_social_flg";
    private static final String SP_KEY_USER_CACHE_HEAD_URL = "head_cache";//用于缓存重置头像后的新URL

    private static final String SP_KEY_APP_CURRENT_VERSION = "app_current_version";//保存当前APP版本号

    private static final String HAVE_CLICK_RATE_BAD = "have_click_rate_bad";//保存用户对app好的评价
    private static final String HAVE_CLICK_RATE_GOOD = "have_click_rate_good";//保存用户对app好的评价

    private static final String SP_KEY_USER_LATITUDE = "sp_key_user_latitude";//保存用户定位的经度
    private static final String SP_KEY_USER_LONGITUDE = "sp_key_user_longitude";//保存用户定位的纬度

    private static final String SP_KEY_USER_HOME_BANNER_DEFAULT_IMG = "sp_key_user_home_banner_default_img";//保存用户首页banner默认图

    public static boolean checkUserLogin(Context context) {
        String token = getUserToken(context);
        return !TextUtils.isEmpty(token);
    }

    public static void saveUserLoginInfo(Context context, LoginStepResponse.DataEntity info) {
        if (info != null) {
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, String.valueOf(info.getUser_id()));
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_USERNAME, info.getUser_name());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, info.getToken());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, info.getHeadimg());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, info.getMobile());
        }
    }

    public static void saveUserLoginInfo(Context context, UserLoginInfo info) {
        if (info != null) {
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, info.getId());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_USERNAME, info.getUserName());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, info.getToken());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, info.getPortraitUrl());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, info.getPhone());
        }
    }

    public static void saveUserLoginInfo(Context context, RegisterResponse.DataEntity info) {
        if (info != null) {
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, String.valueOf(info.getUser_id()));
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_USERNAME, info.getUser_name());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, info.getToken());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, info.getHeadimg());
            SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, info.getMobile());
        }
    }

    public static void saveUserName(Context context, String username) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_USERNAME, username);
    }

    public static void saveNickName(Context context, String nickName) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_NICKNAME, nickName);
    }

    public static void setSocialLoginFlg(Context context, boolean isSocialLogin) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_SOCIAL_LOGIN_FLG, isSocialLogin);
    }

    public static boolean getSocialLoginFlg(Context context) {
        return SharedPreferencesUtils.getSharedPreferences(context, SP_KEY_SOCIAL_LOGIN_FLG);
    }

    public static void setPhone(Context context, String phone) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, phone);
    }

    public static String getPhone(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_PHONE);
    }

    public static void saveCacheHeadImagePath(Context context, String path) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_CACHE_HEAD_URL, path);
    }

    public static String getCacheHeadImagePath(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_CACHE_HEAD_URL);
    }


    public static String getUserId(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_ID);
    }

    public static String getUserName(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_USERNAME);
    }

    public static String getUserNickName(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_NICKNAME);
    }

    public static String getUserToken(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_TOKEN);
    }

    public static String getUserHeadImage(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_HEAD_URL);
    }

    public static void clearToken(Context context) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, "");
    }

    public static void clearCacheImageUrl(Context context) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_CACHE_HEAD_URL, "");
    }

    public static void saveUserBadRate(Context context, boolean rate) {
        SharedPreferencesUtils.setSharedPreferences(context, HAVE_CLICK_RATE_BAD, rate);
    }

    public static boolean getUserBadRate(Context context) {
        return SharedPreferencesUtils.getSharedPreferences(context, HAVE_CLICK_RATE_BAD);
    }

    public static void saveUserGoodRate(Context context, boolean rate) {
        SharedPreferencesUtils.setSharedPreferences(context, HAVE_CLICK_RATE_GOOD, rate);
    }

    public static boolean getUserGoodRate(Context context) {
        return SharedPreferencesUtils.getSharedPreferences(context, HAVE_CLICK_RATE_GOOD);
    }

    public static void saveAppIsUpdate(Context context, boolean isUpdate) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_APP_CURRENT_VERSION, isUpdate);
    }

    public static boolean getAppIsUpdate(Context context) {
        return SharedPreferencesUtils.getSharedPreferences(context, SP_KEY_APP_CURRENT_VERSION);
    }

    public static void saveUserLatitude(Context context, double latitude) {
        SharedPreferencesUtils.setSharedPreferencesDouble(context, SP_KEY_USER_LATITUDE, latitude);
    }

    public static double getUserLatitude(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesDouble(context, SP_KEY_USER_LATITUDE, 0.0);
    }

    public static void saveUserLongitude(Context context, double longitude) {
        SharedPreferencesUtils.setSharedPreferencesDouble(context, SP_KEY_USER_LONGITUDE, longitude);
    }

    public static double getUserLongitude(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesDouble(context, SP_KEY_USER_LONGITUDE, 0.0);
    }

    public static void saveBannerImagePath(Context context, String path) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HOME_BANNER_DEFAULT_IMG, path);
    }

    public static String getBannerImagePath(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesString(context, SP_KEY_USER_HOME_BANNER_DEFAULT_IMG);
    }


    public static void logOut(Context context) {
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_ID, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_USERNAME, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_NICKNAME, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_TOKEN, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_HEAD_URL, "");
        SharedPreferencesUtils.setSharedPreferences(context, SP_KEY_USER_PHONE, "");
        SharedPreferencesUtils.setSharedPreferencesBoolean(context, AppConstant.SP_KEY_PHONE_CHECKED, false);
        setSocialLoginFlg(context, false);
        clearCacheImageUrl(context);
    }
}
