package com.mit.mylib.util;

import android.content.Context;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MDensityUtil {

    public MDensityUtil() {
    }

    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dpValue * scale + 0.5F);
    }

    public static int px2dip(Context context, float pxValue) {
        float scale = context.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5F);
    }
}
