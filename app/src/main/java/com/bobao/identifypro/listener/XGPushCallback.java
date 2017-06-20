package com.bobao.identifypro.listener;

import android.content.Context;

import com.bobao.identifypro.utils.LogUtils;
import com.tencent.android.tpush.XGIOperateCallback;

/**
 * Created by you on 2015/7/17.
 */
public class XGPushCallback implements XGIOperateCallback {
    private String mTag;
    private Context mContext;

    public XGPushCallback(Context context, String tag){
        mContext = context;
        mTag = tag;
    }

    @Override
    public void onSuccess(Object data, int i) {
        LogUtils.d(mContext, mTag, "TPush--", "注册成功，设备token为：", data);
    }

    @Override
    public void onFail(Object data, int errCode, String msg) {
        LogUtils.d(mContext, mTag, "TPush--", "注册失败，错误码：", errCode, ",错误信息：", msg, "注册成功，设备token为：", data);
    }
}
