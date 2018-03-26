package com.android.accountmanager.ui.register;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseFragment;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.ui.common.SetPwdFragment;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by mn on 2017/2/5 0001.
 */

public final class RegisterFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener, TextWatcher {
    private TextInputEditText mEditPhoneNumber, mEditVercode;
    private TextView mActionSendVercode;
    private View mParentView, mActionNext;
    private static RegisterFragment mRegisterFragment;
    private ImageView mImgError, mImgPhoneLabel, mImgVercodeLabel, mImgPhoneDelete, mImgVercodeDelete, mImgBack;
    private TextView mTextEror;
    private LinearLayout mLayoutPhone, mLayoutVercode;

    public static RegisterFragment newInstance() {
        if (mRegisterFragment == null) {
            mRegisterFragment = new RegisterFragment();
        }
        return mRegisterFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mParentView = inflater.inflate(R.layout.fragment_register, container, false);
        mEditPhoneNumber = (TextInputEditText) mParentView.findViewById(R.id.et_login_name);
        mEditPhoneNumber.setOnFocusChangeListener(this);
        mEditPhoneNumber.addTextChangedListener(this);
        mEditVercode = (TextInputEditText) mParentView.findViewById(R.id.et_vercode);
        mEditVercode.setOnFocusChangeListener(this);
        mEditVercode.addTextChangedListener(this);
        mActionSendVercode = (TextView) mParentView.findViewById(R.id.send_vercode);
        mActionSendVercode.setOnClickListener(this);
        mActionNext = mParentView.findViewById(R.id.action_next);
        mActionNext.setOnClickListener(this);

        mImgBack = (ImageView) mParentView.findViewById(R.id.image_back);
        mImgBack.setOnClickListener(this);
        mImgError = (ImageView) mParentView.findViewById(R.id.image_error);
        mTextEror = (TextView) mParentView.findViewById(R.id.text_error);
        mImgPhoneLabel = (ImageView) mParentView.findViewById(R.id.img_phone_label);
        mImgVercodeLabel = (ImageView) mParentView.findViewById(R.id.img_vercode_label);
        mImgPhoneDelete = (ImageView) mParentView.findViewById(R.id.img_phone_del);
        mImgPhoneDelete.setOnClickListener(this);
        mImgVercodeDelete = (ImageView) mParentView.findViewById(R.id.img_vercode_delete);
        mImgVercodeDelete.setOnClickListener(this);

        mLayoutPhone = (LinearLayout) mParentView.findViewById(R.id.ly_phone);
        mLayoutVercode = (LinearLayout) mParentView.findViewById(R.id.ly_vercode);
        return mParentView;
    }

    private void setError(String str, int type) {
        mImgError.setVisibility(View.VISIBLE);
        mTextEror.setVisibility(View.VISIBLE);
        mTextEror.setText(str);
        if (type == AppUtils.TYPE_PHONENUMBER) {
            Log.d("test", "setError: TYPE_PHONENUMBER");
            mLayoutPhone.setBackground(getImage(R.drawable.edit_error_border));
        } else if (type == AppUtils.TYPE_VERCODE) {
            Log.d("test", "setError: TYPE_VERCODE");
            mLayoutVercode.setBackground(getImage(R.drawable.edit_error_border));
        }
    }

    private void setSuccess() {
        mLayoutPhone.setBackground(getImage(R.drawable.edit_input_bg));
        mLayoutVercode.setBackground(getImage(R.drawable.edit_input_bg));
        mImgError.setVisibility(View.INVISIBLE);
        mTextEror.setVisibility(View.INVISIBLE);
        mTextEror.setText("");
    }

    @Override
    public void onClick(View view) {
        if (!((getActivity() instanceof RegisterContract.RegisterView))) return;
        RegisterContract.RegisterView registerView = (RegisterContract.RegisterView) getActivity();
        String phoneNumber = mEditPhoneNumber.getText().toString();
        switch (view.getId()) {
            case R.id.send_vercode:
                if (StringUtils.isMobileNO(phoneNumber)) {
                    registerView.sendVercode(RequestUri.TYPE_TEL, phoneNumber);
                    mEditVercode.requestFocus();
                } else {
                    registerView.showAction(R.string.toast_please_input_correct_phone_number);
                }
                break;
            case R.id.action_next:
                if (!StringUtils.isMobileNO(phoneNumber)) {
                    registerView.showAction(R.string.toast_please_input_correct_phone_number);
                    return;
                }
                String vercode = mEditVercode.getText().toString();
                if (!registerView.verifyCode(RequestUri.TYPE_TEL, phoneNumber, vercode)) {
                    registerView.showAction(R.string.toast_vercode_error);
                    Log.d("test", "onClick: seterror" + AppUtils.TYPE_VERCODE);
                    setError(getString(R.string.toast_vercode_error), AppUtils.TYPE_VERCODE);
                    return;
                }
                registerView.setPhoneNumber(mEditPhoneNumber.getText().toString());
                registerView.setVercode(vercode);
                SetPwdFragment setPwdFragment = new SetPwdFragment();
                registerView.nextStep(setPwdFragment);
                break;
            case R.id.img_phone_del:
                mEditPhoneNumber.setText("");
                break;
            case R.id.img_vercode_delete:
                mEditVercode.setText("");
                break;
            case R.id.image_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private Drawable getImage(int resid) {
        return getActivity().getDrawable(resid);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.et_login_name:
                mImgPhoneLabel.setImageDrawable(b ? getImage(R.drawable.ic_label_phonenumber_pressed) : getImage(R.drawable.ic_label_phonenumber));
                if (!b) {
                    mImgPhoneDelete.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.et_vercode:
                mImgVercodeLabel.setImageDrawable(b ? getImage(R.drawable.ic_label_vercode_pressed) : getImage(R.drawable.ic_label_vercode));
                if (!b) {
                    mImgVercodeDelete.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setSuccess();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (mEditPhoneNumber.hasFocus())
            mImgPhoneDelete.setVisibility(StringUtils.isNotEmpty(mEditPhoneNumber) ? View.VISIBLE : View.INVISIBLE);
        if (mEditVercode.hasFocus())
            mImgVercodeDelete.setVisibility(StringUtils.isNotEmpty(mEditVercode) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mActionNext.setEnabled((StringUtils.isNotEmpty(mEditPhoneNumber)));

    }
}
