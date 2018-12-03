package com.mit.mylib.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MPhoneUtil {

    private MPhoneUtil() {
        throw new Error("Do not need instantiate!");
    }

    public static void dialPhoneNumber(Context context, String phoneNumber) {
        Intent intent = new Intent("android.intent.action.DIAL");
        intent.setData(Uri.parse("tel:" + phoneNumber));
        if(intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }

    }

    public static void sendMessage(Context activity, String phoneNumber, String smsContent) {
        if(phoneNumber != null && phoneNumber.length() >= 4) {
            Uri uri = Uri.parse("smsto:" + phoneNumber);
            Intent it = new Intent("android.intent.action.SENDTO", uri);
            it.putExtra("sms_body", smsContent);
            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(it);
        }
    }

    @TargetApi(23)
    public static void callPhones(Context context, String phoneNumber) {
        if(phoneNumber != null && phoneNumber.length() >= 1) {
            Uri uri = Uri.parse("tel:" + phoneNumber);
            Intent intent = new Intent("android.intent.action.CALL", uri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(context.checkSelfPermission("android.permission.CALL_PHONE") == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(intent);
            }
        }
    }
}
