package com.trace.animation.animatecicle;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

/**
 * TODO: document your custom view class.
 */
public class CircleView extends View implements Animator.AnimatorListener{
    private static final int DEFAULT_PROGRESS_WIDTH = 15;
    private Paint mProgressPaintFirst, mProgressPaintSecond;
    private RectF mFirstRect, mSecondRect;
    private float mProgressStrokeWidth;
    private AnimatorSet mAnimator;
    private float mSizeFirst, mSizeSecond;

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

        mProgressPaintFirst = new Paint();
        mProgressPaintFirst.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressPaintFirst.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaintFirst.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaintFirst.setStyle(Paint.Style.STROKE);
        mProgressPaintFirst.setColor(Color.RED);

        mProgressPaintSecond = new Paint();
        mProgressPaintSecond.setFlags(Paint.ANTI_ALIAS_FLAG);
        mProgressPaintSecond.setStrokeWidth(mProgressStrokeWidth);
        mProgressPaintSecond.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaintSecond.setStyle(Paint.Style.STROKE);
        mProgressPaintSecond.setColor(Color.GREEN);

        mFirstRect = new RectF();
        mSecondRect = new RectF();
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
        mSizeFirst = Math.min(actualHeight, actualWidth);
        mSizeFirst -= mProgressStrokeWidth;
        mSizeSecond = mSizeFirst - mProgressStrokeWidth;

        mFirstRect.set(mProgressStrokeWidth, mProgressStrokeWidth, mSizeFirst, mSizeFirst);
        mSecondRect.set(mProgressStrokeWidth * 2, mProgressStrokeWidth * 2, mSizeSecond, mSizeSecond);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mAnimator == null) {
            ObjectAnimator firstCircleAnim = ObjectAnimator.ofFloat(this, "rectSizeFirst", 0, (mSizeFirst /2)-(2 * mProgressStrokeWidth/3), 0);
            firstCircleAnim.setInterpolator(new AccelerateDecelerateInterpolator());
            firstCircleAnim.setDuration(300);

            ObjectAnimator secondCircleStart = ObjectAnimator.ofFloat(this, "rectSizeSecond", 0, (mSizeSecond /2)-(mProgressStrokeWidth * 3/2));
            secondCircleStart.setInterpolator(new AccelerateInterpolator());
            secondCircleStart.setDuration(300);

            ObjectAnimator secondCircleEnd = ObjectAnimator.ofFloat(this, "rectSizeSecond", (mSizeSecond /2)-(mProgressStrokeWidth * 3/2), 0);
            secondCircleEnd.setInterpolator(new DecelerateInterpolator());
            secondCircleEnd.setDuration(300);

            mAnimator = new AnimatorSet();
            mAnimator.play(firstCircleAnim).with(secondCircleEnd).after(secondCircleStart);
            mAnimator.addListener(this);
            mAnimator.setStartDelay(200);
            mAnimator.start();
        }
        canvas.drawArc(mFirstRect, 90, 180, false, mProgressPaintFirst);
        canvas.drawArc(mSecondRect, 0, 360, false, mProgressPaintSecond);
        canvas.drawArc(mFirstRect, 270, 180, false, mProgressPaintFirst);



    }


    public void setRectSizeFirst(float baseValue) {
        mFirstRect.set(mProgressStrokeWidth + baseValue, mProgressStrokeWidth, mSizeFirst - baseValue, mSizeFirst);
    }

    public void setRectSizeSecond(float baseValue) {
        mSecondRect.set(mProgressStrokeWidth * 2 + baseValue, mProgressStrokeWidth * 2, mSizeSecond - baseValue, mSizeSecond);
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
        mAnimator.setStartDelay(200);
        mAnimator.start();
    }

    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }
}
