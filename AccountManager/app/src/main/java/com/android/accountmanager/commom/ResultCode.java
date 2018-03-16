package com.android.accountmanager.commom;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public interface ResultCode {
    int RC_SUCCESS = 1; //操作成功
    //用户相关
    int RC_TOKEN_ERR= 20001;//用户不存在
    int RC_ACCOUNT_PASSWORD_ERR = 20002;//用户名或密码错误
    int RC_USER_EXIST = 20003;//用户已存在
    int RC_CODE_ERR = 20004;//验证码错误
    int RC_INVALID_PARAM = 30001;//无效参数

    int RETCODE_PASSWOER_ERR = 0x11;//密码错误
    int RETCODE_USER_NON_EXIST = 0x12;//用户不存在
    int RETCODE_USER_EXIST = 0x13;//用户已存在
    int RETCODE_LOGIN_SESSION_EXPIRED = 0x14;//会话过期
    int RETCODE_USER_TYPE_ERR = 0x15;//用户类型错误 不匹配
    int RETCODE_VERCODE_ERR = 0x16;//验证码错误
    //参数异常相关
    int RECODED_PARAM_PARSE_ERR = 0x30; //参数格式不对 不能json解析,为空等
    int RETCODE_PATAM_TYPE_ERR = 0x31;//参数类型错误
}

