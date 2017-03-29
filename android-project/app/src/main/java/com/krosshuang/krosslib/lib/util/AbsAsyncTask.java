package com.krosshuang.krosslib.lib.util;

import android.os.SystemClock;
import android.util.Log;

/**
 * 主要用于处理超时的情况
 * Created by krosshuang on 2017/1/10.
 */

public abstract class AbsAsyncTask {

    private static final String LOG_TAG = "AbsAsyncTask";

    private long TIME_OUT_DURING = 30 * 1000;

    public static final int STATUS_IDLE = 0;                   // idle status
    public static final int STATUS_RUNNING = 100;              // running status
    public static final int STATUS_DEAD_JOB_FINISHED = 200;    // dead, job finished
    public static final int STATUS_DEAD_TIMEOUT = 201;         // dead, timeout
    public static final int STATUS_DEAD_CANCELED = 202;        // dead, canceled

    public static final int CALLBACK_COUNT_DEFAULT = 1;

    private int mCallbackMaxCount = CALLBACK_COUNT_DEFAULT;
    private int mCallbackCount = 0;

    private int mStatus = STATUS_IDLE;

    private long startTime = -1;

    /**
     * Start the task
     * @return true me ans task bas been started, false means fails
     * */
    public final boolean start() {
        if (changeStatusTo(STATUS_RUNNING)) {
            startTime = SystemClock.elapsedRealtime();
            AbsAsyncTaskManager.INSTANCE.startReq(this);
            work();
            return true;
        } else {
            Log.w(LOG_TAG, "AbsAsyncTask.start failure, this req's status is not idle");
            return false;
        }
    }

    /**
     * Set callback count
     * */
    public final void setCallbackCount(int count) {
        mCallbackMaxCount = count;
    }

    protected abstract void work();

    public final int status() {
        return mStatus;
    }

    final boolean checkTimeOut() {
        boolean isTimeout = false;
        if (startTime == -1) {
            isTimeout = false;
        } else {
            if (SystemClock.elapsedRealtime() - startTime > TIME_OUT_DURING) {
                isTimeout = true;
            } else {
                isTimeout = false;
            }
        }

        return isTimeout;
    }

    protected abstract void onTimeout();

    /**
     * 设置超时
     * */
    protected void setTimeout(long duration) {
        TIME_OUT_DURING = duration;
    }

    public long getTimeout() {
        return TIME_OUT_DURING;
    }

    void invokeTimeout() {
        changeStatusTo(STATUS_DEAD_TIMEOUT);
        onTimeout();
    }

    public boolean isRunning() {
        return mStatus == STATUS_RUNNING;
    }

    public boolean isTimeout() {
        return mStatus == STATUS_DEAD_TIMEOUT;
    }

    public boolean isCanceled() {
        return mStatus == STATUS_DEAD_CANCELED;
    }

    private boolean isDead() {
        switch (mStatus) {
            case STATUS_DEAD_CANCELED:
            case STATUS_DEAD_JOB_FINISHED:
            case STATUS_DEAD_TIMEOUT:
                return true;
            case STATUS_IDLE:
            case STATUS_RUNNING:
            default:
                return false;
        }
    }

    /**
     * Call this method on first line in your callback method
     * @return true means this invocation you don't need to handle
     * */
    protected boolean needSkip() {
        if (isDead()) {
            return true;
        } else {
            mCallbackCount++;
            if (mCallbackCount >= mCallbackMaxCount) {
                changeStatusTo(STATUS_DEAD_JOB_FINISHED);
            }
            return false;
        }
    }

    public void cancel() {
        changeStatusTo(STATUS_DEAD_CANCELED);
    }

    /**
     * Change status
     * @return true means status changed successful
     * */
    private boolean changeStatusTo(int status) {
        boolean result = false;
        switch (mStatus) {
            case STATUS_IDLE:
                if (status == STATUS_RUNNING) {
                    mStatus = STATUS_RUNNING;
                    result = true;
                } else {
                    result = false;
                }
                break;
            case STATUS_RUNNING:
                if (status == STATUS_DEAD_CANCELED || status == STATUS_DEAD_JOB_FINISHED || status == STATUS_DEAD_TIMEOUT) {
                    mStatus = status;
                    result = true;
                } else {
                    result = false;
                }
                break;
            case STATUS_DEAD_CANCELED:
                result = false;
                break;
            case STATUS_DEAD_JOB_FINISHED:
                result = false;
                break;
            case STATUS_DEAD_TIMEOUT:
                result = false;
                break;
        }

        if (!result) {
            Log.w(LOG_TAG, "AbsAsyncTask.changeStatusTo from: " + mStatus + " to: " + status);
        }

        return result;
    }
}