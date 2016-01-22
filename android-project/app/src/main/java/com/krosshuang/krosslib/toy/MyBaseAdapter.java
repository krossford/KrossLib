package com.krosshuang.krosslib.toy;

import android.view.View;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2016/1/22.
 */
public abstract class MyBaseAdapter {

    public interface OnAdapterDataChangedListener {
        void onDataChanged();
        void onDataInvalidate();
    }

    private ArrayList<OnAdapterDataChangedListener> mListenerList = new ArrayList<OnAdapterDataChangedListener>();

    /**
     * get count of your data source.
     * */
    public abstract int getCount();

    public abstract View getView(View convertView, int index);

    public abstract int getViewType(int index);

    public void notifyDataChanged() {
        for (OnAdapterDataChangedListener l : mListenerList) {
            l.onDataChanged();
        }
    }

    public void notifyDataInvalidate() {
        for (OnAdapterDataChangedListener l : mListenerList) {
            l.onDataInvalidate();
        }
    }

    public void addAdapterDataChangedListener(OnAdapterDataChangedListener l) {
        if (!mListenerList.contains(l)) {
            mListenerList.add(l);
        }
    }
}
