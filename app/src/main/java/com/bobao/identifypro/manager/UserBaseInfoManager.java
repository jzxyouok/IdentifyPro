package com.bobao.identifypro.manager;

import android.content.Context;

import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.utils.PropertiesUtils;

import java.util.Properties;

/**
 * 管理自己的用户信息
 */
public class UserBaseInfoManager {

    private static UserBaseInfoManager sInstance = new UserBaseInfoManager();

    private UserBaseInfoManager() {
    }

    public static UserBaseInfoManager getsInstance() {
        return sInstance;
    }

    public void saveBaseInfo(Context context, Properties properties) {
        PropertiesUtils.getsInstance().saveProperties(context, properties);
    }

    public static void updateIntegrateRule(Context context, String rule) {
        PropertiesUtils.getsInstance().updateProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_RULE, rule);
    }

    public static void updateIntegrateMode(Context context, String mode) {
        PropertiesUtils.getsInstance().updateProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_MODE, mode);
    }

    public static void updateIntegrateVersion(Context context, String version) {
        PropertiesUtils.getsInstance().updateProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_VERSION, version);
    }

    public static void updateIntegrateIntegral(Context context, String integral) {
        PropertiesUtils.getsInstance().updateProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_INTEGRAL, integral);
    }

    public static void updateIntegrateSlogan(Context context, String slogan) {
        PropertiesUtils.getsInstance().updateProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_SLOGAN, slogan);
    }

    public static String getIntegrateRule(Context context) {
        return PropertiesUtils.getsInstance().loadProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_RULE, "");
    }

    public static String getIntegrateMode(Context context) {
        return PropertiesUtils.getsInstance().loadProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_MODE, "");
    }

    public static String getIntegrateVersion(Context context) {
        return PropertiesUtils.getsInstance().loadProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_VERSION, "");
    }

    public static String getIntegrateIntegral(Context context) {
        return PropertiesUtils.getsInstance().loadProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_INTEGRAL, "");
    }

    public static String getIntegrateSlogan(Context context) {
        return PropertiesUtils.getsInstance().loadProperties(context, IntentConstant.SP_KEY_USER_INTEGRATE_SLOGAN, "");
    }

    public void logout(Context context) {
        PropertiesUtils.getsInstance().logout(context);
    }
}
