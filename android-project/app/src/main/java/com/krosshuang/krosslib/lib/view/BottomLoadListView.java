package com.krosshuang.krosslib.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 底部加载ListView
 * 已覆盖AbsListView.OnScrollListener，请使用BottomLoadListViewListener
 * Created by krosshuang on 2015/12/15.
 */
public class BottomLoadListView extends ListView implements AbsListView.OnScrollListener{

    private static final String LOG_TAG = "BottomLoadListView";

    public interface BottomLoadListViewListener extends OnScrollListener {
        void onTriggerLoad();
    }

    public static final int TRIGGER_MODE_TOP = 1;
    public static final int TRIGGER_MODE_BOTTOM = 2;

    private BottomLoadListViewListener mListener = null;

    private View mBottomLoadingView = null;

    private int mTriggerMode = TRIGGER_MODE_TOP;

    public BottomLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnScrollListener(this);
    }

    /**
     * 设置触发模式
     * */
    public void setTriggerMode(int triggerMode) {
        mTriggerMode = triggerMode;
    }

    public void setBottomLoadingView(View v) {
        mBottomLoadingView = v;
        addFooterView(mBottomLoadingView);
    }

    public void setListener(BottomLoadListViewListener l) {
        mListener = l;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mListener != null) {
            mListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mListener != null) {
            mListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mBottomLoadingView != null) {
            Log.i(LOG_TAG, "top: " + mBottomLoadingView.getTop());

            if (mTriggerMode == TRIGGER_MODE_TOP) {

                if (mBottomLoadingView.getTop() != 0 && mBottomLoadingView.getTop() < getBottom()) {
                    if (mListener != null) {
                        mListener.onTriggerLoad();
                    }
                }
            } else if (mTriggerMode == TRIGGER_MODE_BOTTOM) {
                if (mBottomLoadingView.getBottom() == getBottom()) {
                    if (mListener != null) {
                        mListener.onTriggerLoad();
                    }
                }
            }
        }
    }

}
