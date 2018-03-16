package com.android.accountmanager.ui.widget;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by yangyalin-os on 2018/1/23.
 */

public class CustomProgressView extends View {

    public static final int TRAVEL_SLOW = 1;
    public static final int TRAVEL_NORMAL = 2;
    // private int mProgressId;
    public static final int TRAVEL_FALST = 3;
    public static final int LINEAR = 0;
    public static final int CIRCULAR = 1;
    public static final int MODE_BUFFER = 2;
    public static final int MODE_QUERY = 3;
    public static final int STOKE_SIZE_SMALL = 0x10000;
    public static final int STOKE_SIZE_NORMAL = 0x11000;
    public static final int STOKE_SIZE_BIG = 0x11100;
    public static final int STOKE_SIZE_XBIG = 0x11110;
    private boolean mAutostart;
    private boolean mCircular;
    private ProgressDrawable mProgressDrawable;

    public CustomProgressView(Context context) {
        super(context);
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr,
                      int defStyleRes) {

        applyStyle(context, attrs, defStyleAttr, defStyleRes);
    }

    public void applyStyle(int resId) {
        applyStyle(getContext(), null, 0, resId);
    }

    @SuppressWarnings("deprecation")
    private void applyStyle(Context context, AttributeSet attrs,
                            int defStyleAttr, int defStyleRes) {
        mAutostart = false;
        if (mCircular) {
            mProgressDrawable = new CircularProgressDrawable(context);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(mProgressDrawable);
        } else {
            setBackgroundDrawable(mProgressDrawable);
        }
    }

    @Override
    public void setVisibility(int v) {
        if (getVisibility() != v) {
            super.setVisibility(v);

            if (getProgressMode() == ProgressDrawable.MODE_INDETERMINATE
                    && mAutostart) {
                if (v == GONE || v == INVISIBLE)
                    stop();
                else
                    start();
            }
        }
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        int progressMode = getProgressMode();
        boolean b = progressMode == ProgressDrawable.MODE_DETERMINATE;
        if (b && mAutostart) {
            if (visibility == GONE || visibility == INVISIBLE) {
                stop();
            } else {
                start();
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getProgressMode() == ProgressDrawable.MODE_INDETERMINATE
                && mAutostart)
            start();
    }

    @Override
    protected void onDetachedFromWindow() {

        if (getProgressMode() == ProgressDrawable.MODE_INDETERMINATE
                && mAutostart)
            stop();

        super.onDetachedFromWindow();
    }

    public int getProgressMode() {
        if (mProgressDrawable != null) {
            if (mCircular) {
                return ProgressDrawable.MODE_INDETERMINATE;
            }
        }
        return -1;
    }

    public float getProgress() {
        return mProgressDrawable.getProgress();
    }

    public void setProgress(float percent) {
        mProgressDrawable.setProgress(percent);
    }

    public float getSecondaryProgress() {
        return mProgressDrawable.getSecondaryProgress();
    }

    public void setSecondaryProgress(float percent) {
        mProgressDrawable.setSecondaryProgress(percent);
    }

    public void setStrokeSize(int strokeSizeType) {
        mProgressDrawable.strokeSize(strokeSizeType);
    }

    public void setStrokeColors(int... strokeColors) {
        mProgressDrawable.strokeColors(strokeColors);
    }

    public void setTravelSpeed(int speed) {
        mProgressDrawable.travelSpeed(speed);
    }

    public void setAutoStart(boolean autoStart) {
        mAutostart = autoStart;
    }

    public void start() {
        if (mProgressDrawable != null)
            ((Animatable) mProgressDrawable).start();
    }

    public void stop() {
        if (mProgressDrawable != null)
            ((Animatable) mProgressDrawable).stop();
    }

    public ProgressDrawable getProgressDrawable() {
        return mProgressDrawable;
    }

    @SuppressWarnings("deprecation")
    public void setType(int type) {
        boolean isCircular;
        if (type == CIRCULAR) {
            isCircular = true;
        } else {
            isCircular = false;
        }

        if (isCircular != mCircular) {
            mCircular = isCircular;
            if (mCircular) {
                mProgressDrawable = new CircularProgressDrawable(getContext());
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                setBackground(mProgressDrawable);
            } else {
                setBackgroundDrawable(mProgressDrawable);
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mProgressDrawable.setLayoutDirection(getLayoutDirection());
    }
}
