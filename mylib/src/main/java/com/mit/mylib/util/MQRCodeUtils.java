package com.mit.mylib.util;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MQRCodeUtils {

    public MQRCodeUtils() {
    }

    public static Bitmap encodeAsBitmap(String str) throws WriterException {
        return encodeAsBitmap(str, 300, 300);
    }

    public static Bitmap encodeAsBitmap(String str, int intWidth, int intHeight) throws WriterException {
        Hashtable<EncodeHintType, String> hints = new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, "0");
        BitMatrix matrix = (new MultiFormatWriter()).encode(str, BarcodeFormat.QR_CODE, intWidth, intHeight, hints);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                if(matrix.get(x, y)) {
                    pixels[y * width + x] = Color.BLACK;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }
}
