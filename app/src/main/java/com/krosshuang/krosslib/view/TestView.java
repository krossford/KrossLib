package com.krosshuang.krosslib.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.krosshuang.krosslib.graphics.Primitive2D;

/**
 * Created by krosshuang on 2015/11/25.
 */
public class TestView extends View{

    private static final String LOG_TAG = "TestView";

    private RectF rect = new RectF();
    private Point[] list = null;
    private Paint paint = new Paint();

    private Point start = new Point();
    private Point end = new Point();
    private PointF center = new PointF();


    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        /*
        canvas.clipRect(rect);
        canvas.drawLine(10, 10, 500, 500, new Paint());
        */

        if (list != null) {
            paint.setColor(Color.BLACK);
            canvas.drawLine(this.start.x + 10, this.start.y + 10, this.end.x + 10, this.end.y + 10, paint);

            paint.setColor(Color.RED);
            for (Point p : list) {
                canvas.drawPoint(p.x, p.y, paint);
            }
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                center.x = event.getX();
                center.y = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //setLine(new Point(360, 640), new Point((int) event.getX(), (int) event.getY()));
                setCircle(event.getX(), event.getY());
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }


    public void setLine(Point start, Point end) {
        this.start = start;
        this.end = end;
        //long time = SystemClock.uptimeMillis();
        list = Primitive2D.line(start, end);
        //Log.i("bresenham", "cast time: " + (SystemClock.uptimeMillis() - time));
    }

    private void setCircle(float x, float y) {
        list = Primitive2D.circle((int)center.x, (int)center.y, (float)Math.hypot(x - center.x, y - center.y));
        //Log.i(LOG_TAG, "circle point list: " + Arrays.toString(list));
    }



    public void startAnimation() {
        ValueAnimator va = ValueAnimator.ofFloat(0, 500);
        va.setDuration(1000);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Log.i("testview" , "onAnimationUpdate");
                rect.left = 0;
                rect.top = 0;
                rect.bottom = 500;
                rect.right = rect.right + 1;
                invalidate();

            }
        });
        va.start();
    }
}
