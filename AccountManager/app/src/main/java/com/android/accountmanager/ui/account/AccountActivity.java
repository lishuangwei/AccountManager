package com.android.accountmanager.ui.account;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseActivity;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.common.ResetPwdActivity;
import com.android.accountmanager.ui.login.LoginActivity;
import com.android.accountmanager.utils.AppUtils;

public class AccountActivity extends BaseActivity implements AccountContract.AccountView {
    private static final String BACK_STACK = "account_back_stack";
    private static final String EXTRA_SHOW_FRAGMENT = "account_show_fragment";
    private static final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = "account_show_fragment_args";

    private Toast mToast;
    private PhotoSelectionHandler mPhotoSelectionHandler;
    private AccountPresenter mPresenter;
    private String mName, mPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        AppUtils.setNoActionbarTheme(this);
        mToast = Toast.makeText(this, "LoginActivity", Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.CENTER, 0, 0);
        mPresenter = new AccountPresenter(this);
        String initialFragment = getIntent().getStringExtra(EXTRA_SHOW_FRAGMENT);
        Bundle initialArguments = getIntent().getBundleExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS);
        Log.d("test", "AccountActivity onCreate: initialFragment=" + initialFragment);
        Log.d("test", "AccountActivity onCreate: initialArguments=" + initialArguments);
        String initialTitle = initialArguments == null ? null : initialArguments.getString("title");
        if (initialFragment != null) {
            startFragment(initialFragment, initialArguments);
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment, new AccountFragment());
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            transaction.commitAllowingStateLoss();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void signOutEvent(SignOutEvent event) {
        AppUtils.clearCurrentAccount(this);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mPhotoSelectionHandler.handlePhotoActivityResult(requestCode, resultCode, data);
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onStartFragment(String fragmentClass, Bundle args) {
        Log.d("test", "onStartFragment: startactivity-----" + fragmentClass);
        if ("com.android.accountmanager.ui.account.EmailFragment".equals(fragmentClass)
                && TextUtils.isEmpty(AppUtils.getAccountSharedPreferences(this)
                .getString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, null))) {
            args.putString("title", getString(R.string.title_verify_password));
            VerifyPwdFragment verifyPwdFragment = new VerifyPwdFragment();
            verifyPwdFragment.setArguments(args);
            verifyPwdFragment.setListener(new VerifyPwdFragment.Listener() {
                @Override
                public void onVerifyComplete(boolean success) {
                    if (success) {
                        Bundle fragmentArgs = new Bundle();
                        fragmentArgs.putString("title", getString(R.string.title_bind_mailbox));
                        startActivity(buildStartFragmentIntent(
                                "com.android.accountmanager.ui.account.EmailFragment$EmailBindFragment", fragmentArgs));
                    } else {
                        showAction(R.string.toast_password_error);
                    }
                }
            });
            verifyPwdFragment.show(getSupportFragmentManager(),
                    getString(R.string.title_verify_password));
            return;
        } else if ("com.android.accountmanager.ui.common.SetPwdFragment".equals(fragmentClass)) {
            args.putString("title", getString(R.string.title_verify_password));
            VerifyPwdFragment verifyPwdFragment = new VerifyPwdFragment();
            verifyPwdFragment.setArguments(args);
            verifyPwdFragment.setListener(new VerifyPwdFragment.Listener() {
                @Override
                public void onVerifyComplete(boolean success) {
                    if (success) {
                        Bundle fragmentArgs = new Bundle();
                        fragmentArgs.putString("title", getString(R.string.title_modify_password));
                        startActivity(buildStartFragmentIntent(
                                "com.android.accountmanager.ui.common.SetPwdFragment", fragmentArgs));
                    } else {
                        showAction(R.string.toast_password_error);
                    }
                }
            });
            verifyPwdFragment.show(getSupportFragmentManager(),
                    getString(R.string.title_verify_password));
            return;
        } else if ("com.android.accountmanager.ui.account.PhoneNumberFragment".equals(fragmentClass)
                && TextUtils.isEmpty(AppUtils.getAccountSharedPreferences(this)
                .getString(UserInfoTemplate.KEY_ACCOUNT_TEL, null))) {
            args.putString("title", getString(R.string.title_verify_password));
            VerifyPwdFragment verifyPwdFragment = new VerifyPwdFragment();
            verifyPwdFragment.setArguments(args);
            verifyPwdFragment.setListener(new VerifyPwdFragment.Listener() {
                @Override
                public void onVerifyComplete(boolean success) {
                    if (success) {
                        Bundle fragmentArgs = new Bundle();
                        fragmentArgs.putString("title", getString(R.string.title_bind_phone));
                        startActivity(buildStartFragmentIntent(
                                "com.android.accountmanager.ui.account.PhoneNumberFragment$PhoneNumberBindFragment", fragmentArgs));
                    } else {
                        showAction(R.string.toast_password_error);
                    }
                }
            });
            verifyPwdFragment.show(getSupportFragmentManager(),
                    getString(R.string.title_verify_password));
            return;
        } else if ("com.android.accountmanager.ui.account.BindEmailFragment".equals(fragmentClass)) { //add by lsw
            if (AppUtils.isBindEmail(this)) {
                startActivity(buildStartFragmentIntent(
                        "com.android.accountmanager.ui.account.EmailFragment", args));
            } else {
                startActivity(buildStartFragmentIntent(fragmentClass, args));
            }
            return;
        }
        Log.d("test", "onStartFragment: startactivity" + fragmentClass);
        startActivity(buildStartFragmentIntent(fragmentClass, args));
    }

    @Override
    public void onNameUpdate(String name) {
        mPresenter.onNameUpdate(name);
    }

    @Override
    public void onBirthdayUpdate(String birthday) {
        mPresenter.onBirthdayUpdate(birthday);
    }

    @Override
    public void onSexUpdate(int sex) {
        mPresenter.onSexUpdate(sex);
    }

    @Override
    public void sendVercode(String type, String receiver) {
        mPresenter.sendVercode(type, receiver);
    }

    @Override
    public void bindPhoneNumber(String mailbox, String code) {
        mPresenter.bindPhoneNumber(mailbox, code);
    }

    @Override
    public void bindMailbox(String mailbox, String vercode) {
        mPresenter.bindMailbox(mailbox, vercode);
    }

    @Override
    public void unBindMailbox() {
        mPresenter.unBindMailbox();
    }

    @Override
    public void modifyPassword(String password) {
        mPresenter.modifyPassword(password);
    }

    @Override
    public void getAccount(String type) {
        mPresenter.getAccount(type);
    }

    @Override
    public void startForgetPassword() {
        Intent intent = new Intent(this, ResetPwdActivity.class);
        startActivity(intent);
    }

    @Override
    public void login(String loginType, String name, String password, int type) {
        mPresenter.login(loginType, name, password, type);
    }

    @Override
    public void nextStep(boolean isEmail) {
        Log.d("test", "nextStep: ");
        if (!isEmail) {
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putString("title", getString(R.string.title_set_new_password));
            fragmentArgs.putString("type", RequestUri.TYPE_TEL);
            fragmentArgs.putString("identifier", getModifyName());
            startFragmentNew("com.android.accountmanager.ui.common.SetPwdFragment", fragmentArgs);
        } else {
            Bundle fragmentArgs = new Bundle();
            fragmentArgs.putString("title", getString(R.string.title_bind_mailbox));
            startFragmentNew("com.android.accountmanager.ui.account.EmailFragment$EmailBindFragment", fragmentArgs);
        }
    }

    @Override
    public String getModifyName() {
        return mName;
    }

    @Override
    public String getModifyPassWord() {
        return mPassword;
    }

    @Override
    public void setModifyName(String name) {
        this.mName = name;

    }

    @Override
    public void setModifyPassWord(String passWord) {
        this.mPassword = passWord;
    }

    @Override
    public void startMain() {
        finish();
    }

    @Override
    public PhotoSelectionHandler getPhotoSelectionHandler() {
        if (mPhotoSelectionHandler == null) {
            mPhotoSelectionHandler = new PhotoSelectionHandler(this);
        }
        return mPhotoSelectionHandler;
    }

    @Override
    public boolean verifyPassword(String password) {
        return mPresenter.verifyPassword(password);
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
    public boolean onPhotoSelected(Uri uri) {
        return mPresenter.onPhotoSelected(uri);
    }

    private void startFragment(String fragmentClass, Bundle args) {
        Fragment f = Fragment.instantiate(this, fragmentClass, args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragment, f);
//        transaction.addToBackStack(BACK_STACK);
        Log.d("shuang", "startFragment: BACK_STACK");
        transaction.commitAllowingStateLoss();
    }

    private Intent buildStartFragmentIntent(String fragmentClass, Bundle args) {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setClass(this, getClass());
        intent.putExtra(EXTRA_SHOW_FRAGMENT, fragmentClass);
        intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, args);
        return intent;
    }

    @Override
    public void startFragmentNew(String fragmentClass, Bundle args) {
        Fragment f = Fragment.instantiate(this, fragmentClass, args);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.fragment_slide_right_in, R.anim.fragment_slide_left_out, R.anim.fragment_slide_left_in, R.anim.fragment_slide_right_out);
        transaction.replace(R.id.fragment, f);
        transaction.addToBackStack(BACK_STACK);
        Log.d("shuang", "startFragment: BACK_STACK");
        transaction.commitAllowingStateLoss();
    }
}
