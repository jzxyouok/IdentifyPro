package com.bobao.identifypro.domain;

/**
 * Created by star on 15/10/14.
 */
public class BaseResponse {
    /**
     * error : true
     * data : {}
     * message : 验证码错误！
     */

    private boolean error;
    private String message;

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
