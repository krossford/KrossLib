package com.krosshuang.krosslib.graphics;

import android.graphics.Point;
import android.util.Log;

/**
 * 二维图元的生成算法
 * Created by krosshuang on 2015/12/1.
 */
public class Primitive2D {

    private static final String LOG_TAG = "Primitive2D";

    /**
     * 画线算法，传入起始点结束点，得到直线上一序列的点，算法为bresenham
     * */
    public static Point[] line(Point start, Point end) {

        if (start.x == end.x && start.y == end.y) {
            //两个端点是同一个点，无法构成线段
            return new Point[]{new Point(start)};
        } else if (start.x == end.x) {
            //x坐标相等，垂直的情况
            Point[] list = getInitedPointArray(Math.abs(end.y - start.y) + 1);
            Point temp = new Point();
            temp.x = start.x;
            temp.y = Math.min(start.y, end.y);
            for (Point p : list) {
                p.x = temp.x;
                p.y = temp.y;
                temp.y++;
            }
            return list;
        } else if (start.y == end.y) {
            //y坐标相等，水平的情况
            Point[] list = getInitedPointArray(Math.abs(end.x - start.x) + 1);
            Point temp = new Point();
            temp.x = Math.min(start.x, end.x);
            temp.y = start.y;
            for (Point p : list) {
                p.x = temp.x;
                p.y = temp.y;
                temp.x++;
            }
            return list;
        }

        int dx = Math.abs(start.x - end.x);
        int dy = Math.abs(start.y - end.y);
        int deltaX = (start.x - end.x) / dx;
        int deltaY = (start.y - end.y) / dy;

        //对角线的情况
        if (dx == dy) {
            Point[] list = getInitedPointArray(Math.abs(end.x - start.x) + 1);
            Point temp = new Point();
            temp.x = end.x;
            temp.y = end.y;
            for (Point p : list) {
                p.x = temp.x;
                p.y = temp.y;
                temp.x += deltaX;
                temp.y += deltaY;
            }
            return list;
        }

        int p;
        int x = 0;
        int y = 0;
        Point temp = new Point();


        //正常情况
        if (dx > dy) {
            p = 2 * dy - dx;
            Point[] list = getInitedPointArray(dx + 1);
            temp.x = end.x;
            temp.y = end.y;
            for (int i = 0; i <= dx; i++) {
                if (i == 0) {
                    x = list[0].x = temp.x;
                    y = list[0].y = temp.y;
                } else {
                    x = list[i].x = x + deltaX;
                    if (p >= 0) {
                        y = list[i].y = y + deltaY;
                        p = p + 2 * dy - 2 * dx;
                    } else {
                        y = list[i].y = y;
                        p = p + 2 * dy;
                    }
                }
            }
            return list;
        } else if (dy > dx) {
            p = 2 * dx - dy;
            Point[] list = new Point[dy + 1];
            temp.x = end.x;
            temp.y = end.y;
            for (int i = 0; i <= dy; i++) {
                list[i] = new Point();
                if (i == 0) {
                    x = list[0].x = temp.x;
                    y = list[0].y = temp.y;
                } else {
                    y = list[i].y = y + deltaY;
                    if (p >= 0) {
                        x = list[i].x = x + deltaX;
                        p = p + 2 * dx - 2 * dy;
                    } else {
                        x = list[i].x = x;
                        p = p + 2 * dx;
                    }
                }
            }
            return list;
        }
        return null;
    }

    /**
     * @param cx
     * @param cy
     * @param r
     * @return
     */
    public static Point[] circle(int cx, int cy, float r) {
        Log.i(LOG_TAG, "circle params: " + cx + " - " + cy + " - " + r);
        int p = (int)(1.25 - r);

        Point[] partCircle = getInitedPointArray(1 + (int)(r * Math.sin(Math.PI / 4f)));

        int x = 0;
        int y = (int)r;

        for (int i = 0; i < partCircle.length; i++) {
            if (i == 0) {
                partCircle[i].x = x;
                partCircle[i].y = y;
            } else {
                x++;
                partCircle[i].x = x;
                if (p >= 0) {
                    y--;
                    partCircle[i].y = y;
                    p = p + 1 + 2 * x - 2 * y;
                } else {
                    partCircle[i].y = y;
                    p = p + 1 + 2 * x;

                }
            }
        }


        Point[] circle = getInitedPointArray(partCircle.length * 8);

        copyPointWithTransform(circle, 0, partCircle.length, partCircle, NO_TRANSFORM);
        copyPointWithTransform(circle, partCircle.length, partCircle.length, circle, ABOUT_DIAGONAL_LINE);
        copyPointWithTransform(circle, partCircle.length * 2, partCircle.length * 2, circle, ABOUT_X);
        copyPointWithTransform(circle, partCircle.length * 4, partCircle.length * 4, circle, ABOUT_Y);

        for (Point tp : circle) {
            tp.x += cx;
            tp.y += cy;
        }

        return circle;
    }

    private static final int NO_TRANSFORM = 0;
    private static final int ABOUT_DIAGONAL_LINE = 1;
    private static final int ABOUT_X = 2;
    private static final int ABOUT_Y = 3;

    private static void copyPointWithTransform(Point[] src, int start, int len, Point[] from, int type) {
        for (int i = 0; i < len; i++) {
            switch (type) {
                case NO_TRANSFORM:
                    src[i + start].x = from[i].x;
                    src[i + start].y = from[i].y;
                    break;
                case ABOUT_DIAGONAL_LINE:
                    src[i + start].x = from[i].y;
                    src[i + start].y = from[i].x;
                    break;
                case ABOUT_X:
                    src[i + start].x = from[i].x;
                    src[i + start].y = -from[i].y;
                    break;
                case ABOUT_Y:
                    src[i + start].x = -from[i].x;
                    src[i + start].y = from[i].y;
                    break;
            }
        }
    }

    private static Point[] getInitedPointArray(int size) {
        Point[] l = new Point[size];
        for (int i = 0; i < l.length; i++) {
            l[i] = new Point();
        }
        return l;
    }
}
