package com.krosshuang.krosslib.lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/*
 如何使用？

 > 1.你需要 implement BottomLoadListViewListener 接口

 > 2.初始化 BottomLoadListView

 mBottomLoadListView = findViewById(...);

 ProgressBar pb = new ProgressBar(this);
 pb.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

 mBottomLoadListView.setBottomLoadingView(pb);
 mBottomLoadListView.setTriggerMode(BottomLoadListView.TRIGGER_MODE_BOTTOM);

 mBottomLoadListView.setListener(this);

 > 3.在 onTriggerLoad() 回调中做你想做的事情

 */

/**
 * 底部加载ListView，当滑动到底部，显示出自定义的loadingView，给回调来让你加载数据
 * 已覆盖AbsListView.OnScrollListener，请使用BottomLoadListViewListener
 * Created by krosshuang on 2015/12/15.
 * update log
 * 1.让这个view的setOnScrollListener方法仍然保持默认的使用方式 2015-12-17 11:06:21
 */
public class BottomLoadListView extends ListView implements AbsListView.OnScrollListener{

    private static final String LOG_TAG = "BottomLoadListView";

    public interface BottomLoadListViewListener {

        /**
         * 根据 TRIGGER_MODE_* 的配置，当达到触发条件时触发一次
         * */
        void onTriggerLoad();
    }

    /**
     * 当 loadingView 顶部刚显示出来就触发
     * */
    public static final int TRIGGER_MODE_TOP = 1;

    /**
     * 当 loadingView 底部显示出来才触发
     * */
    public static final int TRIGGER_MODE_BOTTOM = 2;

    private static final int STATUS_CAN_NOT_SEE = 1;    //不能看见BottomLoadingView
    private static final int STATUS_CAN_SEE_PART = 2;   //只能看见一部分BottomLoadingView
    private static final int STATUS_CAN_SEE_ALL = 3;    //能看见整个BottomLoadingView

    private BottomLoadListViewListener mListener = null;
    private OnScrollListener mOnScrollListener = null;

    private View mBottomLoadingView = null;

    private int mTriggerMode = TRIGGER_MODE_TOP;
    private int mStatus = STATUS_CAN_NOT_SEE;
    private int mLastTop = 0;
    private int mSameCount = 0;
    private boolean mIsBottomLoadingViewHide = false;

    public BottomLoadListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOnScrollListener(this);
    }

    /**
     * 设置触发模式
     * @see #TRIGGER_MODE_TOP
     * @see #TRIGGER_MODE_BOTTOM
     * */
    public void setTriggerMode(int triggerMode) {
        mTriggerMode = triggerMode;
    }

    /**
     * set loading view and showBottomLoadingView
     * */
    public void setBottomLoadingView(View v) {
        if (getFooterViewsCount() != 0 && mBottomLoadingView != null) {
            super.removeFooterView(mBottomLoadingView);
        }
        mBottomLoadingView = v;
        super.addFooterView(mBottomLoadingView);
        mIsBottomLoadingViewHide = false;
    }

    public void hideBottomLoadingView() {
        if (mBottomLoadingView != null && !mIsBottomLoadingViewHide) {
            super.removeFooterView(mBottomLoadingView);
        }
        mIsBottomLoadingViewHide = true;
    }

    public void showBottomLoadingView() {
        if (mBottomLoadingView != null && mIsBottomLoadingViewHide) {
            super.addFooterView(mBottomLoadingView);
        }
        mIsBottomLoadingViewHide = false;
    }

    public void setListener(BottomLoadListViewListener l) {
        mListener = l;
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mOnScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mOnScrollListener != null) {
            mOnScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if (mOnScrollListener != null) {
            mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        if (mBottomLoadingView != null) {

            //Log.i(LOG_TAG, "top: " + mBottomLoadingView.getTop() + " ListView bottom: " + getBottom());

            if (mTriggerMode == TRIGGER_MODE_TOP) {
                // ===============
                // top trigger mode
                // ===============
                switch (mStatus) {
                    //完全看不见
                    case STATUS_CAN_NOT_SEE:
                        //Log.i(LOG_TAG, "STATUS_CAN_NOT_SEE");
                        if (mBottomLoadingView.getTop() != 0 && mBottomLoadingView.getTop() < getBottom()) {
                            //当loading view 显示出来然后再滑上去，top会变成一个定值，如果定值小于 ListView.bottom ，就会一直触发这个，所以如果相等的话，就不触发
                            if (mLastTop == mBottomLoadingView.getTop()) {
                                mSameCount++;
                            } else {
                                mSameCount = 0;
                            }

                            if (mSameCount >= 1) {
                                mStatus = STATUS_CAN_NOT_SEE;
                            } else {
                                // 走到这里满足了以下条件：
                                // 1.loadingView.top != 0 不是初始化进来的情况
                                // 2.loadingView.top < ListView.bottom 说明loadingView是可见的，冒出了头
                                // 3.loadingView.top != lastTop top值不与上一次的相同，说明是可见的
                                mStatus = STATUS_CAN_SEE_PART;
                                if (mListener != null && !mIsBottomLoadingViewHide) {
                                    mListener.onTriggerLoad();
                                }
                            }
                        }
                        break;
                    //看得见一部分
                    case STATUS_CAN_SEE_PART:
                        //Log.i(LOG_TAG, "STATUS_CAN_SEE_PART");
                        //判断是否与上一次相同，如果top值不再变化，说明loadingView不可见了
                        if (mLastTop == mBottomLoadingView.getTop()) {
                            mSameCount++;
                        } else {
                            mSameCount = 0;
                        }

                        if (mSameCount >= 2) {
                            mStatus = STATUS_CAN_NOT_SEE;
                        }
                        break;
                    //完全看得见
                    case STATUS_CAN_SEE_ALL:
                        //Log.i(LOG_TAG, "STATUS_CAN_SEE_ALL");
                        break;
                }
            } else if (mTriggerMode == TRIGGER_MODE_BOTTOM) {
                // ===============
                // bottom trigger mode
                // ===============
                if (mBottomLoadingView.getBottom() == getBottom()) {
                    if (mLastTop == mBottomLoadingView.getTop()) {
                        mSameCount++;
                    } else {
                        mSameCount = 0;
                    }

                    if (mSameCount == 0) {
                        if (mListener != null && !mIsBottomLoadingViewHide) {
                            mListener.onTriggerLoad();
                        }
                    }
                }
            }
            mLastTop = mBottomLoadingView.getTop();
        }
    }


    /**
     * don't use this method
     * */
    @Deprecated
    @Override
    public void addFooterView(View v) { }

    /**
     * don't use this method
     * */
    @Deprecated
    @Override
    public boolean removeFooterView(View v) {
        return false;
    }

}
