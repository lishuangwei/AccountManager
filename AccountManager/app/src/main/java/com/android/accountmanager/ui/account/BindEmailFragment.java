package com.android.accountmanager.ui.account;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.accountmanager.R;
import com.android.accountmanager.base.BaseFragment;
import com.android.accountmanager.commom.RequestUri;
import com.android.accountmanager.model.LoginTemplate;
import com.android.accountmanager.utils.AppUtils;
import com.android.accountmanager.utils.StringUtils;

public class BindEmailFragment extends BaseFragment implements View.OnClickListener, TextWatcher {
    private TextInputEditText mEditName, mEditPassword;
    private CompoundButton mSwitchShowPassword;
    private Button mNextStep;
    private TextView mForgetPassword;
    private TextView mTextCurrentTel;
    private ImageView mImageback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_reset_input, container, false);
        mEditName = (TextInputEditText) parentView.findViewById(R.id.et_login_name);
        mEditPassword = (TextInputEditText) parentView.findViewById(R.id.et_password);
        mEditPassword.requestFocus();
        mEditPassword.addTextChangedListener(this);
        mNextStep = (Button) parentView.findViewById(R.id.action_next);
        mNextStep.setEnabled(false);
        mNextStep.setOnClickListener(this);

        mForgetPassword = (TextView) parentView.findViewById(R.id.action_forget_pwd);
        mForgetPassword.setOnClickListener(this);
        mSwitchShowPassword = (CompoundButton) parentView.findViewById(R.id.sw_show_password);
        mSwitchShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEditPassword.setInputType(b ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });

        mTextCurrentTel = (TextView) parentView.findViewById(R.id.text_current_tel);
        mImageback = (ImageView) parentView.findViewById(R.id.image_back);
        mImageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        LoginTemplate.DataBean.UserinfoBean info = AppUtils.getCurrentAccount(getActivity());
        if (AppUtils.isLogined(getActivity())) {
            mTextCurrentTel.setText(info.getTel());
            mTextCurrentTel.setEnabled(false);
        }

        return parentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {
        if (!(getActivity() instanceof AccountContract.AccountView)) {
            return;
        }
        AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
        String password = mEditPassword.getText().toString();
        String name = mEditName.getText().toString();
        switch (view.getId()) {
            case R.id.action_forget_pwd:
                accountView.startForgetPassword();
                break;
            case R.id.action_next:
                Log.d("test", "onClick:login next ");
                accountView.setModifyName(name);
                accountView.setModifyPassWord(password);
                accountView.login(RequestUri.TYPE_PASSWORD, name, password, true);
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        boolean sizeOk = StringUtils.isPasswordRegular(charSequence.toString());
        boolean complexOk = StringUtils.isPasswordComplex(charSequence.toString());
        mNextStep.setEnabled(sizeOk & complexOk);
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
