package com.bobao.identifypro.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.domain.LoginResponse;
import com.bobao.identifypro.domain.RegisterLoginInfo;
import com.bobao.identifypro.domain.RegisterLoginResponse;
import com.bobao.identifypro.domain.UserLoginInfo;
import com.bobao.identifypro.listener.XGPushCallback;
import com.bobao.identifypro.network.StringRequestListener;
import com.bobao.identifypro.network.StringRequestTask;
import com.bobao.identifypro.ui.activity.MainActivity;
import com.bobao.identifypro.utils.DeviceUtil;
import com.bobao.identifypro.utils.StringUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.tencent.android.tpush.XGPushManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenming on 2015/5/26.
 */
public class UserRegistManeger {

    private Context mContext;
    private static UserRegistManeger sInstance;
    private Class<?> mTargetActivity;
    private final String KEY_APP_NAME = "app";
    private final String KEY_USER_NAME = "mobile";
    private final String KEY_USER_PASSWORD = "password";
    private final String KEY_USER_AUTHCODE = "authcode";

    private final String KEY_USER_ID = "user_id";
    private final String KEY_USERNAME = "user_name";
    private final String KEY_TOKEN = "token";
    private final String KEY_UUID = "uuid";
    private final String KEY_MODULE = "module";
    private final String KEY_ACT = "act";
    private final String KEY_API = "api";
    private final String KEY_OPRATION = "op";

