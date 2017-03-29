package com.krosshuang.krosslib.lib.framework.binding;

import android.app.Activity;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by krosshuang on 2017/3/28.
 */

public abstract class ViewHolder implements View.OnClickListener{

    private SparseArray<View> mViewMap = new SparseArray<>();

    private View mRoot = null;
    private Activity mRootA = null;

    public void setRoot(View v) {
        mRoot = v;
    }

    public void setRoot(Activity activity) {
        mRootA = activity;
    }

    protected void installView(int id) {
        installView(id, true);
    }

    protected void installView(int id, boolean needListener) {
        View v = null;
        if (mRoot != null) {
            v = mRoot.findViewById(id);
        }

        if (v == null) {
            if (mRootA != null) {
                v = mRootA.findViewById(id);
            }
        }

        if (v != null) {
            if (needListener) {
                v.setOnClickListener(this);
            }
        }

        mViewMap.put(id, v);
    }

    /**
     * get view
     */
    <T extends View> T getView(int id) {
        return (T) mViewMap.get(id);
    }

    protected abstract void onViewUpdate(View view);

    @Override
    public abstract void onClick(View v);
}
