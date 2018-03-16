package com.android.accountmanager.commom;

import android.content.Context;
import android.util.Log;

import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.utils.AppUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RequestServer {

    private static RequestServer sRequestServer;

    private RequestServer() {
    }

    public static RequestServer getInstance() {
        if (sRequestServer == null) {
            sRequestServer = new RequestServer();
        }
        return sRequestServer;
    }

    /**
     * 其他.手机解绑接口没有,邮箱解绑交互设计无验证码环节.由于邮箱登录不上,其他邮箱问题暂未测试
     * <p>
     * bug 登录接口,已登录状态下,修改密码，需要输入用户账号密码，调用，一直报用户名或密码错误
     */
    public String login(Context context, int userType, String loginType, String loginName, String loginCode) {
        String key = "password";
        if (RequestUri.TYPE_VERCODE.equals(loginType)) {
            key = "code";
        }
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("tel", loginName);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param(key, loginCode);
        String uri = key == "password" ? RequestUri.LOGIN_PHONE : RequestUri.LOGIN_CODE;
        Log.d("test", "login: loginName = " + loginName + "password=" + loginCode + "key==" + key);
        try {
            String result = OkHttpClientManager.getSync(context, uri, param1, param2);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String register(Context context, int userType, String tel, String password, String vercode) {
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("tel", tel);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("password", password);
        OkHttpClientManager.Param param3 = new OkHttpClientManager.Param("code", vercode);
        OkHttpClientManager.Param param4 = new OkHttpClientManager.Param("account", "");
        try {
            return OkHttpClientManager.getSync(context, RequestUri.REGISTER, param1, param2, param3, param4);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * bug 绑定邮箱接口,绑定成功后无法用邮箱登录
     */
    public String getVercode(Context context, String type, String receiver) {
        String key = "tel";
        String url = RequestUri.TEL_CODE;
        if (RequestUri.TYPE_EMAIL.equals(type)) {
            key = "email";
            url = RequestUri.EMAIL_CODE;
        }
        Log.d("test", "getVercode: " + url);
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param(key, receiver);
        try {
            return OkHttpClientManager.getSync(context, url, param1);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String resetPassword(Context context, String type, String identifier, String newPassword, String vercode) {
        String key = "tel";
        if (RequestUri.TYPE_TEL.equals(type)) {
            key = "tel";
        } else if (RequestUri.TYPE_EMAIL.equals(type)) {
            key = "email";
        }
        Log.d("test", "resetPassword: key=" + key);
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param(key, identifier);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("password", newPassword);
        OkHttpClientManager.Param param3 = new OkHttpClientManager.Param("code", vercode);
        try {
            return OkHttpClientManager.getSync(context, RequestUri.RESET_PASSWORD, param1, param2, param3);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String setNewPassword(Context context, String type, String tel, String newPassword) {
        String key = "tel";
        if (RequestUri.TYPE_TEL.equals(type)) {
            key = "tel";
        } else if (RequestUri.TYPE_EMAIL.equals(type)) {
            key = "email";
        }
        Log.d("test", "resetPassword: key=" + key);
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param(key, tel);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("password", newPassword);
        OkHttpClientManager.Param param3 = new OkHttpClientManager.Param("code", "");
        try {
            return OkHttpClientManager.getSync(context, RequestUri.RESET_PASSWORD, param1, param2, param3);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String updateUserInfo(Context context, String token, String... vars) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[vars.length / 2 + 1];
        for (int i = 0; i < params.length; i++) {
            if (i < params.length - 1) {
                String key = vars[2 * i];
                String value = vars[2 * i + 1];
                params[i] = new OkHttpClientManager.Param(key, value);
                Log.d("test", "updateUserInfo: key===" + key + "value===" + value);
            } else {
                params[i] = new OkHttpClientManager.Param("token", token);
            }
        }
        try {
            return OkHttpClientManager.getSync(context, RequestUri.USER_UPDATE, params);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String updateUserParams(Context context, String token, String key, String value) {
        AppUtils.saveUpdateInfo(context, key, value);
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[6];
        LoginTemplate.DataBean.UserinfoBean user = AppUtils.getCurrentAccount(context);
        params[0] = new OkHttpClientManager.Param("icon", user.getIcon());
        params[1] = new OkHttpClientManager.Param("fullname", user.getFullname());
        params[2] = new OkHttpClientManager.Param("birthday", user.getBirthday());
        params[3] = new OkHttpClientManager.Param("sex", user.getSex() + "");
        params[4] = new OkHttpClientManager.Param("area", user.getArea());
        params[5] = new OkHttpClientManager.Param("token", token);
        try {
            return OkHttpClientManager.getSync(context, RequestUri.USER_UPDATE, params);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getAccount(Context context, String type) {
        try {
            return OkHttpClientManager.getSync(context, RequestUri.ACCOUNT);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * bug 保存模块信息和反馈接口数据不对, 保存模块信息得到的返回参数不对，保存一条信息,得到多条信息
     */
    public String saveModule(Context context, int type, String name, String content, String icon) {
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("mtname", name);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("mticon", icon);
        OkHttpClientManager.Param param3 = new OkHttpClientManager.Param("mtno", content);
        try {
            return OkHttpClientManager.getSync(context, RequestUri.FEEDBACK_SAVA, param1, param2, param3);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * bug 保存模块信息和反馈接口数据不对, 保存模块信息得到的返回参数不对
     */
    public String getFeedbackLis(Context context, String token) {
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("token", token);
        try {
            return OkHttpClientManager.getSync(context, RequestUri.FEEDBACK_GET_LIST, param1);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String unBindEmail(Context context, String email, String code) {
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("email", email);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("code", code);
        OkHttpClientManager.Param param3 = new OkHttpClientManager.Param("account", "");
        try {
            return OkHttpClientManager.getSync(context, RequestUri.EMAIL_UNBIND, param1, param2, param3);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    /**
     *
     *bug 更换手机号接口显示参数错误 accout不是去掉了,传了空或者不传都报参数错误
     */
    public String bindNewPhoneNumber(Context context, String tel, String code, String token) {
        OkHttpClientManager.Param param1 = new OkHttpClientManager.Param("tel", tel);
        OkHttpClientManager.Param param2 = new OkHttpClientManager.Param("code", code);
        OkHttpClientManager.Param param3 = new OkHttpClientManager.Param("account", "");
        OkHttpClientManager.Param param4 = new OkHttpClientManager.Param("token", token);
        try {
            return OkHttpClientManager.getSync(context, RequestUri.EMAIL_UNBIND, param1, param2, param3, param4);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

}