    /**
     * 博宝注册请求
     */
    private StringRequestTask mRegistStringRequestTask;
    private String mRegistUrl = "http://jianbao.artxun.com/index.php?module=jbapp&act=pro&api=center&op=register";
    private Map<String, String> mRegistRequestParamMap = new HashMap<>();
    private StringRequestListener mRegistRequestListener = new StringRequestListener() {

        @Override
        public void onStartingRequest() {
            //TODO Loading..... UI
        }


        @Override
        public void onSuccessResponse(String response) {
            Log.e("====鉴宝注册===", "Resp:" + response);
            //消息提示
            RegisterLoginResponse registLoginInfo = getRegistLoginInfo(response);
            Toast.makeText(mContext, registLoginInfo.getMsg(), Toast.LENGTH_LONG).show();
            //TODO 数据格式校验
            if (!registLoginInfo.isIsError()) {
                mJianBaoLoginRequestParamMap.put(KEY_USER_ID, registLoginInfo.getRegistLogInInfo().getUid());
                try {
                    mJianBaoLoginRequestParamMap.put(KEY_USERNAME, URLEncoder.encode(registLoginInfo.getRegistLogInInfo().getUserName(), "utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                String token = registLoginInfo.getRegistLogInInfo().getToken();
                try {
                    token = URLEncoder.encode(token, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                mJianBaoLoginRequestParamMap.put(KEY_TOKEN, token);
                String uuid = DeviceUtil.getUUID(mContext);
                mJianBaoLoginRequestParamMap.put(KEY_UUID, uuid);//todo uuid取
                mJianBaoLoginRequestParamMap.put(KEY_MODULE, "jbapp");
                mJianBaoLoginRequestParamMap.put(KEY_ACT, "api");
                mJianBaoLoginRequestParamMap.put(KEY_API, "user");
                mJianBaoLoginRequestParamMap.put(KEY_OPRATION, "login");

                if (mJainbaoLoginStringRequestTask != null) {
                    mJainbaoLoginStringRequestTask.cancel(true);
                }

                mJainbaoLoginStringRequestTask = new StringRequestTask(
                        mContext,
                        mJianBaoLoginUrl,
                        mJianBaoLoginRequestParamMap,
                        mJianBaoLoginRequestListener);

                mJainbaoLoginStringRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }

        @Override
        public void onErrorResponse(String errorInfo) {
            Log.e("Regist", errorInfo);
        }
    };

    /**
     * 博宝注册成功后，登陆鉴宝服务器
     *
     * @param context
     */
    //Step2 鉴宝登陆
    private StringRequestTask mJainbaoLoginStringRequestTask;
    private String mJianBaoLoginUrl = "http://jianbao.artxun.com/index.php";
    private Map<String, String> mJianBaoLoginRequestParamMap = new HashMap<>();
    private StringRequestListener mJianBaoLoginRequestListener = new StringRequestListener() {
        @Override
        public void onStartingRequest() {
            //TODO Loadding..... UI
        }

        @Override
        public void onSuccessResponse(String response) {
            //TODO Dismiss Loadding..... UI
            Log.e("JianbaoLoginReq", response);
            LoginResponse jianBaoRegistLoginResponse = getJianBaoRegistLoginInfo(response);
            if (jianBaoRegistLoginResponse != null) {
                if (!jianBaoRegistLoginResponse.isIsError()) {
                    if (jianBaoRegistLoginResponse.getJianbaoUserInfo() != null) {
                        Toast.makeText(mContext, "登录成功!", Toast.LENGTH_SHORT).show();
                        //保存信息到本地
                        UserInfoUtils.saveUserLoginInfo(mContext, jianBaoRegistLoginResponse.getJianbaoUserInfo());
                        UserInfoUtils.setSocialLoginFlg(mContext, false);
                        //注册成功后，刷新信鸽推送的注册名
                        XGPushManager.registerPush(mContext, StringUtils.getString("JQ", UserInfoUtils.getUserId(mContext)), new XGPushCallback(mContext,"XGPush_Regist"));
                        //跳转回指定页面
                        Intent intent = null;
                        if(mTargetActivity != null) {
                            intent = new Intent(mContext, mTargetActivity);
                        } else {
                            intent = new Intent(mContext, MainActivity.class);
                        }

                        intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_user);
                        mContext.startActivity(intent);
                        ((Activity) mContext).finish();
                    }
                } else {
                    Toast.makeText(mContext, "Login failed", Toast.LENGTH_SHORT).show();
                    UserInfoUtils.setSocialLoginFlg(mContext, false);
                }
            } else {
                Toast.makeText(mContext, "Login failed", Toast.LENGTH_SHORT).show();
                UserInfoUtils.setSocialLoginFlg(mContext, false);
            }

        }

        @Override
        public void onErrorResponse(String errorInfo) {
        }
    };

    private UserRegistManeger(Context context) {
        mContext = context;
    }

    public static UserRegistManeger getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new UserRegistManeger(context);
        }
        return sInstance;
    }


    public void register(String tel, String password, String authCode, Class<?> target) {
        mTargetActivity = target;
        if (mRegistStringRequestTask != null) {
            mRegistStringRequestTask.cancel(true);
        }

        //注册请求
        mRegistRequestParamMap.put(KEY_APP_NAME, mContext.getPackageName());
        mRegistRequestParamMap.put(KEY_USER_NAME, tel);
        mRegistRequestParamMap.put(KEY_USER_PASSWORD, password);
        mRegistRequestParamMap.put(KEY_USER_AUTHCODE, authCode);
        mRegistStringRequestTask = new StringRequestTask(mContext, mRegistUrl, mRegistRequestParamMap, mRegistRequestListener);
        mRegistStringRequestTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private RegisterLoginResponse getRegistLoginInfo(String json) {
        RegisterLoginResponse result = new RegisterLoginResponse();
        try {
            JSONObject jsonObj = new JSONObject(json);
            boolean errorFlg = jsonObj.optBoolean("error", false);
            String message = jsonObj.optString("message", "");
            result.setIsError(errorFlg);
            result.setMsg(message);

            JSONObject infoJObj = jsonObj.optJSONObject("data");
            if (infoJObj != null) {
                String infoUid = infoJObj.optString("uid", "");
                String userName = infoJObj.optString("username", "");
                String token = infoJObj.optString("token", "");

                RegisterLoginInfo info = new RegisterLoginInfo();
                info.setUid(infoUid);
                info.setUserName(userName);
                info.setToken(token);
                result.setRegistLogInInfo(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    private LoginResponse getJianBaoRegistLoginInfo(String json) {
        LoginResponse result = new LoginResponse();
        try {
            JSONObject jsonObj = new JSONObject(json);
            boolean errorFlg = jsonObj.optBoolean("error", false);
            result.setIsError(errorFlg);

            JSONObject infoJObj = jsonObj.optJSONObject("data");
            if (infoJObj != null) {
                String userId = infoJObj.optString("user_id", "");
                String nickName = infoJObj.optString("user_name", "");
                String jianbaoToken = infoJObj.optString("token", "");
                String headImg = infoJObj.optString("headimg", "");
                UserLoginInfo info = new UserLoginInfo();
                info.setId(userId);
                info.setUserName(nickName);
                info.setToken(jianbaoToken);
                info.setPortraitUrl(headImg);
                result.setJianbaoUserInfo(info);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
