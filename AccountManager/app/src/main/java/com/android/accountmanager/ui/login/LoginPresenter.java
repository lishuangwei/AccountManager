package com.android.accountmanager.ui.login;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.model.TemplateUtils;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.account.SetNetworkFragment;
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

public class LoginPresenter implements LoginContract.LoginPresenter {

    private LoginContract.LoginView mLoginView;
    private CompositeSubscription mSubscriptions;

    public LoginPresenter(@NonNull LoginContract.LoginView loginView) {
        mLoginView = loginView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void subscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void login(final String loginType) {
        final String password = RequestUri.TYPE_VERCODE.equals(loginType)
                ? mLoginView.getVercode() : AppUtils.encryptPassword(mLoginView.getPassword());
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mLoginView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .login(mLoginView.getContext(), 0, loginType, mLoginView.getLoginName(), password);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "call: login" + s);
                        if (AppUtils.showErrorWithResult(s, mLoginView)) return;
                        LoginTemplate resultTemplate = JackSonUtil.json2Obj(s, LoginTemplate.class);
                        if (resultTemplate == null) {
                            mLoginView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.saveUserinfo(mLoginView.getContext(), resultTemplate);
                                    AppUtils.savePassword(mLoginView.getContext(), password);
                                    mLoginView.showAction(R.string.toast_login_success);
                                    mLoginView.startMain();
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mLoginView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mLoginView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mLoginView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mLoginView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mLoginView.showAction(R.string.toast_account_not_exist);
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
            mLoginView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getVercode(mLoginView.getContext(), type, receiver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "sendVercode: " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mLoginView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    String vercode = resultTemplate.getData();
                                    AppUtils.sendVercode(mLoginView.getContext(), type, receiver, vercode);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mLoginView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mLoginView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mLoginView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mLoginView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mLoginView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public boolean verifyCode(String type, String receiver, String vercode) {
        return AppUtils.verifyCode(mLoginView.getContext(), type, receiver, vercode);
    }
}
