package com.android.accountmanager.ui.common;

import android.util.Log;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.JackSonUtil;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public final class ResetPwdPresenter implements ResetPwdContract.ResetPasswordBasePresenter {
    private ResetPwdContract.ResetPasswordView mResetPasswordView;
    private CompositeSubscription mSubscriptions;

    @Override
    public void subscribe() {
    }

    public ResetPwdPresenter(ResetPwdContract.ResetPasswordView resetPasswordView) {
        mResetPasswordView = resetPasswordView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void resetPassword(final String type, final String identifier, final String newPassword, final String vercode) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mResetPasswordView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .resetPassword(mResetPasswordView.getContext(),
                                        type, identifier, AppUtils.encryptPassword(newPassword), vercode);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "resetPassword: " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mResetPasswordView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.savePassword(mResetPasswordView.getContext(), AppUtils.encryptPassword(newPassword));
                                    HandlerBus.getDefault().post(SignOutEvent.newInstance());
                                    mResetPasswordView.showAction(R.string.toast_reset_password_success);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mResetPasswordView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RETCODE_PATAM_TYPE_ERR:
                                    mResetPasswordView.showAction(R.string.toast_param_error);
                                    break;
                                case ResultCode.RETCODE_USER_NON_EXIST:
                                    mResetPasswordView.showAction(R.string.toast_account_not_exist);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mResetPasswordView.showAction(R.string.toast_vercode_error);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void sendVercode(final String type, final String receiver) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mResetPasswordView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getVercode(mResetPasswordView.getContext(), type, receiver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "sendVercode: " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mResetPasswordView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    String vercode = resultTemplate.getData();
                                    AppUtils.sendVercode(mResetPasswordView.getContext(), type, receiver, vercode);
                                    break;
                                case ResultCode.RETCODE_PATAM_TYPE_ERR:
                                    mResetPasswordView.showAction(R.string.toast_param_error);
                                    break;
                                case ResultCode.RETCODE_USER_NON_EXIST:
                                    mResetPasswordView.showAction(R.string.toast_account_not_exist);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mResetPasswordView.showAction(R.string.toast_account_password_error);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public boolean verifyCode(String type, String receiver, String vercode) {
        return AppUtils.verifyCode(mResetPasswordView.getContext(), type, receiver, vercode);
    }
}
