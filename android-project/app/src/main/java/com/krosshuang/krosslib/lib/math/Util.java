package com.krosshuang.krosslib.lib.math;

/**
 * Created by krosshuang on 2015/12/1.
 */
public class Util {

    private static final int LINE_TYPE_VERTICAL = 1;                        //垂直于x轴的线，斜率无穷大
    private static final int LINE_TYPE_HORIZONTAL = LINE_TYPE_VERTICAL + 1; //水平于x轴的线，斜率为0
    private static final int LINE_TYPE_NORMAL = LINE_TYPE_HORIZONTAL + 1;   //普通的斜线
    private static final int LINE_TYPE_NOT_A_LINE = LINE_TYPE_NORMAL + 1;   //两个点重合，不构成线

    /**
     * 根据四个点两条线，求出焦点，这是一份完备的代码
     * line (x1, y1) - (x2, y2)
     * line (x3, y3) - (x4, y4)
     * @return 如果两条直线无法形成焦点，将返回(0, 0)点
     * */
    public static float[] getCrossPoint(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        int lineType1 = getLineType(x1, y1, x2, y2);
        int lineType2 = getLineType(x3, y3, x4, y4);

        float[] defaultReturn = new float[]{0, 0};

        switch (lineType1) {
            case LINE_TYPE_NORMAL:
                switch (lineType2) {
                    case LINE_TYPE_NORMAL:
                        return calculateCrossPointWithTwoLines(x1, y1, x2, y2, x3, y3, x4, y4);
                    case LINE_TYPE_HORIZONTAL:
                        return calculateCrossPointWithLineAndY(x1, y1, x2, y2, y3);
                    case LINE_TYPE_VERTICAL:
                        return calculateCrossPointWithLineAndX(x1, y1, x2, y2, x3);
                    case LINE_TYPE_NOT_A_LINE:
                        return defaultReturn;
                }
                break;
            case LINE_TYPE_HORIZONTAL:
                switch (lineType2) {
                    case LINE_TYPE_NORMAL:
                        return calculateCrossPointWithLineAndY(x3, y3, x4, y4, y1);
                    case LINE_TYPE_HORIZONTAL:
                        return defaultReturn;
                    case LINE_TYPE_VERTICAL:
                        return new float[]{x3, y1};
                    case LINE_TYPE_NOT_A_LINE:
                        return defaultReturn;
                }
                break;
            case LINE_TYPE_VERTICAL:
                switch (lineType2) {
                    case LINE_TYPE_NORMAL:
                        return calculateCrossPointWithLineAndX(x3, y3, x4, y4, x1);
                    case LINE_TYPE_HORIZONTAL:
                        return new float[]{x1, y3};
                    case LINE_TYPE_VERTICAL:
                        return defaultReturn;
                    case LINE_TYPE_NOT_A_LINE:
                        return defaultReturn;
                }
                break;
            case LINE_TYPE_NOT_A_LINE:
                return defaultReturn;
        }

        return defaultReturn;
    }

    /**
     * 获取这条线的类型
     * */
    private static int getLineType(float x1, float y1, float x2, float y2) {
        if (x1 == x2 && y1 == y2) {
            return LINE_TYPE_NOT_A_LINE;
        } else if (x1 == x2) {
            return LINE_TYPE_VERTICAL;
        } else if (y1 == y2) {
            return LINE_TYPE_HORIZONTAL;
        } else {
            return LINE_TYPE_NORMAL;
        }
    }

    private static float[] calculateCrossPointWithLineAndX(float x1, float y1, float x2, float y2, float x) {
        float[] kb = getKB(x1, y1, x2, y2);
        return new float[]{x, kb[0] * x + kb[1]};
    }

    private static float[] calculateCrossPointWithLineAndY(float x1, float y1, float x2, float y2, float y) {
        float[] kb = getKB(x1, y1, x2, y2);
        return new float[]{(y - kb[1]) / kb[0], y};
    }

    /**
     * 求两条普通直线的焦点，斜率非0，非无穷大
     * @return 如果两条直线平行，返回(0, 0)
     * */
    private static float[] calculateCrossPointWithTwoLines(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        float[] kb1 = getKB(x1, y1, x2, y2);
        float[] kb2 = getKB(x3, y3, x4, y4);
        if (kb1[0] == kb2[0]) {
            return new float[]{0, 0};
        }
        float[] result = new float[2];
        result[0] = (kb2[1] - kb1[1]) / (kb1[0] - kb2[0]);
        result[1] = kb1[0] * result[0] + kb1[1];
        return result;
    }

    /**
     * 计算直线方程kb kb[0] -> k  kb[1] -> b
     * */
    private static float[] getKB(float x1, float y1, float x2, float y2) {
        float[] kb = new float[2];
        kb[0] = (y2 - y1) / (x2 - x1);
        kb[1] = y1 - kb[0] * x1;
        return kb;
    }
}
