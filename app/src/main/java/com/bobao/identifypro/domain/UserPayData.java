package com.bobao.identifypro.domain;

/**
 * Created by you on 2015/6/8.
 */
public class UserPayData extends BaseResponse {
    /**
     * error : false
     * data : 支付完成！
     * message :
     */
    private String data;


    public void setData(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
