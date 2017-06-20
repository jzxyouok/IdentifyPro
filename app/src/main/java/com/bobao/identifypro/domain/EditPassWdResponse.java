package com.bobao.identifypro.domain;

/**
 * Created by you on 2015/6/17.
 */
public class EditPassWdResponse extends BaseResponse {

    /**
     * data : ok
     * error : false
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
