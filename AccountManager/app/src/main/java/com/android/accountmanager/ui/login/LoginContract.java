package com.android.accountmanager.ui.login;

import com.android.accountmanager.base.BasePresenter;
import com.android.accountmanager.base.BaseView;

/**
 * Created by mn on 2016/11/30 0030.
 * This specifies the contract between the view and the presenter.
 */

public interface LoginContract {

    interface LoginView extends BaseView<LoginPresenter> {

        void startMain();

        void startRegister();

        void startForgetPassword();

        void setLoginName(String number);

        void setPassword(String password);

        void setVercode(String vercode);

        void login(String loginType);

        String getLoginName();

        String getPassword();

        String getVercode();
    }

    interface LoginPresenter extends BasePresenter {

        void login(String loginType);
    }
}
