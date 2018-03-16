package com.android.accountmanager.ui.register;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.ui.account.AccountActivity;
import com.android.accountmanager.ui.account.SetNetworkFragment;
import com.android.accountmanager.ui.common.SetPwdFragment;
import com.android.accountmanager.ui.main.MainActivity;

public class RegisterActivity extends BaseActivity implements RegisterContract.RegisterView {
    private RegisterContract.RegisterPresenter mPresenter;
    private RegisterFragment mRegisterFragment;
    private Toast mToast;
    private String mUserName, mPassword, mVercode;
    private SetPwdFragment mSetPassword;

    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        View decorView = getWindow().getDecorView();
        int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(option);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        mPresenter = new RegisterPresenter(this);
        mToast = Toast.makeText(this, "RegisterActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mRegisterFragment = RegisterFragment.newInstance();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.fm_register, mRegisterFragment);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void register() {
        mPresenter.register();
    }

    @Override
    public String getPhoneNumber() {
        return mUserName;
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
    public void startMain() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void setPhoneNumber(String number) {
        mUserName = number;
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
    public void nextStep(Fragment fragment) {
        if(fragment instanceof  SetPwdFragment) {
            mSetPassword = (SetPwdFragment) fragment;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fm_register, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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

}
