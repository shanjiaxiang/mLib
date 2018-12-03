package com.mit.mylib.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MClipboardUtil {
    public MClipboardUtil() {
    }

    //将数据复制到剪切板
    public static void copy(Context context, String label ,String string) {
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText(label, string);
        if (cm != null) {
            cm.setPrimaryClip(clipData);
        }
    }

    //URL型ClipData
    public static void copyURLClipData(Context context, String label ,String url) {
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newRawUri(label, Uri.parse(url));
        if (cm != null) {
            cm.setPrimaryClip(clipData);
        }
    }

    //intent型ClipData
    public static void copyIntentClipData(Context context, String label ,Intent intent) {
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newIntent(label, intent);
        if (cm != null) {
            cm.setPrimaryClip(clipData);
        }
    }

    //从剪切板获取数据
    public static ClipData getClipData(Context context){
        ClipboardManager cm = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cm != null) {
           return cm.getPrimaryClip();
        }
        return null;
    }

}
