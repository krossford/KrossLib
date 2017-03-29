package com.krosshuang.krosslib.lib.util;


import android.graphics.Paint;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by krosshuang on 2016/3/17.
 */
public class EllipsizeUtil {

    private static final String LOG_TAG = "EllipsizeUtil:kross";

    public static final String COMMON_OMIT_STRING = "...";

    /**
     * <pre>
     * ellipsize string
     * 做省略字符串计算，始终保留后缀（suffix参数）,如果显示不下，会在后缀之前插入省略字符（omit参数），并截掉一些原始内容（raw）参数。
     * 能处理原始内容本身就包含换行（'\n'）的情况，会保留原始内容中的换行，然后再次基础上，逐行的进行计算。
     * 只能处理纯字符串的情况，如果字符串有样式啥的，目前还处理不了
     *
     * @param raw            原始字符串，内容字符串
     * @param availableWidth 显示这个字符串的TextView的可用宽度，通常是 measuredWidth - paddingLeft - paddingRight
     * @param textPaint      TextView的Paint
     * @param maxLine        textView的maxLine属性
     * @param omit           省略字符，当显示不下的时候，会出现这个，通常是"..."或"…"
     * @param suffix         后缀，始终保持在最后需要显示的字符串，如"的聊天记录"
     * @param output         存放结果的引用，存着每一行（结尾不带'\n'），传null的话，会帮你new一个，传个非null的，会帮你先clear了
     * @return 返回值是直接拼接好的结果，每一行之间插入'\n'（最后一行没有'\n'）
     * </pre>
     */
    public static CharSequence ellipsize(CharSequence raw, int availableWidth, Paint textPaint, int maxLine, CharSequence omit, CharSequence suffix, ArrayList<CharSequence> output) {
        if (availableWidth < 1 || TextUtils.isEmpty(raw) || null == textPaint) {
            return "";
        }

        Log.v(LOG_TAG, "raw: " + raw + " availableWidth: " + availableWidth + " maxLine: " + maxLine + " omit: " + omit + " suffix: " + suffix);
        int len = raw.length();
        if(len < 1) return raw;

        if (maxLine <= 0) {
            maxLine = 1;
        }

        if (omit == null) {
            omit = COMMON_OMIT_STRING;
        }

        if (suffix == null) {
            suffix = "";
        }

        if (output == null) {
            output = new ArrayList<CharSequence>();
        }
        output.clear();

        CharSequence tempResult;

        int currentLineCount = 1;

        // 如果原始字符串本身就有换行了，那么把事先把行都换好了
        String[] lines = raw.toString().split("\n");

        //Log.v(LOG_TAG, "lines: " + Arrays.toString(lines));

        int start = 0;

        for (String singleLine : lines) {
            len = singleLine.length();
            start = 0;
            for (; currentLineCount <= maxLine; currentLineCount++) {
                tempResult = sub(singleLine.subSequence(start, len), availableWidth, textPaint);
                if (tempResult.equals("")) {
                    break;
                }
                start += tempResult.length();
                output.add(tempResult);
            }
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
        for (int i = 0; i < output.size(); i++) {
            sb2.append(output.get(i));
            if (i != output.size() - 1) {
                sb2.append("\n");
            }
        }

        return sb2.subSequence(0, sb2.length());
    }

    private static CharSequence sub(CharSequence raw, int availableWidth, Paint textPaint) {
        int len = raw.length();
        if(len < 1) return raw;

        while (textPaint.measureText(raw.subSequence(0, len), 0, len) > availableWidth) {
            len--;
        }
        return raw.subSequence(0, len);
    }

    private static CharSequence subWithSuffix(CharSequence raw, CharSequence omit, CharSequence suffix, int availableWidth, Paint textPaint) {

        boolean b = true;
        int len = raw.length();
        if(len < 1) return raw;

        String realSuffix = suffix.toString();
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

    /**
     * 末尾省略，支持各种span（不支持的span类型可以找我进行处理）
     * @param spannable     输入文本
     * @param paint         textView的paint对象
     * @param viewWidth     textView的精确宽度
     * @param maxLine       行数
     * @param omit          省略符号
     * @param suffix        后缀，在省略符号之后
     * */
    public static CharSequence ellipsize(Spannable spannable, Paint paint, int viewWidth, int maxLine, String omit, String suffix) {
        if (suffix == null) {
            suffix = "";
        }

        if (omit == null) {
            omit = "...";
        }

        List<ComposingElement> elements = parseToComposingElement(spannable, paint);

        float suffixWidth = paint.measureText(suffix);
        float omitWidth = paint.measureText(omit);

        float totalWidth = getWidth(elements) + suffixWidth;

        // todo 还得考虑天然换行的情况
        // todo 这样计算还是有问题，还是得分行计算，计算总量这样虽然简洁，但是有的东西太大，第一行最后剩余的空间站不下，就会到第二行，这样其实是浪费了一些空间
        if (totalWidth > viewWidth * maxLine) {

            for (int useHowMany = elements.size(); useHowMany != 0; useHowMany--) {
                float widthCount = 0;
                List<ComposingElement> resultList = new ArrayList<>();
                for (int i = 0; i < useHowMany; i++) {
                    widthCount += elements.get(i).width;
                    resultList.add(elements.get(i));
                }

                if (widthCount + suffixWidth + omitWidth <= viewWidth * maxLine) {
                    SpannableStringBuilder ssb = new SpannableStringBuilder();
                    ssb.append(toCharSeq(resultList));
                    ssb.append(omit + suffix);
                    return ssb;
                }
            }
            return omit + suffix;
        } else {
            SpannableStringBuilder ssb = new SpannableStringBuilder();
            ssb.append(toCharSeq(elements));
            ssb.append(suffix);
            return ssb;
        }

    }

    private static float getWidth(List<ComposingElement> ce) {
        float w = 0;
        for (ComposingElement c : ce) {
            w += c.width;
        }
        return w;
    }

    private static CharSequence toCharSeq(List<ComposingElement> ceList) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (ComposingElement ce : ceList) {
            ssb.append(ce.content);
        }
        return ssb;
    }

    private static float getSpanWidth(CharacterStyle characterStyle, Spannable text) {
        if (characterStyle instanceof AbsoluteSizeSpan) {
            return getSpanWidth((AbsoluteSizeSpan)characterStyle, text);
        } else if (characterStyle instanceof ImageSpan) {
            return getSpanWidth((ImageSpan)characterStyle, text);
        }
        return 0;
    }

    private static float getSpanWidth(AbsoluteSizeSpan span, Spannable text) {
        int start = text.getSpanStart(span);
        int end = text.getSpanEnd(span);

        CharSequence cs = text.subSequence(start, end);
        Paint p = new Paint();
        p.setTextSize(span.getSize());
        float result = p.measureText(cs, 0, cs.length());
        //android.util.Log.i(LOG_TAG, "AbsoluteSizeSpan " + result);
        return result;
    }

    private static float getSpanWidth(ImageSpan span, Spannable text) {
        float result = span.getDrawable().getIntrinsicWidth();

        //android.util.Log.i(LOG_TAG, "ImageSpan " + result);
        return result;
//        int start = text.getSpanStart(span);
//        int end = text.getSpanEnd(span);
//
//        CharSequence cs = text.subSequence(start, end);
//        Paint p = new Paint();
//        p.setTextSize(span.getSize());
//        return p.measureText(cs, 0, cs.length() - 1);
    }

    private static class ComposingElement {
        float width = 0;
        CharSequence content;

        public ComposingElement(CharSequence content, float width) {
            this.width = width;
            this.content = content;
        }

    }

    /**
     * 解析出排版元素
     * */
    private static List<ComposingElement> parseToComposingElement (Spannable s, Paint paint) {
        CharacterStyle[] arr = s.getSpans(0, s.length(), CharacterStyle.class);

        List<ComposingElement> elements = new ArrayList<>();

        int isSpanStart = -1;
        for (int i = 0; i < s.length(); i++) {
            isSpanStart = isSpanStart(i, arr, s);
            if (isSpanStart == -1) {
                // 不是任何span的开头
                CharSequence normalText = s.subSequence(i, i + 1);
                float width = paint.measureText(normalText, 0, normalText.length());
                elements.add(new ComposingElement(normalText, width));
            } else {
                // 是某个span的开头
                int spanStart = s.getSpanStart(arr[isSpanStart]);
                int spanEnd = s.getSpanEnd(arr[isSpanStart]);
                float width = getSpanWidth(arr[isSpanStart], s);
                CharSequence spanContent = s.subSequence(spanStart, spanEnd);
                elements.add(new ComposingElement(spanContent, width));
                i = spanEnd - 1;
            }
        }

        return elements;
    }

    /**
     * 是否是某个span的开始，返回-1，是false，返回 >= 0 的值，表示是数组中某个span
     * */
    private static int isSpanStart(int index, CharacterStyle[] arr, Spannable content) {
        for (int i = 0; i < arr.length; i++) {
            if (content.getSpanStart(arr[i]) == index) {
                return i;
            }
        }
        return -1;
    }
}
