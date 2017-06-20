package com.bobao.identifypro.network;

/**
 * Created by you on 2015/5/21.
 */
public interface StringRequestListener {
    void onStartingRequest();
    void onSuccessResponse(String response);
    void onErrorResponse(String errorInfo);
}
