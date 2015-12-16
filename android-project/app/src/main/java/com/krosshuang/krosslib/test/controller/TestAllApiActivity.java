package com.krosshuang.krosslib.test.controller;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.view.BottomLoadListView;
import com.krosshuang.krosslib.lib.view.TestView;

/**
 *
 * Created by krosshuang on 2015/11/24.
 */
public class TestAllApiActivity extends Activity implements BottomLoadListView.BottomLoadListViewListener{

    private final static String LOG_TAG = "TestAllApiActivity";

    private TestView mTestView = null;
    private BottomLoadListView mBottomLoadListView = null;
    private TestAdapter mAdapter = null;

    private MyHandler mHandler = new MyHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        mBottomLoadListView = (BottomLoadListView)findViewById(R.id.bottom_load_listview);
        mAdapter = new TestAdapter(this);
        mBottomLoadListView.setAdapter(mAdapter);

        ProgressBar pb = new ProgressBar(this);
        pb.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mBottomLoadListView.setBottomLoadingView(pb);
        mBottomLoadListView.setListener(this);
        mBottomLoadListView.setTriggerMode(BottomLoadListView.TRIGGER_MODE_BOTTOM);

        /*
        mBottomLoadListView.hideBottomLoadingView();
        mBottomLoadListView.showBottomLoadingView();
        */


        //mTestView = (TestView) findViewById(R.id.testview);

        /*
        mTestView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTestView.setLine(new Point(360, 640), new Point(500, 750));
                mTestView.invalidate();
            }
        });

        */

    }


    @Override
    public void onTriggerLoad() {
        Log.i(LOG_TAG, "onTriggerLoad");
        Toast.makeText(this, "onTriggerLoad", Toast.LENGTH_SHORT).show();


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

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
