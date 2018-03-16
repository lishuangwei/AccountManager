package com.android.accountmanager.commom;

public class RequestUri {
    public final static String PROTOCOL = "http://";
    public final static String IP = "59.110.159.147";
    public final static String PORT = ":9525";
    public final static String ACCOUNT_PATH = "/user";
    public final static String FEEDBACK_PATH = "/feedback";

    public final static String LOGIN_PHONE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/p/login";//ok
    public final static String LOGIN_CODE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/user/m/login";//ok
    public final static String REGISTER = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/regist";//ok
    public final static String TEL_CODE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/get/tel/code";//ok
    public final static String EMAIL_CODE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/get/email/code";//ok
    public final static String RESET_PASSWORD = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/find/set/pwd";//ok
    public final static String USER_UPDATE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/update";//ok
    public final static String ACCOUNT = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/get/account";//ok

    public final static String FEEDBACK_SAVA = PROTOCOL + IP + PORT + FEEDBACK_PATH + "/module/save";
    public final static String FEEDBACK_GET_LIST = PROTOCOL + IP + PORT + FEEDBACK_PATH + ACCOUNT_PATH + "/list";
    public final static String EMAIL_UNBIND = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/email/unbind";

    public final static String TYPE_PASSWORD = "type_password";
    public final static String TYPE_VERCODE = "type_vercode";
    public final static String TYPE_TEL = "type_phonenumber";
    public final static String TYPE_EMAIL = "type_mailbox";
}
