package com.android.accountmanager.ui.account;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.android.accountmanager.R;

public class SetNetworkFragment extends BaseDialogFragment implements View.OnClickListener {
    private Button mBtCancel, mBtSetting;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_network_layout, null);
        mBtCancel = (Button) layout.findViewById(R.id.bt_cancel);
        mBtCancel.setOnClickListener(this);
        mBtSetting = (Button) layout.findViewById(R.id.bt_setting);
        mBtSetting.setOnClickListener(this);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity(), R.style.BottomDialog);
        builder.setCancelable(true);
        builder.setView(layout);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        window.getDecorView().setPadding(0, 0, 0, 0);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = getActivity().getResources().getInteger(R.integer.dialog_height);
        window.setAttributes(params);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_cancel:
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
            case R.id.bt_setting:
                Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                getActivity().startActivity(wifiSettingsIntent);
                getDialog().dismiss();
                break;
        }
    }
}