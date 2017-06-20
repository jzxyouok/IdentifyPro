package com.bobao.identifypro.manager;

/**
 * Created by you on 2015/5/22.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.IntentConstant;
import com.bobao.identifypro.domain.LoginResponse;
import com.bobao.identifypro.domain.RegisterLoginInfo;
import com.bobao.identifypro.domain.RegisterLoginResponse;
import com.bobao.identifypro.domain.UserLoginInfo;
import com.bobao.identifypro.network.StringRequestListener;
import com.bobao.identifypro.network.StringRequestTask;
import com.bobao.identifypro.ui.activity.MainActivity;
import com.bobao.identifypro.utils.ActivityUtils;
import com.bobao.identifypro.utils.DeviceUtil;
import com.bobao.identifypro.utils.DialogUtils;
import com.bobao.identifypro.utils.UserInfoUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 第三方登录管理
 */
public class SocialPlatformLoginManager {

    private final String KEY_APP_NAME = "app";
    private final String KEY_OAUTH_TOKEN = "access_token";
    private final String KEY_OPEN_ID = "openid";
    private final String KEY_PLATFORM = "platform";

    private Class<?> mTargetActivity;

    /**
     * 鉴宝登录请求参数
     */
    private final String KEY_MODULE = "module";
    private final String KEY_ACT = "act";
    private final String KEY_API = "api";
    private final String KEY_OPRATION = "op";
    private final String KEY_USER_ID = "user_id";
    private final String KEY_USERNAME = "user_name";
    private final String KEY_TOKEN = "token";
    private final String KEY_UUID = "uuid";
    private final String KEY_OAUTH_USER_NAME = "getname";
    private final String KEY_OAUTH_USER_HEAD_IMG = "gethead";

    private static SocialPlatformLoginManager sInstance;
    private UMShareAPI mShareAPI = null;

    private String mSocialPlaName;//第三方登陆用户名称
    private String mSocialPlaHeadImg;//第三方登陆用户头像

    //拿到三方登陆的回执信息，登陆到博宝--->鉴宝服务器---step1
    private String BOBAO_OAUTH_URL = "http://user.artxun.com/mobile/user/oauth_login_new.jsp";
    private StringRequestTask mBobaoOAuthLoginRequsteTask;
    private HashMap<String, String> mBobaoOAuthParamsMap = new HashMap<>();


    //拿到三方登陆的回执信息，登陆到博宝--->鉴宝服务器---step2
    private String JIANBAO_OAUTH_URL = "http://jianbao.artxun.com/index.php";
    private StringRequestTask mJianbaoOAuthLoginRequsteTask;
    private HashMap<String, String> mJianbaoOAuthParamsMap = new HashMap<>();
    /**
     * 不管是否是第三方登录，注册和登陆的step2返回信息格式一样
     */
    private StringRequestListener mJianBaoLoginRequestListener = new StringRequestListener() {
        @Override
        public void onStartingRequest() {
        }

        @Override
        public void onSuccessResponse(String response) {
            Log.e("======鉴宝登陆=====", "Response:" + response);
            LoginResponse jianBaoRegistLoginResponse = getJianBaoRegistLoginInfo(response);
            if (jianBaoRegistLoginResponse != null) {
                if (!jianBaoRegistLoginResponse.isIsError()) {
                    if (jianBaoRegistLoginResponse.getJianbaoUserInfo() != null) {
                        //todo 保存鉴宝登录信息到本地
                        UserInfoUtils.saveUserLoginInfo(mContext, jianBaoRegistLoginResponse.getJianbaoUserInfo());
                        UserInfoUtils.setSocialLoginFlg(mContext, true);
                        UserInfoUtils.saveCacheHeadImagePath(mContext, jianBaoRegistLoginResponse.getJianbaoUserInfo().getPortraitUrl());
                        Intent intent = new Intent();
//                        if (mTargetActivity == SubmitOrderActivity.class) {
//                            intent.setClass(mContext, mTargetActivity);
//                            intent.putExtra(IntentConstant.IntentAction, IntentConstant.SubmitOrder);
//                        } else {
                        //跳转回到主页----"我的鉴宝"
                        intent.setClass(mContext, MainActivity.class);
                        intent.putExtra(IntentConstant.KEY_MAIN_PAGER_FRAGMENT_ID, R.id.tv_user);
//                        }
                        ActivityUtils.jump(mContext, intent);
                    }
                } else {
                    Toast.makeText(mContext, "登陆失败！", Toast.LENGTH_SHORT).show();
                    UserInfoUtils.setSocialLoginFlg(mContext, false);
                }
            } else {
                Toast.makeText(mContext, "登陆失败！", Toast.LENGTH_SHORT).show();
                UserInfoUtils.setSocialLoginFlg(mContext, false);
            }

        }

        @Override
        public void onErrorResponse(String errorInfo) {
            Log.e("======鉴宝登陆=====", "Error:" + errorInfo);
            Toast.makeText(mContext, "登陆失败", Toast.LENGTH_SHORT).show();
        }
    };


