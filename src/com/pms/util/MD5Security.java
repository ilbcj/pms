package com.pms.util;

import java.io.InputStream;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException;  
public class MD5Security {  
    private final static char[] hexDigits = { '0', '1', '2', '3', '4', '5',  
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
    private static String bytesToHex(byte[] bytes) {  
        StringBuffer sb = new StringBuffer();  
        int t;  
        for (int i = 0; i < 16; i++) {  
            t = bytes[i];  
            if (t < 0)  
                t += 256;  
            sb.append(hexDigits[(t >>> 4)]);  
            sb.append(hexDigits[(t % 16)]);  
        }  
        return sb.toString();  
    }  
    public static String md5(String input) throws Exception {  
        return code(input, 32);  
    }
    
    public static String md5(byte[] input) throws Exception {  
        return code(input, 32);  
    }
    
    public static String md5(InputStream input) throws Exception {  
        return code(input, 32);  
    }
    
    public static String code(String input, int bit) throws Exception {  
        try {  
            MessageDigest md = MessageDigest.getInstance(System.getProperty(  
                    "MD5.algorithm", "MD5"));  
            if (bit == 16)  
                return bytesToHex(md.digest(input.getBytes("utf-8")))  
                        .substring(8, 24);  
            return bytesToHex(md.digest(input.getBytes("utf-8")));  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            throw new Exception("Could not found MD5 algorithm.", e);  
        }  
    }
    
    public static String code(byte[] input, int bit) throws Exception {  
        try {  
            MessageDigest md = MessageDigest.getInstance(System.getProperty(  
                    "MD5.algorithm", "MD5"));  
            if (bit == 16)  
                return bytesToHex(md.digest(input))  
                        .substring(8, 24);  
            return bytesToHex(md.digest(input));  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            throw new Exception("Could not found MD5 algorithm.", e);  
        }  
    }
    
    public static String code(InputStream input, int bit) throws Exception {  
        try {  
            MessageDigest md = MessageDigest.getInstance(System.getProperty(  
                    "MD5.algorithm", "MD5"));
            byte[] buf = new byte[1000];
            int readCount = 0; // 已经成功读取的字节的个数
            while ((readCount = input.read(buf)) != -1) {  
                md.update(buf, 0, readCount);  
            }
            
            if (bit == 16)
                return bytesToHex(md.digest())  
                        .substring(8, 24);  
            return bytesToHex(md.digest());  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
            throw new Exception("Could not found MD5 algorithm.", e);  
        }  
    }
    
    public static String md5_3(String b) throws Exception{  
        MessageDigest md = MessageDigest.getInstance(System.getProperty(  
                "MD5.algorithm", "MD5"));  
        byte[] a = md.digest(b.getBytes());  
        a = md.digest(a);  
        a = md.digest(a);  
          
        return bytesToHex(a);  
    }  
}  