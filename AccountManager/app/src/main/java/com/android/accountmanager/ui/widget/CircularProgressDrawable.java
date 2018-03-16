package com.android.accountmanager.ui.widget;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.TypedValue;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.android.accountmanager.R;

public class CircularProgressDrawable extends ProgressDrawable {

    private static final long FRAME_DURATION = 1000 / 60;
    private static final int PROGRESS_STATE_HIDE = -1;
    private static final int PROGRESS_STATE_STRETCH = 0;
    private static final int PROGRESS_STATE_KEEP_STRETCH = 1;
    private static final int PROGRESS_STATE_SHRINK = 2;
    private static final int PROGRESS_STATE_KEEP_SHRINK = 3;
    private static final int RUN_STATE_STOPPED = 0;
    private static final int RUN_STATE_STARTING = 1;
    private static final int RUN_STATE_STARTED = 2;
    private static final int RUN_STATE_RUNNING = 3;
    private static final int RUN_STATE_STOPPING = 4;
    private long mLastUpdateTime;
    private long mLastProgressStateTime;
    private long mLastRunStateTime;
    private int mProgressState;
    private int mRunState = RUN_STATE_STOPPED;

    private ArgbEvaluator mArgbEvaluator;

    private Paint mPaint;
    private RectF mRect;
    private float mStartAngle;
    private float mSweepAngle;
    private int mStrokeColorIndex;

    private int mPadding;
    private float mInitialAngle;
    private float mSecondaryProgressPercent;
    private float mMaxSweepAngle;
    private float mMinSweepAngle;
    private int mStrokeSize;
    private int[] mStrokeColors;
    private boolean mReverse;
    private int mRotateDuration;
    private int mTransformDuration;
    private int mKeepDuration;
    private float mInStepPercent;
    private int[] mInColors;
    private int mInAnimationDuration;
    private int mOutAnimationDuration;
    private Interpolator mTransformInterpolator;
    private final Runnable mUpdater = new Runnable() {

        @Override
        public void run() {
            update();
        }

    };
    private Context mContext;

    public CircularProgressDrawable(Context context) {
        mContext = context;
        mArgbEvaluator = new ArgbEvaluator();
        padding(0);
        initialAngle(0);
        progressPercent(0);
        secondaryProgressPercent(0);
        maxSweepAngle(270);
        minSweepAngle(1);
        strokeSize(dpToPx(2));

        strokeColors(context.getResources().getColor(R.color.system_blue));

        reverse(false);
        travelSpeed(CustomProgressView.TRAVEL_NORMAL);
        transformInterpolator(AnimationUtils.loadInterpolator(context,
                android.R.anim.decelerate_interpolator));
        inAnimDuration(400);

        inStepPercent(0.5f);
        outAnimDuration(400);
        if (mStrokeColors == null)
            mStrokeColors = new int[]{0xFF0099FF};

        if (mInColors == null && mInAnimationDuration > 0)
            mInColors = new int[]{0xFFB5D4FF, 0xFFDEEAFC, 0xFFFAFFFE};

        if (mTransformInterpolator == null)
            mTransformInterpolator = new DecelerateInterpolator();

        setProgress(0);
        setSecondaryProgress(0);

        mKeepDuration = 200;

        mInAnimationDuration = 0;
        mInStepPercent = 0;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeJoin(Paint.Join.ROUND);

        mRect = new RectF();
    }

    @Override
    public void draw(Canvas canvas) {

        drawIndeterminate(canvas);
    }

    private int getIndeterminateStrokeColor() {
        if (mProgressState != PROGRESS_STATE_KEEP_SHRINK
                || mStrokeColors.length == 1)
            return mStrokeColors[mStrokeColorIndex];

        float value = Math.max(0f, Math.min(1f,
                (float) (SystemClock.uptimeMillis() - mLastProgressStateTime)
                        / mKeepDuration));
        int prev_index = mStrokeColorIndex == 0 ? mStrokeColors.length - 1
                : mStrokeColorIndex - 1;

        return (int) mArgbEvaluator.evaluate(value, mStrokeColors[prev_index], mStrokeColors[mStrokeColorIndex]);
    }

