package com.bobao.identifypro.domain;

/**
 * Created by star on 15/6/29.
 */
public class AuthCodeResponse extends BaseResponse {
    /**
     * data : null
     * error : false
     * message : OK
     */
    private String data;

    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
