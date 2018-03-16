package com.android.accountmanager.ui.common;

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
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.account.BaseInfoFragment;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by fantao on 18-1-24.
 */

public class ResetPwdFragment extends BaseInfoFragment implements TextWatcher {
    private TextInputEditText mEditPhoneNumber, mEditMailbox, mEditVercode;
    private View mLyPhoneNumber, mLyMailbox;
    private TextView mActionVerifyWay, mActionSendVercode;
    private View mActionNext;

    private boolean mIsVerifyMailbox;
    private TextView mTextCurrentTel;
    private ImageView mVercodeDelete,mImgBack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_reset_password, null);
        LoginTemplate.DataBean.UserinfoBean info = AppUtils.getCurrentAccount(getActivity());
        mEditPhoneNumber = (TextInputEditText) parentView.findViewById(R.id.et_login_name);
        mEditMailbox = (TextInputEditText) parentView.findViewById(R.id.et_mailbox);
        mTextCurrentTel = (TextView) parentView.findViewById(R.id.text_current_tel);
        if (AppUtils.isLogined(getActivity())) {
            mTextCurrentTel.setText(info.getTel());
            mTextCurrentTel.setEnabled(false);
        }
        mEditVercode = (TextInputEditText) parentView.findViewById(R.id.et_vercode);
        mLyPhoneNumber = parentView.findViewById(R.id.ly_phone_number);
        mLyMailbox = parentView.findViewById(R.id.ly_mailbox);
        mActionVerifyWay = (TextView) parentView.findViewById(R.id.wy_verify);
        mActionVerifyWay.setOnClickListener(this);
        mActionSendVercode = (TextView) parentView.findViewById(R.id.send_vercode);
        mActionSendVercode.setOnClickListener(this);
        mActionNext = parentView.findViewById(R.id.action_next);
        mActionNext.setOnClickListener(this);
        mVercodeDelete = (ImageView) parentView.findViewById(R.id.vercode_delete);
        mVercodeDelete.setOnClickListener(this);
        mImgBack = (ImageView) parentView.findViewById(R.id.image_back);
        mImgBack.setOnClickListener(this);
        return parentView;
    }

    @Override
    public void onClick(View view) {
        if (!((getActivity() instanceof ResetPwdContract.ResetPasswordView))) return;
        ResetPwdContract.ResetPasswordView resetPasswordView = (ResetPwdContract.ResetPasswordView) getActivity();
        String phoneNumber = mEditPhoneNumber.getText().toString();
        String mailbox = mEditMailbox.getText().toString();
        switch (view.getId()) {
            case R.id.send_vercode:
                if (mIsVerifyMailbox) {
                    if (StringUtils.isMailboxRegular(mailbox)) {
                        resetPasswordView.sendVercode(RequestUri.TYPE_EMAIL, mailbox);
                        mEditVercode.requestFocus();
                    } else {
                        resetPasswordView.showAction(R.string.toast_please_input_correct_mailbox);
                    }
                } else {
                    if (StringUtils.isMobileNO(mTextCurrentTel.getText().toString())) {
                        resetPasswordView.sendVercode(RequestUri.TYPE_TEL, phoneNumber);
                        mEditVercode.requestFocus();
                    } else {
                        resetPasswordView.showAction(R.string.toast_please_input_correct_phone_number);
                    }
                }
                break;
            case R.id.action_next:
                if (mIsVerifyMailbox) {
                    if (!StringUtils.isMailboxRegular(mailbox)) {
                        resetPasswordView.showAction(R.string.toast_please_input_correct_mailbox);
                        return;
                    }
                } else {
                    if (!StringUtils.isMobileNO(mTextCurrentTel.getText().toString())) {
                        resetPasswordView.showAction(R.string.toast_please_input_correct_phone_number);
                        return;
                    }
                }
                String vercode = mEditVercode.getText().toString();
                if (!resetPasswordView.verifyCode(
                        mIsVerifyMailbox ? RequestUri.TYPE_EMAIL : RequestUri.TYPE_TEL,
                        mIsVerifyMailbox ? mailbox : phoneNumber, vercode)) {
                    resetPasswordView.showAction(R.string.toast_vercode_error);
                    return;
                }
                SetPwdFragment setPwdFragment = new SetPwdFragment();
                Bundle args = new Bundle();
                args.putString("type", mIsVerifyMailbox ? RequestUri.TYPE_EMAIL : RequestUri.TYPE_TEL);
                args.putString("identifier", mIsVerifyMailbox ? mailbox : phoneNumber);
                args.putString("vercode", vercode);
                setPwdFragment.setArguments(args);
                resetPasswordView.nextStep(setPwdFragment);
                break;
            case R.id.wy_verify:
                mIsVerifyMailbox = !mIsVerifyMailbox;
                mLyPhoneNumber.setVisibility(mIsVerifyMailbox ? View.GONE : View.VISIBLE);
                mLyMailbox.setVisibility(mIsVerifyMailbox ? View.VISIBLE : View.GONE);
                mEditVercode.setText("");
                mActionVerifyWay.setText(mIsVerifyMailbox ? R.string.verify_type_phone_number : R.string.verify_type_mailbox);
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        mVercodeDelete.setVisibility(StringUtils.isNotEmpty(mEditVercode) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
