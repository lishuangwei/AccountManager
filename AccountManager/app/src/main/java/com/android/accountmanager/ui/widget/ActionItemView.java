package com.android.accountmanager.ui.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.accountmanager.R;

/**
 * Created by fantao on 18-1-19.
 */

public class ActionItemView extends LinearLayout {
    private String mKey;
    private Drawable mDefaultIcon;
    private String mDefaultTitle;
    private String mDefaultSummary;
    private Drawable mIcon;
    private String mTitle;
    private String mSummary;
    private String mFragment;
    private String mFragmentTitle;
    private int mLayoutResId = R.layout.action_item_layout;

    private ImageView mIconView;
    private TextView mTitleView;
    private TextView mSummaryView;

    public String getKey() {
        return mKey;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
        if (mIconView != null) {
            mIconView.setImageDrawable(mIcon != null ? mIcon : mDefaultIcon);
        }
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(!TextUtils.isEmpty(mTitle) ? mTitle : mDefaultTitle);
        }
    }

    public String getSummary() {
        return mSummary;
    }

    public void setSummary(String summary) {
        this.mSummary = summary;
        if (mSummaryView != null) {
            mSummaryView.setText(!TextUtils.isEmpty(mSummary) ? mSummary : mDefaultSummary);
        }
    }

    public String getFragment() {
        return mFragment;
    }

    public void setFragment(String fragment) {
        this.mFragment = fragment;
    }

    public String getFragmentTitle() {
        return mFragmentTitle;
    }

    public void setFragmentTitle(String fragmentTitle) {
        this.mFragmentTitle = fragmentTitle;
    }

    public ActionItemView(Context context) {
        this(context, null);
    }

    public ActionItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public ActionItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.ActionItemView, defStyleAttr, defStyleRes);
        for (int i = a.getIndexCount() - 1; i >= 0; i--) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.ActionItemView_key:
                    mKey = a.getString(attr);
                    break;
                case R.styleable.ActionItemView_icon:
                    mDefaultIcon = a.getDrawable(attr);
                    break;
                case R.styleable.ActionItemView_title:
                    mDefaultTitle = a.getString(attr);
                    break;
                case R.styleable.ActionItemView_summary:
                    mDefaultSummary = a.getString(attr);
                    break;
                case R.styleable.ActionItemView_fragment:
                    mFragment = a.getString(attr);
                    break;
                case R.styleable.ActionItemView_fragmentTitle:
                    mFragmentTitle = a.getString(attr);
                    break;
                case R.styleable.ActionItemView_layout:
                    mLayoutResId = a.getResourceId(attr, mLayoutResId);
                    break;
            }
        }
        a.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater inflater = LayoutInflater.from(getContext());
        inflater.inflate(mLayoutResId, this);
        mIconView = (ImageView) findViewById(R.id.icon);
        mTitleView = (TextView) findViewById(R.id.title);
        mSummaryView = (TextView) findViewById(R.id.summary);
        if (mIconView != null) {
            mIconView.setImageDrawable(mIcon != null ? mIcon : mDefaultIcon);
        }
        if (mTitleView != null) {
            mTitleView.setText(!TextUtils.isEmpty(mTitle) ? mTitle : mDefaultTitle);
        }
        if (mSummaryView != null) {
            mSummaryView.setText(!TextUtils.isEmpty(mSummary) ? mSummary : mDefaultSummary);
        }
    }
}
