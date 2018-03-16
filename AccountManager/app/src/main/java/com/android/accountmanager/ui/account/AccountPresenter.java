package com.android.accountmanager.ui.account;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.commom.InternetHelper;
import com.android.accountmanager.commom.RequestServer;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.commom.ResultCode;
import com.android.accountmanager.commom.SimulateServer;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.model.AccountTemplate;
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.model.ResultTemplate;
import com.android.accountmanager.model.TemplateUtils;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.model.UserUpdateTemplate;
import com.android.accountmanager.ui.common.SetPwdFragment;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.JackSonUtil;

import java.io.FileNotFoundException;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by fantao on 18-1-23.
 */

public class AccountPresenter implements AccountContract.AccountPresenter {
    private AccountContract.AccountView mAccountView;
    private CompositeSubscription mSubscriptions;

    public AccountPresenter(@NonNull AccountContract.AccountView accountView) {
        mAccountView = accountView;
        mSubscriptions = new CompositeSubscription();
    }

    public void subscribe() {
        mSubscriptions.clear();
    }

    @Override
    public void unSubscribe() {
        mSubscriptions.clear();
    }

    @Override
    public boolean verifyPassword(String password) {
        return AppUtils.verifyPassword(mAccountView.getContext(), password);
    }

    @Override
    public void sendVercode(final String type, final String receiver) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getVercode(mAccountView.getContext(), type, receiver);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "sendVercode: " + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    String vercode = resultTemplate.getData();
                                    AppUtils.sendVercode(mAccountView.getContext(), type, receiver, vercode);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public boolean verifyCode(String type, String receiver, String vercode) {
        return AppUtils.verifyCode(mAccountView.getContext(), type, receiver, vercode);
    }

