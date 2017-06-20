package com.bobao.identifypro.network;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by you on 2015/5/21.
 */
public class StringRequestTask extends AsyncTask<Void, Void, Void> {
    private Context mContext;

    private String mUrl;
    private Map<String, String> mRequestParamsMap;

    private String response;
    private String exceptionInfo;
    private StringRequestListener mStringRequstLitener;

    public StringRequestTask(Context context,String url,Map<String, String> requestParamsMap,StringRequestListener responseListeners){
        mContext = context;
        mUrl = url;
        mRequestParamsMap = requestParamsMap;
        mStringRequstLitener = responseListeners;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(mStringRequstLitener != null){
            mStringRequstLitener.onStartingRequest();
        }
    }

    @Override
    protected Void doInBackground(Void... params) {
        response = null;
        try {
            StringBuilder urlStringBuiler = new StringBuilder();
            urlStringBuiler.append(mUrl).append("?");
            Iterator<String> iters = mRequestParamsMap.keySet().iterator();
            while (iters.hasNext()) {
                String key = iters.next();
                String value = mRequestParamsMap.get(key);
                urlStringBuiler.append(key).append("=").append(value).append("&");
            }
            urlStringBuiler.deleteCharAt(urlStringBuiler.toString().length()-1);
            String url = urlStringBuiler.toString();
            byte[] bytes = HttpRequestService.doGet(url,mContext);
            response = new String(bytes);
//            response = HttpRequestService.startPostRequest(mContext, mUrl, mRequestParamsMap);
        } catch (Exception e) {
            response = null;
            exceptionInfo = e.getMessage();
            return null;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (!TextUtils.isEmpty(response)) {
            if (mStringRequstLitener != null) {
                mStringRequstLitener.onSuccessResponse(response);
            }
        } else {
            if (mStringRequstLitener != null) {
                mStringRequstLitener.onErrorResponse(exceptionInfo);
            }
        }
    }
}

