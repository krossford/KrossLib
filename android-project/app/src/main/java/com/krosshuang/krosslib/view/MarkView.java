package com.krosshuang.krosslib.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.krosshuang.krosslib.graphics.Element;
import com.krosshuang.krosslib.graphics.ImageProcess;
import com.krosshuang.krosslib.graphics.RectElement;

import java.util.ArrayList;

/**
 * A view that can makes some marks on a image.
 * There are kinds of tool can be chose.
 * Created by krosshuang on 2015/10/26.
 */
public class MarkView extends View {

    private static final String LOG_TAG = "MarkView";

    //tools type
    public static final int TOOL_SELECT = 0;
    public static final int TOOL_PEN = 1;
    public static final int TOOL_TEXT = 2;
    public static final int TOOL_CIRCLE = 3;
    public static final int TOOL_RECTANGLE = 4;

    private int mCurrentTool = TOOL_SELECT;
    private float mLastDownX;
    private float mLastDownY;
    private float mActiveBorderWidth = 3;

    private Paint mBitmapPaint = null;
    private Paint mElementPaint = null;
    private Paint mActivePaint = null;

    private Bitmap mImage = null;
    private RectF mImageRect = null;

    private Element mLastCreateElement = null;
    private ArrayList<Element> mElementList = new ArrayList<>();

    public MarkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mBitmapPaint = new Paint();

        mElementPaint = new Paint();
        mElementPaint.setColor(Color.RED);
        mElementPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mElementPaint.setStrokeWidth(10);

        mActivePaint = new Paint();
        mActivePaint.setColor(Color.BLUE);
        mActivePaint.setStrokeWidth(mActiveBorderWidth);
        mActivePaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(LOG_TAG, "onDraw");
        if (mImage != null) {
            canvas.drawBitmap(mImage, null, mImageRect, mBitmapPaint);
        }

        for (Element e : mElementList) {
            switch (e.type) {
                case Element.TYPE_RECTANGLE:
                    RectElement rectElement = (RectElement)e;
                    mElementPaint.setStrokeWidth(rectElement.borderWidth);
                    canvas.drawRect(rectElement.rectangle, mElementPaint);
                    if (rectElement.isSelected) {
                        canvas.drawRect(rectElement.rectangle.left - mActiveBorderWidth,
                                rectElement.rectangle.top - mActiveBorderWidth,
                                rectElement.rectangle.right + mActiveBorderWidth,
                                rectElement.rectangle.bottom + mActiveBorderWidth,
                                mActivePaint);
                    }
                    break;
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(LOG_TAG, "onTouchEvent");
        switch (mCurrentTool) {
            case TOOL_SELECT:
                return onTouchWithSelect(event);
            case TOOL_RECTANGLE:
                return onTouchWithRectangle(event);
            case TOOL_CIRCLE:
                return onTouchWithCircle(event);

        }
        return true;
    }

    private boolean onTouchWithCircle(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }

        return true;
    }

    private boolean onTouchWithSelect(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownX = event.getX();
                mLastDownY = event.getY();
                for (Element e : mElementList) {
                    if (e.checkSelect(mLastDownX, mLastDownY)) {
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
        }
        return true;
    }

    private boolean onTouchWithRectangle(MotionEvent event) {
        Log.i(LOG_TAG, "onTouchWithRectangle");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastDownX = event.getX();
                mLastDownY = event.getY();
                mLastCreateElement = new RectElement();
                ((RectElement)mLastCreateElement).rectangle = new RectF(mLastDownX, mLastDownY, mLastDownX, mLastDownY);
                mElementList.add(mLastCreateElement);
                break;
            case MotionEvent.ACTION_MOVE:
                ((RectElement) mLastCreateElement).rectangle.left = Math.min(event.getX(), mLastDownX);
                ((RectElement) mLastCreateElement).rectangle.top = Math.min(event.getY(), mLastDownY);
                ((RectElement)mLastCreateElement).rectangle.right = Math.max(event.getX(), mLastDownX);
                ((RectElement)mLastCreateElement).rectangle.bottom = Math.max(event.getY(), mLastDownY);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return true;
    }

    public void setCurrentTool(int tool) {
        mCurrentTool = tool;
    }

    public boolean hasImage() {
        return (mImage == null) ? false : true;
    }

    public void loadImage(Bitmap bitmap) {
        mImage = bitmap;
        long t = SystemClock.uptimeMillis();
        mElementPaint.setShader(new BitmapShader(ImageProcess.fastMosaic(mImage), Shader.TileMode.MIRROR, Shader.TileMode.MIRROR));
        Log.i(LOG_TAG, "mosaic cast: " + (SystemClock.uptimeMillis() - t));
        if (mImage.getWidth() <= getWidth() && mImage.getHeight() <= getHeight()) {
            mImageRect = new RectF(0, 0, mImage.getWidth(), mImage.getHeight());
        } else {
            //scale
            float viewAspectRadio = (float)getHeight() / (float)getWidth();
            float imageAspectRadio = (float)mImage.getHeight() / (float)mImage.getWidth();
            Log.i(LOG_TAG, viewAspectRadio + " - " + imageAspectRadio);

            if (viewAspectRadio < imageAspectRadio) {
                mImageRect = new RectF(0, 0, getHeight() / imageAspectRadio, getHeight());
            } else {
                mImageRect = new RectF(0, 0, getWidth(), getWidth() / imageAspectRadio);
            }
        }
    }
}
