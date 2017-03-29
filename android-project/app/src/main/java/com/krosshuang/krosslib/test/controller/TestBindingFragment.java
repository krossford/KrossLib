package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.framework.binding.DataHolder;
import com.krosshuang.krosslib.lib.framework.binding.ObserverData;
import com.krosshuang.krosslib.lib.framework.binding.ViewHolder;

/**
 * Created by krosshuang on 2017/3/28.
 */

public class TestBindingFragment extends Fragment {

    private static final String LOG_TAG = "TestBindingFragment";

    private class DS extends DataHolder {

        public DS(ViewHolder vh) {
            super(vh);
        }

        ObserverData<String> string1;
        ObserverData<String> string2;
        ObserverData<String> string3;
        ObserverData<Integer> int4;

    }

    private class VH extends ViewHolder {

        private void init() {
            installView(R.id.text1);
            installView(R.id.text2);
            installView(R.id.text3);
            installView(R.id.text4);
            installView(R.id.btn1);
            installView(R.id.btn2);
            installView(R.id.btn3);
            installView(R.id.btn4);
        }

        @Override
        protected void onViewUpdate(View view) {
            if (view == null) {
                Log.w(LOG_TAG, "onViewUpdate fuck, view is null!!!!");
                return;
            }

            Log.i(LOG_TAG, view.getId() + "");
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.btn1:
                    mData.string1.set("btn1 " + System.currentTimeMillis());
                    mData.notifyViewUpdate();
                    break;
                case R.id.btn2:
                    mData.string2.set("btn2 " + System.currentTimeMillis());
                    mData.notifyViewUpdate();
                    break;
                case R.id.btn3:
                    mData.string3.set("btn3 " + System.currentTimeMillis());
                    mData.notifyViewUpdate();
                    break;
                case R.id.btn4:
                    mData.int4.set((int) (System.currentTimeMillis() / 1000L));
                    mData.notifyViewUpdate();
                    break;
            }
        }
    }

    private VH mViews = new VH();
    private DS mData = new DS(mViews);

    //private DataHolder mBinding = new DataHolder(mViews);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mData.string1 = mData.create("string1", R.id.text1);
        mData.string2 = mData.create("string2", R.id.text1, R.id.text2);
        mData.string3 = mData.create("string3", R.id.text1, R.id.text2, R.id.text3);
        mData.int4 = mData.create(9999, R.id.text1, R.id.text2, R.id.text3, R.id.text4);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_test_binding, null);
        mViews.setRoot(v);
        mViews.init();
        return v;
    }
}
