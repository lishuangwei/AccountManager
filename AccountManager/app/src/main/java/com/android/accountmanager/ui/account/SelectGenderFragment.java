package com.android.accountmanager.ui.account;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.accountmanager.R;

public class SelectGenderFragment extends BaseDialogFragment implements RadioGroup.OnCheckedChangeListener {
    private int mSelectWhich = -1;
    private TextView mTextTitle;
    private RadioGroup mRadiogroup;
    private RadioButton mRadioMan, mRadioFeman;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) LayoutInflater.from(getActivity())
                .inflate(R.layout.custon_setgender_layout, null);
        mTextTitle = (TextView) layout.findViewById(R.id.text_title);
        mRadiogroup = (RadioGroup) layout.findViewById(R.id.radio_group);
        mRadiogroup.setOnCheckedChangeListener(this);
        mRadioMan = (RadioButton) layout.findViewById(R.id.radio_man);
        mRadioFeman = (RadioButton) layout.findViewById(R.id.radio_feman);

        String[] choices = {getString(R.string.item_male), getString(R.string.item_female)};
        Bundle args = getArguments();
        String title = args.getString("title");
        String summary = args.getString("summary");
        setDefaultSummary(summary);
        mTextTitle.setText(title);

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

    private void setDefaultSummary(String str) {
        if (str.equals(getString(R.string.item_male))) {
            mRadioMan.setChecked(true);
            mSelectWhich = 0;
        } else if (str.equals(getString(R.string.item_female))) {
            mRadioFeman.setChecked(true);
            mSelectWhich = 1;
        } else {
            mRadioFeman.setChecked(false);
            mRadioMan.setChecked(false);
            mSelectWhich = -1;
        }

    }

    private void updateSex(int choice) {
        if (!(getActivity() instanceof AccountContract.AccountView)) return;
        final AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
        if (choice != mSelectWhich) {
            accountView.onSexUpdate(choice);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.radio_man:
                Log.d("test", "onCheckedChanged: man");
                updateSex(0);
                if (null != getDialog() && getDialog().isShowing()) getDialog().dismiss();
                break;
            case R.id.radio_feman:
                Log.d("test", "onCheckedChanged: feman");
                updateSex(1);
                if (null != getDialog() && getDialog().isShowing()) getDialog().dismiss();
                break;
        }
    }
}