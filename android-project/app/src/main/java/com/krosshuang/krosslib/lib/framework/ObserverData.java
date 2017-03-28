package com.krosshuang.krosslib.lib.framework;

/**
 * Created by krosshuang on 2017/3/28.
 */

public class ObserverData<T> {

    /**
     * 影响的View
     * */
    int[] mEffectViews = null;

    private DataHolder mBinding = null;

    T data;

    public ObserverData(DataHolder binding, int ... id) {
        mBinding = binding;
        mEffectViews = id;
    }

    public void effect(int[] viewIds) {
        mEffectViews = viewIds;
    }

    public void set(T newData) {
        data = newData;
        if (mBinding != null) {
            mBinding.effect(mEffectViews);
        }
    }

    public T get() {
        return data;
    }
}
