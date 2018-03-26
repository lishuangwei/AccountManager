package com.android.accountmanager.ui.common;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.ui.account.SetNetworkFragment;
import com.android.accountmanager.ui.login.LoginActivity;
import com.android.accountmanager.ui.main.MainActivity;
import com.android.accountmanager.utils.AppUtils;

public class ResetPwdActivity extends BaseActivity implements ResetPwdContract.ResetPasswordView {
    private ResetPwdContract.ResetPasswordBasePresenter mPresenter;
    private ResetPwdFragment mResetPwdFragment;
    private Toast mToast;

    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        AppUtils.setNoActionbarTheme(this);

        mPresenter = new ResetPwdPresenter(this);
        mToast = Toast.makeText(this, "ResetPwdActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mResetPwdFragment = new ResetPwdFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fm_reset_password, mResetPwdFragment);
        transaction.commit();
    }

    @Override
    public void nextStep(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fm_reset_password, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void sendVercode(String type, String receiver) {
        mPresenter.sendVercode(type, receiver);
    }

    @Override
    public boolean verifyCode(String type, String phoneNumber, String vercode) {
        return mPresenter.verifyCode(type, phoneNumber, vercode);
    }

    @Override
    public void networkAnomaly() {
        SetNetworkFragment fragment = new SetNetworkFragment();
        fragment.show(getSupportFragmentManager(), getClass().toString());
    }

    @Override
    public void resetPassword(String type, String identifier, String newPassword, String vercode) {
        mPresenter.resetPassword(type, identifier, newPassword, vercode);
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
        mToast.setText(strId);
        mToast.show();
    }

    @Override
    protected void onDestroy() {
        mPresenter.unSubscribe();
        super.onDestroy();
    }

    @Override
    public void signOutEvent(SignOutEvent event) {
        Log.d("test", "signOutEvent: resetPwd");
        finish();

    }

}
