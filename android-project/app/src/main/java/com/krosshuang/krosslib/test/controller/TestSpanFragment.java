package com.krosshuang.krosslib.test.controller;

import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.CharacterStyle;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.krosshuang.krosslib.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 用来测试Span
 * Created by krosshuang on 2017/3/27.
 */

public class TestSpanFragment extends Fragment {

    TextView textView;

    private static final String LOG_TAG = "TestSpanFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_span, null);
        textView = (TextView) root.findViewById(R.id.text);

        updateView();

        return root;
    }

    private void updateView() {
        SpannableString spannableString = new SpannableString("0123456789abcdab");
        SpannableStringBuilder sb = new SpannableStringBuilder();
        sb.append(spannableString);

        AbsoluteSizeSpan absoluteSizeSpan = new AbsoluteSizeSpan(90);

        ImageSpan imageSpan = new ImageSpan(BitmapFactory.decodeResource(getResources(), R.drawable.tool_arrow));

        //Drawable drawable =

        sb.setSpan(absoluteSizeSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(ellipsize(sb, textView.getPaint(), 200, 3, "...", "的聊天记录"));
    }

    public CharSequence ellipsize(Spannable spannable, Paint paint, int viewWidth, int maxLine, String omit, String suffix) {
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

    public static CharSequence toCharSeq(List<ComposingElement> ceList) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        for (ComposingElement ce : ceList) {
            ssb.append(ce.content);
        }
        return ssb;
    }

    public float getSpanWidth(CharacterStyle characterStyle, Spannable text) {
        if (characterStyle instanceof AbsoluteSizeSpan) {
            return getSpanWidth((AbsoluteSizeSpan)characterStyle, text);
        } else if (characterStyle instanceof ImageSpan) {
            return getSpanWidth((ImageSpan)characterStyle, text);
        }
        return 0;
    }

    public float getSpanWidth(AbsoluteSizeSpan span, Spannable text) {
        int start = text.getSpanStart(span);
        int end = text.getSpanEnd(span);

        CharSequence cs = text.subSequence(start, end);
        Paint p = new Paint();
        p.setTextSize(span.getSize());
        float result = p.measureText(cs, 0, cs.length());
        Log.i(LOG_TAG, "AbsoluteSizeSpan " + result);
        return result;
    }

    public float getSpanWidth(ImageSpan span, Spannable text) {
        float result = span.getDrawable().getIntrinsicWidth();
        Log.i(LOG_TAG, "ImageSpan " + result);
        return result;
//        int start = text.getSpanStart(span);
//        int end = text.getSpanEnd(span);
//
//        CharSequence cs = text.subSequence(start, end);
//        Paint p = new Paint();
//        p.setTextSize(span.getSize());
//        return p.measureText(cs, 0, cs.length() - 1);
    }

    static class ComposingElement {
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
    public List<ComposingElement> parseToComposingElement (Spannable s, Paint paint) {
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
    public static int isSpanStart(int index, CharacterStyle[] arr, Spannable content) {
        for (int i = 0; i < arr.length; i++) {
            if (content.getSpanStart(arr[i]) == index) {
                return i;
            }
        }
        return -1;
    }
}
