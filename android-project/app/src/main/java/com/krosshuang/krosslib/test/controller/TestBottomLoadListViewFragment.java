package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.view.BottomLoadListView;

/**
 *
 * Created by krosshuang on 2015/11/24.
 */
public class TestBottomLoadListViewFragment extends Fragment implements BottomLoadListView.BottomLoadListViewListener, AbsListView.OnScrollListener{

    private final static String LOG_TAG = "TestAllApiActivity";

    private View mRoot = null;
    private BottomLoadListView mBottomLoadListView = null;
    private TestAdapter mAdapter = null;

    private MyHandler mHandler = new MyHandler();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_test_bottom_list_view, null);

        mBottomLoadListView = (BottomLoadListView)mRoot.findViewById(R.id.bottom_load_listview);
        mAdapter = new TestAdapter(getActivity());

        ProgressBar pb = new ProgressBar(getActivity());
        pb.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBottomLoadListView.setBottomLoadingView(pb);
        mBottomLoadListView.setAdapter(mAdapter);
        mBottomLoadListView.setListener(this);
        //mBottomLoadListView.setOnScrollListener(this);
        mBottomLoadListView.setTriggerMode(BottomLoadListView.TRIGGER_MODE_TOP);

        return mRoot;
    }

    @Override
    public void onTriggerLoad() {
        Log.i(LOG_TAG, "onTriggerLoad");
        Toast.makeText(getActivity(), "onTriggerLoad", Toast.LENGTH_SHORT).show();


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mHandler.obtainMessage().sendToTarget();
            }
        }).start();

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i(LOG_TAG, "onScrollStateChanged");
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i(LOG_TAG, "onScroll");
    }

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            mAdapter.addData();
            if (TestAdapter.mIndex == 100) {
                mBottomLoadListView.hideBottomLoadingView();
            }
        }
    }
}
