package com.krosshuang.krosslib.math;

import android.graphics.PointF;

/**
 *
 * Created by krosshuang on 2015/10/23.
 */
public class PolarPointF {

    public float r;         //distance to reference point.
    public float angle;     //angle in radians.

    public static PolarPointF parseFromCartesianCoordinatePoint(float x, float y) {
        PolarPointF p = new PolarPointF();
        p.angle = (float)Math.atan2(y, x);
        p.r = (float)Math.sqrt(x * x + y * y);
        return p;
    }

    public PointF toCartesianCoordinatePoint() {
        PointF p = new PointF();
        p.x = r * (float)Math.cos(angle);
        p.y = r * (float)Math.sin(angle);
        return p;
    }
}
