package com.krosshuang.krosslib.controller;

import android.app.Activity;
import android.os.Bundle;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.view.TestView;

/**
 *
 * Created by krosshuang on 2015/11/24.
 */
public class TestAllApiActivity extends Activity{

    private TestView mTestView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mTestView = (TestView) findViewById(R.id.testview);

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
