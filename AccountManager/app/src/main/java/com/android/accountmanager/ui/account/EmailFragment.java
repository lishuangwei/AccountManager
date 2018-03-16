package com.android.accountmanager.ui.account;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by fantao on 18-1-18.
 */

public class EmailFragment extends BaseInfoFragment {
    private TextView mMailBox;
    private View mActionUnbind, mActionChangeMail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_mailbox, null);
        mMailBox = (TextView) parentView.findViewById(R.id.tv_mailbox);
        mMailBox.setText(AppUtils.getAccountSharedPreferences(getActivity()).getString(UserInfoTemplate.KEY_ACCOUNT_TEL, ""));
        mActionUnbind = parentView.findViewById(R.id.action_unbind);
        mActionUnbind.setOnClickListener(this);
        mActionChangeMail = parentView.findViewById(R.id.action_change_mail);
        mActionChangeMail.setOnClickListener(this);
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
                verifyPwdFragment.setListener(new VerifyPwdFragment.Listener() {
                    @Override
                    public void onVerifyComplete(boolean success) {
                        if (!(getActivity() instanceof AccountContract.AccountView)) return;
                        AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                        if (success) {
                            accountView.unBindMailbox();
                            getActivity().finish();
                        } else {
                            accountView.showAction(R.string.toast_password_error);
                        }
                    }
                });
                break;
            case R.id.action_change_mail:
                verifyPwdFragment.setListener(new VerifyPwdFragment.Listener() {
                    @Override
                    public void onVerifyComplete(boolean success) {
                        if (!(getActivity() instanceof AccountContract.AccountView)) return;
                        AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                        if (success) {
                            Bundle args = new Bundle();
                            args.putString("title", getString(R.string.action_change_mail));
                            accountView.onStartFragment("com.android.accountmanager.ui.account.EmailFragment$EmailBindFragment", args);
                        } else {
                            accountView.showAction(R.string.toast_password_error);
                        }
                    }
                });
                break;
        }
        verifyPwdFragment.show(getActivity().getSupportFragmentManager(),
                getString(R.string.title_verify_password));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        super.onSharedPreferenceChanged(sharedPreferences, key);
        if (UserInfoTemplate.KEY_ACCOUNT_EMAIL.equals(key)) {
            mMailBox.setText(sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_EMAIL, ""));
        }
    }

    public static class EmailBindFragment extends BaseInfoFragment implements View.OnFocusChangeListener, TextWatcher {
        private TextInputEditText mEditMailbox, mEditVercode;
        private TextView mActionSendVercode;
        private View mActionComplete;
        private ImageView mImgMailboxlabel, mImgVercodelabel, mImgMailboxDelete, mImgVercodeDelete;
        private ImageView mWarnImage, mImgback;
        private TextView mWarnText;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View parentView = inflater.inflate(R.layout.fragment_bind_mailbox, null);
            mEditMailbox = (TextInputEditText) parentView.findViewById(R.id.et_mailbox);
            mEditMailbox.setOnFocusChangeListener(this);
            mEditMailbox.addTextChangedListener(this);
            mEditVercode = (TextInputEditText) parentView.findViewById(R.id.et_vercode);
            mEditVercode.setOnFocusChangeListener(this);
            mEditVercode.addTextChangedListener(this);
            mActionSendVercode = (TextView) parentView.findViewById(R.id.send_vercode);
            mActionSendVercode.setOnClickListener(this);
            mActionComplete = parentView.findViewById(R.id.action_complete);
            mActionComplete.setOnClickListener(this);

            mImgMailboxlabel = (ImageView) parentView.findViewById(R.id.label_mailbox);
            mImgVercodelabel = (ImageView) parentView.findViewById(R.id.label_vercode);
            mImgMailboxDelete = (ImageView) parentView.findViewById(R.id.mailbox_delete);
            mImgMailboxDelete.setOnClickListener(this);
            mImgVercodeDelete = (ImageView) parentView.findViewById(R.id.vercode_delete);
            mImgVercodeDelete.setOnClickListener(this);
            mWarnImage = (ImageView) parentView.findViewById(R.id.warn_image);
            mWarnText = (TextView) parentView.findViewById(R.id.warn_text);
            mImgback = (ImageView) parentView.findViewById(R.id.image_back);
            mImgback.setOnClickListener(this);
            return parentView;
        }

        private void setError(String str) {
            mWarnImage.setVisibility(View.VISIBLE);
            mWarnImage.setImageDrawable(getImage(R.drawable.ic_error));
            mWarnText.setVisibility(View.VISIBLE);
            mWarnText.setText(str);
            mWarnText.setTextColor(getResources().getColor(R.color.error_text_color));
        }

        public void setSuccess(String str) {
            mWarnImage.setImageDrawable(getImage(R.drawable.ic_input_correct));
            mWarnText.setText(str);
            mWarnText.setTextColor(getResources().getColor(R.color.text_green));
        }

        @Override
        public void onClick(View view) {
            super.onClick(view);
            if (!(getActivity() instanceof AccountContract.AccountView)) {
                return;
            }
            AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
            String mailbox = mEditMailbox.getText().toString();
            String vercode = mEditVercode.getText().toString();
            switch (view.getId()) {
                case R.id.action_complete:
                    if (!StringUtils.isMailboxRegular(mailbox)) {
                        accountView.showAction(R.string.toast_please_input_correct_mailbox);
                        setError(getString(R.string.toast_please_input_correct_mailbox));
                        return;
                    }
                    if (!accountView.verifyCode(RequestUri.TYPE_EMAIL, mailbox, vercode)) {
                        accountView.showAction(R.string.toast_vercode_error);
                        setError(getString(R.string.toast_vercode_error));
                        return;
                    }
                    accountView.bindMailbox(mailbox);
                    getActivity().finish();
                    break;
                case R.id.send_vercode:
                    if (StringUtils.isMailboxRegular(mailbox)) {
                        accountView.sendVercode(RequestUri.TYPE_EMAIL, mailbox);
                        mEditVercode.requestFocus();
                    } else {
                        accountView.showAction(R.string.toast_please_input_correct_mailbox);
                        setError(getString(R.string.toast_please_input_correct_mailbox));
                    }
                    break;
                case R.id.mailbox_delete:
                    mEditMailbox.setText("");
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
                case R.id.et_mailbox:
                    mImgMailboxlabel.setImageDrawable(b ? getImage(R.drawable.ic_label_mailbox_pressed) : getImage(R.drawable.ic_label_mailbox));
                    if (!b) {
                        mImgMailboxDelete.setVisibility(View.INVISIBLE);
                    }
                    break;
                case R.id.et_vercode:
                    mImgVercodelabel.setImageDrawable(b ? getImage(R.drawable.ic_label_vercode_pressed) : getImage(R.drawable.ic_label_vercode));
                    if (!b) {
                        mImgVercodeDelete.setVisibility(View.INVISIBLE);
                    }
                    break;
            }
        }

        private Drawable getImage(int resid) {
            return getActivity().getDrawable(resid);
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if (mEditMailbox.hasFocus())
                mImgMailboxDelete.setVisibility(StringUtils.isNotEmpty(mEditMailbox) ? View.VISIBLE : View.INVISIBLE);
            if (mEditVercode.hasFocus())
                mImgVercodeDelete.setVisibility(StringUtils.isNotEmpty(mEditVercode) ? View.VISIBLE : View.INVISIBLE);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
