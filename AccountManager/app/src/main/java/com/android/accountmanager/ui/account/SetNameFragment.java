package com.android.accountmanager.ui.account;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.accountmanager.R;

public class SetNameFragment extends BaseDialogFragment implements View.OnClickListener {
    private TextView mTile;
    private EditText mEditName;
    private Button mBtCancel, mBtOk;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        String title = args.getString("title");
        Log.d("test", "onCreateDialog: title" + title);
        String summary = args.getString("summary");
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_edit_text, null);
        mTile = (TextView) layout.findViewById(R.id.text_title);
        mEditName = (EditText) layout.findViewById(R.id.edittext_name);
        mBtCancel = (Button) layout.findViewById(R.id.bt_cancel);
        mBtCancel.setOnClickListener(this);
        mBtOk = (Button) layout.findViewById(R.id.bt_ok);
        mBtOk.setOnClickListener(this);

        mEditName.setText(summary);
        mTile.setText(title);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.UserInfoDialogStyle);
        builder.setView(layout);
        builder.setCancelable(true);
        return builder.create();
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
            case R.id.bt_ok:
                if (!(getActivity() instanceof AccountContract.AccountView)) return;
                ((AccountContract.AccountView) getActivity())
                        .onNameUpdate(mEditName.getText().toString());
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
        }
    }
}