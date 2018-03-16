package com.android.accountmanager.ui.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * Created by fantao on 18-1-19.
 */

public class IconItemView extends ActionItemView {

    public IconItemView(Context context) {
        this(context, null);
    }

    public IconItemView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IconItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public IconItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
