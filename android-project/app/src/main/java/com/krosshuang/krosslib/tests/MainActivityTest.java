package com.krosshuang.krosslib.tests;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.test.controller.MainActivity;

/**
 * Created by krosshuang on 2016/1/1.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    private ListView mListView;


    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        mListView = (ListView) mMainActivity.findViewById(R.id.lv_list);
    }

    public void testWhat() throws Exception {
        assertNotNull("MainActivity is not null", mMainActivity);
        assertNotNull("ListView is not null", mListView);
    }
}
