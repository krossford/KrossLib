package com.krosshuang.krosslib.lib.image;

import android.media.ExifInterface;

/**
 * Created by krosshuang on 2016/1/1.
 */
public class ImageUtil {

    /**
     * get the pic's orientation, some pic make by Camera
     * */
    public static int getOritentationDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return degree;
    }
}
