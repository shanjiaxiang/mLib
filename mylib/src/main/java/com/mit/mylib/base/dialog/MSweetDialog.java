package com.mit.mylib.base.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MSweetDialog {

    private static SweetAlertDialog mDialog;

    public MSweetDialog() {
    }

    public static void showProgressDialog(Context context) {
        showProgressDialog(context, "Loading", Color.parseColor("#A5DC86"));
    }

    public static void showDialog(Context context, String content) {
        showDialog(context, content, (SweetAlertDialog.OnSweetClickListener)null);
    }

    public static void showDialog(Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener) {
        showNormalDialog(context, "提示", content, "确定", true, confirmListener);
    }

    public static void showDialog(Context context, String content, boolean cancel, SweetAlertDialog.OnSweetClickListener confirmListener) {
        showNormalDialog(context, "提示", content, "确定", cancel, confirmListener);
    }

    public static void showDialog(Context context, String content, SweetAlertDialog.OnSweetClickListener confirmListener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        showNormalDialog(context, "提示", content, "确定", "取消", true, confirmListener, cancelListener);
    }

    public static void showRepeatLoginDialog(Context context, SweetAlertDialog.OnSweetClickListener confirmListener) {
        showSingleDialog(context, "警告", "身份验证信息过期，请重新登录", "登录", "取消", confirmListener, (SweetAlertDialog.OnSweetClickListener)null);
    }

    public static void showNetErrorDialog(Context context, SweetAlertDialog.OnSweetClickListener confirmListener) {
        showSingleDialog(context, "提示", "网络连接异常，请检查您的网络！", "确定", (String)null, confirmListener, (SweetAlertDialog.OnSweetClickListener)null);
    }

    public static void showSingleDialog(Context context, String title, String content, String confirmText, String cancelText, SweetAlertDialog.OnSweetClickListener confirmListener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        try {
            if (!((Activity)context).isFinishing()) {
                if (mDialog != null && mDialog.isShowing() && mDialog.getContentText().equals(content)) {
                    return;
                }

                mDialog = new SweetAlertDialog(context, 3);
                mDialog.setTitleText(title);
                mDialog.setContentText(content);
                mDialog.setConfirmText(confirmText);
                mDialog.setCancelText(cancelText);
                mDialog.setConfirmClickListener(confirmListener);
                mDialog.setCancelClickListener(cancelListener);
                mDialog.show();
            }
        } catch (Exception var8) {
            ;
        }

    }

    public static void showProgressDialog(Context context, String title, int color) {
        try {
            if (!((Activity)context).isFinishing()) {
                SweetAlertDialog mDialog = new SweetAlertDialog(context, 5);
                mDialog.getProgressHelper().setBarColor(color);
                mDialog.setTitleText(title);
                mDialog.setCancelable(true);
                mDialog.setCanceledOnTouchOutside(true);
                mDialog.show();
            }
        } catch (Exception var4) {
            ;
        }

    }

    public static void showNormalDialog(Context context, String title, String content, String confirmText, boolean cancel, SweetAlertDialog.OnSweetClickListener confirmListener) {
        showSweetDialog(context, 0, title, content, confirmText, (String)null, (Drawable)null, cancel, confirmListener, (SweetAlertDialog.OnSweetClickListener)null);
    }

    public static void showNormalDialog(Context context, String title, String content, String confirmText, String cancelText, boolean cancel, SweetAlertDialog.OnSweetClickListener confirmListener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        showSweetDialog(context, 0, title, content, confirmText, cancelText, (Drawable)null, cancel, confirmListener, cancelListener);
    }

    public static void showWarningDialog(Context context, String title, String content, String confirmText, String cancelText, boolean cancel, SweetAlertDialog.OnSweetClickListener confirmListener, SweetAlertDialog.OnSweetClickListener cancelListener) {
        if (!((Activity)context).isFinishing()) {
            SweetAlertDialog mDialog = new SweetAlertDialog(context, 3);
            mDialog.setTitleText(title);
            mDialog.setContentText(content);
            mDialog.setConfirmText(confirmText);
            mDialog.setCancelText(cancelText);
            mDialog.setCanceledOnTouchOutside(cancel);
            mDialog.setCancelable(cancel);
            mDialog.setConfirmClickListener(confirmListener);
            mDialog.setCancelClickListener(cancelListener);
            mDialog.show();
        }
    }

    public static void showSweetDialog(Context context, int alertType, String title, String content,
                                       String confirmText, String cancelText, Drawable drawable, boolean cancel,
                                       SweetAlertDialog.OnSweetClickListener confirmListener,
                                       SweetAlertDialog.OnSweetClickListener cancelListener) {
        try {
            if (!((Activity) context).isFinishing()) {
                if (mDialog != null && mDialog.isShowing() && mDialog.getContentText().equals(content)) {
                    return;
                }

                mDialog = new SweetAlertDialog(context, alertType);
                mDialog.setTitleText(title);
                mDialog.setContentText(content);
                mDialog.setConfirmText(confirmText);
                mDialog.setCancelText(cancelText);
                mDialog.setCustomImage(drawable);
                mDialog.setCanceledOnTouchOutside(cancel);
                mDialog.setCancelable(cancel);
                mDialog.setConfirmClickListener(confirmListener);
                mDialog.setCancelClickListener(cancelListener);
                mDialog.show();
            }
        }catch (Exception var11){
        }
    }

    public static void dismissDialog(){
        if (mDialog != null && mDialog.isShowing()){
            mDialog.dismiss();
            mDialog = null;
        }
    }
}
