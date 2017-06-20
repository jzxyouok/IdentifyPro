package com.bobao.identifypro.domain;

/**
 * Created by kakaxicm on 2015/11/17.
 */
public class UserNoChargeResponse extends BaseResponse {
    /**
     * error : false
     * data : 155
     * message :
     */
    private int data;

    public void setData(int data) {
        this.data = data;
    }

    public int getData() {
        return data;
    }
}
