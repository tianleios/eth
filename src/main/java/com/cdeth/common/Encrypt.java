package com.cdeth.common;

import java.security.MessageDigest;

/**
 * Created by tianlei on 2017/十月/13.
 */
public class Encrypt {

    static public String md5(String str) {

        if (str == null || str.length() <= 0) {
            return null;
        }

        try {

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(str.getBytes());

            byte[] taget =  messageDigest.digest();
            return byte2Hex(taget);

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    static public String sha256(String str) {

        if (str == null || str.length() <= 0) {
            return null;
        }

        try {

            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(str.getBytes());

            //由于输出32未自己，所以 byte[] 长度为32，
            byte[] taget =  messageDigest.digest();
            return byte2Hex(taget);

        } catch (Exception e) {

            e.printStackTrace();

        }
        return null;
    }

    static private String byte2Hex(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        String temp = null;
        for (int i = 0 ; i < bytes.length ; i ++) {
            temp = Integer.toHexString(bytes[i] & 0xFF);
            if (temp.length() == 1) {
                stringBuffer.append("0");
            }
            stringBuffer.append(temp);
        }
        return stringBuffer.toString();
    }


}
