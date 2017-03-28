package com.krosshuang.krosslib.lib.codec;

public class Main {
 
    public static void main(String[] args) {
        
        /*
        List<Integer> result = LZW.encode("abcbcabcabcd");
        System.out.println(result);
        
        System.out.println(LZW.decode(result));
        */
        
        /*
        byte b = 0;
        for (int i = 0; i < 256; i++) {
            b = (byte)i;
            System.out.println(i + " - " + Util.getUnsignedNumberFromByte(b));
        }
        */
       
        String str = "abcdaaaaaaaaaaaaaaaaaaaaabcdqqqqqqqqqqqqqqqqqqqqqqqabcdqqqqqqqqabc";
        byte[] originData = str.getBytes();
        
        byte[] result = RunLength.encode(originData);
        
        byte[] src = RunLength.decode(result);
        
        System.out.println("origin length: " + originData.length + " compressed data length: " + result.length);
        System.out.println("uncompressed data == origin data: " + str.equals(new String(src)));
    }
}
