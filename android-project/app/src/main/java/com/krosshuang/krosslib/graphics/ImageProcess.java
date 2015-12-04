package com.krosshuang.krosslib.graphics;

import android.graphics.Bitmap;
import android.graphics.Color;

/**
 *
 * Created by krosshuang on 2015/10/26.
 */
public class ImageProcess {

    private static final String LOG_TAG = "ImageProcess";
    private static final int TEMPLATE_SIZE = 20;
    private static final int INTERVAL = 15;

    private static boolean allowRun = true;
    private static boolean statusRun = false;

    private static Bitmap sBitmap = null;
    private static int[] sPixels = null;

    public static void stop() {
        allowRun = false;
    }

    public static boolean checkStatus() {
        return statusRun;
    }

    /**
     * fast mosaic
     * */
    public static Bitmap fastMosaic(Bitmap b) {
        if (b == null) {
            statusRun = false;
            return null;
        }
        allowRun = true;
        if (sBitmap != null) {
            sBitmap.recycle();
        }
        Bitmap.Config c = b.getConfig();
        if (c == null) {
            //有些图片getConfig拿到的是null，图片没问题，可以显示，这个时候copy返回就是null，默认为设为ARGB_8888
            c = Bitmap.Config.ARGB_8888;
        }
        sBitmap = b.copy(c, true);

        int averageColor;
        for (int y = 0; y < b.getHeight(); y += TEMPLATE_SIZE) {
            for (int x = 0; x < b.getWidth(); x += TEMPLATE_SIZE) {
                if (allowRun) {
                    statusRun = true;
                    averageColor = getApproximateAverage(x, y, INTERVAL, TEMPLATE_SIZE, sBitmap);
                    setAreaColor(x, y, TEMPLATE_SIZE, averageColor, sBitmap);

                    //averageColor = getApproximateAverageWithArray(x, y, INTERVAL, TEMPLATE_SIZE, rawPixels);
                } else {
                    statusRun = false;
                    return null;
                }

            }
        }
        statusRun = false;
        return sBitmap;
    }

    public static Bitmap fastMosaic2(Bitmap b) {
        allowRun = true;
        if (sBitmap != null) {
            sBitmap.recycle();
        }
        sBitmap = b.copy(b.getConfig(), true);
        sPixels = new int[sBitmap.getWidth() * sBitmap.getHeight()];

        sBitmap.getPixels(sPixels, 0, sBitmap.getWidth(), 0, 0, sBitmap.getWidth(), sBitmap.getHeight());

        int averageColor;
        for (int y = 0; y < b.getHeight(); y += TEMPLATE_SIZE) {
            for (int x = 0; x < b.getWidth(); x += TEMPLATE_SIZE) {
                if (allowRun) {
                    statusRun = true;
                    averageColor = getApproximateAverage(x, y, INTERVAL, TEMPLATE_SIZE, sBitmap);
                    updatePixels(x, y, TEMPLATE_SIZE, averageColor);
                } else {
                    statusRun = false;
                    return null;
                }

            }
        }

        sBitmap.setPixels(sPixels, 0, sBitmap.getWidth(), 0, 0, sBitmap.getWidth(), sBitmap.getHeight());

        statusRun = false;
        return sBitmap;
    }

    private static void updatePixels(int x, int y, int templateSize, int color) {
        int w = sBitmap.getWidth() - x;
        int h = sBitmap.getHeight() - y;
        if (w >= templateSize) {
            w = templateSize;
        }
        if (h >= templateSize) {
            h = templateSize;
        }

        for (int row = y; row < y + templateSize && row < sBitmap.getHeight(); row++) {
            for (int col = x; col < x + templateSize && col < sBitmap.getWidth(); col++) {
                if (Color.alpha(sBitmap.getPixel(col, row)) == 0) {
                    continue;
                } else {
                    sPixels[row * w + col] = Color.RED;
                }
            }
        }
    }

    private static int getApproximateAverage(int x, int y, int interval, int templateSize, Bitmap b) {
        int pixel;
        long totalRed = 0;
        long totalGreen = 0;
        long totalBlue = 0;
        int count = 0;

        int[] pixels = new int[templateSize * templateSize];

        int w = b.getWidth() - x;
        int h = b.getHeight() - y;
        if (w >= templateSize) {
            w = templateSize;
        }
        if (h >= templateSize) {
            h = templateSize;
        }
        b.getPixels(pixels, 0, templateSize, x, y, w, h);

        for (int row = 0; row < templateSize && row < h; row += interval) {
            for (int col = 0; col < templateSize && col < w; col += interval) {
                pixel = pixels[row * templateSize + col];
                //pixel = b.getPixel(col, row);
                if (Color.alpha(pixel) == 0) {    //skip completely transparent(0x00)
                    continue;
                }
                totalRed += Color.red(pixel);
                totalGreen += Color.green(pixel);
                totalBlue += Color.blue(pixel);
                count++;
            }
        }

        //Log.i(LOG_TAG, "average red: " + totalRed);
        if (count == 0) {
            return Color.argb(0, 255, 255, 255);
        } else {
            return Color.argb(255, (int)(totalRed / count), (int)(totalGreen / count), (int)(totalBlue / count));
        }

    }

    /**
     * get a rectangle area's average color.
     * if meet a completely sdk_paintpad_transparent pixel, skip it.
     * */
    private static int getAverage(int x, int y, int templateSize, Bitmap b) {
        return getApproximateAverage(x, y, 1, templateSize, b);
    }

    /**
     * set a rectangle area' color to a same value.
     * if the alpha of a pixel is 0 (completely sdk_paintpad_transparent), then skip this pixel.
     * */
    private static void setAreaColor(int x, int y, int templateSize, int color, Bitmap b) {
        int w = b.getWidth() - x;
        int h = b.getHeight() - y;
        if (w >= templateSize) {
            w = templateSize;
        }
        if (h >= templateSize) {
            h = templateSize;
        }

        int[] pixels = new int[w * h];

        for (int row = 0; row < templateSize && row < h; row++) {
            for (int col = 0; col < x + templateSize && col < w; col++) {
                if (Color.alpha(b.getPixel(col + x, row + y)) == 0) {
                    continue;
                } else {
                    pixels[row * w + col] = color;
                }
            }
        }

        b.setPixels(pixels, 0, w, x, y, w, h);

        /*
        for (int row = y; row < y + templateSize && row < b.getHeight(); row++) {
            for (int col = x; col < x + templateSize && col < b.getWidth(); col++) {
                if (Color.alpha(b.getPixel(col, row)) == 0) {
                    continue;
                } else {
                    b.setPixel(col, row, color);
                }

            }
        }
        */
    }

    public static Bitmap blur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap;

        bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }
}
