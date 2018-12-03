package com.mit.mylib.util;

import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MStringUtils {
    public MStringUtils() {
        throw new Error("Do not need intantiate!");
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str);
    }

    public static int toInt(String str, int defaultValue) {
        try {
            return Integer.parseInt(str);
        } catch (Exception var3) {
            return defaultValue;
        }
    }

    public static final String byteArrayToHexString(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        byte[] tempData = data;
        int len = data.length;
        for (int i = 0; i < len; i++) {
            byte b = tempData[i];
            int v = b & 255;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.getDefault());
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] d = new byte[len / 2];

        for (int i = 0; i < len; i++) {
            d[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return d;
    }

    public static String keywordMadeRed(String sourceString, String keyword) {
        String result = "";
        if(sourceString != null && !"".equals(sourceString.trim())) {
            if(keyword != null && !"".equals(keyword.trim())) {
                result = sourceString.replaceAll(keyword, "<font color=\"red\">" + keyword + "</font>");
            } else {
                result = sourceString;
            }
        }

        return result;
    }

    public static String addHtmlRedFlag(String string) {
        return "<font color=\"red\">" + string + "</font>";
    }

}
