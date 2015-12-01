package com.krosshuang.krosslib.graphics;

import android.graphics.Point;

/**
 * Created by krosshuang on 2015/12/1.
 */
public class Bresenham {

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

        //对角线的情况
        if (dx == dy) {
            Point[] list = getInitedPointArray(Math.abs(end.x - start.x) + 1);
            Point temp = new Point();
            temp.x = Math.min(start.x, end.x);
            temp.y = Math.min(start.y, end.y);
            for (Point p : list) {
                p.x = temp.x;
                p.y = temp.y;
                temp.x++;
                temp.y++;
            }
            return list;
        }

        int p;
        int x = 0;
        int y = 0;
        int delta = 1;
        Point temp = new Point();


        //正常情况
        if (dx > dy) {
            p = 2 * dy - dx;
            Point[] list = getInitedPointArray(dx + 1);
            if (start.x < end.x) {//将两个端点中x偏小的点作为起始点，然后比较y轴是递增还递减
                temp.x = start.x;
                temp.y = start.y;
                if (end.y < start.y) {
                    delta = -1;
                } else {
                    delta = 1;
                }
            } else {
                temp.x = end.x;
                temp.y = end.y;
                if (end.y > start.y) {
                    delta = -1;
                } else {
                    delta = 1;
                }
            }
            for (int i = 0; i <= dx; i++) {
                if (i == 0) {

                    x = list[0].x = temp.x;
                    y = list[0].y = temp.y;
                } else {
                    x = list[i].x = x + 1;
                    if (p >= 0) {
                        y = list[i].y = y + delta;
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
            if (start.y < end.y) {//将两个端点中x偏小的点作为起始点，然后比较y轴是递增还递减
                temp.x = start.x;
                temp.y = start.y;
                if (end.x < start.x) {
                    delta = -1;
                } else {
                    delta = 1;
                }
            } else {
                temp.x = end.x;
                temp.y = end.y;
                if (end.x > start.x) {
                    delta = -1;
                } else {
                    delta = 1;
                }
            }
            for (int i = 0; i <= dy; i++) {
                list[i] = new Point();
                if (i == 0) {
                    x = list[0].x = temp.x;
                    y = list[0].y = temp.y;
                } else {
                    y = list[i].y = y + 1;
                    if (p >= 0) {
                        x = list[i].x = x + delta;
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

    private static Point[] getInitedPointArray(int size) {
        Point[] l = new Point[size];
        for (int i = 0; i < l.length; i++) {
            l[i] = new Point();
        }
        return l;
    }
}
