package com.android.accountmanager.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.OkHttpClientManager;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.event.VercodeStateChangeEvent;
import com.android.accountmanager.model.LoginTemplate;
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
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ID, info.getData().getUserinfo().getId());
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
            editor.putString(UserInfoTemplate.KEY_ACCOUNT_ID, info.getData().getUserinfo().getId());
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

    public static void sendVercode(Context context, String type, String receiver, String vercode) {
        HandlerBus.getDefault().post(VercodeStateChangeEvent.newInstance());
        SharedPreferences sp = context.getSharedPreferences("register_vercode_list", Context.MODE_PRIVATE);
        sp.edit().putString(receiver, vercode).commit();
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
//        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
//        String correct = password;
//        return StringUtils.isEquals(correct, encryptPassword(password));
        return true;
    }

    public static void clearCurrentAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences("current_account", Context.MODE_PRIVATE);
        sp.edit().clear().commit();
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

}
