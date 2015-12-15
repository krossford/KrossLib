package com.krosshuang.krosslib.test.controller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.krosshuang.krosslib.R;
import com.krosshuang.krosslib.lib.view.MarkView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by krosshuang on 2015/10/26.
 */
public class ScreenshotActivity extends AppCompatActivity {

    private ArrayList<ImageView> mToolArray = new ArrayList<>();

    private ImageView mIvToolSelect = null;
    private ImageView mIvToolPen = null;
    private ImageView mIvToolText = null;
    private ImageView mIvToolCircle = null;
    private ImageView mIvToolRectangle = null;

    private MarkView mMarkView = null;
    private Button mBtnLoadImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screenshot);

        initView();
    }

    private void initView() {

        mIvToolSelect = (ImageView)findViewById(R.id.iv_tool_select);
        mIvToolPen = (ImageView)findViewById(R.id.iv_tool_pen);
        mIvToolText = (ImageView)findViewById(R.id.iv_tool_text);
        mIvToolCircle = (ImageView)findViewById(R.id.iv_tool_circle);
        mIvToolRectangle = (ImageView)findViewById(R.id.iv_tool_rectangle);

        mToolArray.add(mIvToolSelect);
        mToolArray.add(mIvToolPen);
        mToolArray.add(mIvToolText);
        mToolArray.add(mIvToolCircle);
        mToolArray.add(mIvToolRectangle);

        ToolClickListener listener = new ToolClickListener();
        mIvToolSelect.setOnClickListener(listener);
        mIvToolPen.setOnClickListener(listener);
        mIvToolText.setOnClickListener(listener);
        mIvToolCircle.setOnClickListener(listener);
        mIvToolRectangle.setOnClickListener(listener);

        mMarkView = (MarkView)findViewById(R.id.markview);

        NormalClickListener clickListener = new NormalClickListener();
        mBtnLoadImage = (Button)findViewById(R.id.btn_load_image);
        mBtnLoadImage.setOnClickListener(clickListener);

        updateView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {
            Uri uri = data.getData();
            Bitmap bitmap = null;
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            mMarkView.loadImage(bitmap);
        } catch (IOException e) {

        }

        updateView();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateView() {
        if (!mMarkView.hasImage()) {
            mBtnLoadImage.setVisibility(View.VISIBLE);
        } else {
            mBtnLoadImage.setVisibility(View.GONE);
        }
    }

    private void pickImage() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_PICK);
        i.setType("image/*");
        startActivityForResult(i, 0);
    }

    class NormalClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_load_image:
                    pickImage();
                    break;
            }
        }
    }

    class ToolClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            for (ImageView iv: mToolArray) {
                iv.setBackgroundColor(Color.TRANSPARENT);
            }
            ((ImageView)v).setBackgroundColor(Color.GRAY);
            switch (v.getId()) {
                default:
                case R.id.iv_tool_select:
                    mMarkView.setCurrentTool(MarkView.TOOL_SELECT);

                    break;
                case R.id.iv_tool_pen:
                    mMarkView.setCurrentTool(MarkView.TOOL_PEN);
                    break;
                case R.id.iv_tool_text:
                    mMarkView.setCurrentTool(MarkView.TOOL_TEXT);
                    break;
                case R.id.iv_tool_circle:
                    mMarkView.setCurrentTool(MarkView.TOOL_CIRCLE);
                    break;
                case R.id.iv_tool_rectangle:
                    mMarkView.setCurrentTool(MarkView.TOOL_RECTANGLE);
                    break;
            }
        }
    }
}
