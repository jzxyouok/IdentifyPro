package com.bobao.identifypro.domain;

/**
 * 通用的返回信息数据
 */
public class NormalMessageResponse extends BaseResponse {
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
