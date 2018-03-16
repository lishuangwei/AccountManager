package com.android.accountmanager.ui.register;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.model.DataTemplate;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.model.TemplateUtils;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.JackSonUtil;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/12/1 0001.
 */

public final class RegisterPresenter implements RegisterContract.RegisterPresenter {
    private RegisterContract.RegisterView mRegisterView;
    private CompositeSubscription mSubscriptions;

    @Override
    public void subscribe() {
    }

    public RegisterPresenter(RegisterContract.RegisterView registerView) {
        mRegisterView = registerView;
        mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void register() {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mRegisterView.networkAnomaly();
            return;
        }
        Subscription subscription = Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .register(mRegisterView.getContext(), 0, mRegisterView.getPhoneNumber(),
                                        AppUtils.encryptPassword(mRegisterView.getPassword()), mRegisterView.getVercode());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        android.util.Log.e("test", "register : " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mRegisterView.showAction(R.string.toast_register_failed);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    mRegisterView.showAction(R.string.toast_register_success);
                                    mRegisterView.startMain();
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mRegisterView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mRegisterView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mRegisterView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mRegisterView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mRegisterView.showAction(R.string.toast_account_not_exist);
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
            mRegisterView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getVercode(mRegisterView.getContext(), type, receiver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        android.util.Log.e("test", "getVercode : " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mRegisterView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    String vercode = resultTemplate.getData();
                                    AppUtils.sendVercode(mRegisterView.getContext(), type, receiver, vercode);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mRegisterView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mRegisterView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mRegisterView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mRegisterView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mRegisterView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public boolean verifyCode(String type, String receiver, String vercode) {
        return AppUtils.verifyCode(mRegisterView.getContext(), type, receiver, vercode);
    }
}
