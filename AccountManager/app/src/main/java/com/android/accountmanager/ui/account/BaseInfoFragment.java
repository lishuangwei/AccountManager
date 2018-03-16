package com.android.accountmanager.ui.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.accountmanager.R;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.widget.ActionItemView;
import com.android.accountmanager.ui.widget.IconItemView;
import com.android.accountmanager.utils.StringUtils;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.io.FileNotFoundException;
import java.lang.reflect.Method;

/**
 * Created by fantao on 18-1-20.
 */

public class BaseInfoFragment extends Fragment implements OnSharedPreferenceChangeListener, View.OnClickListener {
    private SharedPreferences mSharedPreferences;
    private long mYears = 100L * 365 * 1000 * 60 * 60 * 24L;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSharedPreferences = getActivity().getApplication().getSharedPreferences("current_account", Context.MODE_PRIVATE);
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initActions();
    }

    private void initActions() {
        if (getView() == null) return;
        ViewGroup actionViews = (ViewGroup) getView().findViewById(R.id.actions);
        if (actionViews == null) return;
        for (int i = 0; i < actionViews.getChildCount(); i++) {
            View child = actionViews.getChildAt(i);
            if (child instanceof ActionItemView) {
                ActionItemView action = (ActionItemView) child;
                action.setOnClickListener(this);
                onBindActionItemView(action, mSharedPreferences);
            }
        }
    }

    protected void onActionItemClick(ActionItemView actionItemView) {
    }

    @Override
    public void onClick(View view) {
        if (view instanceof ActionItemView) {
            ActionItemView actionItem = (ActionItemView) view;
            String fragment = actionItem.getFragment();
            String fragmentTitle = actionItem.getFragmentTitle();
            String title = !TextUtils.isEmpty(fragmentTitle) ? fragmentTitle : actionItem.getTitle();
            String summary = actionItem.getSummary();
            Bundle args = new Bundle();
            args.putString("title", title);
            args.putString("summary", summary);
            if (!TextUtils.isEmpty(fragment) && getActivity() instanceof AccountContract.AccountView) {
                Fragment f = Fragment.instantiate(getActivity(), fragment, args);
                if (f instanceof BaseDialogFragment) {
                    if (getString(R.string.title_select_birthday).equals(title)) {
                        Log.d("test", "onClick:111 " + title);
                        showTimeSelect().show(getActivity().getSupportFragmentManager(), title);
                    } else {
                        Log.d("test", "onClick:222 " + title);
                        ((BaseDialogFragment) f).show(getActivity().getSupportFragmentManager(), title);
                    }
                } else {
                    ((AccountContract.AccountView) getActivity()).onStartFragment(fragment, args);
                }
            }
            onActionItemClick(actionItem);
        }

    }

    protected void onBindActionItemView(ActionItemView actionItemView, SharedPreferences sharedPreferences) {
        String actionKey = actionItemView.getKey();
        if (!TextUtils.isEmpty(actionKey)) {
            if (actionItemView instanceof IconItemView) {
                String uriStr = sharedPreferences.getString(actionKey, null);
                Log.d("test", "onBindActionItemView: uri=" + uriStr + "----" + actionKey);
                if (!TextUtils.isEmpty(uriStr)) {
                    try {
                        final Drawable drawable = PhotoSelectionHandler
                                .getRoundDrawableFromUri(actionItemView.getContext(), Uri.parse(uriStr));
                        actionItemView.setIcon(drawable);
                    } catch (FileNotFoundException e) {
                        Log.d("test", "onBindActionItemView: FileNotFoundException");
                        actionItemView.setIcon(null);
                        e.printStackTrace();
                    }
                }
                return;
            }
            Log.d("test", "onBindActionItemView: " + actionKey);
            if (actionKey.equals(UserInfoTemplate.KEY_ACCOUNT_SEX)) {
                int type = sharedPreferences.getInt(actionKey, -1);
                actionItemView.setSummary(getString(StringUtils.getSexName(type)));
            } else {
                actionItemView.setSummary(sharedPreferences.getString(actionKey, null));
            }
        }
    }

    protected void onUpdateActionItemView(ActionItemView actionItemView, SharedPreferences sharedPreferences, String key) {
        String actionKey = actionItemView.getKey();
        if (key.equals(actionKey)) {
            if (actionItemView instanceof IconItemView) {
                final String uriStr = mSharedPreferences.getString(actionKey, null);
                Log.d("test", "onUpdateActionItemView: uri" + uriStr + "----" + actionKey);
                if (!TextUtils.isEmpty(uriStr)) {
                    try {
                        final Drawable drawable = PhotoSelectionHandler
                                .getRoundDrawableFromUri(actionItemView.getContext(), Uri.parse(uriStr));
                        actionItemView.setIcon(drawable);
                    } catch (FileNotFoundException e) {
                        Log.d("test", "onBindActionItemView: FileNotFoundException");
                        actionItemView.setIcon(null);
                        e.printStackTrace();
                    }
                }
                return;
            }
            if (actionKey.equals(UserInfoTemplate.KEY_ACCOUNT_SEX)) {
                int type = sharedPreferences.getInt(actionKey, -1);
                actionItemView.setSummary(getString(StringUtils.getSexName(type)));
            } else {
                actionItemView.setSummary(sharedPreferences.getString(actionKey, null));
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getView() == null) return;
        ViewGroup actionViews = (ViewGroup) getView().findViewById(R.id.actions);
        if (actionViews == null) return;
        for (int i = 0; i < actionViews.getChildCount(); i++) {
            View child = actionViews.getChildAt(i);
            if (child instanceof ActionItemView) {
                ActionItemView action = (ActionItemView) child;
                onUpdateActionItemView(action, sharedPreferences, key);
            }
        }
    }

    @Override
    public void onDestroy() {
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }

    private TimePickerDialog showTimeSelect() {
        TimePickerDialog dialog = new TimePickerDialog.Builder()
                .setType(Type.YEAR_MONTH_DAY)
                .setCallBack(new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        if (getActivity() instanceof AccountContract.AccountView) {
                            ((AccountContract.AccountView) getActivity()).onBirthdayUpdate(StringUtils.formatTime(millseconds));
                            Toast.makeText(getContext(), millseconds + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setMinMillseconds(System.currentTimeMillis() - mYears)
                .setMaxMillseconds(System.currentTimeMillis())
                .setCurrentMillseconds(System.currentTimeMillis())
                .setTitleStringId(getString(R.string.title_select_birthday))
                .setWheelItemTextSelectorColor(getResources().getColor(R.color.text_blue_color))
                .setThemeColor(getResources().getColor(R.color.text_blue_color))
                .setWheelItemTextSize(21)
                .build();
        return dialog;
    }
}
