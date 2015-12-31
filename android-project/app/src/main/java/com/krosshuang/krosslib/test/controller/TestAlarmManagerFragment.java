package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krosshuang.krosslib.R;

/**
 * Created by krosshuang on 2015/12/30.
 */
public class TestAlarmManagerFragment extends Fragment implements View.OnClickListener{

    private static final String LOG_TAG = "TestAlarmManagerFragment:kross";

    private View mRoot = null;
    private TextView view = null;

    private int i = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_test_alarm_manager, null);
        view = (TextView) mRoot.findViewById(R.id.tv_test);
        view.setOnClickListener(this);
        return mRoot;
    }

    @Override
    public void onClick(View v) {
        /*
        AlarmManager am = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = PendingIntent.getActivity()
        am.set(AlarmManager.RTC, System.currentTimeMillis() + 5000L, );
        */

        v.post(new MyRun());

//        new Thread(){
//            @Override
//            public void run() {
//                int i = 0;
//                while (true) {
//
//                    try {
//                        Thread.sleep(1000);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }.start();
    }

    class MyRun implements Runnable {
        @Override
        public void run() {
            //Log.i(LOG_TAG, "do: " + i++);
            view.postDelayed(this, 1000);
        }
    }
}
