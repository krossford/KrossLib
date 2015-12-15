package com.krosshuang.krosslib.lib.codec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LZW {

    public static List<Integer> encode(String data) {
        List<Integer> result = new ArrayList<>();
        
        //��ʼ��dictionary
        int idleCode = 256;
        HashMap<String, Integer> dic = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dic.put((char)i + "", i);
        }
        
        String previous = "";
        String pc = "";
        for (char c : data.toCharArray()) {
            pc = previous + c;
            if (dic.containsKey(pc)) {
                previous = pc;
            } else {
                dic.put(pc, idleCode++);
                result.add(dic.get(previous));
                previous = "" + c;
            }
        }
        
        //���һ�����
        if (!previous.equals("")) {
            result.add(dic.get(previous));
        }
        
        return result;
    }
    
    public static String decode(List<Integer> arr) {
        
        StringBuilder result = new StringBuilder();
        int idleCode = 256;
        HashMap<Integer, String> dic = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dic.put(i, (char)i + "");
        }
        
        String p = "";
        String c = "";
        
        for (int code : arr) {
            if (dic.containsKey(code)) {
                c = dic.get(code);
            } else if (code == idleCode) {// aaaaaaa
                c = c + c.charAt(0);
            } else {
                System.out.println("bad encode");
            }
            
            if (!p.equals("")) {
                dic.put(idleCode++, p + c.charAt(0));
            }
            result.append(c);
            p = c;
        }
        
        return result.toString();
    }
    
    
}
