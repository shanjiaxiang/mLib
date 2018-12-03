package com.mit.mylib.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Process;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.WindowManager;

import com.mit.mylib.base.MLog;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.security.auth.x500.X500Principal;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MAppUtils {

    private static final boolean DEBUG = true;
    private static final String TAG = "XAppUtils";
    private static final X500Principal DEBUG_DN = new X500Principal("CN=Android Debug,O=Android,C=US");

    private MAppUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static String getActivityName(Activity activity) {
        String name = activity.getClass().toString();
        return name.substring(name.lastIndexOf(".") + 1, name.length());
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return context.getResources().getDimensionPixelSize(resourceId);
    }

    public static int getStatusBarHeight(Activity activity) {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        return rect.top;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeigth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return windowManager.getDefaultDisplay().getHeight();
    }

    public static String getPhoneModel() {
        return Build.MODEL;
    }

    public static String getSystemVersion() {
        return Build.VERSION.RELEASE;
    }

    public static String getDeviceId(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return telephonyManager.getDeviceId();
    }

    public static int getVerCode(Context context) {
        int verCode = -1;

        try {
            String packageName = context.getPackageName();
            verCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return verCode;
    }

    public static String getVerName(Context context) {
        String verName = "";

        try {
            String packageName = context.getPackageName();
            verName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
        }

        return verName;
    }

    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void installApk(Context context, Uri file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction("android.intent.action.VIEW");
        intent.setDataAndType(file, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    public static void uninstallApk(Context context, String packageName) {
        Intent intent = new Intent("android.intent.action.DELETE");
        Uri packageURI = Uri.parse("package:" + packageName);
        intent.setData(packageURI);
        context.startActivity(intent);
    }

    public static boolean isServiceRunning(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> servicesList = activityManager.getRunningServices(2147483647);
        Iterator var5 = servicesList.iterator();

        while(var5.hasNext()) {
            ActivityManager.RunningServiceInfo si = (ActivityManager.RunningServiceInfo)var5.next();
            if(className.equals(si.service.getClassName())) {
                isRunning = true;
            }
        }

        return isRunning;
    }

    public static boolean stopRunningService(Context context, String className) {
        Intent intent_service = null;
        boolean ret = false;

        try {
            intent_service = new Intent(context, Class.forName(className));
        } catch (Exception var5) {
            var5.printStackTrace();
        }

        if(intent_service != null) {
            ret = context.stopService(intent_service);
        }

        return ret;
    }

    public static int getNumCores() {
        try {
            File dir = new File("/sys/devices/system/cpu/");
            File[] files = dir.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return Pattern.matches("cpu[0-9]", pathname.getName());
                }
            });
            return files.length;
        } catch (Exception var2) {
            return 1;
        }
    }

    public static boolean isNamedProcess(Context context, String processName) {
        if(context != null && !TextUtils.isEmpty(processName)) {
            int pid = Process.myPid();
            ActivityManager manager = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> processInfoList = manager.getRunningAppProcesses();
            if(processInfoList == null) {
                return true;
            } else {
                Iterator var5 = manager.getRunningAppProcesses().iterator();

                ActivityManager.RunningAppProcessInfo processInfo;
                do {
                    if(!var5.hasNext()) {
                        return false;
                    }

                    processInfo = (ActivityManager.RunningAppProcessInfo)var5.next();
                } while(processInfo.pid != pid || !processName.equalsIgnoreCase(processInfo.processName));

                return true;
            }
        } else {
            return false;
        }
    }

    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskList = am.getRunningTasks(1);
        if(taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = ((ActivityManager.RunningTaskInfo)taskList.get(0)).topActivity;
            if(topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public static String getSign(Context context, String pkgName) {
        try {
            PackageInfo pis = context.getPackageManager().getPackageInfo(pkgName, PackageManager.GET_SIGNATURES);
            return hexdigest(pis.signatures[0].toByteArray());
        } catch (PackageManager.NameNotFoundException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    private static String hexdigest(byte[] paramArrayOfByte) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramArrayOfByte);
            byte[] arrayOfByte = localMessageDigest.digest();
            char[] arrayOfChar = new char[32];
            int i = 0;

            for(int j = 0; i < 16; ++j) {
                int k = arrayOfByte[i];
                arrayOfChar[j] = hexDigits[15 & k >>> 4];
                ++j;
                arrayOfChar[j] = hexDigits[k & 15];
                ++i;
            }

            return new String(arrayOfChar);
        } catch (Exception var8) {
            var8.printStackTrace();
            return "";
        }
    }

    @TargetApi(8)
    public static int gc(Context context) {
        long i = (long)getDeviceUsableMemory(context);
        int count = 0;
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
        if(serviceList != null) {
            Iterator var6 = serviceList.iterator();

            while(var6.hasNext()) {
                ActivityManager.RunningServiceInfo service = (ActivityManager.RunningServiceInfo)var6.next();
                if(service.pid != Process.myPid()) {
                    try {
                        Process.killProcess(service.pid);
                        ++count;
                    } catch (Exception var16) {
                        var16.getStackTrace();
                    }
                }
            }
        }

        List<ActivityManager.RunningAppProcessInfo> processList = am.getRunningAppProcesses();
        if(processList != null) {
            Iterator var18 = processList.iterator();

            label46:
            while(true) {
                ActivityManager.RunningAppProcessInfo process;
                do {
                    if(!var18.hasNext()) {
                        break label46;
                    }

                    process = (ActivityManager.RunningAppProcessInfo)var18.next();
                } while(process.importance <= 200);

                String[] pkgList = process.pkgList;
                String[] var10 = pkgList;
                int var11 = pkgList.length;

                for(int var12 = 0; var12 < var11; ++var12) {
                    String pkgName = var10[var12];
                    MLog.d("XAppUtils", "======正在杀死包名：" + pkgName);

                    try {
                        am.killBackgroundProcesses(pkgName);
                        ++count;
                    } catch (Exception var15) {
                        var15.getStackTrace();
                    }
                }
            }
        }

        MLog.d("XAppUtils", "清理了" + ((long)getDeviceUsableMemory(context) - i) + "M内存");
        return count;
    }

    public static int getDeviceUsableMemory(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return (int)(mi.availMem / 1048576L);
    }

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList();
        PackageManager pManager = context.getPackageManager();
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);

        for(int i = 0; i < paklist.size(); ++i) {
            PackageInfo pak = (PackageInfo)paklist.get(i);
            if((pak.applicationInfo.flags & 1) <= 0) {
                apps.add(pak);
            }
        }

        return apps;
    }

    @TargetApi(4)
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isDalvik() {
        return "Dalvik".equals(getCurrentRuntimeValue());
    }

    public static boolean isART() {
        String currentRuntime = getCurrentRuntimeValue();
        return "ART".equals(currentRuntime) || "ART debug build".equals(currentRuntime);
    }

    public static String getCurrentRuntimeValue() {
        try {
            Class systemProperties = Class.forName("android.os.SystemProperties");

            try {
                Method get = systemProperties.getMethod("get", new Class[]{String.class, String.class});
                if(get == null) {
                    return "WTF?!";
                } else {
                    try {
                        String value = (String)get.invoke(systemProperties, new Object[]{"persist.sys.dalvik.vm.lib", "Dalvik"});
                        return "libdvm.so".equals(value)?"Dalvik":("libart.so".equals(value)?"ART":("libartd.so".equals(value)?"ART debug build":value));
                    } catch (IllegalAccessException var3) {
                        return "IllegalAccessException";
                    } catch (IllegalArgumentException var4) {
                        return "IllegalArgumentException";
                    } catch (InvocationTargetException var5) {
                        return "InvocationTargetException";
                    }
                }
            } catch (NoSuchMethodException var6) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException var7) {
            return "SystemProperties class is not found";
        }
    }

    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;

        try {
            PackageInfo pinfo = ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = pinfo.signatures;

            for(int i = 0; i < signatures.length; ++i) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(signatures[i].toByteArray());
                X509Certificate cert = (X509Certificate)cf.generateCertificate(stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if(debuggable) {
                    break;
                }
            }
        } catch (PackageManager.NameNotFoundException var8) {
            ;
        } catch (CertificateException var9) {
            ;
        }

        return debuggable;
    }

    public static String getUUID(Context context) {
        String tmDevice = "";
        String tmSerial = "";
        String tmPhone = "";
        String androidId = "";
        if(ActivityCompat.checkSelfPermission(context, "android.permission.READ_PHONE_STATE") == 0) {
            try {
                TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
                tmDevice = "" + tm.getDeviceId();
                tmSerial = "" + tm.getSimSerialNumber();
                androidId = "" + Settings.Secure.getString(context.getContentResolver(), "android_id");
            } catch (Exception var7) {
                Log.e("AppUtils", "exception:" + var7.getMessage());
            }
        } else {
            Log.e("AppUtils", "没有 android.permission.READ_PHONE_STATE 权限");
            tmDevice = "device";
            tmSerial = "serial";
            androidId = "androidid";
        }

        UUID deviceUuid = new UUID((long)androidId.hashCode(), (long)tmDevice.hashCode() << 32 | (long)tmSerial.hashCode());
        String uniqueId = deviceUuid.toString();
        return uniqueId;
    }

    public static boolean isMainProcess(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        Iterator var5 = processInfos.iterator();

        ActivityManager.RunningAppProcessInfo info;
        do {
            if(!var5.hasNext()) {
                return false;
            }

            info = (ActivityManager.RunningAppProcessInfo)var5.next();
        } while(info.pid != myPid || !mainProcessName.equals(info.processName));

        return true;
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(info != null && info.isConnected()) {
            if(info.getType() == 0) {
                try {
                    Enumeration en = NetworkInterface.getNetworkInterfaces();

                    while(en.hasMoreElements()) {
                        NetworkInterface intf = (NetworkInterface)en.nextElement();
                        Enumeration enumIpAddr = intf.getInetAddresses();

                        while(enumIpAddr.hasMoreElements()) {
                            InetAddress inetAddress = (InetAddress)enumIpAddr.nextElement();
                            if(!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException var6) {
                    var6.printStackTrace();
                }
            } else if(info.getType() == 1) {
                WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
                return ipAddress;
            }
        }

        return null;
    }

    public static String intIP2StringIP(int ip) {
        return (ip & 255) + "." + (ip >> 8 & 255) + "." + (ip >> 16 & 255) + "." + (ip >> 24 & 255);
    }
}
