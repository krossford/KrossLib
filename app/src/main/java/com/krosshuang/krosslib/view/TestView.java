package com.krosshuang.krosslib.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.krosshuang.krosslib.graphics.Bresenham;

/**
 * Created by krosshuang on 2015/11/25.
 */
public class TestView extends View{

    private RectF rect = new RectF();
    private Point[] list = null;
    private Paint paint = new Paint();

    private Point start;
    private Point end;

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
                break;
            case MotionEvent.ACTION_MOVE:
                setLine(new Point(360, 640), new Point((int) event.getX(), (int) event.getY()));
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
        list = Bresenham.line(start, end);
        //Log.i("bresenham", "cast time: " + (SystemClock.uptimeMillis() - time));
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
