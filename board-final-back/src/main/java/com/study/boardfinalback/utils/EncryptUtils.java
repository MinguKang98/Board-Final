package com.study.boardfinalback.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptUtils {

    public static String salt = "dfd1c31fa0c3dafdfd0990decdbc8386"; // 소금값

    public static String encryptPassword(String str) {
        String result = "";

        byte[] strBytes = str.getBytes();
        byte[] saltBytes = salt.getBytes();
        byte[] mergeBytes = new byte[strBytes.length + saltBytes.length];

        System.arraycopy(strBytes, 0, mergeBytes, 0, strBytes.length);
        System.arraycopy(saltBytes, 0, mergeBytes, strBytes.length, saltBytes.length);
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(mergeBytes);

            byte byteData[] = md.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Encrypt Error - NoSuchAlgorithmException");
            result = null;
        }
        return result;
    }

}
