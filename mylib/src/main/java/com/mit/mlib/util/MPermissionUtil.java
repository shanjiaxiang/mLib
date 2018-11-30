package com.mit.mlib.util;

import android.app.Activity;

import com.tbruyelle.rxpermissions.RxPermissions;


/**
 * Created by shanjiaxiang on 2018\11\30 0030.
 */

public class MPermissionUtil {
    public MPermissionUtil() {
    }

    public static RxPermissions getRxPermission(Activity activity){
        return new RxPermissions(activity);
    }

    public static boolean checkPermission(Activity activity, String permission){
        return getRxPermission(activity).isGranted(permission);
    }
}
