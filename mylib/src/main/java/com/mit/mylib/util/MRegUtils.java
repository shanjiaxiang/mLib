package com.mit.mylib.util;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MRegUtils {

    private static final int[] Wi = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1};
    private static final int[] ValideCode = new int[]{1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2};

    private MRegUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static boolean isEmail(String email) {
        Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isEmail2(String data) {
        String expr = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        return data.matches(expr);
    }

    public static boolean isMobileNumber(String data) {
        String expr = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        return data.matches(expr);
    }

    public static boolean isNumberLetter(String data) {
        String expr = "^[A-Za-z0-9]+$";
        return data.matches(expr);
    }

    public static boolean isNumber(String data) {
        String expr = "^[0-9]+$";
        return data.matches(expr);
    }

    public static boolean isLetter(String data) {
        String expr = "^[A-Za-z]+$";
        return data.matches(expr);
    }

    public static boolean isChinese(String data) {
        String expr = "^[Α-￥]+$";
        return data.matches(expr);
    }

    public static boolean isContainChinese(String data) {
        String chinese = "[Α-￥]";
        if(MStringUtils.isEmpty(data)) {
            for(int i = 0; i < data.length(); ++i) {
                String temp = data.substring(i, i + 1);
                boolean flag = temp.matches(chinese);
                if(flag) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean isDianWeiShu(String data, int length) {
        String expr = "^[1-9][0-9]+\\.[0-9]{" + length + "}$";
        return data.matches(expr);
    }

    public static boolean isPostCode(String data) {
        String expr = "^[0-9]{6,10}";
        return data.matches(expr);
    }

    public static boolean isLength(String data, int length) {
        return data != null && data.length() == length;
    }

    public static boolean isCard(String data) {
        String expr = "^[0-9]{17}[0-9xX]$";
        return data.matches(expr);
    }

    public static boolean isCardValidated(String data) {
        return isCard(data) && isValidityBirthBy18IdCard(data) && isTrueValidateCodeBy18IdCard(data);
    }

    public static boolean isValidityBirthBy18IdCard(String idCard18) {
        if(idCard18.length() != 18) {
            return false;
        } else {
            try {
                int year = Integer.valueOf(idCard18.substring(6, 10)).intValue();
                int month = Integer.valueOf(idCard18.substring(10, 12)).intValue();
                int day = Integer.valueOf(idCard18.substring(12, 14)).intValue();
                Date temp_date = new Date(year, month - 1, day);
                return temp_date.getYear() == year && temp_date.getMonth() == month - 1 && temp_date.getDate() == day;
            } catch (NumberFormatException var5) {
                return false;
            }
        }
    }

    public static boolean isTrueValidateCodeBy18IdCard(String idCard18) {
        try {
            if(idCard18.length() != 18) {
                return false;
            } else {
                int sum = 0;
                String s = String.valueOf(idCard18.charAt(17));
                if(s.equals("x") || s.equals("X")) {
                    s = "10";
                }

                int valCodePosition;
                for(valCodePosition = 0; valCodePosition < 17; ++valCodePosition) {
                    sum += Wi[valCodePosition] * Integer.valueOf(String.valueOf(idCard18.charAt(valCodePosition))).intValue();
                }

                valCodePosition = sum % 11;
                return Integer.valueOf(s).intValue() == ValideCode[valCodePosition];
            }
        } catch (NumberFormatException var4) {
            return false;
        }
    }
}
