package com.android.accountmanager.ui.widget;

import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public abstract class ProgressDrawable extends Drawable implements Animatable {
    public static final int MODE_DETERMINATE = 0;
    public static final int MODE_INDETERMINATE = 1;

    public abstract float getProgress();

    public abstract void setProgress(float percent);

    public abstract float getSecondaryProgress();

    public abstract void setSecondaryProgress(float percent);

    public abstract void strokeSize(int strokeSize);

    public abstract void strokeColors(int... strokeColors);

    public abstract int getProgressMode();

    public abstract void travelSpeed(int speed);

    public abstract float getMax();

    public abstract void setMax(float max);

    public abstract void setHasAnimation(boolean hasAnimator);

    public void setAdvanLayoutDirection(int direction) {
    }

    ;

}
