package com.mit.mylib.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MShareUtils {

    public MShareUtils() {
    }

    public static void share(Context context, int stringRes) {
        share(context, context.getString(stringRes));
    }

    public static void shareImage(Context context, String path, String title) {
        shareImage(context, Uri.fromFile(new File(path)), title);
    }

    public static void shareImage(Context context, Uri uri, String title) {
        Intent shareIntent = new Intent();
        shareIntent.setAction("android.intent.action.SEND");
        shareIntent.putExtra("android.intent.extra.STREAM", uri);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent, title));
    }

    public static void share(Context context, String extraText) {
        Intent intent = new Intent("android.intent.action.SEND");
        intent.setType("text/plain");
        intent.putExtra("android.intent.extra.SUBJECT", "分享");
        intent.putExtra("android.intent.extra.TEXT", extraText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    public static void shareTOWX(Context context, String content, ArrayList <Uri> imageUris) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        if (!MStringUtils.isEmpty(content)) {
            intent.putExtra("Kdescription", content);
        }

        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", imageUris);
        context.startActivity(intent);
    }

    public static void shareTOWXTimeline(Context context, String content, ArrayList <Uri> imageUris) {
        Intent intent = new Intent();
        ComponentName comp = new ComponentName("com.tencent.mm", "com.tencent.mm.ui.tools.ShareToTimeLineUI");
        intent.setComponent(comp);
        intent.setAction("android.intent.action.SEND_MULTIPLE");
        intent.setType("image/*");
        if (!MStringUtils.isEmpty(content)) {
            intent.putExtra("Kdescription", content);
        }

        intent.putParcelableArrayListExtra("android.intent.extra.STREAM", imageUris);
        context.startActivity(intent);
    }
}
