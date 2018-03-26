package com.android.accountmanager.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.ui.account.AccountActivity;
import com.android.accountmanager.ui.account.SetNetworkFragment;
import com.android.accountmanager.ui.common.ResetPwdActivity;
import com.android.accountmanager.ui.main.MainActivity;
import com.android.accountmanager.ui.register.RegisterActivity;
import com.android.accountmanager.utils.AppUtils;

public class LoginActivity extends BaseActivity implements LoginContract.LoginView {
    private LoginContract.LoginPresenter mPresenter;
    private LoginFragment mLoginFragment;
    private Toast mToast;
    private String mPhoneNumber, mPassword, mVercode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppUtils.setNoActionbarTheme(this);
        mPresenter = new LoginPresenter(this);
        mToast = Toast.makeText(this, "LoginActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mLoginFragment = LoginFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fm_login, mLoginFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void startMain() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void startRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void startForgetPassword() {
        Intent intent = new Intent(this, ResetPwdActivity.class);
        startActivity(intent);
    }

    @Override
    public void sendVercode(String type, String receiver) {
        mPresenter.sendVercode(type, receiver);
    }

    @Override
    public boolean verifyCode(String type, String receiver, String vercode) {
        return mPresenter.verifyCode(type, receiver, vercode);
    }

    @Override
    public void networkAnomaly() {
        SetNetworkFragment fragment = new SetNetworkFragment();
        fragment.show(getSupportFragmentManager(), getClass().toString());
    }

    @Override
    public void login(String loginType) {
        mPresenter.login(loginType);
    }

    @Override
    public void setLoginName(String number) {
        mPhoneNumber = number;
    }

    @Override
    public void setPassword(String password) {
        mPassword = password;
    }

    @Override
    public void setVercode(String vercode) {
        mVercode = vercode;
    }

    @Override
    public String getLoginName() {
        return mPhoneNumber;
    }

    @Override
    public String getPassword() {
        return mPassword;
    }

    @Override
    public String getVercode() {
        return mVercode;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showAction(CharSequence actionString) {
        mToast.setText(actionString);
        mToast.show();
    }

    @Override
    public void showAction(int strId) {
        if (strId != R.string.toast_login_success) {
            mLoginFragment.setError(strId);
        } else {
            mLoginFragment.setSuccess();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unSubscribe();
    }
}
