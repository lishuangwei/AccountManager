package com.android.accountmanager.ui.account;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.DatePicker;
import android.widget.Toast;

/**
 * Created by fantao on 18-1-18.
 */

public class SetBirthdayFragment extends BaseDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                String birthday = String.format(getString(R.string.format_birthday), year, month + 1, day);
//                if (getActivity() instanceof AccountContract.AccountView) {
//                    ((AccountContract.AccountView) getActivity()).onBirthdayUpdate(birthday);
//                }
//            }
//        };
//        final Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);
//        int month = calendar.get(Calendar.MONTH);
//        int day = calendar.get(Calendar.DAY_OF_MONTH);
//        Bundle args = getArguments();
//        String summary = args.getString("summary");
//        if (!TextUtils.isEmpty(summary)) {
//            String[] dates = summary.split("-");
//            year = Integer.parseInt(dates[0]);
//            month = Integer.parseInt(dates[1]) - 1;
//            day = Integer.parseInt(dates[2]);
//        }

        return null;
    }
}