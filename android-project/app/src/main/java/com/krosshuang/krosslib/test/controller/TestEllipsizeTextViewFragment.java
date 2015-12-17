package com.krosshuang.krosslib.test.controller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.view.EllipsizeEndTextView;

/**
 * Created by krosshuang on 2015/12/17.
 */
public class TestEllipsizeTextViewFragment extends Fragment implements View.OnClickListener{

    private View mRoot = null;

    private EllipsizeEndTextView mTextView = null;
    private EditText mEditText = null;
    private Button mButton = null;
    private TextView mStatus = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_test_ellipsize_textview, null);

        mTextView = (EllipsizeEndTextView) mRoot.findViewById(R.id.ellipsize_textview);
        mTextView.setMaxLines(4);

        mEditText = (EditText) mRoot.findViewById(R.id.et_input);
        mButton = (Button) mRoot.findViewById(R.id.btn_put);
        mStatus = (TextView) mRoot.findViewById(R.id.tv_status);

        mButton.setOnClickListener(this);

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
                break;
        }
    }
}
