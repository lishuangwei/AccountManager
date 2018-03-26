package com.android.accountmanager.ui.account;

import android.content.SharedPreferences;
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
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by fantao on 18-1-18.
 */

public class PhoneNumberFragment extends BaseInfoFragment {
    private TextView mPhoneNumber;
    private View mActionUnbind, mActionChangePhone, mImgback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_phonenumber, null);
        mPhoneNumber = (TextView) parentView.findViewById(R.id.tv_phone_number);
        mPhoneNumber.setText(AppUtils.getAccountSharedPreferences(getActivity()).getString(UserInfoTemplate.KEY_ACCOUNT_TEL, ""));
        mActionUnbind = parentView.findViewById(R.id.action_unbind);
        mActionUnbind.setOnClickListener(this);
        mActionChangePhone = parentView.findViewById(R.id.action_change_phone);
        mActionChangePhone.setOnClickListener(this);
        mImgback = parentView.findViewById(R.id.image_back);
        mImgback.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.action_unbind:
                break;
            case R.id.action_change_phone:
                if (!(getActivity() instanceof AccountContract.AccountView)) return;
                AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                Bundle arg = new Bundle();
                arg.putString("title", getString(R.string.action_change_phone));
                arg.putInt("flag", AppUtils.TYPE_CHANGE_PHONE);
//                accountView.onStartFragment(
//                        "com.android.accountmanager.ui.account.PhoneNumberFragment$PhoneNumberBindFragment", arg);
                accountView.startFragmentNew("com.android.accountmanager.ui.account.ModifyPasswordFragment", arg);
                break;
            case R.id.image_back:
                getActivity().onBackPressed();
                break;
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);
        if (UserInfoTemplate.KEY_ACCOUNT_TEL.equals(key)) {
            mPhoneNumber.setText(sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_TEL, ""));
        }
    }

    public static class PhoneNumberBindFragment extends BaseInfoFragment implements View.OnFocusChangeListener, TextWatcher {
        private TextInputEditText mEditPhoneNumber, mEditVercode;
        private TextView mActionSendVercode, mTextError;
        private View mActionComplete;
        private ImageView mImgback, mImgError, mImgPhoneLabel, mImgVercodeLabel, mImgPhoneDel, mImgVercodeDel;
        private LinearLayout mLayoutPhone, mLayoutVercode;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View parentView = inflater.inflate(R.layout.fragment_bind_phonenumber, null);
            mEditPhoneNumber = (TextInputEditText) parentView.findViewById(R.id.et_login_name);
            mEditPhoneNumber.setOnFocusChangeListener(this);
            mEditPhoneNumber.addTextChangedListener(this);
            mEditVercode = (TextInputEditText) parentView.findViewById(R.id.et_vercode);
            mEditVercode.setOnFocusChangeListener(this);
            mEditVercode.addTextChangedListener(this);
            mActionSendVercode = (TextView) parentView.findViewById(R.id.send_vercode);
            mActionSendVercode.setOnClickListener(this);
            mActionComplete = parentView.findViewById(R.id.action_complete);
            mActionComplete.setOnClickListener(this);
            mImgback = (ImageView) parentView.findViewById(R.id.image_back);
            mImgback.setOnClickListener(this);

            mTextError = (TextView) parentView.findViewById(R.id.text_error);
            mImgError = (ImageView) parentView.findViewById(R.id.image_error);
            mImgPhoneLabel = (ImageView) parentView.findViewById(R.id.img_phone_label);
            mImgVercodeLabel = (ImageView) parentView.findViewById(R.id.img_vercode_label);
            mImgPhoneDel = (ImageView) parentView.findViewById(R.id.img_phone_del);
            mImgPhoneDel.setOnClickListener(this);
            mImgVercodeDel = (ImageView) parentView.findViewById(R.id.img_vercode_delete);
            mImgVercodeDel.setOnClickListener(this);
            mLayoutPhone = (LinearLayout) parentView.findViewById(R.id.ly_phone);
            mLayoutVercode = (LinearLayout) parentView.findViewById(R.id.ly_vercode);

            return parentView;
        }

        private void setError(String str, int type) {
            mImgError.setVisibility(View.VISIBLE);
            mTextError.setVisibility(View.VISIBLE);
            mTextError.setText(str);
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
            mTextError.setVisibility(View.INVISIBLE);
            mTextError.setText("");
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            if (!(getActivity() instanceof AccountContract.AccountView)) {
                return;
            }
            AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
            String phoneNumber = mEditPhoneNumber.getText().toString();
            String vercode = mEditVercode.getText().toString();
            switch (view.getId()) {
                case R.id.action_complete:
                    if (!StringUtils.isMobileNO(phoneNumber)) {
                        accountView.showAction(R.string.toast_please_input_correct_phone_number);
                        setError(getString(R.string.toast_please_input_correct_phone_number), AppUtils.TYPE_PHONENUMBER);
                        return;
                    }
                    if (!accountView.verifyCode(RequestUri.TYPE_TEL, phoneNumber, vercode)) {
                        accountView.showAction(R.string.toast_vercode_error);
                        setError(getString(R.string.toast_vercode_error), AppUtils.TYPE_VERCODE);
                        return;
                    }
                    accountView.bindPhoneNumber(phoneNumber, vercode);
                    getActivity().finish();
                    break;
                case R.id.send_vercode:
                    if (StringUtils.isMobileNO(phoneNumber)) {
                        accountView.sendVercode(RequestUri.TYPE_TEL, phoneNumber);
                        mEditVercode.requestFocus();
                    } else {
                        accountView.showAction(R.string.toast_please_input_correct_phone_number);
                        setError(getString(R.string.toast_please_input_correct_phone_number), AppUtils.TYPE_PHONENUMBER);
                    }
                    break;
                case R.id.image_back:
                    getActivity().onBackPressed();
                    break;
                case R.id.img_phone_del:
                    mEditPhoneNumber.setText("");
                    break;
                case R.id.img_vercode_delete:
                    mEditVercode.setText("");
                    break;
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            setSuccess();
        }

        private Drawable getImage(int resid) {
            return getActivity().getDrawable(resid);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEditPhoneNumber.hasFocus())
                mImgPhoneDel.setVisibility(StringUtils.isNotEmpty(mEditPhoneNumber) ? View.VISIBLE : View.INVISIBLE);
            if (mEditVercode.hasFocus())
                mImgVercodeDel.setVisibility(StringUtils.isNotEmpty(mEditVercode) ? View.VISIBLE : View.INVISIBLE);

        }

        @Override
        public void afterTextChanged(Editable editable) {
            mActionComplete.setEnabled((StringUtils.isNotEmpty(mEditPhoneNumber)));

        }

        @Override
        public void onFocusChange(View view, boolean b) {
            switch (view.getId()) {
                case R.id.et_login_name:
                    mImgPhoneLabel.setImageDrawable(b ? getImage(R.drawable.ic_label_phonenumber_pressed) : getImage(R.drawable.ic_label_phonenumber));
                    if (!b) {
                        mImgPhoneDel.setVisibility(View.INVISIBLE);
                        mLayoutPhone.setBackground(getImage(R.drawable.edit_input_bg));
                    }
                    break;
                case R.id.et_vercode:
                    mImgVercodeLabel.setImageDrawable(b ? getImage(R.drawable.ic_label_vercode_pressed) : getImage(R.drawable.ic_label_vercode));
                    if (!b) {
                        mImgVercodeDel.setVisibility(View.INVISIBLE);
                        mLayoutVercode.setBackground(getImage(R.drawable.edit_input_bg));
                    }
                    break;
            }

        }
    }
}
