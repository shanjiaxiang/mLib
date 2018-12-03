package com.mit.mylib.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;

import com.mit.mylib.base.MLog;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MPollingUtils {

    private static final boolean DEBUG = true;
    private static final String TAG = "PollingUtils";

    private MPollingUtils() {
        throw new Error("Do not need instantiate!");
    }

    public static boolean isPollingServiceExist(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent != null) {
            MLog.v("PollingUtils", pendingIntent.toString());
        }

        MLog.v("PollingUtils", pendingIntent != null?"Exist":"Not exist");
        return pendingIntent != null;
    }

    public static void startPollingService(Context context, int interval, Class<?> cls) {
        startPollingService(context, interval, cls, (String)null);
    }

    public static void startPollingService(Context context, int interval, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        if(!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        long triggerAtTime = SystemClock.elapsedRealtime();
        manager.setRepeating(AlarmManager.ELAPSED_REALTIME, triggerAtTime, (long)(interval * 1000), pendingIntent);
    }

    public static void stopPollingService(Context context, Class<?> cls) {
        stopPollingService(context, cls, (String)null);
    }

    public static void stopPollingService(Context context, Class<?> cls, String action) {
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, cls);
        if(!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }

        PendingIntent pendingIntent = PendingIntent.getService(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager.cancel(pendingIntent);
    }
}
