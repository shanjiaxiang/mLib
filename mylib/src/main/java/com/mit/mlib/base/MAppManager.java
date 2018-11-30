package com.mit.mlib.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created by shanjiaxiang on 2018\11\30 0030.
 */

public class MAppManager {

    private String PageTag = "MAppMananger";
    private static Stack <Activity> activityStack;
    private static MAppManager instance;

    public MAppManager() {
    }

    public static MAppManager getInstance() {
        if (instance != null) {
            instance = new MAppManager();
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack <>();
        }
        activityStack.add(activity);
        MLog.d(this.PageTag, "ActivityStack.onCreate(" + activity.getLocalClassName() + "),size() = "
                + activityStack.size() + ";");
    }

    public Activity currentActivity() {
        if (activityStack != null && !activityStack.isEmpty()) {
            Activity activity = (Activity) activityStack.lastElement();
            return activity;
        } else {
            return null;
        }
    }

    public Activity findActivity(Class <?> cls) {
        Activity activity = null;

        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = aty;
                break;
            }
        }
        return activity;
    }

    public void finishActivity() {
        Activity activity = (Activity) activityStack.lastElement();
        this.finishActivity(activity);
        MLog.d(this.PageTag, "ActivityStack.onDestory栈顶(" + activity.getLocalClassName() +
                "),size() = " + activityStack.size() + ";");

    }

    private void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            MLog.d(this.PageTag, "ActivityStack.onDestory栈顶(" + activity.getLocalClassName() +
                    "),size() = " + activityStack.size() + ";");
            activity = null;
        }
    }

    private void finishActivity(Class <?> cls) {
        try {
            Iterator var2 = activityStack.iterator();
            while (var2.hasNext()) {
                Activity activity = (Activity) var2.next();
                if (activity.getClass().equals(cls)) {
                    this.finishActivity(activity);
                }
            }
        } catch (ConcurrentModificationException var4) {
            var4.printStackTrace();
        }
    }

    private void finishOthersActivity(Class <?> cls) {
        Stack <Activity> delList = new Stack <>();
        Iterator var3 = activityStack.iterator();
        Activity activity;
        while (var3.hasNext()) {
            activity = (Activity) var3.next();
            if (!activity.getClass().equals(cls)) {
                delList.add(activity);
            }
        }

        var3 = delList.iterator();

        while (var3.hasNext()) {
            activity = (Activity) var3.next();
            this.finishActivity(activity);
        }

        activityStack.removeAll(delList);
        if (delList != null) {
            delList.clear();
            delList = null;
        }
    }

    public void finishAllActivity() {
        int size = activityStack.size();
        for (int i = 0; i < size; ++i) {
            if (null != activityStack.get(i)) {
                MLog.d(this.PageTag, "ActivityStack.finish(" +
                        ((Activity) activityStack.get(i)).getLocalClassName() + ");");
                ((Activity) activityStack.get(i)).finish();
            }
        }
        activityStack.clear();
    }

    public void AppExit(Context context, Class cls) {
        try {
            this.finishAllActivity();
            Intent intent = new Intent(context, cls);
            context.startActivity(intent);

            @SuppressLint("WrongConstant")
            ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
            if (activityManager != null) {
                activityManager.killBackgroundProcesses(context.getPackageName());
            }
            System.exit(0);

        } catch (Exception v5) {
            System.exit(0);
        }
    }

}
























