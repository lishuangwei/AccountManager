package com.android.accountmanager.ui.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private View mActionUnbind, mActionChangePhone;

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
        return parentView;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Bundle args = new Bundle();
        args.putString("title", getString(R.string.title_verify_password));
        VerifyPwdFragment verifyPwdFragment = new VerifyPwdFragment();
        verifyPwdFragment.setArguments(args);
        switch (view.getId()) {
            case R.id.action_unbind:
                break;
            case R.id.action_change_phone:
                if (!(getActivity() instanceof AccountContract.AccountView)) return;
                AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                Bundle arg = new Bundle();
                args.putString("title", getString(R.string.action_change_phone));
                accountView.onStartFragment(
                        "com.android.accountmanager.ui.account.PhoneNumberFragment$PhoneNumberBindFragment", arg);
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

    public static class PhoneNumberBindFragment extends BaseInfoFragment {
        private TextInputEditText mEditPhoneNumber, mEditVercode;
        private TextView mActionSendVercode;
        private View mActionComplete;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View parentView = inflater.inflate(R.layout.fragment_bind_phonenumber, null);
            mEditPhoneNumber = (TextInputEditText) parentView.findViewById(R.id.et_login_name);
            mEditVercode = (TextInputEditText) parentView.findViewById(R.id.et_vercode);
            mActionSendVercode = (TextView) parentView.findViewById(R.id.send_vercode);
            mActionSendVercode.setOnClickListener(this);
            mActionComplete = parentView.findViewById(R.id.action_complete);
            mActionComplete.setOnClickListener(this);
            return parentView;
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
                        return;
                    }
                    if (!accountView.verifyCode(RequestUri.TYPE_TEL, phoneNumber, vercode)) {
                        accountView.showAction(R.string.toast_vercode_error);
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
                    }
                    break;
            }
        }
    }
}
