package com.mit.mylib.util;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MNetUtil {

    public MNetUtil() {
    }

    public static boolean isNetworkAvailable(Context context) {
        boolean netstate = false;
        ConnectivityManager connectivity = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if(info != null) {
                for(int i = 0; i < info.length; ++i) {
                    if(info[i].getState() == NetworkInfo.State.CONNECTED) {
                        netstate = true;
                        break;
                    }
                }
            }
        }

        return netstate;
    }

    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        return lm.isProviderEnabled("gps");
    }

    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 1;
    }

    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.getType() == 0;
    }

    public static boolean is4G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null && activeNetInfo.isConnectedOrConnecting() && activeNetInfo.getType() == 13;
    }

    public static boolean isWiFi(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo.State wifi = manager.getNetworkInfo(1).getState();
        return wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING;
    }

    public static boolean isIP(String ip) {
        Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    public static int ipToInt(String addr) {
        String[] addrArray = addr.split("\\.");
        int num = 0;

        for(int i = 0; i < addrArray.length; ++i) {
            int power = 3 - i;
            num = (int)((double)num + (double)(Integer.parseInt(addrArray[i]) % 256) * Math.pow(256.0D, (double)power));
        }

        return num;
    }

    public MNetUtil.NetState isConnected(Context context) {
        MNetUtil.NetState stateCode = MNetUtil.NetState.NET_NO;
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if(ni != null && ni.isConnectedOrConnecting()) {
            switch(ni.getType()) {
                case 0:
                    switch(ni.getSubtype()) {
                        case 1:
                        case 2:
                        case 4:
                        case 7:
                        case 11:
                            stateCode = MNetUtil.NetState.NET_2G;
                            return stateCode;
                        case 3:
                        case 5:
                        case 6:
                        case 8:
                        case 9:
                        case 10:
                        case 12:
                        case 14:
                        case 15:
                            stateCode = MNetUtil.NetState.NET_3G;
                            return stateCode;
                        case 13:
                            stateCode = MNetUtil.NetState.NET_4G;
                            return stateCode;
                        default:
                            stateCode = MNetUtil.NetState.NET_UNKNOWN;
                            return stateCode;
                    }
                case 1:
                    stateCode = MNetUtil.NetState.NET_WIFI;
                    break;
                default:
                    stateCode = MNetUtil.NetState.NET_UNKNOWN;
            }
        }

        return stateCode;
    }

    public static Map<String, String> getUrlParams(String url) {
        HashMap params = null;

        try {
            String[] urlParts = url.split("\\?");
            if(urlParts.length > 1) {
                params = new HashMap();
                String query = urlParts[1];
                String[] var4 = query.split("&");
                int var5 = var4.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    String param = var4[var6];
                    String[] pair = param.split("=");
                    String key = URLDecoder.decode(pair[0], "UTF-8");
                    String value = "";
                    if(pair.length > 1) {
                        value = URLDecoder.decode(pair[1], "UTF-8");
                    }

                    params.put(key, value);
                }
            }
        } catch (UnsupportedEncodingException var11) {
            var11.printStackTrace();
        }

        return params;
    }

    public static boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (MalformedURLException var2) {
            return false;
        }
    }

    public static enum NetState {
        NET_NO,
        NET_2G,
        NET_3G,
        NET_4G,
        NET_WIFI,
        NET_UNKNOWN;

        private NetState() {
        }
    }
}