    private Context mContext;

    private SocialPlatformLoginManager(Context context) {
        mContext = context;
        init();
    }

    public static SocialPlatformLoginManager getsInstance(Context context) {
        if (sInstance == null) {
            sInstance = new SocialPlatformLoginManager(context);
        }
        return sInstance;
    }

    public void init() {
        mShareAPI = UMShareAPI.get(mContext);
    }

    /**
     * QQ授权与登录
     */
    public void tencentAuthAndLogIn(Activity activity, Class<?> targetActivity) {
        mTargetActivity = targetActivity;
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.QQ, umAuthListener);
    }

    /**
     * 微信授权与登陆
     */
    public void weixinAuthAndLogIn(Activity activity, Class<?> targetActivity) {
        mTargetActivity = targetActivity;
        mShareAPI.doOauthVerify(activity, SHARE_MEDIA.WEIXIN, umAuthListener);
    }

    /**
     * auth callback interface
     **/
    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_success);
            Log.e("Authorize", "data---1->>>" + data.toString() + "--action-->>" + action);
            String getAccessToken = "";
            String openid = "";
            String pla = "";
            if (!TextUtils.isEmpty(data.get("access_token"))) {
                if (platform == SHARE_MEDIA.QQ) {
                    getAccessToken = data.get("access_token");
                    openid = data.get("openid");
                    pla = "qq";
                } else if (platform == SHARE_MEDIA.WEIXIN) {
                    getAccessToken = data.get("access_token");//36E6C89037794AEBF18E3F059D99902E
                    openid = data.get("openid").toString();//2CA039A2738995DF1F1E01873449B6A7
                    pla = "weixin";
                } else if (platform == SHARE_MEDIA.SINA) {
                    getAccessToken = data.get("access_key");
                    pla = "sina";
                }
                Log.e("TestAuthData", getAccessToken + "," + openid + "," + getAccessToken + "," + pla);
                //授权成功 三方登录
                UMThirdLogIn((Activity) mContext, platform, getAccessToken, openid, pla);
            } else {
                Log.d("TestData", "Error" + action);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_fail);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_cancel);
        }
    };

    /**
     * 授权。如果授权成功，则获取用户信息
     */
    private void UMThirdLogIn(Activity activity, final SHARE_MEDIA platform, String getAccessToken, String openid, String pla) {
        Log.e("=====第三方授权平台=======", platform.name());
        mShareAPI.getPlatformInfo(activity, platform, new UmAuthLogInListener(getAccessToken, openid, pla));
    }

    public void loginWithOAuthInfo(String accessToken, String openid, String platform) {
        mBobaoOAuthParamsMap.put(KEY_APP_NAME, mContext.getPackageName());
        mBobaoOAuthParamsMap.put(KEY_OAUTH_TOKEN, accessToken);
        mBobaoOAuthParamsMap.put(KEY_OPEN_ID, openid);
        mBobaoOAuthParamsMap.put(KEY_PLATFORM, platform);
        if (mBobaoOAuthLoginRequsteTask != null) {
            mBobaoOAuthLoginRequsteTask.cancel(true);
        }
        mBobaoOAuthLoginRequsteTask = new StringRequestTask(mContext, BOBAO_OAUTH_URL, mBobaoOAuthParamsMap, new StringRequestListener() {
            @Override
            public void onStartingRequest() {
            }

            @Override
            public void onSuccessResponse(String response) {
                //鉴宝登陆请求
                RegisterLoginResponse bobaoResponseInfo = getRegistLoginInfo(response);

                if (!bobaoResponseInfo.isIsError()) {
                    mJianbaoOAuthParamsMap.put(KEY_USER_ID, bobaoResponseInfo.getRegistLogInInfo().getUid());
                    try {
                        mJianbaoOAuthParamsMap.put(KEY_USERNAME, URLEncoder.encode(bobaoResponseInfo.getRegistLogInInfo().getUserName(), "utf-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    String token = bobaoResponseInfo.getRegistLogInInfo().getToken();
                    try {
                        token = URLEncoder.encode(token, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    mJianbaoOAuthParamsMap.put(KEY_TOKEN, token);
                    String uuid = DeviceUtil.getUUID(mContext);
                    mJianbaoOAuthParamsMap.put(KEY_UUID, uuid);
                    mJianbaoOAuthParamsMap.put(KEY_MODULE, "jbapp");
                    mJianbaoOAuthParamsMap.put(KEY_ACT, "api");
                    mJianbaoOAuthParamsMap.put(KEY_API, "user");
                    mJianbaoOAuthParamsMap.put(KEY_OPRATION, "login");

                    /**
                     * 三方授权信息
                     */
                    mJianbaoOAuthParamsMap.put(KEY_OAUTH_USER_NAME, mSocialPlaName);
                    mJianbaoOAuthParamsMap.put(KEY_OAUTH_USER_HEAD_IMG, mSocialPlaHeadImg);

                    if (mJianbaoOAuthLoginRequsteTask != null) {
                        mJianbaoOAuthLoginRequsteTask.cancel(true);
                    }
                    //鉴宝登陆任务
                    mJianbaoOAuthLoginRequsteTask = new StringRequestTask(mContext,
                            JIANBAO_OAUTH_URL, mJianbaoOAuthParamsMap, mJianBaoLoginRequestListener);

                    mJianbaoOAuthLoginRequsteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            }

            @Override
            public void onErrorResponse(String errorInfo) {
                Log.e("======三方博宝登录请求=======", "Error = " + errorInfo);
                Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
            }
        });
        mBobaoOAuthLoginRequsteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    /**
     * 博宝服务器返回的结果解析
     */
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

    private class UmAuthLogInListener implements UMAuthListener {
        private String getAccessToken;
        private String openid;
        private String pla;

        public UmAuthLogInListener(String getAccessToken, String openid, String pla) {
            this.getAccessToken = getAccessToken;
            this.openid = openid;
            this.pla = pla;
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_success);
            if (data != null && !TextUtils.isEmpty(data.get("openid"))) {
                if (platform == SHARE_MEDIA.QQ) {
                    mSocialPlaName = data.get("screen_name").toString();
                    mSocialPlaHeadImg = data.get("profile_image_url").toString();
                } else if (platform == SHARE_MEDIA.WEIXIN) {
                    mSocialPlaName = data.get("nickname").toString();
                    mSocialPlaHeadImg = data.get("headimgurl").toString();
                } else if (platform == SHARE_MEDIA.SINA) {
                    mSocialPlaName = data.get("screen_name").toString();
                    mSocialPlaHeadImg = data.get("profile_image_url").toString();
                }
                Log.e("TestAuthData", getAccessToken + "," + openid + "," + getAccessToken + "," + pla);
                //TODO 登陆到博宝服务器
                loginWithOAuthInfo(getAccessToken, openid, pla);
            } else {
                Log.d("TestData", "Error" + action);
            }
        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_fail);
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            DialogUtils.showShortPromptToast(mContext, R.string.oauth_cancel);
        }
    }
}
