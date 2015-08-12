package com.trace.animation.animatecicle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

/**
 * TODO: document your custom view class.
 */
public class CircleView extends View implements Animator.AnimatorListener{
    private static final int DEFAULT_PROGRESS_WIDTH = 15;
    private static final int DEFAULT_ANIM_DURATION = 1200;
    private static final int[] DEFAULT_COLORS = new int[]{Color.RED, Color.GREEN, Color.BLUE};
    private Paint mProgressPaint;
    private RectF mProgressRect;
    private float mProgressStrokeWidth, mStartAngle = 0, mSweepAngle = 0, mActualCoveredAngle = 0;
    private ObjectAnimator mAnimator;
    private int[] mProgressSwapColors;
    private int mColorArrayPointer = 0;
    private long mProgressCycleDuration;
    private float mSize;

    public CircleView(Context context) {
        super(context);
        init(null, 0);
    }

    public CircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        mProgressStrokeWidth = DEFAULT_PROGRESS_WIDTH
                * getResources().getDisplayMetrics().density;
        mProgressSwapColors = DEFAULT_COLORS;
        mProgressCycleDuration = DEFAULT_ANIM_DURATION;

        mProgressPaint = new Paint();
        mProgressPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setColor(Color.RED);

        mProgressRect = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int actualHeight = h - paddingTop - paddingBottom;
        int actualWidth = w - paddingLeft - paddingRight;
        mSize = Math.min(actualHeight, actualWidth);
        mSize -= mProgressStrokeWidth;

        mProgressRect.set(mProgressStrokeWidth, mProgressStrokeWidth, mSize, mSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mAnimator == null) {
            mAnimator = ObjectAnimator.ofFloat(this, "rectSize", 0, (mSize/2)-(mProgressStrokeWidth/2));
            mAnimator.setRepeatCount(ValueAnimator.INFINITE);
            mAnimator.setRepeatMode(ValueAnimator.REVERSE);
            mAnimator.setDuration(10000);
            mAnimator.start();
        }
        if (mProgressRect.left == mProgressRect.right) {
            canvas.drawLine(mProgressRect.left, 0, mProgressRect.left, mSize, mProgressPaint);
        } else {
            canvas.drawArc(mProgressRect, 0, 360, false, mProgressPaint);
        }


    }

    /**
     * This function is used to create the animation for the progressbar.
     * It represents the sweep angle of the arc at a particular time.
     *
     * @param baseValue The example color attribute value to use.
     */
    public void setSweepAngle(float baseValue) {
        mSweepAngle = baseValue;
        invalidate();
    }


    public void setRectSize(float baseValue) {
        mProgressRect.set(mProgressStrokeWidth + baseValue, mProgressStrokeWidth, mSize - baseValue, mSize);
        invalidate();
    }

    /**
     * This function is used to create the animation for the progressbar.
     * It represents the start angle of the arc at a particular time.
     *
     * @param baseValue The example color attribute value to use.
     */
    public void setStartAngle(float baseValue) {
        mStartAngle = mActualCoveredAngle + baseValue;
        if (mStartAngle > 360) {
            mStartAngle %= 360;
        }

    }

    /**
     * Gets the colors attribute value of the progressbar.
     *
     * @return The progressbar colors attribute value.
     */
    public int[] getProgressSwapColors() {
        return mProgressSwapColors;
    }

    /**
     * Sets the progressbar's colors attribute value.
     *
     * @param progressSwapColors The progressbar colors attribute value to use.
     */
    public void setProgressSwapColors(int[] progressSwapColors) {
        mProgressSwapColors = progressSwapColors;
        invalidate();
    }

    /**
     * Gets the progressbar's stroke width attribute value.
     *
     * @return The stroke width attribute value.
     */
    public float getProgressStrokeWidth() {
        return mProgressStrokeWidth;
    }

    /**
     * Sets the progressbar's stroke width attribute value.
     *
     * @param progressStrokeWidth The stroke width attribute value to use.
     */
    public void setProgressStrokeWidth(float progressStrokeWidth) {
        this.mProgressStrokeWidth = progressStrokeWidth;
        invalidate();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        mActualCoveredAngle = mStartAngle;
        if (animation.equals(mAnimator)) {
            mColorArrayPointer++;
            if (mColorArrayPointer == mProgressSwapColors.length) {
                mColorArrayPointer = 0;
            }
            mAnimator.start();
        }
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
