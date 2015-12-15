package com.krosshuang.krosslib.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.krosshuang.krosslib.lib.graphics.BezierControlPoint;
import com.krosshuang.krosslib.lib.graphics.BezierCurve;
import com.krosshuang.krosslib.lib.graphics.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Advanced Bezier View whose behavior is similar to that of Pen Tool of Photoshop.
 * Created by krosshuang on 2015/10/22.
 *
 */
public class AdvancedBezierView extends View {

    private static final String LOG_TAG = "AdvancedBezierView";
    private long lastDrawStartTimeMillis;

    private static final int CONFIG_DOUBLE_TOUCH_TIME_INTERVAL = 250;



    private Paint mVertexPaint;
    private Paint mPrevPaint;
    private Paint mNextPaint;
    private Paint mControlLinePaint;
    private Paint mCurvePaint;
    private Paint mTextPaint;

    private ArrayList<BezierControlPoint> mControlPointList = new ArrayList<>();
    private BezierControlPoint mStartPoint = null;
    private BezierControlPoint mEndPoint = null;
    private BezierCurve mBezierCurve = new BezierCurve();
    private ArrayList<PointF> mTempPointList = new ArrayList<>();
    private List<PointF> mTempCurveList = null;

    private long mLastUpTimeMillis = -1;
    private float mLastDownX;
    private float mLastDownY;
    private float mLastX;
    private float mLastY;

    private int mCurrentSelectedIndex = -1;
    private int mCurrentSelectedPointType;

    private boolean mIsDoubleTouch = false;
    private boolean mIsMoveOnDoubleTouch = false;
    private boolean mIsCreatedMultiControlPoint = false;

