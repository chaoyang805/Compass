package com.chaoyang805.compass.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.chaoyang805.compass.R;

/**
 * Created by chaoyang805 on 2015/9/9.
 */
public class CompassView extends ImageView {

    private int mCenterX;
    private int mCenterY;
    private float mInnerRadius;
    private float mOutterRadius;
    private Paint mPaint;
    private int mTextHeight;
    private static final String TAG = "CompassView";
    /**
     * 圆盘相对于控件的大小比例
     */
    private float mCircleRatio = 0.7f;
    private float mRotate = 0;


    public CompassView(Context context) {
        this(context, null);
    }

    public CompassView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CompassView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化一些成员变量
     */
    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTextSize(25);
        mPaint.setAntiAlias(true);
        Rect textBounds = new Rect();
        mPaint.getTextBounds("1", 0, 1, textBounds);
        mTextHeight = textBounds.height();
    }

    public void setRotate(int rotate) {
        this.mRotate = rotate;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterY = getMeasuredHeight() / 2;
        mCenterX = getMeasuredWidth() / 2;
        mInnerRadius = mCircleRatio * Math.min(mCenterX, mCenterY);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawArrow(canvas);
        canvas.save();
        canvas.rotate(mRotate, mCenterX, mCenterY);
        drawCircle(canvas);
        canvas.restore();
    }

    /**
     * 绘制带有刻度的指南针圆盘
     *
     * @param canvas
     */
    private void drawCircle(Canvas canvas) {
        float startX;
        float startY;
        float stopX;
        float stopY;
        double rad;
        //经过变换后绘制在圆盘周围的角度数字
        int targetText;
        for (int angle = 0; angle < 360; angle++) {
            rad = Math.toRadians(angle);
            targetText = (360 - angle + 90) % 360;
            //主要刻度比次要刻度长
            if (angle % 5 == 0) {
                mInnerRadius = mCircleRatio * Math.min(mCenterX, mCenterY) - 5;
                mOutterRadius = mInnerRadius + 30;
                if (angle % 45 == 0) {
                    mInnerRadius = mCircleRatio * Math.min(mCenterX, mCenterY) - 10;
                    mOutterRadius = mInnerRadius + 40;
                }
            } else {
                mInnerRadius = mCircleRatio * Math.min(mCenterX, mCenterY);
                mOutterRadius = mInnerRadius + 20;
            }

            startX = (float) (mCenterX + mInnerRadius * Math.cos(rad));
            startY = (float) (mCenterY - mInnerRadius * Math.sin(rad));
            stopX = (float) (mCenterX + mOutterRadius * Math.cos(rad));
            stopY = (float) (mCenterY - mOutterRadius * Math.sin(rad));

            canvas.drawLine(startX, startY, stopX, stopY, mPaint);

            drawAngleText(canvas, rad, targetText);
        }

    }

    /**
     * 绘制圆盘上的角度数字
     *
     * @param canvas
     * @param rad
     * @param targetText
     */
    private void drawAngleText(Canvas canvas, double rad, int targetText) {
        float textX;
        float textY;
        textX = (float) (mCenterX + (mOutterRadius + 5) * Math.cos(rad));
        textY = (float) (mCenterY - (mOutterRadius + 5) * Math.sin(rad));
        if (targetText % 15 == 0) {
            if (targetText > 90 && targetText < 270) {
                textY = textY + mTextHeight;
            }
            if (targetText == 90 || targetText == 270) {
                textY = textY + mTextHeight / 2;
            }
            if (targetText == 0 || targetText == 180) {
                mPaint.setTextAlign(Paint.Align.CENTER);
            }
            if (targetText > 0 && targetText < 180) {
                mPaint.setTextAlign(Paint.Align.LEFT);
            }
            if (targetText > 180 && targetText < 360) {
                mPaint.setTextAlign(Paint.Align.RIGHT);
            }
            //四个主方向上将角度换成N,E,S,W进行绘制
            mPaint.setTextSize(25);
            if (targetText == 0) {
                canvas.drawText("N", textX, textY, mPaint);
            } else if (targetText == 90) {
                canvas.drawText("E", textX, textY, mPaint);
            } else if (targetText == 180) {
                canvas.drawText("S", textX, textY, mPaint);
            } else if (targetText == 270) {
                canvas.drawText("W", textX, textY, mPaint);
            } else {
                mPaint.setTextSize(20);
                canvas.drawText(targetText + "", textX, textY, mPaint);
            }
        }
    }

    /**
     * 绘制指南针的箭头
     */
    private void drawArrow(Canvas canvas) {
        Bitmap arrowBitmap = BitmapFactory.decodeResource(getResources(),
                R.drawable.ic_navigation_grey600_48dp,
                null);

        canvas.drawBitmap(arrowBitmap, mCenterX - arrowBitmap.getWidth() / 2, mCenterY - arrowBitmap.getHeight() / 2, null);
    }
}
