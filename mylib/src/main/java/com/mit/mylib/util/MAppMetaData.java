package com.mit.mylib.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 * 未验证，慎用
 */

public class MAppMetaData {
    public MAppMetaData() {
    }

    public static String getAppMetaData(Context ctx, String key) {
        if(ctx != null && !TextUtils.isEmpty(key)) {
            String resultData = null;

            try {
                PackageManager packageManager = ctx.getPackageManager();
                if(packageManager != null) {
                    ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                    if(applicationInfo != null && applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }
            } catch (PackageManager.NameNotFoundException var5) {
                var5.printStackTrace();
            }

            return resultData;
        } else {
            return null;
        }
    }
}
