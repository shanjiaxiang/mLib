package com.mit.mlib.base;

import android.util.Log;

/**
 * Created by shanjiaxiang on 2018\11\30 0030.
 */

public final class MLog {
    private static boolean isLogShow = false;

    public static void disableLogging() {
        isLogShow = false;
    }

    public static void enableLogging() {
        isLogShow = true;
    }

    public static boolean isLogShow() {
        return isLogShow;
    }

    private MLog() {
        throw new Error("Do not need instantiate!");
    }

    public static void v(Object obj) {
        if(isLogShow) {
            String tag = getClassName();
            String msg = obj != null?obj.toString():"obj == null";
            Log.v(tag, msg);
        }

    }

    public static void d(Object obj) {
        if(isLogShow) {
            String tag = getClassName();
            String msg = obj != null?obj.toString():"obj == null";
            Log.d(tag, msg);
        }

    }

    public static void i(Object obj) {
        if(isLogShow) {
            String tag = getClassName();
            String msg = obj != null?obj.toString():"obj == null";
            Log.i(tag, msg);
        }

    }

    public static void w(Object obj) {
        if(isLogShow) {
            String tag = getClassName();
            String msg = obj != null?obj.toString():"obj == null";
            Log.w(tag, msg);
        }

    }

    public static void e(Object obj) {
        if(isLogShow) {
            String tag = getClassName();
            String msg = obj != null?obj.toString():"obj == null";
            Log.e(tag, msg);
        }

    }

    public static void wtf(Object obj) {
        if(isLogShow) {
            String tag = getClassName();
            String msg = obj != null?obj.toString():"obj == null";
            Log.wtf(tag, msg);
        }

    }

    public static void v(String tag, String msg) {
        if(isLogShow) {
            Log.v(tag, msg);
        }

    }

    public static void d(String tag, String msg) {
        if(isLogShow) {
            Log.d(tag, msg);
        }

    }

    public static void d(Object obj, String msg) {
        if(isLogShow) {
            Log.d(obj.getClass().getName(), msg);
        }

    }

    public static void i(String tag, String msg) {
        if(isLogShow) {
            Log.i(tag, msg);
        }

    }

    public static void i(Object obj, String msg) {
        if(isLogShow) {
            Log.i(obj.getClass().getName(), msg);
        }

    }

    public static void w(String tag, String msg) {
        if(isLogShow) {
            Log.w(tag, msg);
        }

    }

    public static void e(String tag, String msg) {
        if(isLogShow) {
            Log.e(tag, msg);
        }

    }

    public static void e(Object obj, String msg) {
        if(isLogShow) {
            Log.e(obj.getClass().getName(), msg);
        }

    }

    public static void wtf(String tag, String msg) {
        if(isLogShow) {
            Log.wtf(tag, msg);
        }

    }

    //打印类名、调用方法和行数
    public static void print() {
        if(isLogShow) {
            String tag = getClassName();
            String method = callMethodAndLine();
            Log.v(tag, method);
        }

    }

    //打印对象信息
    public static void print(Object object) {
        if(isLogShow) {
            String tag = getClassName();
            String method = callMethodAndLine();
            String content = "";
            if(object != null) {
                content = object.toString() + "                    ----    " + method;
            } else {
                content = " ##                 ----    " + method;
            }

            Log.d(tag, content);
        }

    }

    //打印错误对象信息
    public static void printError(Object object) {
        if(isLogShow) {
            String tag = getClassName();
            String method = callMethodAndLine();
            String content = "";
            if(object != null) {
                content = object.toString() + "                    ----    " + method;
            } else {
                content = " ##                     ----    " + method;
            }

            Log.e(tag, content);
        }

    }

    //打印调用路径
    public static void printCallHierarchy() {
        if(isLogShow) {
            String tag = getClassName();
            String method = callMethodAndLine();
            String hierarchy = getCallHierarchy();
            Log.v(tag, method + hierarchy);
        }

    }

    //打印TAG为MYLOG的自定义格式LOG
    public static void printMyLog(Object object) {
        if(isLogShow) {
            String tag = "MYLOG";
            String method = callMethodAndLine();
            String content = "";
            if(object != null) {
                content = object.toString() + "                    ----    " + method;
            } else {
                content = " ##                 ----    " + method;
            }

            Log.d(tag, content);
        }

    }

    //获取调用路径
    private static String getCallHierarchy() {
        String result = "";
        StackTraceElement[] trace = (new Exception()).getStackTrace();

        for(int i = 2; i < trace.length; ++i) {
            result = result + "\r\t" + trace[i].getClassName() + "" + trace[i].getMethodName() + "():" + trace[i].getLineNumber();
        }

        return result;
    }

    //获取类名
    private static String getClassName() {
        String result = "";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result = thisMethodStack.getClassName();
        return result;
    }

    //获取调用次方法的 类名、方法名、文件名、行号
    private static String callMethodAndLine() {
        String result = "at ";
        StackTraceElement thisMethodStack = (new Exception()).getStackTrace()[2];
        result = result + thisMethodStack.getClassName() + "";
        result = result + thisMethodStack.getMethodName();
        result = result + "(" + thisMethodStack.getFileName();
        result = result + ":" + thisMethodStack.getLineNumber() + ")  ";
        return result;
    }
}
