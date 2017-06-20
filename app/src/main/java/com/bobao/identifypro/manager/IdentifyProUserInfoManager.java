package com.bobao.identifypro.manager;

import android.content.Context;

import com.bobao.identifypro.utils.SharedPreferencesUtils;

/**
 * 管理自己的用户信息
 */
public class IdentifyProUserInfoManager {

    //判断启动页是否已显示
    private static final String SP_KEY_IS_SPLASH_SHOWED = "is_splash_showed";

    private static IdentifyProUserInfoManager sInstance = new IdentifyProUserInfoManager();

    private IdentifyProUserInfoManager() {
    }

    public static IdentifyProUserInfoManager getsInstance() {
        return sInstance;
    }

    public static void setSplashShow(Context context, boolean isShow) {
        SharedPreferencesUtils.setSharedPreferencesBoolean(context, SP_KEY_IS_SPLASH_SHOWED, isShow);
    }

    public static boolean getSplashShow(Context context) {
        return SharedPreferencesUtils.getSharedPreferencesBoolean(context, SP_KEY_IS_SPLASH_SHOWED);
    }
}
