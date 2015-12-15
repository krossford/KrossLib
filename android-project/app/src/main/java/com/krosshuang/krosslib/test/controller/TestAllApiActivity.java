package com.krosshuang.krosslib.test.controller;

import android.app.Activity;
import android.os.Bundle;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.view.BottomLoadListView;
import com.krosshuang.krosslib.lib.view.TestView;

/**
 *
 * Created by krosshuang on 2015/11/24.
 */
public class TestAllApiActivity extends Activity{

    private TestView mTestView = null;
    private BottomLoadListView mBottomLoadListView = null;
    private TestAdapter mAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        mBottomLoadListView = (BottomLoadListView)findViewById(R.id.bottom_load_listview);
        mAdapter = new TestAdapter(this);
        mBottomLoadListView.setAdapter(mAdapter);

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


}
