package com.krosshuang.krosslib.graphics;

import android.graphics.PointF;

import java.util.ArrayList;

/**
 * Created by krosshuang on 2015/10/23.
 */
public class Util {

    public static PointF[] toArray(ArrayList<PointF> arr) {
        if (arr.isEmpty()) {
            return null;
        }
        PointF[] ar = new PointF[arr.size()];
        for (int i = 0; i < arr.size(); i++) {
            ar[i] = arr.get(i);
        }
        return ar;
    }
}
