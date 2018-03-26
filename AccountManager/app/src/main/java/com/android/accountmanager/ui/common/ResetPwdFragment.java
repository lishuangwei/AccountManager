package com.android.accountmanager.ui.common;

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
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.account.AccountPresenter;
import com.android.accountmanager.ui.account.BaseInfoFragment;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by fantao on 18-1-24.
 */

public class ResetPwdFragment extends BaseInfoFragment implements TextWatcher, View.OnFocusChangeListener {
    private TextInputEditText mEditPhoneNumber, mEditMailbox, mEditVercode;
    private View mLyPhoneNumber, mLyMailbox;
    private TextView mActionVerifyWay, mActionSendVercode;
    private View mActionNext;

    private boolean mIsVerifyMailbox;
    private TextView mTextCurrentTel, mTextCurrentTellabel;
    private ImageView mVercodeDelete, mImgBack, mPhoneDelete, mImgPhoneicon, mImgVercodeIcon;
    private boolean isLogined;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_reset_password, null);
        LoginTemplate.DataBean.UserinfoBean info = AppUtils.getCurrentAccount(getActivity());
        mEditPhoneNumber = (TextInputEditText) parentView.findViewById(R.id.et_login_name);
        mEditPhoneNumber.addTextChangedListener(this);
        mEditPhoneNumber.setOnFocusChangeListener(this);
        mEditMailbox = (TextInputEditText) parentView.findViewById(R.id.et_mailbox);
        mTextCurrentTel = (TextView) parentView.findViewById(R.id.text_current_tel);
        mTextCurrentTellabel = (TextView) parentView.findViewById(R.id.text_current_tel_label);
        if (AppUtils.isLogined(getActivity())) {
            mTextCurrentTel.setText(info.getTel());
            mTextCurrentTel.setEnabled(false);
        }
        mEditVercode = (TextInputEditText) parentView.findViewById(R.id.et_vercode);
        mEditVercode.setOnFocusChangeListener(this);
        mEditVercode.addTextChangedListener(this);
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
        mPhoneDelete = (ImageView) parentView.findViewById(R.id.phone_delete);
        mPhoneDelete.setOnClickListener(this);
        mImgBack = (ImageView) parentView.findViewById(R.id.image_back);
        mImgBack.setOnClickListener(this);
        mImgPhoneicon = (ImageView) parentView.findViewById(R.id.img_phone_icon);
        mImgVercodeIcon = (ImageView) parentView.findViewById(R.id.img_vercode_icon);
        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (AppUtils.isLogined(getActivity())) {
            isLogined = true;
            mTextCurrentTel.setVisibility(View.VISIBLE);
            mTextCurrentTellabel.setVisibility(View.VISIBLE);
            mLyPhoneNumber.setVisibility(View.GONE);
            mEditVercode.requestFocus();
        } else {
            isLogined = false;
            mTextCurrentTel.setVisibility(View.GONE);
            mTextCurrentTellabel.setVisibility(View.GONE);
            mLyPhoneNumber.setVisibility(View.VISIBLE);
            mEditPhoneNumber.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        if (!((getActivity() instanceof ResetPwdContract.ResetPasswordView))) return;
        ResetPwdContract.ResetPasswordView resetPasswordView = (ResetPwdContract.ResetPasswordView) getActivity();
        String phoneNumber = mEditPhoneNumber.getText().toString();
        String mailbox = mEditMailbox.getText().toString();
        String realname = AppUtils.isLogined(getActivity()) ? mTextCurrentTel.getText().toString() : phoneNumber;
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
                args.putInt("type", AppUtils.TYPE_RESET_PASSWORD);
                args.putString("type", mIsVerifyMailbox ? RequestUri.TYPE_EMAIL : RequestUri.TYPE_TEL);
                args.putString("identifier", mIsVerifyMailbox ? mailbox : realname);
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
            case R.id.phone_delete:
                mEditPhoneNumber.setText("");
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
        if (mEditPhoneNumber.hasFocus())
            mPhoneDelete.setVisibility(StringUtils.isNotEmpty(mEditPhoneNumber) ? View.VISIBLE : View.INVISIBLE);
        if (mEditVercode.hasFocus())
            mVercodeDelete.setVisibility(StringUtils.isNotEmpty(mEditVercode) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mActionNext.setEnabled(isLogined ? StringUtils.isNotEmpty(mEditVercode) : StringUtils.isNotEmpty(mEditPhoneNumber));

    }

    private Drawable getImage(int resid) {
        return getActivity().getDrawable(resid);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.et_login_name:
                mImgPhoneicon.setImageDrawable(b ? getImage(R.drawable.ic_label_phonenumber_pressed) : getImage(R.drawable.ic_label_phonenumber));
                if (!b) {
                    mPhoneDelete.setVisibility(View.INVISIBLE);
                }
                break;
            case R.id.et_vercode:
                mImgVercodeIcon.setImageDrawable(b ? getImage(R.drawable.ic_label_vercode_pressed) : getImage(R.drawable.ic_label_vercode));
                if (!b) {
                    mVercodeDelete.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }
}
