package com.mit.mylib.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MUltimateBar {

//    private Activity mActivity;
//
//    public XUltimateBar(Activity activity) {
//        super(activity);
//        this.mActivity = activity;
//    }
//
//    @TargetApi(19)
//    public void setTransparentStatusBar(@ColorInt int color, int alpha) {
//        Window window;
//        if(Build.VERSION.SDK_INT >= 21) {
//            window = this.mActivity.getWindow();
//            View decorView = window.getDecorView();
//            int option = 1280;
//            decorView.setSystemUiVisibility(option);
//            int finalColor = alpha == 0?0: Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
//            window.setStatusBarColor(finalColor);
//        } else if(Build.VERSION.SDK_INT >= 19) {
//            window = this.mActivity.getWindow();
//            window.addFlags(67108864);
//            ViewGroup decorView = (ViewGroup)window.getDecorView();
//            int finalColor = alpha == 0?0:Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
//            decorView.addView(this.createStatusBarView(this.mActivity, finalColor));
//        }
//
//    }
//
//    @TargetApi(19)
//    public void setColorStatusBar(@ColorInt int color) {
//        this.setColorStatusBar(color, 0);
//    }
//
//    @TargetApi(19)
//    public void setColorStatusBar(@ColorInt int color, int alpha) {
//        Window window;
//        int alphaColor;
//        if(Build.VERSION.SDK_INT >= 21) {
//            window = this.mActivity.getWindow();
//            window.addFlags(-2147483648);
//            window.clearFlags(67108864);
//            window.clearFlags(134217728);
//            alphaColor = alpha == 0?color:this.calculateColor(color, alpha);
//            window.setStatusBarColor(alphaColor);
//        } else if(Build.VERSION.SDK_INT >= 19) {
//            window = this.mActivity.getWindow();
//            window.addFlags(67108864);
//            alphaColor = alpha == 0?color:this.calculateColor(color, alpha);
//            ViewGroup decorView = (ViewGroup)window.getDecorView();
//            decorView.addView(this.createStatusBarView(this.mActivity, alphaColor));
//            this.setRootView(this.mActivity, true);
//        }
//
//    }
//
//    private View createStatusBarView(Context context, @ColorInt int color) {
//        View mStatusBarTintView = new View(context);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, this.getStatusBarHeight(context));
//        params.gravity = 48;
//        mStatusBarTintView.setLayoutParams(params);
//        mStatusBarTintView.setBackgroundColor(color);
//        return mStatusBarTintView;
//    }
//
//    private int getStatusBarHeight(Context context) {
//        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        return context.getResources().getDimensionPixelSize(resourceId);
//    }
//
//    @ColorInt
//    private int calculateColor(@ColorInt int color, int alpha) {
//        float a = 1.0F - (float)alpha / 255.0F;
//        int red = color >> 16 & 255;
//        int green = color >> 8 & 255;
//        int blue = color & 255;
//        red = (int)((double)((float)red * a) + 0.5D);
//        green = (int)((double)((float)green * a) + 0.5D);
//        blue = (int)((double)((float)blue * a) + 0.5D);
//        return -16777216 | red << 16 | green << 8 | blue;
//    }
//
//    private void setRootView(Activity activity, boolean fit) {
//        ViewGroup parent = (ViewGroup)activity.findViewById(16908290);
//        int i = 0;
//
//        for(int count = parent.getChildCount(); i < count; ++i) {
//            View childView = parent.getChildAt(i);
//            if(childView instanceof ViewGroup) {
//                childView.setFitsSystemWindows(fit);
//                ((ViewGroup)childView).setClipToPadding(fit);
//            }
//        }
//
//    }
//
//    private boolean navigationBarExist(Activity activity) {
//        WindowManager windowManager = activity.getWindowManager();
//        Display d = windowManager.getDefaultDisplay();
//        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
//        if(Build.VERSION.SDK_INT >= 17) {
//            d.getRealMetrics(realDisplayMetrics);
//        }
//
//        int realHeight = realDisplayMetrics.heightPixels;
//        int realWidth = realDisplayMetrics.widthPixels;
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        d.getMetrics(displayMetrics);
//        int displayHeight = displayMetrics.heightPixels;
//        int displayWidth = displayMetrics.widthPixels;
//        return realWidth - displayWidth > 0 || realHeight - displayHeight > 0;
//    }
//
//    private View createNavBarView(Context context, @ColorInt int color) {
//        View mNavBarTintView = new View(context);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(-1, this.getNavigationHeight(context));
//        params.gravity = 80;
//        mNavBarTintView.setLayoutParams(params);
//        mNavBarTintView.setBackgroundColor(color);
//        return mNavBarTintView;
//    }
}
