package com.mit.mylib.util;

import android.view.View;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */
public abstract class MOnClickListener implements View.OnClickListener {
    private static long lastClickTime;

    public MOnClickListener() {
    }

    public abstract void onXClick(View var1);

    public void onClick(View v) {
        if(!isFastDoubleClick()) {
            this.onXClick(v);
        }
    }

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if(Math.abs(time - lastClickTime) < 500L) {
            return true;
        } else {
            lastClickTime = time;
            return false;
        }
    }
}
