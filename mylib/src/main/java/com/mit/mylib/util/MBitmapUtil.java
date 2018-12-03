package com.mit.mylib.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.DimenRes;
import android.support.annotation.Dimension;
import android.util.Log;
import android.view.View;

import com.mit.mylib.base.MLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/**
 * Created by shanjiaxiang on 2018\12\3 0003.
 */

public class MBitmapUtil {
    private static final String TAG = MBitmapUtil.class.getSimpleName();

    private MBitmapUtil() {
        throw new Error("Do not need instantiate!");
    }

    public static Bitmap vagueImage(Context context, Bitmap souBitmap) {
        if(souBitmap == null) {
            return null;
        } else if(Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = souBitmap.copy(souBitmap.getConfig(), true);
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, souBitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius(25.0F);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } else {
            return souBitmap;
        }
    }

    public static BitmapFactory.Options calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if((height > 400 || width > 450) && (height > reqHeight || width > reqWidth)) {
            int heightRatio = Math.round((float)height / (float)reqHeight);
            int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = heightRatio < widthRatio?heightRatio:widthRatio;
        }

        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;
        return options;
    }

    public static Bitmap getBitmapFromLocal(String path, int sampleSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampleSize;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, (Rect)null, opt);
    }

    public static Bitmap getViewBitmap(View view) {
        return getViewBitmap(view, view.getMeasuredWidth(), view.getMeasuredHeight());
    }

    public static Bitmap getViewBitmap(View view, int width, int height) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, width, height);
        view.buildDrawingCache();
        Bitmap bp = view.getDrawingCache();
        if(bp == null) {
            MLog.e("bitmap is null");
        }

        return bp;
    }

    public static String saveImageToGallery(Context context, Bitmap bmp, File f) {
        String fileName = (new Date()).getTime() + ".jpg";
        File file = new File(f, fileName);

        try {
            FileOutputStream fos = new FileOutputStream(file);
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, (String)null);
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", uri));
            return isSuccess?file.getAbsolutePath():"";
        } catch (IOException var8) {
            var8.printStackTrace();
            return "";
        }
    }

    @TargetApi(4)
    public static Bitmap getBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        InputStream is = res.openRawResource(resId);
        return getBitmapFromStream(is, (Rect)null, reqWidth, reqHeight);
    }

    public static Bitmap getBitmapFromFile(String pathName, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options = calculateInSampleSize(options, reqWidth, reqHeight);
        return BitmapFactory.decodeFile(pathName, options);
    }

    public static Bitmap getBitmapFromByteArray(byte[] data, int offset, int length, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, offset, length, options);
        options = calculateInSampleSize(options, reqWidth, reqHeight);
        return BitmapFactory.decodeByteArray(data, offset, length, options);
    }

    public static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static byte[] getBytesFromStream(InputStream inputStream) {
        ByteArrayOutputStream os = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];

        int len;
        try {
            while((len = inputStream.read(buffer)) >= 0) {
                os.write(buffer, 0, len);
            }
        } catch (IOException var5) {
            var5.printStackTrace();
        }

        return os.toByteArray();
    }

    public static Bitmap getBitmapFromBytes(byte[] b) {
        return b.length != 0?BitmapFactory.decodeByteArray(b, 0, b.length):null;
    }

    public static Bitmap getBitmapFromStream(InputStream is, int reqWidth, int reqHeight) {
        byte[] data = MFileUtils.input2byte(is);
        return getBitmapFromByteArray(data, 0, data.length, reqWidth, reqHeight);
    }

    public static Bitmap getBitmapFromStream(InputStream is, Rect outPadding, int reqWidth, int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, outPadding, options);
        options = calculateInSampleSize(options, reqWidth, reqHeight);
        return BitmapFactory.decodeStream(is, outPadding, options);
    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.layout(view.getLeft(), view.getTop(), view.getRight(), view.getBottom());
        view.draw(canvas);
        return bitmap;
    }

    public static Bitmap getBitmapFromView2(View view) {
        view.clearFocus();
        view.setPressed(false);
        boolean willNotCache = view.willNotCacheDrawing();
        view.setWillNotCacheDrawing(false);
        int color = view.getDrawingCacheBackgroundColor();
        view.setDrawingCacheBackgroundColor(0);
        if(color != 0) {
            view.destroyDrawingCache();
        }

        view.buildDrawingCache();
        Bitmap cacheBitmap = view.getDrawingCache();
        if(cacheBitmap == null) {
            Log.e(TAG, "failed getViewBitmap(" + view + ")", new RuntimeException());
            return null;
        } else {
            Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
            view.destroyDrawingCache();
            view.setWillNotCacheDrawing(willNotCache);
            view.setDrawingCacheBackgroundColor(color);
            return bitmap;
        }
    }

    public static Bitmap getBitmapFromDrawable(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE? Bitmap.Config.ARGB_8888: Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap combineImages(Bitmap bgd, Bitmap fg) {
        int width = bgd.getWidth() > fg.getWidth()?bgd.getWidth():fg.getWidth();
        int height = bgd.getHeight() > fg.getHeight()?bgd.getHeight():fg.getHeight();
        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0.0F, 0.0F, (Paint)null);
        canvas.drawBitmap(fg, 0.0F, 0.0F, paint);
        return bmp;
    }

    public static Bitmap combineImagesToSameSize(Bitmap bgd, Bitmap fg) {
        int width = bgd.getWidth() < fg.getWidth()?bgd.getWidth():fg.getWidth();
        int height = bgd.getHeight() < fg.getHeight()?bgd.getHeight():fg.getHeight();
        if(fg.getWidth() != width && fg.getHeight() != height) {
            fg = zoom(fg, width, height);
        }

        if(bgd.getWidth() != width && bgd.getHeight() != height) {
            bgd = zoom(bgd, width, height);
        }

        Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        Canvas canvas = new Canvas(bmp);
        canvas.drawBitmap(bgd, 0.0F, 0.0F, (Paint)null);
        canvas.drawBitmap(fg, 0.0F, 0.0F, paint);
        return bmp;
    }

    public static Bitmap zoom(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidht = (float)w / (float)width;
        float scaleHeight = (float)h / (float)height;
        matrix.postScale(scaleWidht, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newbmp;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -12434878;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap createReflectionBitmap(Bitmap bitmap) {
        boolean reflectionGap = true;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(1.0F, -1.0F);
        Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, height / 2, width, height / 2, matrix, false);
        Bitmap bitmapWithReflection = Bitmap.createBitmap(width, height + height / 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapWithReflection);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, (Paint)null);
        Paint deafalutPaint = new Paint();
        canvas.drawRect(0.0F, (float)height, (float)width, (float)(height + 4), deafalutPaint);
        canvas.drawBitmap(reflectionImage, 0.0F, (float)(height + 4), (Paint)null);
        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0.0F, (float)bitmap.getHeight(), 0.0F, (float)(bitmapWithReflection.getHeight() + 4), 1895825407, 16777215, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        canvas.drawRect(0.0F, (float)height, (float)width, (float)(bitmapWithReflection.getHeight() + 4), paint);
        return bitmapWithReflection;
    }

    public static Bitmap compressImage(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        for(int options = 100; baos.toByteArray().length / 1024 > 100; options -= 10) {
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }

        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, (Rect)null, (BitmapFactory.Options)null);
        return bitmap;
    }

    public static Bitmap convertGreyImg(Bitmap img) {
        int width = img.getWidth();
        int height = img.getHeight();
        int[] pixels = new int[width * height];
        img.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = -16777216;

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                int grey = pixels[width * i + j];
                int red = (grey & 16711680) >> 16;
                int green = (grey & '\uff00') >> 8;
                int blue = grey & 255;
                grey = (int)((double)((float)red) * 0.3D + (double)((float)green) * 0.59D + (double)((float)blue) * 0.11D);
                grey |= alpha | grey << 16 | grey << 8;
                pixels[width * i + j] = grey;
            }
        }

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        result.setPixels(pixels, 0, width, 0, 0, width, height);
        return result;
    }

    public static Bitmap getRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left;
        float top;
        float right;
        float bottom;
        float dst_left;
        float dst_top;
        float dst_right;
        float dst_bottom;
        if(width <= height) {
            roundPx = (float)(width / 2);
            top = 0.0F;
            bottom = (float)width;
            left = 0.0F;
            right = (float)width;
            height = width;
            dst_left = 0.0F;
            dst_top = 0.0F;
            dst_right = (float)width;
            dst_bottom = (float)width;
        } else {
            roundPx = (float)(height / 2);
            float clip = (float)((width - height) / 2);
            left = clip;
            right = (float)width - clip;
            top = 0.0F;
            bottom = (float)height;
            width = height;
            dst_left = 0.0F;
            dst_top = 0.0F;
            dst_right = (float)height;
            dst_bottom = (float)height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = -12434878;
        Paint paint = new Paint();
        Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }
