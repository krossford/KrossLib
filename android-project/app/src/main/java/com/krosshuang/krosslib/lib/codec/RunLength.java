package com.krosshuang.krosslib.lib.codec;

import java.util.ArrayList;

public class RunLength {
    
    public static byte[] decode(byte[] data) {
        int index = 0;
        int length = data.length;
        int input;
        ArrayList<Byte> result = new ArrayList<>();
        
        while (index < length) {
            input = Util.getUnsignedNumberFromByte(data[index++]);
            if (input < 128) {
                for (int i = 0; i < input; i++) {
                    result.add(data[index++]);
                }
            } else if (input > 128) {
                for (int i = 0; i < (257 - input); i++) {
                    result.add(data[index]);
                }
                index++;
            } else if (input == 128) {
                byte[] arr = new byte[result.size()];
                for (int i = 0; i < result.size(); i++) {
                    arr[i] = result.get(i);
                }
                return arr;
            }
            
        }
        //bad end
        return null;
    }
    
    public static byte[] encode(byte[] data) {
        ArrayList<Byte> result = new ArrayList<>();
        ArrayList<Byte> cd = new ArrayList<>();
        int length = 0;
        
        for (byte b : data) {
            if (cd.size() == 0) {   //cd == null
                cd.add(b);
                length = 1;
            } else {
                if (Util.getUnsignedNumberFromByte(b) == cd.get(cd.size() - 1)) {   // input == cd[last]
                    if (length < 128) {
                        if (length < 2) {
                            length = 255;
                        } else {
                            result.add((byte)(length - 1));
                            for (int i = 0; i < cd.size() - 1; i++) {
                                result.add(cd.get(i));
                            }
                            cd.clear();
                            cd.add(b);
                            length = 255;
                        }
                    } else {
                        length--;
                        if (length == 129) {
                            result.add((byte)129);
                            result.add(b);
                            cd.clear();
                        }
                    }
                } else {
                    if (length < 128) {
                        cd.add(b);
                        length++;
                        if (length == 127) {
                            result.add((byte)127);
                            for (int i = 0; i < cd.size() - 1; i++) {
                                result.add(cd.get(i));
                            }
                            cd.clear();
                        }
                    } else {
                        result.add((byte)length);
                        result.add(cd.get(0));
                        cd.clear();
                        cd.add(b);
                        length = 1;
                    }
                }
            }
        }
        
        result.add((byte)length);
        for (int i = 0; i < cd.size(); i++) {
            result.add(cd.get(i));
        }
        
        result.add((byte)128);
        
        byte[] arr = new byte[result.size()];
        for (int i = 0; i < result.size(); i++) {
            arr[i] = result.get(i);
        }
        return arr;
    }
}
