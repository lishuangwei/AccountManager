package com.android.accountmanager.ui.login;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseFragment;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.utils.StringUtils;

import java.security.PrivilegedAction;

public class LoginFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    private TextInputEditText mEditLoginName, mEditPassword, mEditVercode;
    private TextView mLoginWayView, mActionSendVercode;
    private View mLyPassword, mLyVercode, mActionRegister, mActionForget;
    private View mActionLogin;
    private CompoundButton mSwitchShowPassword;
    private boolean mIsVercodeLogin;
    private ImageView mImgName, mImgPassword, mImgVerdode, mImgNameDelete, mImgPasswordDelete, mImgVerdodeDelete,mImgBack;
    private LinearLayout mLinear;
    private TextView mTextError;
    private static LoginFragment mLoginFragment;


    public static LoginFragment newInstance() {
        if (mLoginFragment == null) {
            mLoginFragment = new LoginFragment();
        }
        return mLoginFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_login, container, false);
        mLyPassword = parentView.findViewById(R.id.ly_password);
        mLyVercode = parentView.findViewById(R.id.ly_vercode);
        mEditLoginName = (TextInputEditText) parentView.findViewById(R.id.et_login_name);
        mEditLoginName.setOnFocusChangeListener(this);
        mEditLoginName.addTextChangedListener(this);
        mEditPassword = (TextInputEditText) parentView.findViewById(R.id.et_password);
        mEditPassword.setOnFocusChangeListener(this);
        mEditPassword.addTextChangedListener(this);
        mEditVercode = (TextInputEditText) parentView.findViewById(R.id.et_vercode);
        mEditVercode.setOnFocusChangeListener(this);
        mEditVercode.addTextChangedListener(this);
        mActionLogin = parentView.findViewById(R.id.action_login);
        mActionLogin.setOnClickListener(this);
        mImgName = (ImageView) parentView.findViewById(R.id.image_name);
        mImgPassword = (ImageView) parentView.findViewById(R.id.image_password);
        mImgVerdode = (ImageView) parentView.findViewById(R.id.image_vercode);
        mImgNameDelete = (ImageView) parentView.findViewById(R.id.name_delete);
        mImgNameDelete.setOnClickListener(this);
        mImgPasswordDelete = (ImageView) parentView.findViewById(R.id.password_delete);
        mImgPasswordDelete.setOnClickListener(this);
        mImgVerdodeDelete = (ImageView) parentView.findViewById(R.id.vercode_delete);
        mImgVerdodeDelete.setOnClickListener(this);
        mLinear = (LinearLayout) parentView.findViewById(R.id.error_layout);
        mTextError = (TextView) parentView.findViewById(R.id.error_text);
        mImgBack = (ImageView) parentView.findViewById(R.id.image_back);
        mImgBack.setOnClickListener(this);

        mActionRegister = parentView.findViewById(R.id.action_to_register);
        mActionRegister.setOnClickListener(this);
        mActionForget = parentView.findViewById(R.id.action_forget_pwd);
        mActionForget.setOnClickListener(this);
        mLoginWayView = (TextView) parentView.findViewById(R.id.wy_login);
        mLoginWayView.setOnClickListener(this);
        mActionSendVercode = (TextView) parentView.findViewById(R.id.send_vercode);
        mActionSendVercode.setOnClickListener(this);
        mSwitchShowPassword = (CompoundButton) parentView.findViewById(R.id.sw_show_password);
        mSwitchShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEditPassword.setInputType(b ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        switchLoginWay(mIsVercodeLogin);
        return parentView;
    }

    public void setError(int str) {
        mActionLogin.setEnabled(false);
        mLinear.setVisibility(View.VISIBLE);
        mTextError.setText(str);
    }

    public void setSuccess() {
        mActionLogin.setEnabled(false);
        mLinear.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        if (!(getActivity() instanceof LoginContract.LoginView)) {
            return;
        }
        final LoginContract.LoginView loginView = (LoginContract.LoginView) getActivity();
        final String loginName = mEditLoginName.getText().toString();
        final String password = mEditPassword.getText().toString();
        final String vercode = mEditVercode.getText().toString();
        switch (view.getId()) {
            case R.id.action_login:
                loginView.setLoginName(loginName);
                if (!(StringUtils.isMobileNO(loginName) || StringUtils.isMailboxRegular(loginName))) {
                    loginView.showAction(R.string.toast_please_input_correct);
                    setError(R.string.toast_please_input_correct);
                    return;
                }
                if (mIsVercodeLogin) {
                    loginView.setVercode(vercode);
                } else {
                    if (!StringUtils.isPasswordRegular(password)) {
                        loginView.showAction(R.string.toast_password_format);
                        setError(R.string.toast_password_format);
                        return;
                    }
                    loginView.setPassword(password);
                }
                mActionLogin.setEnabled(true);
                loginView.login(mIsVercodeLogin ? RequestUri.TYPE_VERCODE
                        : RequestUri.TYPE_PASSWORD);
                break;
            case R.id.send_vercode:
                if (StringUtils.isMobileNO(loginName)) {
                    loginView.sendVercode(RequestUri.TYPE_TEL, loginName);
                    mEditVercode.requestFocus();
                } else {
                    loginView.showAction(R.string.toast_please_input_correct_phone_number);
                    setError(R.string.toast_please_input_correct_phone_number);
                }
                break;
            case R.id.action_to_register:
                loginView.startRegister();
                break;
            case R.id.action_forget_pwd:
                loginView.startForgetPassword();
                break;
            case R.id.wy_login:
                setSuccess();
                mIsVercodeLogin = !mIsVercodeLogin;
                switchLoginWay(mIsVercodeLogin);
                break;
            case R.id.name_delete:
                mEditLoginName.setText("");
                break;
            case R.id.password_delete:
                mEditPassword.setText("");
                break;
            case R.id.vercode_delete:
                mEditVercode.setText("");
                break;
            case R.id.image_back:
                getActivity().onBackPressed();
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.et_login_name:
                mImgName.setImageDrawable(b ? getImage(R.drawable.ic_label_account_pressed) : getImage(R.drawable.ic_label_account));
                if (!b) {
                    mImgNameDelete.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.et_password:
                mImgPassword.setImageDrawable(b ? getImage(R.drawable.ic_label_password_pressed) : getImage(R.drawable.ic_label_password));
                if (!b) {
                    mImgPasswordDelete.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.et_vercode:
                mImgVerdode.setImageDrawable(b ? getImage(R.drawable.ic_label_vercode_pressed) : getImage(R.drawable.ic_label_vercode));
                if (!b) {
                    mImgVerdodeDelete.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private Drawable getImage(int resid) {
        return getActivity().getDrawable(resid);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void switchLoginWay(boolean isVercodeLogin) {
        if (isVercodeLogin) {
            mEditLoginName.setInputType(InputType.TYPE_CLASS_PHONE);
            mLyPassword.setVisibility(View.GONE);
            mLyVercode.setVisibility(View.VISIBLE);
            mLoginWayView.setText(R.string.login_type_password);
            mEditLoginName.setText("");
            mEditPassword.setText("");
            mEditVercode.setText("");
        } else {
            mEditLoginName.setInputType(InputType.TYPE_CLASS_TEXT);
            mLyPassword.setVisibility(View.VISIBLE);
            mLyVercode.setVisibility(View.GONE);
            mLoginWayView.setText(R.string.login_type_vercode);
            mEditLoginName.setText("");
            mEditPassword.setText("");
            mEditVercode.setText("");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mEditPassword.hasFocus())
            mImgPasswordDelete.setVisibility(StringUtils.isNotEmpty(mEditPassword) ? View.VISIBLE : View.INVISIBLE);
        if (mEditLoginName.hasFocus())
            mImgNameDelete.setVisibility(StringUtils.isNotEmpty(mEditLoginName) ? View.VISIBLE : View.INVISIBLE);
        if (mEditVercode.hasFocus())
            mImgVerdodeDelete.setVisibility(StringUtils.isNotEmpty(mEditVercode) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (StringUtils.isMobileNO(mEditLoginName.getText().toString()) || StringUtils.isMailboxRegular(mEditLoginName.getText().toString())) {
            if (mIsVercodeLogin) {
                mActionLogin.setEnabled(true);
            } else {
                if (StringUtils.isPasswordRegular(mEditPassword.getText().toString()) && StringUtils.isPasswordComplex(mEditPassword.getText().toString())) {
                    mActionLogin.setEnabled(true);
                } else {
                    mActionLogin.setEnabled(false);
                }
            }
        }
    }
}
