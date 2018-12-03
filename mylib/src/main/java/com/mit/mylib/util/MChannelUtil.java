package com.mit.mylib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MChannelUtil {
    private static final String CHANNEL_KEY = "channel_key";
    private static final String CHANNEL_VERSION_KEY = "channel_version_key";
    private static String mChannel;

    public MChannelUtil() {
    }

    public static String getChannel(Context context) {
        return getChannel(context, "Channel_Default");
    }

    public static String getChannel(Context context, String defaultChannel) {
        if(!TextUtils.isEmpty(mChannel)) {
            return mChannel;
        } else {
            mChannel = getChannelBySharedPreferences(context);
            if(!TextUtils.isEmpty(mChannel)) {
                return mChannel;
            } else {
                mChannel = getChannelFromApk(context, "channel_key");
                if(!TextUtils.isEmpty(mChannel)) {
                    saveChannelBySharedPreferences(context, mChannel);
                    return mChannel;
                } else {
                    return defaultChannel;
                }
            }
        }
    }

    @TargetApi(4)
    private static String getChannelFromApk(Context context, String channelKey) {
        ApplicationInfo appinfo = context.getApplicationInfo();
        String sourceDir = appinfo.sourceDir;
        String key = "META-INF/" + channelKey;
        String ret = "";
        ZipFile zipfile = null;

        try {
            zipfile = new ZipFile(sourceDir);
            Enumeration entries = zipfile.entries();

            while(entries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)entries.nextElement();
                String entryName = entry.getName();
                if(entryName.startsWith(key)) {
                    ret = entryName;
                    break;
                }
            }
        } catch (IOException var18) {
            var18.printStackTrace();
        } finally {
            if(zipfile != null) {
                try {
                    zipfile.close();
                } catch (IOException var17) {
                    var17.printStackTrace();
                }
            }

        }

        String[] split = ret.split("\\*");
        String channel = "";
        if(split != null && split.length >= 2) {
            channel = ret.substring(split[0].length() + 1);
        }

        return channel;
    }

    private static void saveChannelBySharedPreferences(Context context, String channel) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(CHANNEL_KEY, channel);
        editor.putInt(CHANNEL_VERSION_KEY, getVersionCode(context));
        editor.commit();
    }

    private static String getChannelBySharedPreferences(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        int currentVersionCode = getVersionCode(context);
        if(currentVersionCode == -1) {
            return "";
        } else {
            int versionCodeSaved = sp.getInt(CHANNEL_VERSION_KEY, -1);
            return versionCodeSaved == -1?"":(currentVersionCode != versionCodeSaved?"":sp.getString(CHANNEL_KEY, ""));
        }
    }

    private static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException var2) {
            var2.printStackTrace();
            return -1;
        }
    }
}
