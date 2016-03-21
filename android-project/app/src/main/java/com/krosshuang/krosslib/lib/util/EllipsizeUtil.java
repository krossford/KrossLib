package com.krosshuang.krosslib.lib.util;

import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2016/3/17.
 */
public class EllipsizeUtil {

    private static final String LOG_TAG = "EllipsizeUtil";

    /**
     * ellipsize string
     *
     * @param raw            原始字符串
     * @param availableWidth 显示这个字符串的TextView的可用宽度，通常是 measuredWidth - paddingLeft - paddingRight
     * @param textPaint      TextView的Paint
     * @param maxLine        textView的maxLine属性
     * @param omit           省略字符，当显示不下的时候，会出现这个，通常是"..."或"…"
     * @param suffix         后缀，始终保持在最后需要显示的字符串，如"的聊天记录"
     * @param output         存放结果的引用，存着每一行
     * @return 返回值是直接拼接好的结果，每一行之间插入'\n'
     */
    public static CharSequence ellipsize(CharSequence raw, int availableWidth, Paint textPaint, int maxLine, CharSequence omit, CharSequence suffix, ArrayList<CharSequence> output) {

        Log.v(LOG_TAG, "raw: " + raw + " availableWidth: " + availableWidth + " maxLine: " + maxLine + " omit: " + omit + " suffix: " + suffix);

        if (raw == null || raw.equals("")) {
            return "";
        }

        if (availableWidth <= 0 || textPaint == null) {
            throw new IllegalArgumentException("availableWidth should greater than 0 or textPaint should not be null");
        }

        if (maxLine <= 0) {
            maxLine = 1;
        }

        if (omit == null) {
            omit = "...";
        }

        if (suffix == null) {
            suffix = "";
        }

        if (output == null) {
            output = new ArrayList<CharSequence>();
        }
        output.clear();

        int start = 0;
        int len = raw.length();

        CharSequence tempResult;

        for (int i = 1; i <= maxLine; i++) {
            tempResult = sub(raw.subSequence(start, len), availableWidth, textPaint);
            if (tempResult.equals("")) {
                break;
            }
            start += tempResult.length();
            Log.v(LOG_TAG, "i: " + i + "sub result: " + tempResult);
            output.add(tempResult);
        }

        StringBuilder sb = new StringBuilder();
        if (textPaint.measureText(sb.append(output.get(output.size() - 1)).append(suffix).toString()) > availableWidth) {
            // 判断，如果最后一行文本拼上后缀超出了可用宽度
            if (output.size() < maxLine) {
                // 如果当前行数小于maxLine，那么直接把后缀文本拼在最后一行上面，这样最后一行能自动换行到下一行
                output.set(output.size() - 1, sb.subSequence(0, sb.length()));
            } else {
                // 如果已经是最后一行了，那么进行裁剪
                CharSequence c = subWithSuffix(output.get(output.size() - 1), omit, suffix, availableWidth, textPaint);
                output.remove(output.size() - 1);
                output.add(c);
            }
        } else {
            output.remove(output.size() - 1);
            output.add(sb.subSequence(0, sb.length()));
        }

        StringBuilder sb2 = new StringBuilder();
        for (CharSequence cs : output) {
            //Log.v(LOG_TAG, "最终结果：" + cs);
            sb2.append(cs).append('\n');
        }

        return sb2.subSequence(0, sb2.length());
    }

    private static CharSequence sub(CharSequence raw, int availableWidth, Paint textPaint) {
        int len = raw.length();
        while (textPaint.measureText(raw.subSequence(0, len), 0, len) > availableWidth) {
            len--;
        }
        return raw.subSequence(0, len);
    }

    private static CharSequence subWithSuffix(CharSequence raw, CharSequence omit, CharSequence suffix, int availableWidth, Paint textPaint) {
        String realSuffix = suffix.toString();
        boolean b = true;
        int len = raw.length();
        while (textPaint.measureText(raw.subSequence(0, len).toString() + realSuffix, 0, len + realSuffix.length()) > availableWidth) {
            len--;
            if (b) {
                realSuffix = omit + realSuffix;
                b = false;
            }
        }
        return raw.subSequence(0, len).toString() + realSuffix;

    }

    /*
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
    */
}
