package com.android.accountmanager.ui.account;

import android.content.Context;
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

public class EmailFragment extends BaseInfoFragment {
    private TextView mMailBox;
    private View mActionUnbind, mActionChangeMail;
    private ImageView mImgback;

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
        mImgback = (ImageView) parentView.findViewById(R.id.image_back);
        mImgback.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.action_unbind:
                if (!(getActivity() instanceof AccountContract.AccountView)) return;
                AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                Bundle arg1 = new Bundle();
                arg1.putString("title", getString(R.string.title_unbind_mailbox));
                arg1.putInt("flag", AppUtils.TYPE_UNBIND_EMAIL);
                accountView.startFragmentNew("com.android.accountmanager.ui.account.ModifyPasswordFragment", arg1);
                break;
            case R.id.action_change_mail:
                if (!(getActivity() instanceof AccountContract.AccountView)) return;
                AccountContract.AccountView account = (AccountContract.AccountView) getActivity();
                Bundle arg2 = new Bundle();
                arg2.putString("title", getString(R.string.action_change_mail));
                account.startFragmentNew("com.android.accountmanager.ui.account.EmailFragment$EmailBindFragment", arg2);
                break;
            case R.id.image_back:
                getActivity().onBackPressed();
                break;
        }
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
        private TextView mWarnText, mTile;
        private LinearLayout mEmail, mVercode;
        private static EmailBindFragment mEmailBindFragment;

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
            mTile = (TextView) parentView.findViewById(R.id.custom_actionbar_title);
            mTile.setText(getArguments().getString("title"));
            mImgback = (ImageView) parentView.findViewById(R.id.image_back);
            mImgback.setOnClickListener(this);

            mEmail = (LinearLayout) parentView.findViewById(R.id.ly_email);
            mVercode = (LinearLayout) parentView.findViewById(R.id.ly_vercode);
            return parentView;
        }

        public static EmailBindFragment newInstance() {
            if (mEmailBindFragment == null) {
                mEmailBindFragment = new EmailBindFragment();
            }
            return mEmailBindFragment;
        }

        private void setError(String str, int type) {
            mWarnImage.setVisibility(View.VISIBLE);
            mWarnImage.setImageDrawable(getImage(R.drawable.ic_error));
            mWarnText.setVisibility(View.VISIBLE);
            mWarnText.setText(str);
            mWarnText.setTextColor(getResources().getColor(R.color.error_text_color));
            if (type == AppUtils.TYPE_EMAIL) {
                mEmail.setBackground(getImage(R.drawable.edit_error_border));
            } else if (type == AppUtils.TYPE_VERCODE) {
                mVercode.setBackground(getImage(R.drawable.edit_error_border));
            }
        }

        public void setSuccess(String str) {
            mEmail.setBackground(getImage(R.drawable.edit_input_bg));
            mVercode.setBackground(getImage(R.drawable.edit_input_bg));
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
                        setError(getString(R.string.toast_please_input_correct_mailbox), AppUtils.TYPE_EMAIL);
                        return;
                    }
                    if (!accountView.verifyCode(RequestUri.TYPE_EMAIL, mailbox, vercode)) {
                        accountView.showAction(R.string.toast_vercode_error);
                        setError(getString(R.string.toast_vercode_error), AppUtils.TYPE_VERCODE);
                        return;
                    }
                    accountView.bindMailbox(mailbox, vercode);
                    break;
                case R.id.send_vercode:
                    if (StringUtils.isMailboxRegular(mailbox)) {
                        accountView.sendVercode(RequestUri.TYPE_EMAIL, mailbox);
                        mEditVercode.requestFocus();
                    } else {
                        accountView.showAction(R.string.toast_please_input_correct_mailbox);
                        setError(getString(R.string.toast_please_input_correct_mailbox), AppUtils.TYPE_EMAIL);
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
                        mEmail.setBackground(getImage(R.drawable.edit_input_bg));
                    }
                    break;
                case R.id.et_vercode:
                    mImgVercodelabel.setImageDrawable(b ? getImage(R.drawable.ic_label_vercode_pressed) : getImage(R.drawable.ic_label_vercode));
                    if (!b) {
                        mImgVercodeDelete.setVisibility(View.INVISIBLE);
                        mVercode.setBackground(getImage(R.drawable.edit_input_bg));
                    }
                    break;
            }
        }

        @Override
        public void onDetach() {
            super.onDetach();
            mEmail.setBackground(getImage(R.drawable.edit_input_bg));
            mVercode.setBackground(getImage(R.drawable.edit_input_bg));
            mWarnImage.setVisibility(View.INVISIBLE);
            mWarnText.setVisibility(View.INVISIBLE);
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
