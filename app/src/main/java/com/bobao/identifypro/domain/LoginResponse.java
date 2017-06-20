package com.bobao.identifypro.domain;

/**
 * Created by you on 2015/5/22.
 */

/***
 * 鉴宝用户登陆信息封装类
 */
public class LoginResponse {
    private boolean mIsError;
    private UserLoginInfo mJianbaoUserInfo;

    public UserLoginInfo getJianbaoUserInfo() {
        return mJianbaoUserInfo;
    }

    public void setJianbaoUserInfo(UserLoginInfo mJianbaoUserInfo) {
        this.mJianbaoUserInfo = mJianbaoUserInfo;
    }

    public boolean isIsError() {
        return mIsError;
    }

    public void setIsError(boolean mIsError) {
        this.mIsError = mIsError;
    }
}
