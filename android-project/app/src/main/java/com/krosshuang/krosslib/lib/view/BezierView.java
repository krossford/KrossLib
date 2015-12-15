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

import com.krosshuang.krosslib.lib.graphics.BezierCurve;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krosshuang on 2015/10/21.
 *
 */
public class BezierView extends View {

    private final static String LOG_TAG = "BezierView";
    
    private final static float POINT_RADIUS = 5;

    private ArrayList<PointF> mPointList = new ArrayList<>();

    private int mCurrentSelectedPointIndex = -1;

    private Paint mPointPaint = new Paint();
    private Paint mCurvePaint = new Paint();

    private float lastX;
    private float lastY;
    private List<PointF> curvePoints;
    private BezierCurve bc = new BezierCurve();


    private long mLastUpTimeMillis = -1;    //last touch up time, for check whether double click

    public BezierView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPointPaint.setStrokeWidth(1);
        mPointPaint.setTextSize(30);
        mCurvePaint.setStrokeWidth(2);
        mCurvePaint.setColor(Color.RED);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        PointF[] input = new PointF[mPointList.size()];
        int i = 0;
        for (PointF p : mPointList) {
            input[i++] = p;
            canvas.drawCircle(p.x, p.y, POINT_RADIUS, mPointPaint);
            canvas.drawText(i+"", p.x, p.y, mPointPaint);
        }

        if (input.length < 2) {
            return;
        }

        curvePoints = bc.setPointSequence(input).build();


        PointF firstPoint = curvePoints.remove(0);
        lastX = firstPoint.x;
        lastY = firstPoint.y;

        for (PointF curveP: curvePoints) {
            canvas.drawLine(lastX, lastY, lastX = curveP.x, lastY = curveP.y, mCurvePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                for (int i = 0; i < mPointList.size(); i++) {
                    PointF p = mPointList.get(i);
                    if (Math.abs(p.x - event.getX()) <= 20 && Math.abs(p.y - event.getY()) <= 20) {
                        mCurrentSelectedPointIndex = i;
                        return true;
                    }
                }
                mCurrentSelectedPointIndex = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mCurrentSelectedPointIndex != -1) {
                    mPointList.get(mCurrentSelectedPointIndex).x = event.getX();
                    mPointList.get(mCurrentSelectedPointIndex).y = event.getY();
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
                if (mLastUpTimeMillis != -1) {
                    if (SystemClock.uptimeMillis() - mLastUpTimeMillis < 250) {

                        onDoubleClick(event);
                    }
                }
                mLastUpTimeMillis = SystemClock.uptimeMillis();
                break;
        }
        return true;
    }

    private void onDoubleClick(MotionEvent event) {
        Log.i(LOG_TAG, "onDoubleClick");
        mPointList.add(new PointF(event.getX(), event.getY()));
        invalidate();
    }
}