    public AdvancedBezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaints();
    }

    private void initPaints() {
        mVertexPaint = new Paint();
        mVertexPaint.setColor(Color.BLACK);

        mPrevPaint = new Paint();
        mPrevPaint.setColor(Color.RED);

        mNextPaint = new Paint();
        mNextPaint.setColor(Color.BLUE);

        mControlLinePaint = new Paint();
        mControlLinePaint.setColor(Color.parseColor("#0d8e47"));
        mControlLinePaint.setStrokeWidth(2);
        mControlLinePaint.setAntiAlias(true);

        mCurvePaint = new Paint();
        mCurvePaint.setColor(Color.parseColor("#9c36c1"));
        mCurvePaint.setStrokeWidth(2);
        mCurvePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(25);
        mTextPaint.setColor(Color.GRAY);
        mTextPaint.setAntiAlias(true);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        lastDrawStartTimeMillis = SystemClock.uptimeMillis();
        for (BezierControlPoint p : mControlPointList) {
            if (p.isSingle) {
                canvas.drawCircle(p.vertex.x, p.vertex.y, 5, mVertexPaint);
            } else {
                canvas.drawLine(p.prevPoint.x, p.prevPoint.y, p.nextPoint.x, p.nextPoint.y, mControlLinePaint);
                canvas.drawCircle(p.prevPoint.x, p.prevPoint.y, 5, mPrevPaint);
                canvas.drawCircle(p.vertex.x, p.vertex.y, 5, mVertexPaint);
                canvas.drawCircle(p.nextPoint.x, p.nextPoint.y, 5, mNextPaint);
            }
        }

        if (mControlPointList.size() < 2) {
            return;
        }

        for (int i = 0; i < mControlPointList.size() - 1; i++) {
            mTempPointList.clear();
            mStartPoint = mControlPointList.get(i);
            mEndPoint = mControlPointList.get(i + 1);

            mTempPointList.add(mStartPoint.vertex);
            if (!mStartPoint.isSingle) {
                mTempPointList.add(mStartPoint.nextPoint);
            }
            if (!mEndPoint.isSingle) {
                mTempPointList.add(mEndPoint.prevPoint);
            }
            mTempPointList.add(mEndPoint.vertex);

            mBezierCurve.setPointSequence(Util.toArray(mTempPointList));
            mTempCurveList = mBezierCurve.build();
            drawCurve(canvas);
        }

        lastDrawStartTimeMillis = SystemClock.uptimeMillis() - lastDrawStartTimeMillis;
        canvas.drawText("draw cast: " + lastDrawStartTimeMillis + "(ms)", 10, 30, mTextPaint);
    }

    private void drawCurve(Canvas canvas) {
        PointF firstPoint = mTempCurveList.remove(0);
        mLastX = firstPoint.x;
        mLastY = firstPoint.y;

        for (PointF p : mTempCurveList) {
            canvas.drawLine(mLastX, mLastY, mLastX = p.x, mLastY = p.y, mCurvePaint);
        }

    }

    private boolean checkSelectPoint(float x, float y, PointF p) {
        if (Math.abs(p.x - x) <= 20 && Math.abs(p.y - y) <= 20) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownX = event.getX();
                mLastDownY = event.getY();
                if (mLastUpTimeMillis != -1) {  //check whether double touch
                    if (SystemClock.uptimeMillis() - mLastUpTimeMillis < CONFIG_DOUBLE_TOUCH_TIME_INTERVAL) {
                        mIsDoubleTouch = true;
                    } else {
                        mIsDoubleTouch = false;
                    }
                } else {
                    mIsDoubleTouch = false;
                }

                if (!mIsDoubleTouch) {
                    for (int i = 0; i < mControlPointList.size(); i++) {
                        BezierControlPoint p = mControlPointList.get(i);
                        if (checkSelectPoint(event.getX(), event.getY(), p.vertex)) {
                            mCurrentSelectedIndex = i;
                            mCurrentSelectedPointType = BezierControlPoint.TYPE_VERTEX;
                            break;
                        }
                        if (!p.isSingle) {
                            if (checkSelectPoint(event.getX(), event.getY(), p.prevPoint)) {
                                mCurrentSelectedIndex = i;
                                mCurrentSelectedPointType = BezierControlPoint.TYPE_PREV;
                                break;
                            }
                            if (checkSelectPoint(event.getX(), event.getY(), p.nextPoint)) {
                                mCurrentSelectedIndex = i;
                                mCurrentSelectedPointType = BezierControlPoint.TYPE_NEXT;
                                break;
                            }
                        }
                        mCurrentSelectedIndex = -1;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mIsDoubleTouch) {
                    mIsMoveOnDoubleTouch = true;
                    if (mIsCreatedMultiControlPoint) {
                        updateMultiControlPoint(mControlPointList.size() - 1, BezierControlPoint.TYPE_NEXT, event.getX(), event.getY(), true);
                    } else {
                        createMultiControlPoint(mLastDownX, mLastDownY, event.getX(), event.getY());
                        mIsCreatedMultiControlPoint = true;
                    }
                } else {
                    mIsMoveOnDoubleTouch = false;
                    if (mCurrentSelectedIndex != -1) {
                        updateMultiControlPoint(mCurrentSelectedIndex, mCurrentSelectedPointType, event.getX(), event.getY(), false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mCurrentSelectedIndex = -1;
                if (mIsDoubleTouch && !mIsMoveOnDoubleTouch) {
                    createSingleControlPoint(event.getX(), event.getY());
                    mLastUpTimeMillis = -1;
                } else {
                    if (mIsCreatedMultiControlPoint) {
                        mIsCreatedMultiControlPoint = false;
                    }
                    mLastUpTimeMillis = SystemClock.uptimeMillis();
                }
                mIsMoveOnDoubleTouch = false;
                break;
        }
        return true;
    }

    private void createSingleControlPoint(float x, float y) {
        Log.i(LOG_TAG, "createSingleControlPoint");
        mControlPointList.add(BezierControlPoint.createSingle(x, y));
        invalidate();
    }

    private void createMultiControlPoint(float vertexX, float vertexY, float nextX, float nextY) {
        Log.i(LOG_TAG, "createMultiControlPoint");
        mControlPointList.add(BezierControlPoint.createMulti(vertexX, vertexY, nextX, nextY));
        invalidate();
    }

    /**
     *
     * update the multi-control point.
     * @param index it specified that which one point will be controlled.
     * @param type it specified that which point in multi-control point will be controlled.
     *             there are 3 types be chose.
     * @param x new coordinate
     * @param y new coordinate <br/>
     * {@link BezierControlPoint#TYPE_PREV} -> previous point <br/>
     * {@link BezierControlPoint#TYPE_VERTEX} -> vertex point <br/>
     * {@link BezierControlPoint#TYPE_NEXT} -> next point <br/>
     * */
    private void updateMultiControlPoint(int index, int type, float x, float y, boolean isMirror) {
        Log.i(LOG_TAG, "updateMultiControlPoint");
        BezierControlPoint p = mControlPointList.get(index);
        switch (type) {
            case BezierControlPoint.TYPE_PREV:
                p.movePrev(x, y, isMirror);
                break;
            case BezierControlPoint.TYPE_VERTEX:
                p.moveVertex(x, y);
                break;
            case BezierControlPoint.TYPE_NEXT:
                p.moveNext(x, y, isMirror);
                break;
        }
        invalidate();
    }
}