//未知17104896
//    public static Bitmap createThumbnailBitmap(Bitmap bitmap, Context context) {
//        Resources resources = context.getResources();
//        int sIconHeight;
//        int sIconWidth = sIconHeight = (int)resources.getDimension(17104896);
//        Paint sPaint = new Paint();
//        Rect sBounds = new Rect();
//        Rect sOldBounds = new Rect();
//        Canvas sCanvas = new Canvas();
//        int width = sIconWidth;
//        int height = sIconHeight;
//        sCanvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
//        int bitmapWidth = bitmap.getWidth();
//        int bitmapHeight = bitmap.getHeight();
//        if(sIconWidth > 0 && sIconHeight > 0) {
//            if(sIconWidth < bitmapWidth || sIconHeight < bitmapHeight) {
//                float ratio = (float)bitmapWidth / (float)bitmapHeight;
//                if(bitmapWidth > bitmapHeight) {
//                    height = (int)((float)sIconWidth / ratio);
//                } else if(bitmapHeight > bitmapWidth) {
//                    width = (int)((float)sIconHeight * ratio);
//                }
//
//                Bitmap.Config c = width == sIconWidth && height == sIconHeight?bitmap.getConfig(): Bitmap.Config.ARGB_8888;
//                Bitmap thumb = Bitmap.createBitmap(sIconWidth, sIconHeight, c);
//                sCanvas.setBitmap(thumb);
//                sPaint.setDither(false);
//                sPaint.setFilterBitmap(true);
//                sBounds.set((sIconWidth - width) / 2, (sIconHeight - height) / 2, width, height);
//                sOldBounds.set(0, 0, bitmapWidth, bitmapHeight);
//                sCanvas.drawBitmap(bitmap, sOldBounds, sBounds, sPaint);
//                return thumb;
//            }
//
//            if(bitmapWidth < sIconWidth || bitmapHeight < sIconHeight) {
//                Bitmap.Config c = Bitmap.Config.ARGB_8888;
//                Bitmap thumb = Bitmap.createBitmap(sIconWidth, sIconHeight, c);
//                sCanvas.setBitmap(thumb);
//                sPaint.setDither(false);
//                sPaint.setFilterBitmap(true);
//                sCanvas.drawBitmap(bitmap, (float)((sIconWidth - bitmapWidth) / 2), (float)((sIconHeight - bitmapHeight) / 2), sPaint);
//                return thumb;
//            }
//        }
//
//        return bitmap;
//    }

    public static Bitmap createWatermarkBitmap(Bitmap src, Bitmap watermark) {
        if(src == null) {
            return null;
        } else {
            int w = src.getWidth();
            int h = src.getHeight();
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas cv = new Canvas(newb);
            cv.drawBitmap(src, 0.0F, 0.0F, (Paint)null);
            cv.drawBitmap(watermark, (float)(w - ww + 5), (float)(h - wh + 5), (Paint)null);
            cv.save(Canvas.ALL_SAVE_FLAG);
            cv.restore();
            return newb;
        }
    }

    public static Bitmap codec(Bitmap src, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        src.compress(format, quality, os);
        byte[] array = os.toByteArray();
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public static void compress(Bitmap bitmap, double maxSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, baos);
        byte[] b = baos.toByteArray();
        double mid = (double)(b.length / 1024);
        double i = mid / maxSize;
        if(i > 1.0D) {
            scale(bitmap, (double)bitmap.getWidth() / Math.sqrt(i), (double)bitmap.getHeight() / Math.sqrt(i));
        }

    }

    public static Bitmap scale(Bitmap src, double newWidth, double newHeight) {
        float width = (float)src.getWidth();
        float height = (float)src.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = (float)newWidth / width;
        float scaleHeight = (float)newHeight / height;
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(src, 0, 0, (int)width, (int)height, matrix, true);
    }

    public static Bitmap scale(Bitmap src, Matrix scaleMatrix) {
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), scaleMatrix, true);
    }

    public static Bitmap scale(Bitmap src, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    public static Bitmap scale(Bitmap src, float scale) {
        return scale(src, scale, scale);
    }

    public static Bitmap rotate(Bitmap bitmap, int angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate((float)angle);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static Bitmap reverseByHorizontal(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0F, 1.0F);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static Bitmap reverseByVertical(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0F, -1.0F);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
    }

    public static Bitmap adjustTone(Bitmap src, int delta) {
        if(delta < 24 && delta > 0) {
            int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            int newR = 0;
            int newG = 0;
            int newB = 0;
            int[] pixels = new int[width * height];
            src.getPixels(pixels, 0, width, 0, 0, width, height);
            int i = 1;

            for(int length = height - 1; i < length; ++i) {
                int k = 1;

                for(int len = width - 1; k < len; ++k) {
                    int idx = 0;

                    for(int m = -1; m <= 1; ++m) {
                        for(int n = -1; n <= 1; ++n) {
                            int pixColor = pixels[(i + m) * width + k + n];
                            int pixR = Color.red(pixColor);
                            int pixG = Color.green(pixColor);
                            int pixB = Color.blue(pixColor);
                            newR += pixR * gauss[idx];
                            newG += pixG * gauss[idx];
                            newB += pixB * gauss[idx];
                            ++idx;
                        }
                    }

                    newR /= delta;
                    newG /= delta;
                    newB /= delta;
                    newR = Math.min(255, Math.max(0, newR));
                    newG = Math.min(255, Math.max(0, newG));
                    newB = Math.min(255, Math.max(0, newB));
                    pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                    newR = 0;
                    newG = 0;
                    newB = 0;
                }
            }

            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            return bitmap;
        } else {
            return null;
        }
    }

    public static Bitmap convertToBlackWhite(Bitmap bmp) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        int[] pixels = new int[width * height];
        bmp.getPixels(pixels, 0, width, 0, 0, width, height);
        int alpha = -16777216;

        for(int i = 0; i < height; ++i) {
            for(int j = 0; j < width; ++j) {
                int grey = pixels[width * i + j];
                int red = (grey & 16711680) >> 16;
                int green = (grey & '\uff00') >> 8;
                int blue = grey & 255;
                grey = (int)((double)red * 0.3D + (double)green * 0.59D + (double)blue * 0.11D);
                grey |= alpha | grey << 16 | grey << 8;
                pixels[width * i + j] = grey;
            }
        }

        Bitmap newBmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        newBmp.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBmp;
    }

    @TargetApi(5)
    public static int getImageDegree(String path) {
        short degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    degree = 180;
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return degree;
    }

    @SuppressLint({"InlinedApi"})
    public static Bitmap blur(Context context, Bitmap sentBitmap, int radius) {
        if(radius < 0) {
            radius = 0;
            MLog.w(TAG, "radius must be 0 < r < 25 , forcing radius=0");
        } else if(radius > 25) {
            radius = 25;
            MLog.w(TAG, "radius must be 0 < r < 25 , forcing radius=25");
        }

        Bitmap bitmap;
        if(Build.VERSION.SDK_INT > 16) {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            RenderScript rs = RenderScript.create(context);
            Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE, 1);
            Allocation output = Allocation.createTyped(rs, input.getType());
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setRadius((float)radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        } else {
            bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
            if(radius < 1) {
                return null;
            } else {
                int w = bitmap.getWidth();
                int h = bitmap.getHeight();
                int[] pix = new int[w * h];
                Log.e("pix", w + " " + h + " " + pix.length);
                bitmap.getPixels(pix, 0, w, 0, 0, w, h);
                int wm = w - 1;
                int hm = h - 1;
                int wh = w * h;
                int div = radius + radius + 1;
                int[] r = new int[wh];
                int[] g = new int[wh];
                int[] b = new int[wh];
                int[] vmin = new int[Math.max(w, h)];
                int divsum = div + 1 >> 1;
                divsum *= divsum;
                int[] dv = new int[256 * divsum];

                int i;
                for(i = 0; i < 256 * divsum; ++i) {
                    dv[i] = i / divsum;
                }

                int yi = 0;
                int yw = 0;
                int[][] stack = new int[div][3];
                int r1 = radius + 1;

                int rsum;
                int gsum;
                int bsum;
                int x;
                int y;
                int p;
                int stackpointer;
                int stackstart;
                int[] sir;
                int rbs;
                int routsum;
                int goutsum;
                int boutsum;
                int rinsum;
                int ginsum;
                int binsum;
                for(y = 0; y < h; ++y) {
                    bsum = 0;
                    gsum = 0;
                    rsum = 0;
                    boutsum = 0;
                    goutsum = 0;
                    routsum = 0;
                    binsum = 0;
                    ginsum = 0;
                    rinsum = 0;

                    for(i = -radius; i <= radius; ++i) {
                        p = pix[yi + Math.min(wm, Math.max(i, 0))];
                        sir = stack[i + radius];
                        sir[0] = (p & 16711680) >> 16;
                        sir[1] = (p & '\uff00') >> 8;
                        sir[2] = p & 255;
                        rbs = r1 - Math.abs(i);
                        rsum += sir[0] * rbs;
                        gsum += sir[1] * rbs;
                        bsum += sir[2] * rbs;
                        if(i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }
                    }

                    stackpointer = radius;

                    for(x = 0; x < w; ++x) {
                        r[yi] = dv[rsum];
                        g[yi] = dv[gsum];
                        b[yi] = dv[bsum];
                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;
                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];
                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];
                        if(y == 0) {
                            vmin[x] = Math.min(x + radius + 1, wm);
                        }

                        p = pix[yw + vmin[x]];
                        sir[0] = (p & 16711680) >> 16;
                        sir[1] = (p & '\uff00') >> 8;
                        sir[2] = p & 255;
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;
                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[stackpointer % div];
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];
                        ++yi;
                    }

                    yw += w;
                }

                for(x = 0; x < w; ++x) {
                    bsum = 0;
                    gsum = 0;
                    rsum = 0;
                    boutsum = 0;
                    goutsum = 0;
                    routsum = 0;
                    binsum = 0;
                    ginsum = 0;
                    rinsum = 0;
                    int yp = -radius * w;

                    for(i = -radius; i <= radius; ++i) {
                        yi = Math.max(0, yp) + x;
                        sir = stack[i + radius];
                        sir[0] = r[yi];
                        sir[1] = g[yi];
                        sir[2] = b[yi];
                        rbs = r1 - Math.abs(i);
                        rsum += r[yi] * rbs;
                        gsum += g[yi] * rbs;
                        bsum += b[yi] * rbs;
                        if(i > 0) {
                            rinsum += sir[0];
                            ginsum += sir[1];
                            binsum += sir[2];
                        } else {
                            routsum += sir[0];
                            goutsum += sir[1];
                            boutsum += sir[2];
                        }

                        if(i < hm) {
                            yp += w;
                        }
                    }

                    yi = x;
                    stackpointer = radius;

                    for(y = 0; y < h; ++y) {
                        pix[yi] = -16777216 & pix[yi] | dv[rsum] << 16 | dv[gsum] << 8 | dv[bsum];
                        rsum -= routsum;
                        gsum -= goutsum;
                        bsum -= boutsum;
                        stackstart = stackpointer - radius + div;
                        sir = stack[stackstart % div];
                        routsum -= sir[0];
                        goutsum -= sir[1];
                        boutsum -= sir[2];
                        if(x == 0) {
                            vmin[y] = Math.min(y + r1, hm) * w;
                        }

                        p = x + vmin[y];
                        sir[0] = r[p];
                        sir[1] = g[p];
                        sir[2] = b[p];
                        rinsum += sir[0];
                        ginsum += sir[1];
                        binsum += sir[2];
                        rsum += rinsum;
                        gsum += ginsum;
                        bsum += binsum;
                        stackpointer = (stackpointer + 1) % div;
                        sir = stack[stackpointer];
                        routsum += sir[0];
                        goutsum += sir[1];
                        boutsum += sir[2];
                        rinsum -= sir[0];
                        ginsum -= sir[1];
                        binsum -= sir[2];
                        yi += w;
                    }
                }

                Log.e("pix", w + " " + h + " " + pix.length);
                bitmap.setPixels(pix, 0, w, 0, 0, w, h);
                return bitmap;
            }
        }
    }

    public static Bitmap saturation(Bitmap bitmap, int saturationValue) {
        float newSaturationValue = (float)saturationValue * 1.0F / 127.0F;
        ColorMatrix saturationColorMatrix = new ColorMatrix();
        saturationColorMatrix.setSaturation(newSaturationValue);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(saturationColorMatrix));
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return newBitmap;
    }

    public static Bitmap lum(Bitmap bitmap, int lumValue) {
        float newlumValue = (float)lumValue * 1.0F / 127.0F;
        ColorMatrix lumColorMatrix = new ColorMatrix();
        lumColorMatrix.setScale(newlumValue, newlumValue, newlumValue, 1.0F);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(lumColorMatrix));
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return newBitmap;
    }

    public static Bitmap hue(Bitmap bitmap, int hueValue) {
        float newHueValue = (float)(hueValue - 127) * 1.0F / 127.0F * 180.0F;
        ColorMatrix hueColorMatrix = new ColorMatrix();
        hueColorMatrix.setRotate(0, newHueValue);
        hueColorMatrix.setRotate(1, newHueValue);
        hueColorMatrix.setRotate(2, newHueValue);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(hueColorMatrix));
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return newBitmap;
    }

    public static Bitmap lumAndHueAndSaturation(Bitmap bitmap, int lumValue, int hueValue, int saturationValue) {
        float newSaturationValue = (float)saturationValue * 1.0F / 127.0F;
        float newlumValue = (float)lumValue * 1.0F / 127.0F;
        float newHueValue = (float)(hueValue - 127) * 1.0F / 127.0F * 180.0F;
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.setSaturation(newSaturationValue);
        colorMatrix.setScale(newlumValue, newlumValue, newlumValue, 1.0F);
        colorMatrix.setRotate(0, newHueValue);
        colorMatrix.setRotate(1, newHueValue);
        colorMatrix.setRotate(2, newHueValue);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawBitmap(bitmap, 0.0F, 0.0F, paint);
        return newBitmap;
    }

    public static Bitmap nostalgic(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        for(int i = 0; i < height; ++i) {
            for(int k = 0; k < width; ++k) {
                int pixColor = pixels[width * i + k];
                int pixR = Color.red(pixColor);
                int pixG = Color.green(pixColor);
                int pixB = Color.blue(pixColor);
                int newR = (int)(0.393D * (double)pixR + 0.769D * (double)pixG + 0.189D * (double)pixB);
                int newG = (int)(0.349D * (double)pixR + 0.686D * (double)pixG + 0.168D * (double)pixB);
                int newB = (int)(0.272D * (double)pixR + 0.534D * (double)pixG + 0.131D * (double)pixB);
                int newColor = Color.argb(255, newR > 255?255:newR, newG > 255?255:newG, newB > 255?255:newB);
                pixels[width * i + k] = newColor;
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    public static Bitmap soften(Bitmap bitmap) {
        int[] gauss = new int[]{1, 2, 1, 2, 4, 2, 1, 2, 1};
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int newR = 0;
        int newG = 0;
        int newB = 0;
        int delta = 16;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int i = 1;

        for(int length = height - 1; i < length; ++i) {
            int k = 1;

            for(int len = width - 1; k < len; ++k) {
                int idx = 0;

                for(int m = -1; m <= 1; ++m) {
                    for(int n = -1; n <= 1; ++n) {
                        int pixColor = pixels[(i + m) * width + k + n];
                        int pixR = Color.red(pixColor);
                        int pixG = Color.green(pixColor);
                        int pixB = Color.blue(pixColor);
                        newR += pixR * gauss[idx];
                        newG += pixG * gauss[idx];
                        newB += pixB * gauss[idx];
                        ++idx;
                    }
                }

                newR /= delta;
                newG /= delta;
                newB /= delta;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    public static Bitmap sunshine(Bitmap bitmap, int centerX, int centerY) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        int radius = Math.min(centerX, centerY);
        float strength = 150.0F;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int i = 1;

        for(int length = height - 1; i < length; ++i) {
            int k = 1;

            for(int len = width - 1; k < len; ++k) {
                int pos = i * width + k;
                int pixColor = pixels[pos];
                int pixR = Color.red(pixColor);
                int pixG = Color.green(pixColor);
                int pixB = Color.blue(pixColor);
                int newR = pixR;
                int newG = pixG;
                int newB = pixB;
                int distance = (int)(Math.pow((double)(centerY - i), 2.0D) + Math.pow((double)(centerX - k), 2.0D));
                if(distance < radius * radius) {
                    int result = (int)(150.0D * (1.0D - Math.sqrt((double)distance) / (double)radius));
                    newR = pixR + result;
                    newG = pixG + result;
                    newB = pixB + result;
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    public static Bitmap film(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int i = 1;

        for(int length = height - 1; i < length; ++i) {
            int k = 1;

            for(int len = width - 1; k < len; ++k) {
                int pos = i * width + k;
                int pixColor = pixels[pos];
                int pixR = Color.red(pixColor);
                int pixG = Color.green(pixColor);
                int pixB = Color.blue(pixColor);
                int newR = 255 - pixR;
                int newG = 255 - pixG;
                int newB = 255 - pixB;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    public static Bitmap sharpen(Bitmap bitmap) {
        int[] laplacian = new int[]{-1, -1, -1, -1, 9, -1, -1, -1, -1};
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int newR = 0;
        int newG = 0;
        int newB = 0;
        float alpha = 0.3F;
        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int i = 1;

        for(int length = height - 1; i < length; ++i) {
            int k = 1;

            for(int len = width - 1; k < len; ++k) {
                int idx = 0;

                for(int m = -1; m <= 1; ++m) {
                    for(int n = -1; n <= 1; ++n) {
                        int pixColor = pixels[(i + n) * width + k + m];
                        int pixR = Color.red(pixColor);
                        int pixG = Color.green(pixColor);
                        int pixB = Color.blue(pixColor);
                        newR += (int)((float)(pixR * laplacian[idx]) * alpha);
                        newG += (int)((float)(pixG * laplacian[idx]) * alpha);
                        newB += (int)((float)(pixB * laplacian[idx]) * alpha);
                        ++idx;
                    }
                }

                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[i * width + k] = Color.argb(255, newR, newG, newB);
                newR = 0;
                newG = 0;
                newB = 0;
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    public static Bitmap emboss(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

        int[] pixels = new int[width * height];
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
        int i = 1;

        for(int length = height - 1; i < length; ++i) {
            int k = 1;

            for(int len = width - 1; k < len; ++k) {
                int pos = i * width + k;
                int pixColor = pixels[pos];
                int pixR = Color.red(pixColor);
                int pixG = Color.green(pixColor);
                int pixB = Color.blue(pixColor);
                pixColor = pixels[pos + 1];
                int newR = Color.red(pixColor) - pixR + 127;
                int newG = Color.green(pixColor) - pixG + 127;
                int newB = Color.blue(pixColor) - pixB + 127;
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                pixels[pos] = Color.argb(255, newR, newG, newB);
            }
        }

        newBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return newBitmap;
    }

    public static byte[] yuvLandscapeToPortrait(byte[] sourceData, int width, int height) {
        byte[] rotatedData = new byte[sourceData.length];

        for(int y = 0; y < height; ++y) {
            for(int x = 0; x < width; ++x) {
                rotatedData[x * height + height - y - 1] = sourceData[x + y * width];
            }
        }

        return rotatedData;
    }
}
