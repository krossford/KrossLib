package com.krosshuang.krosslib.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * android自己的TextView对多行ellipsize处理的不好
 * Created by krosshuang on 2015/12/17.
 */
public class EllipsizeTextView extends TextView {

    private static final String LOG_TAG = "EllipsizeTextView";

    /** 每一行都有省略号 */
    public static final int MODE_EACH_LINE = 1;

    /** 最后一行才有省略号 */
    public static final int MODE_LAST_LINE = 2;

    private static final String ELLIPSIZE = "...";

    private int mMultilineEllipsizeMode = MODE_LAST_LINE;
    private TextUtils.TruncateAt mEllipsize = TextUtils.TruncateAt.END;

    private ArrayList<String> mTextLines = new ArrayList<String>();
    private CharSequence mSrcText = null;

    private boolean mNeedIgnoreTextChangeAndSelfInvoke = false;


    public EllipsizeTextView(Context context) {
        super(context);
    }

    public EllipsizeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsizeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (!mNeedIgnoreTextChangeAndSelfInvoke) {
            super.onTextChanged(text, start, lengthBefore, lengthAfter);
            mSrcText = text;
            Log.i(LOG_TAG, "onTextChanged src text: " + text +" " +  start + " " + lengthBefore + " " + lengthAfter  );
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        setVisibleText();
        super.onDraw(canvas);
        Log.i(LOG_TAG, "onDraw");
        mNeedIgnoreTextChangeAndSelfInvoke = false;
    }

    private void setVisibleText() {

        if (mNeedIgnoreTextChangeAndSelfInvoke) {
            Log.i(LOG_TAG, "跳过 setVisibleText mNeedIgnoreTextChangeAndSelfInvoke == ture");
            return;
        }

        if (mSrcText == null) {
            Log.i(LOG_TAG, "跳过 setVisibleText mSrcText == null");
            return;
        }

        Log.i(LOG_TAG, "setVisibleText");


        final int aw = getWidth() - getPaddingLeft() - getPaddingRight();
        //Log.i(LOG_TAG, "available width, height: " + aw + ", " + ah);


        String srcText = mSrcText.toString();

        String[] lines = srcText.split("\n");       //将原始的字符串先按原始数据中存在的换行符弄成多行字符串
        Log.i(LOG_TAG, "原始数据有: " + lines.length + " 行 " + Arrays.toString(lines));


        mEllipsize = getEllipsize();

        int maxLines = getMaxLines();
        Log.i(LOG_TAG, "行数限定: " + maxLines);

        mTextLines.clear();
        for (int i = 0; i < lines.length; i++) {
            mTextLines.add(lines[i]);
        }

        Log.i(LOG_TAG, "初始化数组 TextLines，实际放入行数: " + mTextLines.size());

        switch (mMultilineEllipsizeMode) {

            case MODE_EACH_LINE:
                break;

            default:
            case MODE_LAST_LINE:
                Log.i(LOG_TAG, "MODE_LAST_LINE 开始遍历 TextLines ...");
                for (int i = 0; i < mTextLines.size() && i < maxLines - 1; i++) {
                    String eachLine = mTextLines.get(i);
                    float eachLineWidth = getPaint().measureText(eachLine, 0, eachLine.length());
                    if (eachLineWidth <= aw) {
                        Log.i(LOG_TAG, eachLine + " 没有超过可用宽度，安全！: " +aw);
                    } else {
                        //当行超过可用宽度
                        Log.i(LOG_TAG, eachLine + " 超过可用宽度: " +aw);
                        boolean isOut = true;
                        int end = eachLine.length() - 1;
                        while (isOut) {
                            Log.i(LOG_TAG, "isOut true, 开始裁剪: " + eachLine.substring(0, end));
                            if (getPaint().measureText(eachLine.substring(0, end), 0, end) > aw) {
                                end--;
                            } else {
                                isOut = false;
                            }
                        }

                        mTextLines.set(i, eachLine.substring(0, end));
                        mTextLines.add(i + 1, eachLine.substring(end, eachLine.length()));

                    }
                    //Log.i(LOG_TAG, "measureText: " +eachLine + " width "+ getPaint().measureText(eachLine, 0, eachLine.length()));
                }

                Log.i(LOG_TAG, "遍历完成，TextLines 大小: " + mTextLines.size());
                break;
        }



        Log.i(LOG_TAG, "最终输出结果：");
        int resultSize = Math.min(maxLines, mTextLines.size());

        String lastLine = mTextLines.get(resultSize - 1);
        if (getPaint().measureText(lastLine, 0, lastLine.length()) > aw || resultSize < mTextLines.size()) {

            Log.i(LOG_TAG, "最后一行超出可用宽度");

            boolean isOut = true;
            int end = lastLine.length();
            while (isOut) {
                Log.i(LOG_TAG, "isOut true, 开始裁剪: " + lastLine.substring(0, end) + ELLIPSIZE);
                if (getPaint().measureText(lastLine.substring(0, end) + ELLIPSIZE, 0, end + 3) > aw) {
                    end--;
                } else {
                    isOut = false;
                }
            }

            mTextLines.set(resultSize - 1, lastLine.substring(0, end) + ELLIPSIZE);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <  resultSize ; i++) {
            sb.append(mTextLines.get(i));
            if (i != resultSize - 1) {
                sb.append('\n');
            }
            Log.i(LOG_TAG, "第" + i + "行: " + mTextLines.get(i));
        }

        Log.i(LOG_TAG, "当前 getText: " + getText());
        if (sb.toString().equals(getText())) {
            Log.i(LOG_TAG, "相同！ return");
            return;
        } else {
            Log.i(LOG_TAG, "不相同！ set");
            mNeedIgnoreTextChangeAndSelfInvoke = true;
            setText(sb.toString());
        }
    }

    public void setMultilineEllipsizeMode(int mode) {
        mMultilineEllipsizeMode = mode;
    }
}
