package com.krosshuang.krosslib.test.controller;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.util.EllipsizeUtil;
import com.krosshuang.krosslib.lib.view.DelayCallbackEditText;
import com.krosshuang.krosslib.lib.view.EllipsizeEndTextView;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2015/12/17.
 */
public class TestEllipsizeTextViewFragment extends Fragment implements View.OnClickListener, DelayCallbackEditText.OnDelayCallbackListener{

    private static final String LOG_TAG = "TestEllipsizeTextViewFragment";
    private View mRoot = null;

    private EllipsizeEndTextView mTextView = null;
    private DelayCallbackEditText mEditText = null;
    private Button mButton = null;
    private TextView mStatus = null;
    private TextView mTest2 = null;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_test_ellipsize_textview, null);

        mTextView = (EllipsizeEndTextView) mRoot.findViewById(R.id.ellipsize_textview);
        mTextView.setMaxLines(4);

        mTest2 = (TextView) mRoot.findViewById(R.id.tv_test2);

        mTest2.getViewTreeObserver().addOnGlobalFocusChangeListener(new ViewTreeObserver.OnGlobalFocusChangeListener() {
            @Override
            public void onGlobalFocusChanged(View oldFocus, View newFocus) {
                Log.v(LOG_TAG, "onGlobalFocusChanged: " + mTest2.getMeasuredWidth());

                //CharSequence raw = "一二三四五六七八九十1234567890一二三四五六七八九十1234567890一二三四五六七八九十1234567890一二三四五六七八九十";
                //CharSequence raw = "今天忽然发现android项目中的文字排版参差不齐的情况非常严重，不得不想办法解决一下。经过研究之后，终于找到了textview自动换行导致混乱的原因了----半角字符与全角字符混乱所致！一般情况下，我们输入的数字、字母以及英文标点都是半角，所以占位无法确定。它们与汉字的占位大大的不同，由于这个原因，导致很多文字的排版都是参差不齐的。对此我找到了两种办法可以解决这个问题";


            }
        });



        mEditText = (DelayCallbackEditText) mRoot.findViewById(R.id.et_input);
        mEditText.setDelayCallback(this, 2000);

        mButton = (Button) mRoot.findViewById(R.id.btn_put);
        mStatus = (TextView) mRoot.findViewById(R.id.tv_status);

        mButton.setOnClickListener(this);

        return mRoot;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ellipsize_textview:
                break;
            case R.id.et_input:
                break;
            case R.id.btn_put:

                mTextView.setText(mEditText.getText().toString());
                setEllipsize(mEditText.getText().toString());
                break;
        }
    }

    private void setEllipsize(CharSequence raw) {
        ArrayList<CharSequence> output = new ArrayList<>();
        EllipsizeUtil.ellipsize(raw, mTest2.getMeasuredWidth(), mTest2.getPaint(), mTest2.getMaxLines(), "...", "的后缀", output);

        StringBuilder sb = new StringBuilder();
        for (CharSequence cs : output) {
            Log.v(LOG_TAG, "最终结果：" + cs);
            sb.append(cs);
        }

        mTest2.setText(sb.toString());
    }

    @Override
    public void onDelayCallback(CharSequence text) {
        Toast.makeText(getActivity(), "abcd", Toast.LENGTH_SHORT).show();
    }
}
