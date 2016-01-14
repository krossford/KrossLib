package com.krosshuang.krosslib.lib.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

/**
 * Created by krosshuang on 2016/1/13.
 */
public class DelayCallbackEditText extends EditText {

    private static final String LOG_TAG = "DelayCallbackEditText";

    public interface OnDelayCallbackListener {
        void onDelayCallback(CharSequence text);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_DELAY_CALLBACK:
                    if (mCallback != null) {
                        mCallback.onDelayCallback(getText().toString());
                    }
                    break;
            }
        }
    };

    private static final int WHAT_DELAY_CALLBACK = 1;

    private long maxInterval = 5000;
    private long lastTypeTime = 0;
    private CheckRunnable mRunnable = new CheckRunnable();
    private OnDelayCallbackListener mCallback = null;

    public DelayCallbackEditText(Context context) {
        super(context);
    }

    public DelayCallbackEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DelayCallbackEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DelayCallbackEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        Log.i(LOG_TAG, "onTextChanged");
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        lastTypeTime = System.currentTimeMillis();
        if (mRunnable != null) {
            mRunnable.start();
        }
    }

    public void setDelayCallback(OnDelayCallbackListener l) {
        mCallback = l;
    }

    private class CheckRunnable implements Runnable {

        private boolean allowRun = false;


        @Override
        public void run() {
            while (allowRun) {
                Log.i(LOG_TAG, "run");
                if (System.currentTimeMillis() - lastTypeTime > maxInterval) {
                    allowRun = false;
                    mHandler.obtainMessage(WHAT_DELAY_CALLBACK).sendToTarget();
                    return;
                } else {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void start() {
            if (allowRun) {
                return;
            } else {
                allowRun = true;
                new Thread(this).start();
            }
        }

        public void die() {
            allowRun = false;
        }

        public void updateTime() {

        }
    }
}
