package com.mit.mylib.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MEncodeURI {

    public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    public MEncodeURI() {
    }

    public static String encodeURI(String str) throws UnsupportedEncodingException {
        String isoStr = new String(str.getBytes("UTF8"), "ISO-8859-1");
        char[] chars = isoStr.toCharArray();
        StringBuffer sb = new StringBuffer();

        for(int i = 0; i < chars.length; ++i) {
            if((chars[i] > 122 || chars[i] < 97) && (chars[i] > 90 || chars[i] < 65) && chars[i] != 45 && chars[i] != 95 && chars[i] != 46 && chars[i] != 33 && chars[i] != 126 && chars[i] != 42 && chars[i] != 39 && chars[i] != 40 && chars[i] != 41 && chars[i] != 59 && chars[i] != 47 && chars[i] != 63 && chars[i] != 58 && chars[i] != 64 && chars[i] != 38 && chars[i] != 61 && chars[i] != 43 && chars[i] != 36 && chars[i] != 44 && chars[i] != 35 && (chars[i] > 57 || chars[i] < 48)) {
                sb.append("%");
                sb.append(Integer.toHexString(chars[i]));
            } else {
                sb.append(chars[i]);
            }
        }

        return sb.toString();
    }

    public static String encodeURIComponent(String input) {
        if(null != input && !"".equals(input.trim())) {
            int l = input.length();
            StringBuilder o = new StringBuilder(l * 3);

            try {
                for(int i = 0; i < l; ++i) {
                    String e = input.substring(i, i + 1);
                    if("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()".indexOf(e) == -1) {
                        byte[] b = e.getBytes("utf-8");
                        o.append(getHex(b));
                    } else {
                        o.append(e);
                    }
                }

                return o.toString();
            } catch (UnsupportedEncodingException var6) {
                var6.printStackTrace();
                return input;
            }
        } else {
            return input;
        }
    }

    private static String getHex(byte[] buf) {
        StringBuilder o = new StringBuilder(buf.length * 3);

        for(int i = 0; i < buf.length; ++i) {
            int n = buf[i] & 255;
            o.append("%");
            if(n < 16) {
                o.append("0");
            }

            o.append(Long.toString((long)n, 16).toUpperCase());
        }

        return o.toString();
    }

    public static String decodeURIComponent(String encodedURI) {
        StringBuffer buffer = new StringBuffer();
        int sumb = 0;
        int i = 0;

        for(int more = -1; i < encodedURI.length(); ++i) {
            char actualChar = encodedURI.charAt(i);
            int bytePattern;
            switch(actualChar) {
                case '%':
                    ++i;
                    actualChar = encodedURI.charAt(i);
                    int hb = (Character.isDigit(actualChar)?actualChar - 48:10 + Character.toLowerCase(actualChar) - 97) & 15;
                    ++i;
                    actualChar = encodedURI.charAt(i);
                    int lb = (Character.isDigit(actualChar)?actualChar - 48:10 + Character.toLowerCase(actualChar) - 97) & 15;
                    bytePattern = hb << 4 | lb;
                    break;
                case '+':
                    bytePattern = 32;
                    break;
                default:
                    bytePattern = actualChar;
            }

            if((bytePattern & 192) == 128) {
                sumb = sumb << 6 | bytePattern & 63;
                --more;
                if(more == 0) {
                    buffer.append((char)sumb);
                }
            } else if((bytePattern & 128) == 0) {
                buffer.append((char)bytePattern);
            } else if((bytePattern & 224) == 192) {
                sumb = bytePattern & 31;
                more = 1;
            } else if((bytePattern & 240) == 224) {
                sumb = bytePattern & 15;
                more = 2;
            } else if((bytePattern & 248) == 240) {
                sumb = bytePattern & 7;
                more = 3;
            } else if((bytePattern & 252) == 248) {
                sumb = bytePattern & 3;
                more = 4;
            } else {
                sumb = bytePattern & 1;
                more = 5;
            }
        }

        return buffer.toString();
    }

    public static String urlEncode(String url) {
        try {
            return URLEncoder.encode(url, "utf-8");
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }

    public static String urlDecode(String url) {
        try {
            return URLDecoder.decode(url, "utf-8");
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }
}
