package com.android.accountmanager.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseView;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.event.VercodeStateChangeEvent;
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.model.UserUpdateTemplate;
import com.android.accountmanager.ui.main.MainActivity;

import java.util.Random;

/**
 * Created by Administrator on 2016/12/4 0004.
 */

public class AppUtils {
    public static final int TYPE_PHONENUMBER = 0;
    public static final int TYPE_VERCODE = 1;
    public static final int TYPE_PASSWORD = 2;
    public static final int TYPE_EMAIL = 3;
    public static final int TYPE_LOGIN_PASSWORD = 4;
    public static final int TYPE_RESET_PASSWORD = 5;

    public static final int TYPE_MDDIFY_PASSWORD = 6;
    public static final int TYPE_BIND_EMAIL = 7;
    public static final int TYPE_UNBIND_EMAIL = 8;
    public static final int TYPE_FORGET_PASSWORD = 9;
    public static final int TYPE_CHANGE_PHONE = 10;
    public static final int TYPE_LOGING_OUT = 11;


    public static String getUniqueID() {
        if (Build.VERSION.SDK_INT < 9) {
            return "451551315ad45d" + new Random().nextInt(1000);
        }
        return Build.SERIAL;
    }

    public static SharedPreferences getAccountSharedPreferences(Context context) {
        return context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
    }

    public static void saveUserinfo(Context context, LoginTemplate info) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (info != null) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_AREA, info.getData().getUserinfo().getArea());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_BIRTHDAY, info.getData().getUserinfo().getBirthday());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, info.getData().getUserinfo().getEmail());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_NAME, info.getData().getUserinfo().getFullname());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ICON, info.getData().getUserinfo().getIcon());
            //modify by john - begin, 账号ID 应该取 account , 请参考文档"手机密码登录接口", 后面"绑定邮件"，"更换手机" 都需要用到该字段
//            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ID, info.getData().getUserinfo().getId());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ID, info.getData().getUserinfo().getAccount());
            //modify by john - begin, 账号ID 应该取 account , 请参考文档"手机密码登录接口", 后面"绑定邮件"，"更换手机" 都需要用到该字段

            editor.putInt(UserInfoTemplate.KEY_ACCOUNT_SEX, info.getData().getUserinfo().getSex());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_TEL, info.getData().getUserinfo().getTel());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_TOKEN, info.getData().getToken());
        }
        editor.apply();
    }

    public static void updateUserinfo(Context context, UserUpdateTemplate info) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (info != null) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_AREA, info.getData().getUserinfo().getArea());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_BIRTHDAY, info.getData().getUserinfo().getBirthday());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, info.getData().getUserinfo().getEmail());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_NAME, info.getData().getUserinfo().getFullname());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ICON, info.getData().getUserinfo().getIcon());
            //modify by john - begin, 账号ID 应该取 account , 请参考文档"手机密码登录接口", 后面"绑定邮件"，"更换手机" 都需要用到该字段
