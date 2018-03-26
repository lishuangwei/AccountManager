package com.android.accountmanager.ui.account;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.ui.common.ResetPwdActivity;
import com.android.accountmanager.utils.AppUtils;

public class VerifyPwdFragment extends BaseDialogFragment implements View.OnClickListener {
    private Listener mListener;
    private TextView mTile, mTextError, mTextForget;
    private EditText mEditName;
    private Button mBtCancel, mBtOk;
    private ToggleButton mBtswitch;

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
            case R.id.bt_ok:
                if (!(getActivity() instanceof AccountContract.AccountView)) return;
                AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                mListener.onVerifyComplete(accountView.verifyPassword(mEditName.getText().toString()));
                if (!accountView.verifyPassword(mEditName.getText().toString())) {
                    setError(getString(R.string.text_password_incorrect));
                } else {
                    setSuccess();
                    if (getDialog().isShowing()) getDialog().dismiss();
                }
                break;
            case R.id.action_forget_pwd:
                Intent intent = new Intent(getActivity(), ResetPwdActivity.class);
                startActivity(intent);
                if (getDialog().isShowing()) getDialog().dismiss();
                break;

        }
    }

    interface Listener {
        void onVerifyComplete(boolean success);
    }

    public void setListener(Listener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        final String title = args.getString("title");
//        final EditText editText = (EditText) LayoutInflater.from(getActivity())
//                .inflate(R.layout.custom_edit_text, null);
//        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//        AlertDialog.Builder builder =
//                new AlertDialog.Builder(getActivity());
//        builder.setView(editText).setTitle(title);
//        builder.setPositiveButton(android.R.string.ok,
//                new OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (!(getActivity() instanceof AccountContract.AccountView) || mListener == null) return;
//                        AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
//                        mListener.onVerifyComplete(accountView.verifyPassword(editText.getText().toString()));
//                    }
//                });
//        builder.setNegativeButton(android.R.string.cancel,
//                null);
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_loginout_layout, null);
        mTile = (TextView) layout.findViewById(R.id.text_title);
        mTile.setText(title);
        mEditName = (EditText) layout.findViewById(R.id.edit_password);
        mBtCancel = (Button) layout.findViewById(R.id.bt_cancel);
        mBtCancel.setOnClickListener(this);
        mBtOk = (Button) layout.findViewById(R.id.bt_ok);
        mBtOk.setOnClickListener(this);
        mBtswitch = (ToggleButton) layout.findViewById(R.id.sw_show_password);
        mBtswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mEditName.setInputType(b ? InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                        : InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }
        });
        mTextError = (TextView) layout.findViewById(R.id.error_text);
        mTextForget = (TextView) layout.findViewById(R.id.action_forget_pwd);
        mTextForget.setOnClickListener(this);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.UserInfoDialogStyle);
        builder.setCancelable(true);
        builder.setView(layout);
        return builder.create();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        setSuccess();
    }

    public void setError(String str) {
        mTextError.setVisibility(View.VISIBLE);
        mTextError.setText(str);
    }

    private void setSuccess() {
        mTextError.setVisibility(View.INVISIBLE);
        mTextError.setText("");
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.getDecorView().setPadding(0, 0, 0, 0);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = getActivity().getResources().getInteger(R.integer.dialog_photo_width);
        window.setAttributes(params);
    }
}