package com.krosshuang.krosslib.lib.util;

import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by krosshuang on 2017/1/10.
 */

public enum AbsAsyncTaskManager {
    INSTANCE;

    private static final long DETECT_INTERVAL = 1000;

    private static final String LOG_TAG = "AbsAsyncTaskManager";
    List<AbsAsyncTask> mReqList = new ArrayList<>();

    AbsAsyncTaskManager() {
        //Log.i(LOG_TAG, "AbsAsyncTaskManager.AbsAsyncTaskManager constructor");
        mHandler = new Handler();
    }

    private Handler mHandler = null;

    private boolean isCheckTimeoutRunning = false;

    private final Runnable checkTimeOut = new Runnable() {
        @Override
        public void run() {
            Log.i(LOG_TAG, "AbsAsyncTaskManager.run check time out... req size: " + mReqList.size());

            isCheckTimeoutRunning = true;

            Iterator<AbsAsyncTask> it = mReqList.iterator();
            AbsAsyncTask req = null;
            while (it.hasNext()) {
                req = it.next();
                switch (req.status()) {
                    case AbsAsyncTask.STATUS_IDLE:
                    case AbsAsyncTask.STATUS_DEAD_CANCELED:
                    case AbsAsyncTask.STATUS_DEAD_JOB_FINISHED:
                    case AbsAsyncTask.STATUS_DEAD_TIMEOUT:
                        it.remove();
                        break;
                    case AbsAsyncTask.STATUS_RUNNING:
                        if (req.checkTimeOut()) {
                            req.invokeTimeout();
                            it.remove();
                        }
                        break;
                }
            }

            if (!mReqList.isEmpty()) {
                mHandler.postDelayed(checkTimeOut, DETECT_INTERVAL);
            } else {
                isCheckTimeoutRunning = false;
                Log.i(LOG_TAG, "AbsAsyncTaskManager.run reqList is empty, died");
            }
        }
    };

    public void startReq(AbsAsyncTask req) {
        if (mReqList.isEmpty()) {
            mReqList.add(req);
        } else {
            if (!mReqList.contains(req)) {
                mReqList.add(req);
            }
        }

        if (!mReqList.isEmpty()) {
            if (!isCheckTimeoutRunning) {
                checkTimeOut.run();
            }
        }

    }
}
