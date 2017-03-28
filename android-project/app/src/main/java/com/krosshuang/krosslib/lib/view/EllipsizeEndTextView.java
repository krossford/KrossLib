package com.krosshuang.krosslib.lib.view;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.ArrayList;

/*
如何使用？
How to use it?

> 1.在xml或者java代码中常规使用
> 1.use it like other views on xml and java code.

> 2.[必须] setMaxLines 方法替代在xml中的 "android:maxLines" 属性
> 2.[must] call the setMaxLines method to instead of the xml property android:maxLines.

> 3.[可选] 注意调用 setMultilineEllipsizeMode() 方法，具体请查看注释
> 3.[option] you can invoke setMultilineEllipsizeMode method, but I have not implement it.

*/

/**
* android自己的TextView对多行ellipsize处理的不好
* Created by krosshuang on 2015/12/17.
*/
public class EllipsizeEndTextView extends TextView {

    private static final String LOG_TAG = "EllipsizeTextView";

    /** 每一行都有省略号 */
    //TODO 该特性待完成
    public static final int MODE_EACH_LINE = 1;

    /** 最后一行才有省略号 */
    public static final int MODE_LAST_LINE = 2;

    private static final String ELLIPSIZE = "...";


    private ArrayList<String> mTextLines = new ArrayList<String>();

    private CharSequence mSrcText = null;
    private int mMultilineEllipsizeMode = MODE_LAST_LINE;
    private int mMaxLines = 1;
    private boolean mNeedIgnoreTextChangeAndSelfInvoke = false;

    public EllipsizeEndTextView(Context context) {
        super(context);
    }

    public EllipsizeEndTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EllipsizeEndTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        if (!mNeedIgnoreTextChangeAndSelfInvoke) {
            super.onTextChanged(text, start, lengthBefore, lengthAfter);
            mSrcText = text;
        }
    }

    @Override
    public void setMaxLines(int maxlines) {
        super.setMaxLines(maxlines);
        mMaxLines = maxlines;
    }

    public int getSupportedMaxLines() {
        return mMaxLines;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getEllipsize() == TextUtils.TruncateAt.END) {
            setVisibleText();
        }
        super.onDraw(canvas);
        mNeedIgnoreTextChangeAndSelfInvoke = false;
    }

    private void setVisibleText() {

        if (mSrcText == null) {
            return;
        }

        //获得可使用的width get available width
        final int aw = getWidth() - getPaddingLeft() - getPaddingRight();

        String srcText = mSrcText.toString();

        //将原始的字符串先按原始数据中存在的换行符弄成多行字符串
        String[] lines = srcText.split("\n");
        //Log.i(LOG_TAG, "原始数据有: " + lines.length + " 行 " + Arrays.toString(lines));

        int maxLines = getSupportedMaxLines();

        //将原始文本分成几行后加入list
        mTextLines.clear();
        for (int i = 0; i < lines.length; i++) {
            mTextLines.add(lines[i]);
        }

        switch (mMultilineEllipsizeMode) {

            case MODE_EACH_LINE:
                break;

            default:
            case MODE_LAST_LINE:
                //开始遍历
                String eachLine = null;
                for (int i = 0; i < mTextLines.size() && i < maxLines - 1; i++) {

                    eachLine = mTextLines.get(i);

                    if (getPaint().measureText(eachLine, 0, eachLine.length()) > aw) {

                        //当前行超过可用宽度
                        boolean isOut = true;
                        int end = eachLine.length() - 1;
                        while (isOut) {
                            if (getPaint().measureText(eachLine.substring(0, end), 0, end) > aw) {
                                end--;
                            } else {
                                isOut = false;
                            }
                        }

                        mTextLines.set(i, eachLine.substring(0, end));  //当前行设置为裁剪后的
                        mTextLines.add(i + 1, eachLine.substring(end, eachLine.length()));  //将裁剪剩余的部分，加入下一行，刚好接下来发生的遍历就可以处理它，相当于一个递归

                    }
                }

                //遍历处理结束，所有的行都是在可用宽度以内的
                break;
        }

        //根据 maxLines 和 结果的行数，决定最小需要多少行
        int resultSize = Math.min(maxLines, mTextLines.size());

        //对最后一行做处理
        String lastLine = mTextLines.get(resultSize - 1);

        //最后一行有两种情况需要加...
        //1.最后一行数据本身很长，超过了可用宽度，那么裁剪后尾部加上...
        //2.最后一行不是很长，并没有超过可用宽度，但是它底下还有行没有显示，因此加上...
        if (getPaint().measureText(lastLine, 0, lastLine.length()) > aw || resultSize < mTextLines.size()) {

            boolean isOut = true;
            int end = lastLine.length();
            while (isOut) {
                if (getPaint().measureText(lastLine.substring(0, end) + ELLIPSIZE, 0, end + 3) > aw) {
                    end--;
                } else {
                    isOut = false;
                }
            }

            mTextLines.set(resultSize - 1, lastLine.substring(0, end) + ELLIPSIZE);
        }

        //开始构建结果
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <  resultSize ; i++) {
            sb.append(mTextLines.get(i));
            if (i != resultSize - 1) {
                sb.append('\n');
            }
        }

        //构建完成，set
        if (sb.toString().equals(getText())) {
            return;
        } else {
            mNeedIgnoreTextChangeAndSelfInvoke = true;
            setText(sb.toString());
        }
    }

    /**
     * 设置ellipsize mode，暂时不支持
     * @deprecated
     * */
    public void setMultilineEllipsizeMode(int mode) {
        mMultilineEllipsizeMode = mode;
    }
}
