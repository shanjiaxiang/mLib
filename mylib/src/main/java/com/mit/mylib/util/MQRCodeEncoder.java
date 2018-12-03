package com.mit.mylib.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MQRCodeEncoder {

    public static final Map<EncodeHintType, Object> HINTS = new EnumMap(EncodeHintType.class);

    private MQRCodeEncoder() {
    }

    public static Bitmap syncEncodeQRCode(String content, int size) {
        return syncEncodeQRCode(content, size, -16777216, -1, (Bitmap)null);
    }

    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor) {
        return syncEncodeQRCode(content, size, foregroundColor, -1, (Bitmap)null);
    }

    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, Bitmap logo) {
        return syncEncodeQRCode(content, size, foregroundColor, -1, logo);
    }

    public static Bitmap syncEncodeQRCode(String content, int size, int foregroundColor, int backgroundColor, Bitmap logo) {
        try {
            BitMatrix matrix = (new MultiFormatWriter()).encode(content, BarcodeFormat.QR_CODE, size, size, HINTS);
            int[] pixels = new int[size * size];

            for(int y = 0; y < size; ++y) {
                for(int x = 0; x < size; ++x) {
                    if(matrix.get(x, y)) {
                        pixels[y * size + x] = foregroundColor;
                    } else {
                        pixels[y * size + x] = backgroundColor;
                    }
                }
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, size, 0, 0, size, size);
            return addLogoToQRCode(bitmap, logo);
        } catch (Exception var9) {
            return null;
        }
    }

    private static Bitmap addLogoToQRCode(Bitmap src, Bitmap logo) {
        if(src != null && logo != null) {
            int srcWidth = src.getWidth();
            int srcHeight = src.getHeight();
            int logoWidth = logo.getWidth();
            int logoHeight = logo.getHeight();
            float scaleFactor = (float)srcWidth * 1.0F / 3.0F / (float)logoWidth;
            Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);

            try {
                Canvas canvas = new Canvas(bitmap);
                canvas.drawBitmap(src, 0.0F, 0.0F, (Paint)null);
                canvas.scale(scaleFactor, scaleFactor, (float)(srcWidth / 2), (float)(srcHeight / 2));
                canvas.drawBitmap(logo, (float)((srcWidth - logoWidth) / 2), (float)((srcHeight - logoHeight) / 2), (Paint)null);
                canvas.save(Canvas.ALL_SAVE_FLAG);
                canvas.restore();
            } catch (Exception var9) {
                bitmap = null;
            }

            return bitmap;
        } else {
            return src;
        }
    }

    static {
        HINTS.put(EncodeHintType.CHARACTER_SET, "utf-8");
        HINTS.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        HINTS.put(EncodeHintType.MARGIN, Integer.valueOf(0));
    }
}
