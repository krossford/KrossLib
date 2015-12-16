package com.krosshuang.krosslib.test.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.krosshuang.krosslib.R;

/**
 * Created by krosshuang on 2015/12/16.
 */
public class ShowFragmentActivity extends FragmentActivity {

    private static String sFragmentName = null;

    private FragmentManager mFragmentManager = null;
    private Fragment mFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_fragment);

        mFragmentManager = getSupportFragmentManager();

        try {
            Class<?> cls = getClassLoader().loadClass("com.krosshuang.krosslib.test.controller." + sFragmentName);
            mFragment = (Fragment)cls.newInstance();
            FragmentTransaction ft = mFragmentManager.beginTransaction();
            ft.add(R.id.fragment_container, mFragment);
            ft.show(mFragment);
            ft.commit();
        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    public static void start(Context from, String fragment) {
        sFragmentName = fragment;
        from.startActivity(new Intent(from, ShowFragmentActivity.class));
    }
}
