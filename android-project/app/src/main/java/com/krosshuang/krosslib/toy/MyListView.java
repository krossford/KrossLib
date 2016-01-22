package com.krosshuang.krosslib.toy;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by krosshuang on 2016/1/22.
 */
public class MyListView extends ViewGroup implements MyBaseAdapter.OnAdapterDataChangedListener {

    private static final String LOG_TAG = "MyListView:kross";
    private MyBaseAdapter mAdapter = null;

    private HashMap<Integer, LinkedList<View>> mViewCachePool = new HashMap<Integer, LinkedList<View>>();

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    int type = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.i(LOG_TAG, "onMeasure: " + getMeasuredWidth() + ", " + getMeasuredHeight());

        View temp = null;
        int consumedHeight = 0;
        removeAllViews();

        for (int i = 0; i < mAdapter.getCount(); i++) {
            temp = mAdapter.getView(getViewFromCachePool(mAdapter.getViewType(i)), i);
            temp.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED));
            addView(temp);
            consumedHeight += temp.getMeasuredHeight();
            if (consumedHeight >= getMeasuredHeight()) {
                break;
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(LOG_TAG, "call onLayout: " + l + ", " + t + ", " + r + ", " + b);
        View temp;
        int castHeight = 0;
        for (int i = 0; i < getChildCount(); i++) {
            temp = getChildAt(i);
            temp.layout(0, castHeight, temp.getMeasuredWidth(), castHeight + temp.getMeasuredHeight());
            castHeight = castHeight + temp.getHeight();
            Log.i(LOG_TAG, "castHeight: " + castHeight);
        }
    }

    private View getViewFromCachePool(int type) {
        if (mViewCachePool.get(type) == null) {
            return null;
        } else if (mViewCachePool.get(type).size() == 0) {
            return null;
        } else {
            return mViewCachePool.get(type).poll();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    public void onDataChanged() {
        Log.i(LOG_TAG, "call onDataChanged");
    }

    @Override
    public void onDataInvalidate() {
        Log.i(LOG_TAG, "call onDataInvalidate");
    }

    public void setAdapter(MyBaseAdapter adapter) {
        mAdapter = adapter;
        adapter.addAdapterDataChangedListener(this);
        invalidate();
    }
}
