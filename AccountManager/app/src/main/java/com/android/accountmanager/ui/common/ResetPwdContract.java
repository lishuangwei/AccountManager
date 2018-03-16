package com.android.accountmanager.ui.common;

import android.support.v4.app.Fragment;

import com.android.accountmanager.base.BasePresenter;
import com.android.accountmanager.base.BaseView;

/**
 * Created by mn on 2017/2/5 0005.
 */
public class ResetPwdContract {
    interface ResetPasswordView extends BaseView<ResetPasswordBasePresenter> {
        void nextStep(Fragment fragment);
        void resetPassword(String type, String identifier, String newPassword, String vercode);
    }

    interface ResetPasswordBasePresenter extends BasePresenter {
        void resetPassword(String type, String identifier, String newPassword, String vercode);
    }
}
