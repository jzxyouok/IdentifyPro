package com.bobao.identifypro.domain;

/**
 * Created by you on 2015/5/21.
 */
public class RegisterLoginInfo {
    private String mUid;
    private String mUserName;
    private String mToken;

    public String getUid() {
        return mUid;
    }

    public void setUid(String mUid) {
        this.mUid = mUid;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getToken() {
        return mToken;
    }

    public void setToken(String mToken) {
        this.mToken = mToken;
    }
}
