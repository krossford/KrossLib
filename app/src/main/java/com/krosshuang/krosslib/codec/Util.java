package com.krosshuang.krosslib.codec;

public class Util {
    
    public static int getUnsignedNumberFromByte(byte b) {
        if (b < 0) {
            return b + 256;
        }
        return b;
    }
}
