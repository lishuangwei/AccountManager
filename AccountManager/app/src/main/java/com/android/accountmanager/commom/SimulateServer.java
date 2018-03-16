package com.android.accountmanager.commom;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import com.android.accountmanager.R;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.main.MainActivity;
import com.android.accountmanager.utils.JackSonUtil;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by fantao on 18-2-5.
 */

public class SimulateServer {
    private static SimulateServer sSimulateServer;

    private SimulateServer () {
    }

    public static SimulateServer getInstance() {
        if (sSimulateServer == null) {
            sSimulateServer = new SimulateServer();
        }
        return sSimulateServer;
    }

    public String register(Context context, int userType, String phone, String encryptPwd) {
        final ResultTemplate result = new ResultTemplate();
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(encryptPwd)) {
            result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
            return JackSonUtil.obj2Json(result);
        }
        SharedPreferences phoneList = context.getSharedPreferences("register_phone_list", Context.MODE_PRIVATE);
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        if (phoneList.contains(phone)) {
            result.setResultCode(ResultCode.RETCODE_USER_EXIST);
            return JackSonUtil.obj2Json(result);
        } else {
            UserInfoTemplate info = new UserInfoTemplate();
            info.setId(allocateUserId(context));
            info.setTel(phone);
            allList.edit().putString(info.getId(), info.toString()).commit();
            phoneList.edit().putString(phone, info.getId()).commit();
            result.setResultCode(ResultCode.RC_SUCCESS);
            result.setData(info.toString());
            return JackSonUtil.obj2Json(result);
        }
    }

    public String login(Context context, int userType, String loginType, String loginName, String loginPassword) {
        final ResultTemplate result = new ResultTemplate();
        if (TextUtils.isEmpty(loginType)
                || TextUtils.isEmpty(loginName)
                || TextUtils.isEmpty(loginPassword)) {
            result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
            return JackSonUtil.obj2Json(result);
        }
        SharedPreferences phoneList = context.getSharedPreferences("register_phone_list", Context.MODE_PRIVATE);
        SharedPreferences mailboxList = context.getSharedPreferences("register_mailbox_list", Context.MODE_PRIVATE);
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        if (RequestUri.TYPE_PASSWORD.equals(loginType)) {
            String id = phoneList.getString(loginName, "");
            id = TextUtils.isEmpty(id) ? mailboxList.getString(loginName, "") : id;
            if (TextUtils.isEmpty(id)) {
                result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
                return JackSonUtil.obj2Json(result);
            } else {
                String value = allList.getString(id, "");
                UserInfoTemplate info = new UserInfoTemplate();

                result.setResultCode(ResultCode.RC_SUCCESS);
                result.setData(value);
                return JackSonUtil.obj2Json(result);
            }
        } else if (RequestUri.TYPE_VERCODE.equals(loginType)) {
            String id = phoneList.getString(loginName, "");
            if (TextUtils.isEmpty(id)) {
                result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
                return JackSonUtil.obj2Json(result);
            } else {
                if (!verifyCode(context, RequestUri.TYPE_TEL, loginName, loginPassword)) {
                    result.setResultCode(ResultCode.RETCODE_VERCODE_ERR);
                    return JackSonUtil.obj2Json(result);
                }
                String value = allList.getString(id, "");
                result.setResultCode(ResultCode.RC_SUCCESS);
                result.setData(value);
                return JackSonUtil.obj2Json(result);
            }
        }
        result.setResultCode(ResultCode.RETCODE_PATAM_TYPE_ERR);
        return JackSonUtil.obj2Json(result);
    }

    public String updateUserInfo(Context context, UserInfoTemplate info) {
        final ResultTemplate result = new ResultTemplate();
        final String id = info.getId();
        if (TextUtils.isEmpty(id)) {
            result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
            return JackSonUtil.obj2Json(result);
        }
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        String value = allList.getString(info.getId(), "");
        if (TextUtils.isEmpty(value)) {
            result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
            return JackSonUtil.obj2Json(result);
        }
        allList.edit().putString(id, info.toString()).commit();
        result.setResultCode(ResultCode.RC_SUCCESS);
        result.setData(info.toString());
        return JackSonUtil.obj2Json(result);
    }

    public String updatePhone(Context context, String id, String newPhone) {
        final ResultTemplate result = new ResultTemplate();
        if (TextUtils.isEmpty(id) || TextUtils.isEmpty(newPhone)) {
            result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
            return JackSonUtil.obj2Json(result);
        }
        SharedPreferences phoneList = context.getSharedPreferences("register_phone_list", Context.MODE_PRIVATE);
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        String value = allList.getString(id, "");
        if (TextUtils.isEmpty(value)) {
            result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
            return JackSonUtil.obj2Json(result);
        }
        UserInfoTemplate info = new UserInfoTemplate();
        String originPhone = info.getTel();
        phoneList.edit().remove(originPhone).commit();
        phoneList.edit().putString(newPhone, id).commit();
        info.setTel(newPhone);
        allList.edit().putString(id, info.toString()).commit();
        result.setResultCode(ResultCode.RC_SUCCESS);
        result.setData(info.toString());
        return JackSonUtil.obj2Json(result);
    }

    public String updateMailbox(Context context, String id, String newMailbox) {
        final ResultTemplate result = new ResultTemplate();
        if (TextUtils.isEmpty(id)) {
            result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
            return JackSonUtil.obj2Json(result);
        }
        SharedPreferences mailboxList = context.getSharedPreferences("register_mailbox_list", Context.MODE_PRIVATE);
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        String value = allList.getString(id, "");
        if (TextUtils.isEmpty(value)) {
            result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
            return JackSonUtil.obj2Json(result);
        }
        UserInfoTemplate info = new UserInfoTemplate();
        String originMailbox = info.getEmail();
        if (TextUtils.isEmpty(originMailbox)) {
            if (TextUtils.isEmpty(newMailbox)) {
                result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
                return JackSonUtil.obj2Json(result);
            } else {
                mailboxList.edit().putString(newMailbox, id).commit();
            }
        } else {
            if (TextUtils.isEmpty(newMailbox)) {
                mailboxList.edit().remove(newMailbox).commit();
            } else if (originMailbox.equals(newMailbox)) {
                result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
                return JackSonUtil.obj2Json(result);
            } else {
                mailboxList.edit().remove(newMailbox).commit();
                mailboxList.edit().putString(newMailbox, id).commit();
            }
        }
        info.setEmail(newMailbox);
        allList.edit().putString(id, info.toString()).commit();
        result.setResultCode(ResultCode.RC_SUCCESS);
        result.setData(info.toString());
        return JackSonUtil.obj2Json(result);
    }

    public String resetPassword(Context context, String type, String identifier, String newPassword) {
        final ResultTemplate result = new ResultTemplate();
        if (TextUtils.isEmpty(identifier) || TextUtils.isEmpty(newPassword)) {
            result.setResultCode(ResultCode.RECODED_PARAM_PARSE_ERR);
            return JackSonUtil.obj2Json(result);
        }
        SharedPreferences phoneList = context.getSharedPreferences("register_phone_list", Context.MODE_PRIVATE);
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        SharedPreferences mailboxList = context.getSharedPreferences("register_mailbox_list", Context.MODE_PRIVATE);
        String id = null;
        if (RequestUri.TYPE_TEL.equals(type)) {
            id = phoneList.getString(identifier, "");
        } else if (RequestUri.TYPE_EMAIL.equals(type)) {
            id = mailboxList.getString(identifier, "");
        }
        if (TextUtils.isEmpty(id)) {
            result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
            return JackSonUtil.obj2Json(result);
        }
        String value = allList.getString(id, "");
        if (TextUtils.isEmpty(value)) {
            result.setResultCode(ResultCode.RETCODE_USER_NON_EXIST);
            return JackSonUtil.obj2Json(result);
        }
        UserInfoTemplate info = new UserInfoTemplate();
        allList.edit().putString(id, info.toString()).commit();
        result.setResultCode(ResultCode.RC_SUCCESS);
        result.setData(info.toString());
        return JackSonUtil.obj2Json(result);
    }

    public boolean isUserExist(Context context, String type, String receiver) {
        SharedPreferences phoneList = context.getSharedPreferences("register_phone_list", Context.MODE_PRIVATE);
        SharedPreferences mailboxList = context.getSharedPreferences("register_mailbox_list", Context.MODE_PRIVATE);
        if (RequestUri.TYPE_TEL.equals(type)) {
            return phoneList.contains(receiver);
        } else if (RequestUri.TYPE_EMAIL.equals(type)) {
            return mailboxList.contains(receiver);
        }
        return false;
    }

    public void sendVercode(Context context, String type, String receiver) {
//        HandlerBus.getDefault().getHandler().sendEmptyMessage(HandlerBus.SEND_VERCODE_START);
        SharedPreferences sp = context.getSharedPreferences("register_vercode_list", Context.MODE_PRIVATE);
        String vercode = StringUtils.createVercode();
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

    public boolean verifyCode(Context context, String type, String receiver, String vercode) {
        SharedPreferences sp = context.getSharedPreferences("register_vercode_list", Context.MODE_PRIVATE);
        String correct = sp.getString(receiver, "");
        return StringUtils.isEquals(correct, vercode);
    }

    private String allocateUserId(Context context) {
        SharedPreferences allList = context.getSharedPreferences("register_all_list", Context.MODE_PRIVATE);
        return "6u835f924c-" + allList.getAll().size();
    }
}
