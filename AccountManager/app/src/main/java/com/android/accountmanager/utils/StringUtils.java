package com.android.accountmanager.utils;

import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;

import com.android.accountmanager.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author wzw
 * @version 2010-3-12
 */
public class StringUtils {
    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
    };

    public static boolean isMobileNO(String mobiles) {
        if (TextUtils.isEmpty(mobiles)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPasswordRegular(String password) {
        if (TextUtils.isEmpty(password) || password.length() < 6 || password.length() > 16) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isPasswordComplex(String password) {
        Pattern p1 = Pattern.compile("[0-9]");
        Pattern p2 = Pattern.compile("[A-Za-z]");
        Matcher m1 = p1.matcher(password);
        Matcher m2 = p2.matcher(password);
        return password != null && m1.find() && m2.find();
    }

    public static boolean isPasswordSame(String password, String confirmPassword) {
        return !TextUtils.isEmpty(password) && !TextUtils.isEmpty(confirmPassword) && confirmPassword.equals(password);
    }

    public static boolean isVercodeCorrect(String vercode, String sendVercode) {
        return !TextUtils.isEmpty(vercode) && !TextUtils.isEmpty(sendVercode) && vercode.equals(sendVercode);
    }

    public static String createVercode() {
        StringBuilder buffer = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            buffer.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return buffer.toString();
    }

    public static boolean isMailboxRegular(String mailbox) {
        if (TextUtils.isEmpty(mailbox)) {
            return false;
        } else {
            return Patterns.EMAIL_ADDRESS.matcher(mailbox).matches();
        }
    }

    public static boolean isEquals(String str1, String str2) {
        return !TextUtils.isEmpty(str1) && !TextUtils.isEmpty(str2) && str1.equals(str2);
    }

    public static String getPhoneNumber(String phonenumber) {
        String tel = phonenumber.substring(0, 3) + "****" + phonenumber.substring(7, 11);
        return tel;
    }

    public static int getSexName(int type) {
        int sex = -1;
        if (type == 0) {
            sex = R.string.item_male;
        } else if (type == 1) {
            sex = R.string.item_female;
        } else {
            sex = R.string.action_settings;
        }
        return sex;
    }

    public static boolean isNotEmpty(EditText editText) {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            return false;
        } else {
            return true;
        }
    }

    public static String getCurrentTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(System.currentTimeMillis());
    }

    public static String formatTime(long time) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(time);
    }

}
