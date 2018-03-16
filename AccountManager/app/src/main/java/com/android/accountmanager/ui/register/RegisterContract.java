package com.android.accountmanager.ui.register;

import android.support.v4.app.Fragment;

import com.android.accountmanager.base.BasePresenter;
import com.android.accountmanager.base.BaseView;

/**
 * Created by mn on 2017/2/5 0005.
 */
public class RegisterContract {
    public interface RegisterView extends BaseView<RegisterPresenter> {
        void startMain();
        void setPhoneNumber(String number);
        void setPassword(String password);
        void setVercode(String vercode);
        void nextStep(Fragment fragment);
        void register();
        String getPhoneNumber();
        String getPassword();
        String getVercode();
    }

    interface RegisterPresenter extends BasePresenter {
        void register();
    }
}
