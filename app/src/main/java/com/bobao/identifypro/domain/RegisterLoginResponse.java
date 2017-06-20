package com.bobao.identifypro.domain;

/**
 * Created by you on 2015/5/21.
 */
public class RegisterLoginResponse {
    private boolean mIsError;
    private String mMsg;
    private RegisterLoginInfo mRegisterLogInInfo;//用户注册或者登陆成功信息

    public boolean isIsError() {
        return mIsError;
    }

    public void setIsError(boolean mIsError) {
        this.mIsError = mIsError;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public RegisterLoginInfo getRegistLogInInfo() {
        return mRegisterLogInInfo;
    }

    public void setRegistLogInInfo(RegisterLoginInfo mRegisterLogInInfo) {
        this.mRegisterLogInInfo = mRegisterLogInInfo;
    }
}
