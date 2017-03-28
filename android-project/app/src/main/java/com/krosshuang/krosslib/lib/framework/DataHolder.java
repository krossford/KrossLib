package com.krosshuang.krosslib.lib.framework;

import android.util.SparseArray;

/**
 * Created by krosshuang on 2017/3/28.
 */

public abstract class DataHolder {

    private ViewHolder mViewHolder = null;
    private SparseArray<Integer> mNeedUpdateViews = null;

    public DataHolder(ViewHolder vh) {
        mViewHolder = vh;
        mNeedUpdateViews = new SparseArray<>();
    }

    public <T> ObserverData<T> create(T data, int ... ids) {
        return (ObserverData<T>) new ObserverData(this, ids);
    }

    public void notifyViewUpdate() {
        if (mNeedUpdateViews != null) {
            for (int i = 0; i < mNeedUpdateViews.size(); i++) {
                int id = mNeedUpdateViews.keyAt(i);
                mViewHolder.onViewUpdate(mViewHolder.getView(id));
            }
            mNeedUpdateViews.clear();
        }
    }

    void effect(int[] ids) {
        if (ids != null) {
            for (int id : ids) {
                mNeedUpdateViews.put(id, 0);
            }
        }
    }
}
