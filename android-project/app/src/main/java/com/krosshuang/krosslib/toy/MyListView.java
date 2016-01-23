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

    //TODO this needs checking whether it works well.
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

    /**
     * 当前，第一个可见的item的top值
     * write by onTouch
     * */
    private int mFirstItemTop = 0;

    /**
     * 当前，第一个可见的Item在数据中的index
     * write by onLayout
     * */
    private int mCurrentFirstVisibleIndex = 0;

    /**
     * write by onMeasure
     * */
    private int mCurrentVisibleItemCount = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //1.可以知道自己的尺寸，也就是这个ListView的尺寸
        //2.决定mChildren里面有几个View
        //3.决定了visible view count有多少个

        View temp = null;
        int consumedHeight = mFirstItemTop;
        mCurrentVisibleItemCount = 0;
        removeAllViews();

        for (int i = mCurrentFirstVisibleIndex; i < mAdapter.getCount(); i++) {
            temp = mAdapter.getView(getViewFromCachePool(mAdapter.getViewType(i)), i);
            temp.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec), MeasureSpec.UNSPECIFIED));
            mCurrentVisibleItemCount++;
            addView(temp);
            consumedHeight += temp.getMeasuredHeight();
            if (consumedHeight >= getMeasuredHeight()) {
                break;
            }
        }

        Log.i(LOG_TAG, "call onMeasure: " + " firstIndex: " + mCurrentFirstVisibleIndex + " visibleCount: " + mCurrentVisibleItemCount + " mChildren.count: " + getChildCount());
    }



    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        Log.i(LOG_TAG, "call onLayout: " + l + ", " + t + ", " + r + ", " + b);

        //1.负责把mChildren都摆上去就完事了
        //2.决定first visible item view在数据中的index

        View temp;
        int type;
        int consumedHeight = 0;
        int itemTop;
        int itemBottom;

        for (int i = 0; i < getChildCount(); i++) {

            temp = getChildAt(i);

            type = mAdapter.getViewType(i + mCurrentFirstVisibleIndex);

            itemTop = mFirstItemTop + consumedHeight;
            itemBottom = mFirstItemTop + consumedHeight + temp.getMeasuredHeight();

            if (i == 0 && itemTop <= 0 && itemBottom <= 0) {
                putViewToCachePool(type, temp);
                consumedHeight = 0;
                mFirstItemTop = 0;
                mCurrentFirstVisibleIndex++;
            } else if (i == 0 && itemTop > 0 && mCurrentFirstVisibleIndex > 0) {
                mFirstItemTop = mFirstItemTop - temp.getMeasuredHeight();
                mCurrentFirstVisibleIndex--;

            }

            temp.layout(0, itemTop, temp.getMeasuredWidth(), itemBottom);
            consumedHeight = consumedHeight + temp.getHeight();
        }
        Log.i(LOG_TAG, "mCurrentFirstVisibleIndex: " + mCurrentFirstVisibleIndex + " mFirstItemTop: " + mFirstItemTop);
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

    private void putViewToCachePool(int type, View view) {
        if (mViewCachePool.get(type) == null) {
            mViewCachePool.put(type, new LinkedList<View>());
        }

        mViewCachePool.get(type).push(view);
    }

    private float mDownY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i(LOG_TAG, "call onTouchEvent");
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float delta = event.getY() - mDownY;
                mFirstItemTop = (int)(mFirstItemTop + delta);
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        requestLayout();
        return true;
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
