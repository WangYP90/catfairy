package com.tj24.base.utils;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import androidx.annotation.NonNull;

/**
 * @Description:md5
 * @Createdtime:2020/2/14 14:05
 * @Author:TangJiang
 * @Version: V.1.0.0
 */
public class MD5Util {

    @NonNull
    public static String encode(String string, String slat) {
        return encode(string + slat);
    }

    /**
     * MD5加密
     */
    @NonNull
    public static String encode(String string) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] bytes = md5.digest(string.getBytes());
            StringBuilder result = new StringBuilder();
            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    /**
     * MD5加密
     */
    @NonNull
    public static String encode(String string, int times) {
        if (TextUtils.isEmpty(string)) {
            return "";
        }
        if (times <= 1) {
            return encode(string);
        }
        String md5 = encode(string);
        for (int i = 0; i < times - 1; i++) {
            md5 = encode(md5);
        }
        return md5;
    }

    @NonNull
    public static String encode2(String string) {
        return encode(string, 2);
    }

    @NonNull
    public static String encode(File file) {
        if (file == null || !file.isFile() || !file.exists()) {
            return "";
        }
        FileInputStream in = null;
        StringBuilder result = new StringBuilder();
        byte buffer[] = new byte[8192];
        int len;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer)) != -1) {
                md5.update(buffer, 0, len);
            }
            byte[] bytes = md5.digest();

            for (byte b : bytes) {
                String temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = "0" + temp;
                }
                result.append(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result.toString();
    }
}