    @Override
    public boolean onPhotoSelected(final Uri uri) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.networkAnomaly();
            return false;
        }
        Log.d("test", "onPhotoSelected: uri=" + uri);
        Bitmap bitmap = null;
        try {
            bitmap = PhotoSelectionHandler.getBitmapFromUri(mAccountView.getContext(), uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        if (bitmap == null || bitmap.getWidth() <= 0 || bitmap.getHeight() <= 0) {
            mAccountView.showAction(R.string.accountPhotoSavedErrorToast);
            return false;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance().updateUserParams(mAccountView.getContext(),
                                AppUtils.getCurrentToken(mAccountView.getContext()),
                                "icon", uri.toString());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "onPhotoSelected: " + s);
                        UserUpdateTemplate resultTemplate = JackSonUtil.json2Obj(s, UserUpdateTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.updateUserinfo(mAccountView.getContext(), resultTemplate);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
        return true;
    }

    @Override
    public void onNameUpdate(final String name) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance().updateUserParams(mAccountView.getContext(),
                                AppUtils.getCurrentToken(mAccountView.getContext()),
                                "fullname", name);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "onNameUpdate: " + s);
                        UserUpdateTemplate resultTemplate = JackSonUtil.json2Obj(s, UserUpdateTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.updateUserinfo(mAccountView.getContext(), resultTemplate);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onBirthdayUpdate(final String birthday) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance().updateUserParams(mAccountView.getContext(),
                                AppUtils.getCurrentToken(mAccountView.getContext()),
                                "birthday", birthday);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "onBirthdayUpdate: " + s);
                        UserUpdateTemplate resultTemplate = JackSonUtil.json2Obj(s, UserUpdateTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.updateUserinfo(mAccountView.getContext(), resultTemplate);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void onSexUpdate(final int sex) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.networkAnomaly();
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance().updateUserParams(mAccountView.getContext(),
                                AppUtils.getCurrentToken(mAccountView.getContext()),
                                "sex", sex + "");
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "onSexUpdate: " + s);
                        UserUpdateTemplate resultTemplate = JackSonUtil.json2Obj(s, UserUpdateTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.updateUserinfo(mAccountView.getContext(), resultTemplate);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void bindPhoneNumber(final String phoneNumber, final String code) {
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .bindNewPhoneNumber(mAccountView.getContext(), phoneNumber, code, AppUtils.getCurrentToken(mAccountView.getContext()));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "call: bindPhoneNumber" + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.saveUpdateInfo(mAccountView.getContext(), "tel", phoneNumber);
                                    mAccountView.showAction(R.string.toast_reset_password_success);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void bindMailbox(final String mailbox) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.showAction(R.string.toast_network_exception);
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getVercode(mAccountView.getContext(), RequestUri.TYPE_EMAIL, mailbox);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    AppUtils.saveUpdateInfo(mAccountView.getContext(), "email", mailbox);
                                    mAccountView.showAction(R.string.toast_bind_mailbox);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void unBindMailbox() {
        final LoginTemplate.DataBean.UserinfoBean currentInfo = AppUtils.getCurrentAccount(mAccountView.getContext());
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .unBindEmail(mAccountView.getContext(), AppUtils.getCurrentAccount(mAccountView.getContext()).getEmail().toString(), null);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    mAccountView.showAction(R.string.toast_unbind_email_success);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void modifyPassword(final String password) {
        final LoginTemplate.DataBean.UserinfoBean currentInfo = AppUtils.getCurrentAccount(mAccountView.getContext());
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .setNewPassword(mAccountView.getContext(), RequestUri.TYPE_TEL,
                                        currentInfo.getTel(), AppUtils.encryptPassword(password));
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "call: modifyPassword" + s);
                        ResultTemplate resultTemplate = JackSonUtil.json2Obj(s, ResultTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    HandlerBus.getDefault().post(SignOutEvent.newInstance());
                                    mAccountView.showAction(R.string.toast_reset_password_success);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void getAccount(final String type) {
        if (BaseActivity.getNetState() == InternetHelper.I_NET_NONE) {
            mAccountView.showAction(R.string.toast_network_exception);
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .getAccount(mAccountView.getContext(), type);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "getAccount: " + s);
                        AccountTemplate resultTemplate = JackSonUtil.json2Obj(s, AccountTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    String account = resultTemplate.getData().getAccount();
                                    mAccountView.showAction("account=" + account);
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }
                    }
                });
        mSubscriptions.add(subscription);
    }

    @Override
    public void login(final String loginType, final String name, final String password, final boolean isEmail) {
        int netState = BaseActivity.getNetState();
        if (netState == InternetHelper.I_NET_NONE) {
            mAccountView.showAction(R.string.toast_network_exception);
            return;
        }
        Subscription subscription = rx.Observable.just("")
                .subscribeOn(Schedulers.io())
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        return RequestServer.getInstance()
                                .login(mAccountView.getContext(), 0, loginType, name, password);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("test", "login accountpresenter: " + s);
                        if (s.contains(ResultCode.RC_ACCOUNT_PASSWORD_ERR + "")) {
                            mAccountView.showAction(R.string.toast_account_password_error);
                            mAccountView.nextStep(isEmail);
                            return;
                        }

                        LoginTemplate resultTemplate = JackSonUtil.json2Obj(s, LoginTemplate.class);
                        if (resultTemplate == null) {
                            mAccountView.nextStep(isEmail);
                            mAccountView.showAction(R.string.toast_unknown_error);
                        } else {
                            switch (resultTemplate.getResultCode()) {
                                case ResultCode.RC_SUCCESS:
                                    if (!isEmail) {
                                        SetPwdFragment setPwdFragment = new SetPwdFragment();
                                        Bundle args = new Bundle();
                                        args.putString("type", RequestUri.TYPE_TEL);
                                        args.putString("identifier", mAccountView.getModifyName());
                                        setPwdFragment.setArguments(args);
                                        mAccountView.nextStep(isEmail);
                                    } else {
                                        mAccountView.nextStep(isEmail);
                                    }
                                    break;
                                case ResultCode.RC_USER_EXIST:
                                    mAccountView.showAction(R.string.toast_account_exist);
                                    break;
                                case ResultCode.RC_INVALID_PARAM:
                                    mAccountView.showAction(R.string.toast_invalid_param);
                                    break;
                                case ResultCode.RC_CODE_ERR:
                                    mAccountView.showAction(R.string.toast_vercode_error);
                                    break;
                                case ResultCode.RC_ACCOUNT_PASSWORD_ERR:
                                    mAccountView.showAction(R.string.toast_account_password_error);
                                    break;
                                case ResultCode.RC_TOKEN_ERR:
                                    mAccountView.showAction(R.string.toast_account_not_exist);
                                    break;
                            }
                        }

                    }
                });
        mSubscriptions.add(subscription);
    }
}
