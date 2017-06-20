package com.bobao.identifypro.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.bobao.identifypro.R;
import com.bobao.identifypro.constant.NetConstant;
import com.bobao.identifypro.domain.BaseResponse;
import com.bobao.identifypro.task.StringToBeanTask;
import com.bobao.identifypro.ui.dialog.ProgressDialog;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huyongsheng on 2015/3/18.
 * <p/>
 * 用于处理网络相关
 * <p/>
 * 已添加get请求相关
 */
public class NetUtils {

    /**
     * 判断网络连接是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断用户是否为移动网络
     *
     * @param context
     * @return
     */
    public static boolean isMobileNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否使用WiFi
     *
     * @param context
     * @return
     */
    public static boolean isWifiNet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    /**
     * 返回运营商的名字
     *
     * @param context
     * @return "中国移动" "中国联通" "中国电信"
     */
    public static String getOperatorName(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String mOperatorName = telephonyManager.getNetworkOperatorName();
        return mOperatorName;
    }

    /**
     * 判断用户是否使用的为2G网络
     * GPRS | EDGE 中国移动和中国联通2G ，CDMA 中国电信2G
     *
     * @param context
     * @return
     */
    public static boolean isSecGenerationNet(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && isMobileNet(context) && (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_GPRS
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EDGE
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_CDMA)) {
            return true;
        }
        return false;
    }

    /**
     * 判断用户是否使用3G网络
     * UMTS/HSDPA 中国联通3G，EVDO 中国电信3G
     *
     * @param context
     * @return
     */
    public static boolean isThirdGenerationNet(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null && isMobileNet(context) && (telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_HSDPA
                || telephonyManager.getNetworkType() == TelephonyManager.NETWORK_TYPE_EVDO_0)) {
            return true;
        }
        return false;
    }

    /**
     * 获取用户当前网络状态
     *
     * @param context
     * @return
     */
    public static String getNetworkState(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (!isNetworkConnected(context)) {
            return StringUtils.getString("无网络");
        } else if (isWifiNet(context)) {
            return StringUtils.getString("wifi");
        } else if (isMobileNet(context)) {
            switch (telephonyManager.getNetworkType()) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                    return StringUtils.getString("2G", "100 kbps");
                case TelephonyManager.NETWORK_TYPE_EDGE:
                    return StringUtils.getString("50-100 kbps");
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                    return StringUtils.getString("100 kbps");
                case TelephonyManager.NETWORK_TYPE_UMTS:
                    return StringUtils.getString("3G", "100 kbps");
                case TelephonyManager.NETWORK_TYPE_CDMA:
                    return StringUtils.getString("2G", "14-64 kbps");
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    return StringUtils.getString("400-1000 kbps");
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    return StringUtils.getString("600-1400 kbps");
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                    return StringUtils.getString("3G", "2-14 Mbps");
                case TelephonyManager.NETWORK_TYPE_HSPA:
                    return StringUtils.getString("1-23 Mbps");
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    return StringUtils.getString("25 kbps ");
                case TelephonyManager.NETWORK_TYPE_LTE:
                    return StringUtils.getString(" 10+ Mbps");
            }
        }
        return StringUtils.getString("Unknown");
    }


    private static final String TAG = NetUtils.class.getSimpleName();
    public static final int ErrorData = -5000;
    private static final int ErrorJson = -5001;

    private static NetUtils mInstance;
    private static ProgressDialog mProgressDialog;
    private boolean isNeedProgress;
    private String mProgressNote = "";

    private NetUtils() {
    }

    /**
     * 使用单例模式，确保控件只有一个
     */
    public static NetUtils getInstance(boolean isNeedProgress) {
        if (mInstance == null) {
            mInstance = new NetUtils();
        }
        mInstance.isNeedProgress = isNeedProgress;
        return mInstance;
    }

    /**
     * 使用单例模式，确保控件只有一个
     */
    public static NetUtils getInstance(boolean isNeedProgress, String progressNote) {
        if (mInstance == null) {
            mInstance = new NetUtils();
        }
        mInstance.isNeedProgress = isNeedProgress;
        mInstance.mProgressNote = progressNote;
        return mInstance;
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public static HttpHandler<String> getNet(Context context, String url, RequestParams params,
                                             RequestCallBack<String> callBack) {
        if (isNetworkConnected(context)) {
            // 请求网络
            return new HttpUtils().send(HttpRequest.HttpMethod.GET, url, params, callBack);
        } else {
            DialogUtils.showShortPromptToast(context, R.string.network_not_available);
            return null;
        }
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public static HttpHandler<String> postNet(Context context, String url, RequestParams params,
                                              RequestCallBack<String> callBack) {
        if (isNetworkConnected(context)) {
            // 请求网络
            return new HttpUtils().send(HttpRequest.HttpMethod.POST, url, params, callBack);
        } else {
            DialogUtils.showShortPromptToast(context, R.string.network_not_available);
            return null;
        }
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler get(Context context, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.GET, NetConstant.HOST, params, callBack, 0, 10);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler get(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.GET, url, params, callBack, 0, 10);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler getNoCache(Context context, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.GET, NetConstant.HOST, params, callBack, 0, 0);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler getNoCache(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.GET, url, params, callBack, 0, 0);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler post(Context context, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.POST, NetConstant.HOST, params, callBack, 0, 10);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler post(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.POST, url, params, callBack, 0, 10);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler postNocache(Context context, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.POST, NetConstant.HOST, params, callBack, 0, 0);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public HttpHandler postNocache(Context context, String url, RequestParams params, Callback callBack) {
        return send(context, HttpRequest.HttpMethod.POST, url, params, callBack, 0, 0);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    private HttpHandler send(Context context, HttpRequest.HttpMethod method, String url, RequestParams params, Callback callBack,
                             int cacheTime, int fileTime) {
        List<NameValuePair> names = new ArrayList<>();
        if (params != null && params.getQueryStringParams() != null) {
            names.addAll(params.getQueryStringParams());
            for (NameValuePair name : names) {
                callBack.cacheFileName = StringUtils.getString(callBack.cacheFileName, name.getName(), ":", name.getValue(), "--");
            }
            callBack.cacheFileName = MD5Utils.toMD5(callBack.cacheFileName);
        }

        // 如果从文件获取数据
        if (fileTime > 0) {
            String fileCache = FileUtils.readStringFromFileCache(context, callBack.cacheFileName);
            if (!TextUtils.isEmpty(fileCache)) {
                ResponseInfo<String> info = new ResponseInfo<>(null, fileCache, true);
                callBack.onSuccess(info);
            }
        }
        if (isNetworkConnected(context)) {
            if (isNeedProgress) {
                if (!TextUtils.isEmpty(mProgressNote)) {
                    mProgressDialog = DialogUtils.showProgressDialog(context, mProgressNote);
                } else {
                    mProgressDialog = DialogUtils.showProgressDialog(context);
                }
            }
            // 请求网络
            return new HttpUtils().configCurrentHttpCacheExpiry(cacheTime)
                    .send(method, url, params, callBack);
        } else {
            DialogUtils.showShortPromptToast(context, R.string.network_not_available);
        }
        return null;
    }

    /**
     * 获取网络字串，如果本地有缓存则从本地取
     */
    public static HttpHandler getStringReponseWithCache(Context context, HttpRequest.HttpMethod method, String url, RequestParams params, StringResponseCallback callBack,
                                                        int memoryCacheTime, boolean enableReadCache, String cacheFileName) {
        callBack.cacheFileName = cacheFileName;
        //读缓存 or 请求网络
        String fileCache = FileUtils.readStringFromFileCache(context, callBack.cacheFileName);
        if (!TextUtils.isEmpty(fileCache) && enableReadCache) {
            ResponseInfo<String> info = new ResponseInfo<>(null, fileCache, true);
            callBack.onSuccess(info);
        } else {
            return new HttpUtils().configCurrentHttpCacheExpiry(memoryCacheTime)
                    .send(method, url, params, callBack);
        }
        return null;
    }

    /**
     * 抓取字体字串时的回调，不用json解析
     */
    public static abstract class StringResponseCallback extends RequestCallBack<String> {
        private Context mContext;
        private String cacheFileName;

        public StringResponseCallback(Context context) {
            mContext = context;
            cacheFileName = "cacheFileName";
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            onStringRequestSuccess(responseInfo.result);
            String cache = FileUtils.readStringFromFileCache(mContext, cacheFileName);
            if (TextUtils.isEmpty(cache)) {
                FileUtils.writeStringToFileCache(mContext, cacheFileName, responseInfo.result);
            }
        }

        @Override
        public void onFailure(HttpException error, String msg) {
            DialogUtils.showShortPromptToast(mContext, "获取字体信息失败");
        }

        //成功拿到字串，交给业务处理
        protected abstract void onStringRequestSuccess(String response);
    }

    /**
     * 将网络请求、内容解析和等待进度条封装在一起使用
     */
    public abstract static class Callback<Response extends BaseResponse> extends RequestCallBack<String>
            implements StringToBeanTask.ConvertListener<Response> {
        private Context mContext;
        private Class<Response> mClassType;
        private boolean isFileCache;
        private String cacheFileName;

        public abstract void onNetSuccess(Response response);

        public void onNetFailed(HttpException e) {
        }

        public Callback(Context context, Class<Response> classType) {
            mContext = context;
            mClassType = classType;
            isFileCache = false;
            cacheFileName = "cacheFileName";
        }

        @Override
        public void onSuccess(ResponseInfo<String> responseInfo) {
            isFileCache = responseInfo.getAllHeaders() == null;
            StringToBeanTask<Response> task = new StringToBeanTask<>(mClassType, this);
            task.execute(responseInfo.result);
            // 写入缓存
            FileUtils.writeStringToFileCache(mContext, cacheFileName, responseInfo.result);
        }

        @Override
        public void onFailure(HttpException e, String s) {
            if (mProgressDialog != null && mProgressDialog.isShowing() && !isFileCache) {
                mProgressDialog.dismiss();
            }
            dealError(e);
        }

        @Override
        public void onConvertSuccess(Response response) {
            if (mProgressDialog != null && mProgressDialog.isShowing() && !isFileCache) {
                mProgressDialog.dismiss();
            }
            if (!response.getError()) {
                onNetSuccess(response);
            } else {
                HttpException e = new HttpException(ErrorData, response.getMessage());
                dealError(e);
            }
        }

        @Override
        public void onConvertFailed(String json) {
            if (mProgressDialog != null && mProgressDialog.isShowing() && !isFileCache) {
                mProgressDialog.dismiss();
            }
            HttpException e = new HttpException(ErrorJson, "Convert Json Failed" + json);
            dealError(e);
        }

        private void dealError(HttpException e) {
            switch (e.getExceptionCode()) {
                case ErrorData:
                    DialogUtils.showShortPromptToast(mContext, e.getMessage());
                    break;
                case ErrorJson:
                    LogForTest.logE(e.getMessage());
                    break;
                default:
                    DialogUtils.showShortPromptToast(mContext, R.string.network_not_available);
                    break;
            }
            LogUtils.e(mContext, TAG, e);
            onNetFailed(e);
        }
    }
}