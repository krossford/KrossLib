package com.krosshuang.krosslib.graphics;

import android.graphics.RectF;

/**
 * Created by krosshuang on 2015/10/26.
 */
public class RectElement extends Element{

    public RectF rectangle = null;

    public RectElement() {
        type = Element.TYPE_RECTANGLE;
    }

    @Override
    public boolean checkSelect(float x, float y) {
        //矩形检测逻辑，如果在矩形内，就判断为选中
        isSelected =  rectangle.contains(x, y);
        return isSelected;
    }
}
