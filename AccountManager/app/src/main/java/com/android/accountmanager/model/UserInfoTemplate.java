package com.android.accountmanager.model;

import java.io.Serializable;

/**
 * Created by fantao on 18-2-8.
 */

public class UserInfoTemplate implements Serializable {
    public static final String KEY_ACCOUNT_USER_HEADER = "key_account_user_header";
    public static final String KEY_ACCOUNT_ID = "key_account_id";
    public static final String KEY_ACCOUNT_NAME = "key_account_name";
    public static final String KEY_ACCOUNT_TEL = "key_account_tel";
    public static final String KEY_ACCOUNT_EMAIL = "key_account_email";
    public static final String KEY_ACCOUNT_ICON = "key_account_icon";
    public static final String KEY_ACCOUNT_SEX = "key_account_sex";
    public static final String KEY_ACCOUNT_BIRTHDAY = "key_account_birthday";
    public static final String KEY_ACCOUNT_AREA = "key_account_area";
    public static final String KEY_ACCOUNT_TOKEN = "key_account_token";
    public static final String KEY_ACCOUNT_PASSWORD = "key_account_password";

    private int atype = 0;
    private int sex = 0;
    private String area = "";
    private String birthday = "";
    private String email = "";
    private String fullname = "";
    private String icon = "";
    private String id = "";
    private String imei = "";
    private String tel = "";

    public UserInfoTemplate() {
    }

    public String getArea() {
        return area;
    }

    public UserInfoTemplate setArea(String area) {
        this.area = area;
        return this;
    }

    public int getAtype() {
        return atype;
    }

    public UserInfoTemplate setAtype(int atype) {
        this.atype = atype;
        return this;
    }

    public String getBirthday() {
        return birthday;
    }

    public UserInfoTemplate setBirthday(String birthday) {
        this.birthday = birthday;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserInfoTemplate setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getFullname() {
        return fullname;
    }

    public UserInfoTemplate setFullname(String fullname) {
        this.fullname = fullname;
        return this;
    }

    public String getIcon() {
        return icon;
    }

    public UserInfoTemplate setIcon(String icon) {
        this.icon = icon;
        return this;
    }

    public String getId() {
        return id;
    }

    public UserInfoTemplate setId(String id) {
        this.id = id;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public UserInfoTemplate setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public int getSex() {
        return sex;
    }

    public UserInfoTemplate setSex(int sex) {
        this.sex = sex;
        return this;
    }

    public String getTel() {
        return tel;
    }

    public UserInfoTemplate setTel(String tel) {
        this.tel = tel;
        return this;
    }
}
