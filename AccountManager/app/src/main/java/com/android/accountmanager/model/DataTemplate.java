package com.android.accountmanager.model;

import java.io.Serializable;

/**
 * Created by fantao on 18-2-8.
 */

public class DataTemplate implements Serializable {
    private String token;
    private String userinfo;

    public DataTemplate() {
    }

    public DataTemplate(String token, String userinfo) {
        this.token = token;
        this.userinfo = userinfo;
    }

    public String getToken() {
        return token;
    }

    public DataTemplate setToken(String token) {
        this.token = token;
        return this;
    }

    public String getUserinfo() {
        return userinfo;
    }

    public DataTemplate setUserinfo(String userinfo) {
        this.userinfo = userinfo;
        return this;
    }
}
