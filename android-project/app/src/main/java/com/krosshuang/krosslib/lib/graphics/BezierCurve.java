package com.krosshuang.krosslib.lib.graphics;

import android.graphics.PointF;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * How to use it
 *
 * BezierCurve bc = new BezierCurve();
 * bc.setPointSequence(PointFp[)
 *   .progressWith(1)   //optional
 *   .build();
 *
 * after above code run end, you will get a List of BezierCurve Point.
 *
 * Created by krosshuang on 2015/10/21.
 *
 */
public class BezierCurve {

    private static final String LOG_TAG = "BezierCurve";

    private List<PointF> mCurvePoints = null;
    private PointF[] mPointSequence = null;

    private static final int MAX_DIVIDED = 100;
    private float mProgressIncrement = 1;

    private float incrementY;
    private float incrementX;

    public BezierCurve setPointSequence(PointF[] pointSequence) {
        mPointSequence = pointSequence;
        return this;
    }

    /**
     * Progress increment that means how many times calculate for creating the Bezier Curve.
     * The value is 1.0 as default. So, progress is 0.0 on initial, and become 1.0, 2.0, 3.0, ..., 100.0, then over.
     * Every time, we will only create one Bezier Curve Point. so there are 100 points as default.
     * If you want to let your curve be more smooth, set progressIncrement to less value.
     * */
    public BezierCurve progressWith(float progressIncrement) {
        mProgressIncrement = progressIncrement;
        return this;
    }

    public List<PointF> build() {
        if (mPointSequence.length <= 1) {
            //only one point, no curve.
            Log.e(LOG_TAG, "only one point, could not create bezier curve. please check the parameter of function setPointSequence().");
            return null;
        }
        if (mCurvePoints == null) {
            mCurvePoints = new ArrayList<PointF>();
        } else {
            mCurvePoints.clear();
        }

        for (float i = 0; i <= MAX_DIVIDED; i += mProgressIncrement) {
            calculate(i, mPointSequence);
        }

        return mCurvePoints;
    }

    private void calculate(float progress, PointF[] pointList) {
        if (pointList.length == 2) {
            mCurvePoints.add(getPoint(progress, pointList[0], pointList[1]));
        } else if (pointList.length > 2) {
            PointF[] tempList = new PointF[pointList.length - 1];
            for (int i = 0; i < pointList.length - 1; i++) {
                tempList[i] = getPoint(progress, pointList[i], pointList[i + 1]);
            }
            calculate(progress, tempList);
        } else {
            //only one point, bad parameters.
            return;
        }
    }

    private PointF getPoint(float processPercent, PointF startPoint, PointF endPoint) {
        incrementY = (endPoint.y - startPoint.y) / MAX_DIVIDED;
        incrementX = (endPoint.x - startPoint.x) / MAX_DIVIDED;

        PointF p = new PointF();
        p.x = startPoint.x + processPercent * incrementX;
        p.y = startPoint.y + processPercent * incrementY;
        return p;
    }
}
