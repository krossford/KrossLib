package com.krosshuang.krosslib.lib.graphics;

/**
 * Created by krosshuang on 2015/10/26.
 */
public abstract class Element {

    public static final int TYPE_RECTANGLE = 1;

    public boolean isSelected = false;
    public int type;
    public float borderWidth = 10;
    public int size = 0;    //用来记录history

    /**
     * 每一个子类都应该实现这个方法，去判断当前触碰点是否能选择到这个Element
     * */
    public abstract boolean checkSelect(float x, float y);
}
