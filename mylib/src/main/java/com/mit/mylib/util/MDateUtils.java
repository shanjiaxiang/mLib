package com.mit.mylib.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MINUTE;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MDateUtils {
    public static final String yyyyMMDD = "yyyy-MM-dd";
    public static final String yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
    public static final String MMddHHmm = "MM-dd HH:mm";
    public static final String HHmmss = "HH:mm:ss";
    public static final String yyyyMMDDHHmm = "yyyy年M月d日 HH:mm";
    public static final String MMDDHHmm = "M月d日 HH:mm";

    public MDateUtils() {
    }

    public static String dateToString(Date date, String pattern) throws Exception {
        return (new SimpleDateFormat(pattern)).format(date);
    }

    public static Date stringToDate(String dateStr, String pattern) throws Exception {
        return (new SimpleDateFormat(pattern)).parse(dateStr);
    }

    public static String formatMillisToDate(long millis, String format) {
        if(String.valueOf(millis).length() == 10) {
            millis *= 1000L;
        }

        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(Long.valueOf(millis));
    }

    public static long formatDateToMillis(String dateStr, String format) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(format);
            return df.parse(dateStr).getTime();
        } catch (ParseException var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static String formatDate(Date date, String type) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(type);
            return df.format(date);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Date parseDate(String dateStr, String type) {
        SimpleDateFormat df = new SimpleDateFormat(type);
        Date date = null;

        try {
            date = df.parse(dateStr);
        } catch (ParseException var5) {
            var5.printStackTrace();
        }

        return date;
    }

    public static int getYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.YEAR);
    }

    public static int getMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MONTH) + 1;
    }

    public static int getDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.DATE);
    }

    public static String translateDate(Long time) {
        long oneDay = 86400000L;
        Calendar current = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        today.set(Calendar.YEAR, current.get(Calendar.YEAR));
        today.set(Calendar.MONTH, current.get(Calendar.MONTH));
        today.set(Calendar.DATE, current.get(Calendar.DATE));
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis();
        if(time.longValue() >= todayStartTime && time.longValue() < todayStartTime + oneDay) {
            return "今天";
        } else if(time.longValue() >= todayStartTime - oneDay && time.longValue() < todayStartTime) {
            return "昨天";
        } else if(time.longValue() >= todayStartTime - oneDay * 2L && time.longValue() < todayStartTime - oneDay) {
            return "前天";
        } else if(time.longValue() > todayStartTime + oneDay) {
            return "将来某一天";
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date(time.longValue());
            return dateFormat.format(date);
        }
    }

    private String translateDate(long time, long curTime) {
        long oneDay = 86400L;
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(curTime * 1000L);
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        long todayStartTime = today.getTimeInMillis() / 1000L;
        if(time >= todayStartTime) {
            long d = curTime - time;
            if(d <= 60L) {
                return "1分钟前";
            } else if(d <= 3600L) {
                long m = d / 60L;
                if(m <= 0L) {
                    m = 1L;
                }

                return m + "分钟前";
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("今天 HH:mm");
                Date date = new Date(time * 1000L);
                String dateStr = dateFormat.format(date);
                if(!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }

                return dateStr;
            }
        } else {
            SimpleDateFormat dateFormat;
            Date date;
            String dateStr;
            if(time < todayStartTime && time > todayStartTime - oneDay) {
                dateFormat = new SimpleDateFormat("昨天 HH:mm");
                date = new Date(time * 1000L);
                dateStr = dateFormat.format(date);
                if(!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }

                return dateStr;
            } else if(time < todayStartTime - oneDay && time > todayStartTime - 2L * oneDay) {
                dateFormat = new SimpleDateFormat("前天 HH:mm");
                date = new Date(time * 1000L);
                dateStr = dateFormat.format(date);
                if(!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }

                return dateStr;
            } else {
                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                date = new Date(time * 1000L);
                dateStr = dateFormat.format(date);
                if(!TextUtils.isEmpty(dateStr) && dateStr.contains(" 0")) {
                    dateStr = dateStr.replace(" 0", " ");
                }

                return dateStr;
            }
        }
    }
}