//            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ID, info.getData().getUserinfo().getId());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ID, info.getData().getUserinfo().getAccount());
            //modify by john - begin, 账号ID 应该取 account , 请参考文档"手机密码登录接口", 后面"绑定邮件"，"更换手机" 都需要用到该字段
            editor.putInt(UserInfoTemplate.KEY_ACCOUNT_SEX, info.getData().getUserinfo().getSex());
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_TEL, info.getData().getUserinfo().getTel());
        }
        editor.apply();
    }

    public static LoginTemplate.DataBean.UserinfoBean getCurrentAccount(Context context) {
        LoginTemplate.DataBean.UserinfoBean info = new LoginTemplate.DataBean.UserinfoBean();
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        if (sp == null) return info;
        info.setArea(sp.getString(UserInfoTemplate.KEY_ACCOUNT_AREA, ""));
        info.setBirthday(sp.getString(UserInfoTemplate.KEY_ACCOUNT_BIRTHDAY, ""));
        info.setEmail(sp.getString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, ""));
        info.setFullname(sp.getString(UserInfoTemplate.KEY_ACCOUNT_NAME, ""));
        info.setIcon(sp.getString(UserInfoTemplate.KEY_ACCOUNT_ICON, ""));
        info.setAccount(sp.getString(UserInfoTemplate.KEY_ACCOUNT_ID, ""));
        info.setSex(sp.getInt(UserInfoTemplate.KEY_ACCOUNT_SEX, 0));
        info.setTel(sp.getString(UserInfoTemplate.KEY_ACCOUNT_TEL, ""));
        return info;
    }

    public static void saveUpdateInfo(Context context, String key, String value) {
        LoginTemplate.DataBean.UserinfoBean info = getCurrentAccount(context);
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (key.equals("icon")) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ICON, value);
        } else if (key.equals("fullname")) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_NAME, value);
        } else if (key.equals("birthday")) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_BIRTHDAY, value);
        } else if (key.equals("sex")) {
            editor.putInt(UserInfoTemplate.KEY_ACCOUNT_SEX, Integer.parseInt(value));
        } else if (key.equals("area")) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_AREA, "");
        } else if (key.equals("email")) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, value);
        } else if (key.equals("tel")) {
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_TEL, value);
        }
        editor.commit();
    }

    public static String getCurrentToken(Context context) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        if (sp == null) return null;
        return sp.getString(UserInfoTemplate.KEY_ACCOUNT_TOKEN, "");
    }

    public static boolean isLogined(Context context) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        String id = sp.getString(UserInfoTemplate.KEY_ACCOUNT_ID, "");
        return !TextUtils.isEmpty(id);
    }

    public static boolean isBindEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        String email = sp.getString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, "");
        return !TextUtils.isEmpty(email);
    }

    public static void unbindEmail(Context context) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, "");
    }

    public static void sendVercode(Context context, String type, String receiver, String vercode) {
        HandlerBus.getDefault().post(VercodeStateChangeEvent.newInstance());
        SharedPreferences sp = context.getSharedPreferences("register_vercode_list", Context.MODE_PRIVATE);
        sp.edit().putString(receiver, vercode).apply();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, new Intent(context, MainActivity.class), 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getString(R.string.label_vercode))
                .setContentText("用户中心验证码: " + vercode)
                .setSubText((RequestUri.TYPE_TEL.equals(type) ? "手机号码: " : "邮箱地址: ") + receiver)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setFullScreenIntent(pendingIntent, false)
                .setAutoCancel(true);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Service.NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }

    public static boolean verifyCode(Context context, String type, String receiver, String vercode) {
        SharedPreferences sp = context.getSharedPreferences("register_vercode_list", Context.MODE_PRIVATE);
        String correct = sp.getString(receiver, "");
        return StringUtils.isEquals(correct, vercode);
    }

    public static boolean verifyPassword(Context context, String password) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        String pass = sp.getString(UserInfoTemplate.KEY_ACCOUNT_PASSWORD, "");
        Log.d("test", "verifyPassword: " + pass + "-----" + encryptPassword(password));
        return StringUtils.isEquals(pass, encryptPassword(password));
    }

    public static void savePassword(Context context, String password) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        sp.edit().putString(UserInfoTemplate.KEY_ACCOUNT_PASSWORD, password).apply();
    }

    public static void clearCurrentAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static String encryptPassword(String password) {
        String reverse = TextUtils.getReverse(password, 0, password.length()).toString();
        byte[] realkeyPassword = EncryptUtils.encryptHashCode((password + reverse).hashCode());
        try {
            return EncryptUtils.aesEncrypt(password, realkeyPassword);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setNoActionbarTheme(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    public static boolean showErrorWithResult(String s, BaseView view) {
        ResultTemplate result = JackSonUtil.json2Obj(s, ResultTemplate.class);
        if (result != null) {
            switch (result.getResultCode()) {
                case ResultCode.RC_USER_EXIST:
                    view.showAction(R.string.toast_account_exist);
                    return true;
                case ResultCode.RC_INVALID_PARAM:
                    Log.d("test", "showErrorWithResult: RC_INVALID_PARAM");
                    view.showAction(R.string.toast_invalid_param);
                    return true;
                case ResultCode.RC_CODE_ERR:
                    view.showAction(R.string.toast_vercode_error);
                    return true;
                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                    view.showAction(R.string.toast_account_password_error);
                    return true;
                case ResultCode.RC_TOKEN_ERR:
                    view.showAction(R.string.toast_account_not_exist);
                    return true;
            }
        }
        return false;
    }


}
