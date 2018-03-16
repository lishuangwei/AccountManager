package com.android.accountmanager.ui.common;

import android.content.Context;
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
import com.android.accountmanager.base.BaseView;
import com.android.accountmanager.ui.account.AccountContract;
import com.android.accountmanager.ui.register.RegisterContract;
import com.android.accountmanager.utils.StringUtils;

/**
 * Created by fantao on 18-1-18.
 */

public class SetPwdFragment extends BaseFragment implements View.OnClickListener, TextWatcher, View.OnFocusChangeListener {
    private TextInputEditText mEditPassword, mEditConfirmPassword;
    private View mActionConfirm, mFormatSizeIcon, mFormatSizeText, mFormatComplexIcon, mFormatComplexText;
    private ImageView mImgeback, mImgNewDelete, mImgConfirmDelete, mImgNewLabel, mImgCfmLabel;
    private TextView mTextError;
    private LinearLayout mConfirmLay;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_setpassword, container, false);
        mImgeback = (ImageView) parentView.findViewById(R.id.image_back);
        mImgeback.setOnClickListener(mClick);
        mImgNewDelete = (ImageView) parentView.findViewById(R.id.img_new_password_del);
        mImgNewDelete.setOnClickListener(mClick);
        mImgConfirmDelete = (ImageView) parentView.findViewById(R.id.img_confirm_password_del);
        mImgConfirmDelete.setOnClickListener(mClick);
        mImgNewLabel = (ImageView) parentView.findViewById(R.id.img_new_lebel);
        mImgCfmLabel = (ImageView) parentView.findViewById(R.id.img_cfm_lebel);
        mConfirmLay = (LinearLayout) parentView.findViewById(R.id.confirm_layout);

        mEditPassword = (TextInputEditText) parentView.findViewById(R.id.et_password);
        mEditPassword.addTextChangedListener(this);
        mEditPassword.setOnFocusChangeListener(this);
        mEditPassword.requestFocus();
        mEditConfirmPassword = (TextInputEditText) parentView.findViewById(R.id.et_confirm_password);
        mEditConfirmPassword.setOnFocusChangeListener(this);
        mEditConfirmPassword.addTextChangedListener(this);
        mActionConfirm = parentView.findViewById(R.id.action_ok);
        mActionConfirm.setOnClickListener(this);
        mActionConfirm.setEnabled(false);
        mFormatSizeIcon = parentView.findViewById(R.id.format_size_icon);
        mFormatSizeText = parentView.findViewById(R.id.format_size_text);
        mFormatComplexIcon = parentView.findViewById(R.id.format_complex_icon);
        mFormatComplexText = parentView.findViewById(R.id.format_complex_text);
        mTextError = (TextView) parentView.findViewById(R.id.text_input_error);
        return parentView;
    }

    private void setError(String str) {
        mTextError.setText(str);
        mTextError.setVisibility(View.VISIBLE);
        if (mEditConfirmPassword.hasFocus())
            mConfirmLay.setBackground(getActivity().getDrawable(R.drawable.edit_error_border));
    }

    private void setSuccess() {
        mTextError.setVisibility(View.INVISIBLE);
        mConfirmLay.setBackground(null);
    }

    View.OnClickListener mClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_back:
                    getActivity().onBackPressed();
                    break;
                case R.id.img_new_password_del:
                    mEditPassword.setText("");
                    break;
                case R.id.img_confirm_password_del:
                    mEditConfirmPassword.setText("");
                    break;
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setSuccess();
    }

    @Override
    public void onClick(View view) {
        final String password = mEditPassword.getText().toString();
        String confirmPassword = mEditConfirmPassword.getText().toString();
        Bundle args = getArguments();
        final String type = args == null ? null : args.getString("type");
        final String identifier = args == null ? null : args.getString("identifier");
        final String vercode = args == null ? null : args.getString("vercode");
        if (!(getActivity() instanceof BaseView)) return;
        final BaseView baseView = (BaseView) getActivity();
        if (!StringUtils.isPasswordRegular(password)) {
            baseView.showAction(R.string.toast_password_format);
            return;
        }
        if (!StringUtils.isPasswordSame(password, confirmPassword)) {
            baseView.showAction(R.string.toast_password_not_equals);
            setError(getString(R.string.text_password_not_equals));
            return;
        }
        if (baseView instanceof ResetPwdContract.ResetPasswordView) {
            ResetPwdContract.ResetPasswordView resetPasswordView = (ResetPwdContract.ResetPasswordView) baseView;
            resetPasswordView.resetPassword(type, identifier, password, vercode);
        } else if (baseView instanceof RegisterContract.RegisterView) {
            RegisterContract.RegisterView registerView = (RegisterContract.RegisterView) getActivity();
            registerView.setPassword(password);
            registerView.register();
        } else if (baseView instanceof AccountContract.AccountView) {
            AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
            accountView.setModifyPassWord(password);
            accountView.modifyPassword(password);
        }

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        Log.d("test", "onTextChanged: " + charSequence);
        boolean sizeOk = StringUtils.isPasswordRegular(charSequence.toString());
        boolean complexOk = StringUtils.isPasswordComplex(charSequence.toString());
        mFormatSizeIcon.setSelected(sizeOk);
        mFormatSizeText.setSelected(sizeOk);
        mFormatComplexIcon.setSelected(complexOk);
        mFormatComplexText.setSelected(complexOk);
        mActionConfirm.setEnabled(sizeOk & complexOk);
    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (mEditPassword.hasFocus())
            mImgNewDelete.setVisibility(StringUtils.isNotEmpty(mEditPassword) ? View.VISIBLE : View.INVISIBLE);
        if (mEditConfirmPassword.hasFocus())
            mImgConfirmDelete.setVisibility(StringUtils.isNotEmpty(mEditConfirmPassword) ? View.VISIBLE : View.INVISIBLE);
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        switch (view.getId()) {
            case R.id.et_password:
                mImgNewLabel.setImageDrawable(b ? getImage(R.drawable.ic_label_password_pressed) : getImage(R.drawable.ic_label_password));
                if (!b) {
                    mImgNewDelete.setVisibility(View.INVISIBLE);
                } else {
                    mConfirmLay.setBackground(null);
                }
                break;
            case R.id.et_confirm_password:
                mImgCfmLabel.setImageDrawable(b ? getImage(R.drawable.ic_label_password_pressed) : getImage(R.drawable.ic_label_password));
                if (!b) {
                    mImgConfirmDelete.setVisibility(View.INVISIBLE);
                }
                break;
        }
    }

    private Drawable getImage(int resid) {
        return getActivity().getDrawable(resid);
    }

}
