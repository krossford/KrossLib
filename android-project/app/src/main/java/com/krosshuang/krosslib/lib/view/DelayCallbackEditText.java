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
 * <pre>
 * How to use it:
 * first, you just use it like {@link EditText}, don't worry about anything.
 *
 * 1.implement interface {@link OnDelayCallbackListener}.
 * 2.set callback, and delay time length.
 * mEditText = findViewById(...);
 * mEditText.setDelayCallback(callback, 3000);
 *
 * if you want to update the delay time only, like this:
 * mEditText.setDelay(2000);
 *
 * 3.do what you want in interface callback {@link OnDelayCallbackListener#onDelayCallback(CharSequence)}
 *
 * when user don't type text in EditText, after waiting a little time, we will give you a callback.
 * Created by krosshuang on 2016/1/13.
 * </pre>
 */
public class DelayCallbackEditText extends EditText {

    private static final String LOG_TAG = "DelayCallbackEditText";

    public interface OnDelayCallbackListener {
        void onDelayCallback(CharSequence text);
    }

    //TODO maybe not best way.
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

    private long maxInterval = 2000;
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

    /**
     * set callback listener and delay during.
     * @param l the callback.
     * @param delay invoking callback's waiting time length.
     * */
    public void setDelayCallback(OnDelayCallbackListener l, long delay) {
        mCallback = l;
        maxInterval = delay;
    }

    /**
     * set(update) the delay during.
     * @param delay invoking callback's waiting time length.
     * */
    public void setDelay(long delay) {
        maxInterval = delay;
    }

    //TODO when this view invisible(Activity finished or be covered), I think it's best that this runnable could be die.
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
                        Thread.sleep(maxInterval / 3);
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

        /**
         * update marker that tell runnable stop work.
         * */
        public void die() {
            allowRun = false;
        }
    }
}
