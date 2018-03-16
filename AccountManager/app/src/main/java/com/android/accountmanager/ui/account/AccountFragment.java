package com.android.accountmanager.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.accountmanager.R;
import com.android.accountmanager.commom.HandlerBus;
import com.android.accountmanager.event.SignOutEvent;
import com.android.accountmanager.model.UserInfoTemplate;
import com.android.accountmanager.ui.common.ResetPwdActivity;
import com.android.accountmanager.ui.widget.ActionItemView;
import com.android.accountmanager.ui.widget.IconItemView;
import com.android.accountmanager.utils.StringUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;

/**
 * Created by fantao on 18-1-20.
 */

public class AccountFragment extends BaseInfoFragment {
    private View mActionSignOut;
    private ImageView mImgeback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("test", "onCreateView: AccountFragment");
        View parentView = inflater.inflate(R.layout.fragment_account, null);
        mActionSignOut = parentView.findViewById(R.id.action_sign_out);
        mActionSignOut.setOnClickListener(this);
        mImgeback = (ImageView) parentView.findViewById(R.id.image_back);
        mImgeback.setOnClickListener(this);
        return parentView;
    }

    @Override
    protected void onBindActionItemView(ActionItemView actionItemView, SharedPreferences sharedPreferences) {
        super.onBindActionItemView(actionItemView, sharedPreferences);
        String actionKey = actionItemView.getKey();
        if (UserInfoTemplate.KEY_ACCOUNT_USER_HEADER.equals(actionKey)) {
            String uriStr = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_ICON, null);
            Log.d("test", "onBindActionItemView: aaaaaa" + uriStr);
            if (!TextUtils.isEmpty(uriStr)) {
                try {
                    final Drawable drawable = PhotoSelectionHandler
                            .getRoundDrawableFromUri(actionItemView.getContext(), Uri.parse(uriStr));
                    actionItemView.setIcon(drawable);
                } catch (FileNotFoundException e) {
                    actionItemView.setIcon(null);
                    e.printStackTrace();
                }
            } else {
                actionItemView.setIcon(null);
            }
            String name = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_NAME, "");
            String phoneNumber = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_TEL, "");
            if (TextUtils.isEmpty(name)) {
                actionItemView.setTitle(StringUtils.getPhoneNumber(phoneNumber));
                actionItemView.setSummary(getString(R.string.summary_name_empty));
            } else {
                actionItemView.setTitle(name);
                actionItemView.setSummary(StringUtils.getPhoneNumber(phoneNumber));
            }
        }
    }

    @Override
    protected void onUpdateActionItemView(ActionItemView actionItemView, SharedPreferences sharedPreferences, String key) {
        super.onUpdateActionItemView(actionItemView, sharedPreferences, key);
        String actionKey = actionItemView.getKey();
        if (UserInfoTemplate.KEY_ACCOUNT_USER_HEADER.equals(actionKey)) {
            if (actionItemView instanceof IconItemView) {
                if (UserInfoTemplate.KEY_ACCOUNT_ICON.equals(key)) {
                    String uriStr = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_ICON, null);
                    if (!TextUtils.isEmpty(uriStr)) {
                        try {
                            final Drawable drawable = PhotoSelectionHandler
                                    .getRoundDrawableFromUri(actionItemView.getContext(), Uri.parse(uriStr));
                            actionItemView.setIcon(drawable);
                        } catch (FileNotFoundException e) {
                            actionItemView.setIcon(null);
                            e.printStackTrace();
                        }
                    } else {
                        actionItemView.setIcon(null);
                    }
                } else if (UserInfoTemplate.KEY_ACCOUNT_NAME.equals(key)) {
                    String name = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_NAME, "");
                    String phoneNumber = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_TEL, "");
                    if (TextUtils.isEmpty(name)) {
                        actionItemView.setTitle(phoneNumber);
                        actionItemView.setSummary(getString(R.string.summary_name_empty));
                    } else {
                        actionItemView.setTitle(name);
                        actionItemView.setSummary(phoneNumber);
                    }
                } else if (UserInfoTemplate.KEY_ACCOUNT_TEL.equals(key)) {
                    String name = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_NAME, "");
                    String phoneNumber = sharedPreferences.getString(UserInfoTemplate.KEY_ACCOUNT_TEL, "");
                    if (TextUtils.isEmpty(name)) {
                        actionItemView.setTitle(phoneNumber);
                        actionItemView.setSummary(getString(R.string.summary_name_empty));
                    } else {
                        actionItemView.setTitle(name);
                        actionItemView.setSummary(phoneNumber);
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.item_getback_password:
                startActivity(new Intent(getActivity(), ResetPwdActivity.class));
                break;
            case R.id.action_sign_out:
                Bundle args = new Bundle();
                args.putString("title", getString(R.string.title_verify_password));
                VerifyPwdFragment verifyPwdFragment = new VerifyPwdFragment();
                verifyPwdFragment.setArguments(args);
                verifyPwdFragment.setListener(new VerifyPwdFragment.Listener() {
                    @Override
                    public void onVerifyComplete(boolean success) {
                        if (!(getActivity() instanceof AccountContract.AccountView)) return;
                        AccountContract.AccountView accountView = (AccountContract.AccountView) getActivity();
                        if (success) {
                            HandlerBus.getDefault().post(SignOutEvent.newInstance());
                        } else {
                            accountView.showAction(R.string.toast_password_error);
                        }
                    }
                });
                verifyPwdFragment.show(getActivity().getSupportFragmentManager(),
                        getString(R.string.title_verify_password));
                break;

            case R.id.image_back:
                getActivity().onBackPressed();
                break;
        }
    }

}
