package com.krosshuang.krosslib.lib.graphics;

import android.graphics.PointF;
import android.util.Log;

import com.krosshuang.krosslib.lib.math.PolarPointF;

/**
 *
 * Created by krosshuang on 2015/10/22.
 */
public class BezierControlPoint {

    private static final String LOG_TAG = "BezierControlPoint";

    public static final int TYPE_PREV = 0;
    public static final int TYPE_VERTEX = 1;
    public static final int TYPE_NEXT = 2;

    public PointF prevPoint = new PointF();
    public PointF nextPoint = new PointF();
    public PointF vertex = new PointF();
    public boolean isSingle = false;


    public static BezierControlPoint createSingle(float x, float y) {
        BezierControlPoint p = new BezierControlPoint();
        p.isSingle = true;
        p.vertex.x = x;
        p.vertex.y = y;
        return p;
    }

    /**
     * create multi control point
     * */
    public static BezierControlPoint createMulti(float vertexX, float vertexY, float nextX, float nextY) {
        BezierControlPoint p = new BezierControlPoint();
        p.isSingle = false;
        p.vertex.x = vertexX;
        p.vertex.y = vertexY;
        p.nextPoint.x = nextX;
        p.nextPoint.y = nextY;
        p.prevPoint.x = 2 * p.vertex.x - p.nextPoint.x;
        p.prevPoint.y = 2 * p.vertex.y - p.nextPoint.y;
        return p;
    }

    /**
     * move vertex
     * */
    public void moveVertex(float x, float y) {
        Log.i(LOG_TAG, "moveVertex");
        float deltaX = x - vertex.x;
        float deltaY = y - vertex.y;
        vertex.x = x;
        vertex.y = y;
        prevPoint.x += deltaX;
        prevPoint.y += deltaY;
        nextPoint.x += deltaX;
        nextPoint.y += deltaY;
    }

    public void moveNext(float x, float y, boolean isMirror) {
        Log.i(LOG_TAG, "moveNext x: " + x + " y: " + y + " isMirror: " + isMirror);
        if (isMirror) {
            float deltaX = x - vertex.x;
            float deltaY = y - vertex.y;
            prevPoint.x = vertex.x - deltaX;
            prevPoint.y = vertex.y - deltaY;
            nextPoint.x = x;
            nextPoint.y = y;
            return;
        } else {
            float nextEndX = x - vertex.x;
            float nextEndY = y - vertex.y;
            float relativePrevX = prevPoint.x - vertex.x;
            float relativePrevY = prevPoint.y - vertex.y;

            PolarPointF polarPrev = PolarPointF.parseFromCartesianCoordinatePoint(relativePrevX, relativePrevY);
            PolarPointF polarNextEnd = PolarPointF.parseFromCartesianCoordinatePoint(nextEndX, nextEndY);

            polarPrev.angle =  - (float)Math.PI + polarNextEnd.angle;

            prevPoint = polarPrev.toCartesianCoordinatePoint();
            prevPoint.x = prevPoint.x + vertex.x;
            prevPoint.y = prevPoint.y + vertex.y;
            nextPoint.x = x;
            nextPoint.y = y;
        }
        //Log.i(LOG_TAG, "prev: " + prevPoint.toString() + " vertex: " + vertex.toString() + " next: " + nextPoint.toString());
    }

    public void movePrev(float x, float y, boolean isMirror) {
        Log.i(LOG_TAG, "movePrev");
        if (isMirror) {
            float deltaX = x - vertex.x;
            float deltaY = y - vertex.y;
            prevPoint.x = vertex.x - deltaX;
            prevPoint.y = vertex.y - deltaY;
            nextPoint.x = x;
            nextPoint.y = y;
            return;
        } else {
            float prevEndX = x - vertex.x;
            float prevEndY = y - vertex.y;
            float relativeNextX = nextPoint.x - vertex.x;
            float relativeNextY = nextPoint.y - vertex.y;

            PolarPointF polarNext = PolarPointF.parseFromCartesianCoordinatePoint(relativeNextX, relativeNextY);
            PolarPointF polarPrevEnd = PolarPointF.parseFromCartesianCoordinatePoint(prevEndX, prevEndY);

            polarNext.angle =  - (float)Math.PI + polarPrevEnd.angle;

            nextPoint = polarNext.toCartesianCoordinatePoint();
            nextPoint.x = nextPoint.x + vertex.x;
            nextPoint.y = nextPoint.y + vertex.y;
            prevPoint.x = x;
            prevPoint.y = y;
        }
    }
}
