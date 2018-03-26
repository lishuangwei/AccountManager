package com.android.accountmanager.ui.account;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.android.accountmanager.base.BasePresenter;
import com.android.accountmanager.base.BaseView;

/**
 * Created by mn on 2017/2/5 0005.
 */
public class AccountContract {
    public interface AccountView extends BaseView<AccountPresenter> {
        PhotoSelectionHandler getPhotoSelectionHandler();

        boolean verifyPassword(String password);

        boolean onPhotoSelected(Uri uri);

        void onStartFragment(String fragmentClass, Bundle args);

        void onNameUpdate(String name);

        void onBirthdayUpdate(String birthday);

        void onSexUpdate(int sex);

        void bindPhoneNumber(String phoneNumber, String code);

        void bindMailbox(String mailbox, String vercode);   //add by john, 添加验证码参数;

        void unBindMailbox();

        void modifyPassword(String password);

        void getAccount(String type);

        void startForgetPassword();

        void login(String loginType, String name, String password, int type);

        void nextStep(boolean isEmail);

        String getModifyName();

        String getModifyPassWord();

        void setModifyName(String name);

        void setModifyPassWord(String passWord);

        void startMain();

        void startFragmentNew(String fragmentClass, Bundle args);
    }

    public interface AccountPresenter extends BasePresenter {
        boolean verifyPassword(String password);

        boolean onPhotoSelected(Uri uri);

        void onNameUpdate(String name);

        void onBirthdayUpdate(String birthday);

        void onSexUpdate(int sex);

        void bindPhoneNumber(String phoneNumber, String code);

        void bindMailbox(String mailbox, String vercode);  //add by john, 添加验证码参数

        void unBindMailbox();

        void modifyPassword(String password);

        void getAccount(String type);

        void login(String loginType, String name, String password, int type);
    }
}
