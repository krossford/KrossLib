package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.view.EllipsizeTextView;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2015/12/17.
 */
public class TestEllipsizeTextViewFragment extends Fragment implements View.OnClickListener{

    private View mRoot = null;

    private EllipsizeTextView mTextView = null;
    private EditText mEditText = null;
    private Button mButton = null;
    private TextView mStatus = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_test_ellipsize_textview, null);

        mTextView = (EllipsizeTextView) mRoot.findViewById(R.id.ellipsize_textview);
        mEditText = (EditText) mRoot.findViewById(R.id.et_input);
        mButton = (Button) mRoot.findViewById(R.id.btn_put);
        mStatus = (TextView) mRoot.findViewById(R.id.tv_status);

        mButton.setOnClickListener(this);


        ArrayList<String> arr = new ArrayList<>();
        arr.add(1+"");
        arr.add(2+"");
        arr.add(3+"");
        arr.add(4+"");
        arr.add(5+"");

        arr.add(2, 99+"");

        Log.i("tag", arr.toString());



        return mRoot;

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
                mTextView.setFocusable(false);
                mTextView.setEnabled(false);
                break;
        }
    }
}
