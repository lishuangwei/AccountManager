package com.android.accountmanager.commom;

public class RequestUri {
    public final static String PROTOCOL = "http://";
    public final static String IP = "59.110.159.147";
    public final static String PORT = ":9525";
    public final static String ACCOUNT_PATH = "/user";
    public final static String FEEDBACK_PATH = "/feedback";

    public final static String LOGIN_PHONE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/p/login";//ok
    public final static String LOGIN_CODE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/c/login";//ok
    public final static String REGISTER = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/regist";//ok
    public final static String TEL_CODE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/get/tel/code";//ok
    public final static String EMAIL_CODE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/get/email/code";//ok
    //add by john, 找回密码 -- "找回密码"，调用这个接口，找回密码需要传入 验证码
    public final static String RESET_PASSWORD = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/find/set/pwd";//ok
    //add by john, 设置密码 -- "修改密码" 调用这个接口，修改密码不需要传入 验证码
    public final static String SET_PASSWORD = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/set/pwd"; //ok
    //add by john, 绑定邮箱
    public final static String BINDEMAIL = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/email/regist";//ok

    public final static String USER_UPDATE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/update";//ok
    public final static String ACCOUNT = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/get/account";//ok

    //modify by john, 请参照文档 "用户反馈信息" 接口
    public final static String FEEDBACK_SAVA = PROTOCOL + IP + PORT + FEEDBACK_PATH + "/save";
    //modify by john, 请参照文档 "用户反馈信息" 接口

    public final static String FEEDBACK_GET_LIST = PROTOCOL + IP + PORT + FEEDBACK_PATH + ACCOUNT_PATH + "/list";//获取用户反馈信息
    public final static String FEEDBACK_GET_TYPE_LIST = PROTOCOL + IP + PORT + FEEDBACK_PATH + "/type/list"; //获取反馈类型
    public final static String EMAIL_UNBIND = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/email/unbind";

    //add by john, "更换手机" 接口，文档已经更新.
    public final static String EXBIND_PHONE = PROTOCOL + IP + PORT + ACCOUNT_PATH + "/tel/change";  //OK

    public final static String TYPE_PASSWORD = "type_password";
    public final static String TYPE_VERCODE = "type_vercode";
    public final static String TYPE_TEL = "type_phonenumber";
    public final static String TYPE_EMAIL = "type_mailbox";
}
