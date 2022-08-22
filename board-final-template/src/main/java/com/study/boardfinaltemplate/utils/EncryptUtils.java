package com.study.boardfinaltemplate.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 암호화 기능 제공 클래스
 */
@Slf4j
public class EncryptUtils {

    public static String salt = "dfd1c31fa0c3dafdfd0990decdbc8386"; // 소금값

    /**
     * 입력받은 문자열 암호화
     * @param str : 암호화 할 문자열
     * @return 암호화 된 문자열
     */
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
            log.error("Encrypt Error - NoSuchAlgorithmException");
            result = null;
        }
        return result;
    }

}
