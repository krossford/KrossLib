package com.krosshuang.krosslib.lib.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by krosshuang on 2015/12/2.
 */
public class FileUtil {

    private static final String LOG_TAG = "FileUtil";

    /**
     * 获取文件的所有字节
     * @param f 目标文件对象
     * @return 文件中的所有字节
     * */
    public static byte[] getBytesFromFile(File f) throws IOException {
        FileInputStream fis = new FileInputStream(f);
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = fis.read(buffer)) > 0) {
            os.write(buffer, 0, len);
        }

        fis.close();
        os.close();

        return os.toByteArray();
    }

    public static int twoBytesToInt(byte[] bytes) {
        if (bytes.length < 2) {
            return 0;
        }

        int i = 0x00000000;
        i |= (bytes[0] << 8);
        i |= bytes[1];

        //java默认是填1，所以上面两行代码搞完后，整型前面全是1
        i &= 0x0000ffff;

        return i;
    }

    public static int fourBytesToInt(byte[] bytes) {
        if (bytes.length < 4) {
            return 0;
        }
        int i = 0x00000000;
        i |= (bytes[0] << 24);
        i |= (bytes[1] << 16);
        i |= (bytes[2] << 8);
        i |= bytes[3];

        return i;
    }

    public static boolean isPNG(byte[] data) {
        // 137 80 78 71 13 10 26 10
        final int part1 = 0x89504e47;
        final int part2 = 0x0d0a1a0a;
        return part1 == fourBytesToInt(new byte[]{data[0], data[1], data[2], data[3]}) && part2 == fourBytesToInt(new byte[]{data[4], data[5], data[6], data[7]});
    }

    public static boolean isJPG(byte[] data) {
        final int head = 0x0000ffd8;
        final int end = 0x0000ffd9;

        //TODO, 不能通过判断ffd9是否是文件尾的方式来判断是否是JPG图片，因为有的图片尾部会添加额外的数据，ffd9就不是文件尾了 2015-12-4 17:38:36
        return head == twoBytesToInt(new byte[]{data[0], data[1]});
    }

    public static boolean isBMP(byte[] data) {
        final int head = 0x424d;
        return head == twoBytesToInt(new byte[]{data[0], data[1]});
    }

    public static boolean isGIF(byte[] data) {
        return new String(new byte[]{data[0], data[1], data[2]}).equals("GIF");
    }


    /**
     * 获取文件的后缀名
     * */
    public static String getSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename.toLowerCase();
    }
}