    private void drawIndeterminate(Canvas canvas) {
        if (mRunState == RUN_STATE_STARTING) {
            Rect bounds = getBounds();
            float x = (bounds.left + bounds.right) / 2f;
            float y = (bounds.top + bounds.bottom) / 2f;
            float maxRadius = (Math.min(bounds.width(), bounds.height()) - mPadding * 2) / 2f;

            float stepTime = 1f / (mInStepPercent * (mInColors.length + 2) + 1);
            float time = (float) (SystemClock.uptimeMillis() - mLastRunStateTime)
                    / mInAnimationDuration;
            float steps = time / stepTime;

            float outerRadius = 0f;
            float innerRadius = 0f;
            for (int i = (int) Math.floor(steps); i >= 0; i--) {
                innerRadius = outerRadius;
                outerRadius = Math.min(1f, (steps - i) * mInStepPercent)
                        * maxRadius;

                if (i >= mInColors.length)
                    continue;

                if (innerRadius == 0) {
                    mPaint.setColor(mInColors[i]);
                    mPaint.setStyle(Paint.Style.FILL);
                    canvas.drawCircle(x, y, outerRadius, mPaint);
                } else if (outerRadius > innerRadius) {
                    float radius = (innerRadius + outerRadius) / 2;
                    mRect.set(x - radius, y - radius, x + radius, y + radius);

                    mPaint.setStrokeWidth(outerRadius - innerRadius);
                    mPaint.setStyle(Paint.Style.STROKE);
                    mPaint.setColor(mInColors[i]);
                    canvas.drawCircle(x, y, radius, mPaint);
                } else {

                    break;
                }
            }

            if (mProgressState == PROGRESS_STATE_HIDE) {
                if (steps >= 1 / mInStepPercent || time >= 1)
                    resetAnimation();
            } else {
                float radius = maxRadius - mStrokeSize / 2f;

                mRect.set(x - radius, y - radius, x + radius, y + radius);
                mPaint.setStrokeWidth(mStrokeSize);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(getIndeterminateStrokeColor());

                canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint);
            }
        } else if (mRunState == RUN_STATE_STOPPING) {
            float size = (float) mStrokeSize
                    * Math.max(
                    0,
                    (mOutAnimationDuration - SystemClock.uptimeMillis() + mLastRunStateTime))
                    / mOutAnimationDuration;

            if (size > 0) {
                Rect bounds = getBounds();
                float radius = (Math.min(bounds.width(), bounds.height())
                        - mPadding * 2 - mStrokeSize * 2 + size) / 2f;
                float x = (bounds.left + bounds.right) / 2f;
                float y = (bounds.top + bounds.bottom) / 2f;

                mRect.set(x - radius, y - radius, x + radius, y + radius);
                mPaint.setStrokeWidth(size);
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setColor(getIndeterminateStrokeColor());

                canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint);
            }
        } else if (mRunState != RUN_STATE_STOPPED) {
            Rect bounds = getBounds();
            float radius = (Math.min(bounds.width(), bounds.height())
                    - mPadding * 2 - mStrokeSize) / 2f;
            float x = (bounds.left + bounds.right) / 2f;
            float y = (bounds.top + bounds.bottom) / 2f;

            mRect.set(x - radius, y - radius, x + radius, y + radius);
            mPaint.setStrokeWidth(mStrokeSize);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setColor(getIndeterminateStrokeColor());

            canvas.drawArc(mRect, mStartAngle, mSweepAngle, false, mPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    public float getProgress() {
        return 1f;
    }

    public void setProgress(float percent) {
    }

    public float getSecondaryProgress() {
        return mSecondaryProgressPercent;
    }

    public void setSecondaryProgress(float percent) {
        percent = Math.min(1f, Math.max(0f, percent));
        if (mSecondaryProgressPercent != percent) {
            mSecondaryProgressPercent = percent;
            if (isRunning())
                invalidateSelf();
            else if (mSecondaryProgressPercent != 0f)
                start();
        }
    }

    private void resetAnimation() {
        mLastUpdateTime = SystemClock.uptimeMillis();
        mLastProgressStateTime = mLastUpdateTime;
        mStartAngle = mInitialAngle;
        mStrokeColorIndex = 0;
        mSweepAngle = mReverse ? -mMinSweepAngle : mMinSweepAngle;
        mProgressState = PROGRESS_STATE_STRETCH;
    }

    @Override
    public void start() {
        start(mInAnimationDuration > 0);
    }

    @Override
    public void stop() {
        stop(mOutAnimationDuration > 0);
    }

    private void start(boolean withAnimation) {

        boolean running = isRunning();
        if (running) {
            return;
        }
        if (withAnimation) {
            mRunState = RUN_STATE_STARTING;
            mLastRunStateTime = SystemClock.uptimeMillis();
            mProgressState = PROGRESS_STATE_HIDE;
        } else {
            resetAnimation();
        }
        scheduleSelf(mUpdater, SystemClock.uptimeMillis()
                + FRAME_DURATION);
        invalidateSelf();
    }

    private void stop(boolean withAnimation) {
        if (!isRunning())
            return;

        if (withAnimation) {
            mLastRunStateTime = SystemClock.uptimeMillis();
            if (mRunState == RUN_STATE_STARTED) {
                scheduleSelf(mUpdater, SystemClock.uptimeMillis()
                        + FRAME_DURATION);
                invalidateSelf();
            }
            mRunState = RUN_STATE_STOPPING;
        } else {
            mRunState = RUN_STATE_STOPPED;
            unscheduleSelf(mUpdater);
            invalidateSelf();
        }
    }

    @Override
    public boolean isRunning() {

        return mRunState != RUN_STATE_STOPPED;
    }

    @Override
    public void scheduleSelf(Runnable what, long when) {
        if (mRunState == RUN_STATE_STOPPED)
            mRunState = mInAnimationDuration > 0 ? RUN_STATE_STARTING
                    : RUN_STATE_RUNNING;
        super.scheduleSelf(what, when);
    }

    private void update() {
        updateIndeterminate();
    }

    private void updateIndeterminate() {
        long curTime = SystemClock.uptimeMillis();
        float rotateOffset = (curTime - mLastUpdateTime) * 360f
                / mRotateDuration;
        if (mReverse)
            rotateOffset = -rotateOffset;
        mLastUpdateTime = curTime;

        switch (mProgressState) {
            case PROGRESS_STATE_STRETCH:
                if (mTransformDuration <= 0) {
                    mSweepAngle = mReverse ? -mMinSweepAngle : mMinSweepAngle;
                    mProgressState = PROGRESS_STATE_KEEP_STRETCH;
                    mStartAngle += rotateOffset;
                    mLastProgressStateTime = curTime;
                } else {
                    float value = (curTime - mLastProgressStateTime)
                            / (float) mTransformDuration;
                    float maxAngle = mReverse ? -mMaxSweepAngle : mMaxSweepAngle;
                    float minAngle = mReverse ? -mMinSweepAngle : mMinSweepAngle;

                    mStartAngle += rotateOffset;
                    mSweepAngle = mTransformInterpolator.getInterpolation(value)
                            * (maxAngle - minAngle) + minAngle;

                    if (value > 1f) {
                        mSweepAngle = maxAngle;
                        mProgressState = PROGRESS_STATE_KEEP_STRETCH;
                        mLastProgressStateTime = curTime;
                    }
                }
                break;
            case PROGRESS_STATE_KEEP_STRETCH:
                mStartAngle += rotateOffset;

                if (curTime - mLastProgressStateTime > mKeepDuration) {
                    mProgressState = PROGRESS_STATE_SHRINK;
                    mLastProgressStateTime = curTime;
                }
                break;
            case PROGRESS_STATE_SHRINK:
                if (mTransformDuration <= 0) {
                    mSweepAngle = mReverse ? -mMinSweepAngle : mMinSweepAngle;
                    mProgressState = PROGRESS_STATE_KEEP_SHRINK;
                    mStartAngle += rotateOffset;
                    mLastProgressStateTime = curTime;
                    mStrokeColorIndex = (mStrokeColorIndex + 1)
                            % mStrokeColors.length;
                } else {
                    float value = (curTime - mLastProgressStateTime)
                            / (float) mTransformDuration;
                    float maxAngle = mReverse ? -mMaxSweepAngle : mMaxSweepAngle;
                    float minAngle = mReverse ? -mMinSweepAngle : mMinSweepAngle;

                    float newSweepAngle = (1f - mTransformInterpolator
                            .getInterpolation(value))
                            * (maxAngle - minAngle)
                            + minAngle;
                    mStartAngle += rotateOffset + mSweepAngle - newSweepAngle;
                    mSweepAngle = newSweepAngle;

                    if (value > 1f) {
                        mSweepAngle = minAngle;
                        mProgressState = PROGRESS_STATE_KEEP_SHRINK;
                        mLastProgressStateTime = curTime;
                        mStrokeColorIndex = (mStrokeColorIndex + 1)
                                % mStrokeColors.length;
                    }
                }
                break;
            case PROGRESS_STATE_KEEP_SHRINK:
                mStartAngle += rotateOffset;

                if (curTime - mLastProgressStateTime > mKeepDuration) {
                    mProgressState = PROGRESS_STATE_STRETCH;
                    mLastProgressStateTime = curTime;
                }
                break;
        }

        if (mRunState == RUN_STATE_STARTING) {
            if (curTime - mLastRunStateTime > mInAnimationDuration) {
                mRunState = RUN_STATE_RUNNING;
                if (mProgressState == PROGRESS_STATE_HIDE)
                    resetAnimation();
            }
        } else if (mRunState == RUN_STATE_STOPPING) {
            if (curTime - mLastRunStateTime > mOutAnimationDuration) {
                stop(false);
                return;
            }
        }

        if (isRunning())
            scheduleSelf(mUpdater, SystemClock.uptimeMillis()
                    + FRAME_DURATION);

        invalidateSelf();
    }

    public int getProgressMode() {
        return MODE_INDETERMINATE;
    }

    public void padding(int padding) {
        mPadding = padding;
    }

    public void initialAngle(float angle) {
        mInitialAngle = angle;
    }

    public void progressPercent(float percent) {
        // mProgressPercent = percent;
    }

    public void secondaryProgressPercent(float percent) {
        mSecondaryProgressPercent = percent;
    }

    public void maxSweepAngle(float angle) {
        mMaxSweepAngle = angle;
    }

    public void minSweepAngle(float angle) {
        mMinSweepAngle = angle;
    }

    public void strokeSize(int strokeSize) {
        switch (strokeSize) {
            case CustomProgressView.STOKE_SIZE_SMALL:
                mStrokeSize = dpToPx(1);
                break;
            case CustomProgressView.STOKE_SIZE_NORMAL:
                mStrokeSize = dpToPx(2);
                break;
            case CustomProgressView.STOKE_SIZE_BIG:
                mStrokeSize = dpToPx(3);
                break;
            case CustomProgressView.STOKE_SIZE_XBIG:
                mStrokeSize = dpToPx(4);
                break;
            default:
                mStrokeSize = dpToPx(2);
        }
    }

    public void strokeColors(int... strokeColors) {
        mStrokeColors = strokeColors;
    }

    public void reverse(boolean reverse) {
        mReverse = reverse;
    }

    public void rotateDuration(int duration) {
        mRotateDuration = duration;
    }

    public void transformDuration(int duration) {
        mTransformDuration = duration;
    }

    public void transformInterpolator(Interpolator interpolator) {
        mTransformInterpolator = interpolator;
    }

    public void inAnimDuration(int duration) {
        mInAnimationDuration = duration;
    }

    public void inStepPercent(float percent) {
        mInStepPercent = percent;
    }

    public void outAnimDuration(int duration) {
        mOutAnimationDuration = duration;
    }

    public void travelSpeed(int speed) {
        if (speed == CustomProgressView.TRAVEL_SLOW) {
            rotateDuration(1200);
            transformDuration(700);
        } else if (speed == CustomProgressView.TRAVEL_NORMAL) {
            rotateDuration(1000);
            transformDuration(600);
        } else if (speed == CustomProgressView.TRAVEL_FALST) {
            rotateDuration(700);
            transformDuration(400);
        }
    }

    @Override
    public float getMax() {
        return 1f;
    }

    @Override
    public void setMax(float max) {

    }

    @Override
    public void setHasAnimation(boolean hasAnimator) {

    }

    private int dpToPx(float dp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources()
                .getDisplayMetrics()) + 0.5f);
    }
}
