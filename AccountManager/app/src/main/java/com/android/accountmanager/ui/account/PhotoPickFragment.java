package com.android.accountmanager.ui.account;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.accountmanager.R;

public class PhotoPickFragment extends BaseDialogFragment implements View.OnClickListener {
    public static final String CAMERA_PERMISSION = android.Manifest.permission.CAMERA;
    private TextView mTextTakephoto, mTextPickPhoto, mTextCancel;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.custom_selectphoto_layout, null);
        mTextTakephoto = (TextView) layout.findViewById(R.id.take_photo);
        mTextTakephoto.setOnClickListener(this);
        mTextPickPhoto = (TextView) layout.findViewById(R.id.pick_photo);
        mTextPickPhoto.setOnClickListener(this);
        mTextCancel = (TextView) layout.findViewById(R.id.cancel);
        mTextCancel.setOnClickListener(this);

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
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Override
    public void onClick(View view) {
        if (!(getActivity() instanceof AccountContract.AccountView)) return;
        AccountActivity activity = (AccountActivity) getActivity();
        final PhotoSelectionHandler handler =
                ((AccountContract.AccountView) getActivity()).getPhotoSelectionHandler();
        switch (view.getId()) {
            case R.id.take_photo:
                if (!(activity.checkHasPermission(getActivity(), CAMERA_PERMISSION))) {
                    activity.requestPermission(CAMERA_PERMISSION);
                }
                try {
                    if (activity.checkHasPermission(getActivity(), CAMERA_PERMISSION))
                        handler.startTakePhotoActivity();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
            case R.id.pick_photo:
                try {
                    handler.startPickFromGalleryActivity();
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
            case R.id.cancel:
                if (getDialog().isShowing()) getDialog().dismiss();
                break;
        }

    }
}
