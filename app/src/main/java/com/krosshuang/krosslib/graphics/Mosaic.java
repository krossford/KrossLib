package com.krosshuang.krosslib.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;

/**
 * Created by krosshuang on 2015/10/26.
 */
public class Mosaic {

    private static final String LOG_TAG = "Mosaic";
    private static final int TEMPLATE_SIZE = 20;

    public static Bitmap mosaic(Bitmap b) {
        Bitmap newBitmap = b.copy(b.getConfig(), true);
        int averageColor;
        for (int y = 0; y < b.getHeight(); y += TEMPLATE_SIZE) {
            for (int x = 0; x < b.getWidth(); x += TEMPLATE_SIZE) {
                averageColor = getAverage(x, y, TEMPLATE_SIZE, b);
                setAreaColor(x, y, TEMPLATE_SIZE, averageColor, newBitmap);
            }
        }
        return newBitmap;
    }

    /**
     * get a rectangle area's average color.
     * if meet a completely transparent pixel, skip it.
     * */
    private static int getAverage(int x, int y, int templateSize, Bitmap b) {
        int pixel;
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        for (int row = y; row < y + templateSize && row < b.getHeight(); row++) {
            for (int col = x; col < x + templateSize && col < b.getWidth(); col++) {
                pixel = b.getPixel(col, row);
                if (Color.alpha(pixel) == 0) {    //skip completely transparent(0x00)
                    continue;
                }
                totalRed += Color.red(pixel);
                totalGreen += Color.green(pixel);
                totalBlue += Color.blue(pixel);
            }
        }

        templateSize = templateSize * templateSize;
        Log.i(LOG_TAG, "average red: " + totalRed);
        return Color.argb(255, (int)(totalRed / templateSize), (int)(totalGreen / templateSize), (int)(totalBlue / templateSize));
    }

    /**
     * set a rectangle area' color to a same value.
     * if the alpha of a pixel is 0 (completely transparent), then skip this pixel.
     * */
    private static void setAreaColor(int x, int y, int templateSize, int color, Bitmap b) {
        for (int row = y; row < y + templateSize && row < b.getHeight(); row++) {
            for (int col = x; col < x + templateSize && col < b.getWidth(); col++) {
                if (Color.alpha(b.getPixel(col, row)) == 0) {
                    continue;
                } else {
                    b.setPixel(col, row, color);
                }

            }
        }
    }
}
