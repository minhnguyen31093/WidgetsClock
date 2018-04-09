package com.myst.kingdomheartsclock.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import com.myst.kingdomheartsclock.R;

import rapid.decoder.BitmapDecoder;

/**
 * Created by minh on 2/8/18.
 */

public class BitmapUtils {

    public static final int maxSize = 384;

    public static Bitmap getBitmap(Context context, int resId) {
        return BitmapDecoder.from(context.getResources(), resId).scale(maxSize, maxSize).useMemoryCache(true).decode();
    }

    public static Bitmap getBitmapMutable(Context context, int resId) {
        return BitmapDecoder.from(context.getResources(), resId).scale(maxSize, maxSize).mutable().useMemoryCache(true).decode();
    }

    public static Bitmap getBitmapWithShadow(Context context, int resId, float degree) {
        return getBitmapWithShadow(getBitmapMutable(context, resId), degree);
    }

    public static Bitmap getBitmapWithShadowTask(Context context, int resId, final float degree) {
        return getBitmapWithShadow(getBitmapMutable(context, resId), degree);
    }

    public static Bitmap getBitmapWithShadow(Bitmap src, float degree) {
        if (degree > 360) {
            degree -= 360;
        }
        Bitmap bitmap = Bitmap.createBitmap(maxSize, maxSize, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setAlpha(180);
        paint.setColorFilter(new LightingColorFilter(Color.BLACK, 0));
        paint.setMaskFilter(new BlurMaskFilter(1, BlurMaskFilter.Blur.NORMAL));
        paint.setFilterBitmap(true);

        int translate = degree > 0 && degree < 180 ? 2 : degree > 180 && degree < 360 ? -2 : 0;
        canvas.save();
        if (degree > 0 && degree < 360) {
            canvas.rotate(degree, maxSize / 2, maxSize / 2);
        }
        canvas.drawBitmap(src, translate, translate == 0 ? 2 : translate, paint);
        canvas.restore();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.save();
        if (degree > 0 && degree < 360) {
            canvas.rotate(degree, maxSize / 2, maxSize / 2);
        }
        canvas.drawBitmap(src, 0, 0, paint);
        canvas.restore();

        src.recycle();

        return bitmap;
    }

    public static Bitmap getFrameBitmap(Context context) {
        Bitmap src = getBitmapMutable(context, R.drawable.frame);
        Bitmap bitmap = Bitmap.createBitmap(maxSize, maxSize, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setAlpha(180);
        paint.setColorFilter(new LightingColorFilter(Color.BLACK, 0));
        paint.setMaskFilter(new BlurMaskFilter(50, BlurMaskFilter.Blur.NORMAL));
        paint.setFilterBitmap(true);

        canvas.save();
        canvas.drawBitmap(src, 2, 8, paint);
        canvas.restore();

        paint.reset();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        canvas.save();
        canvas.drawBitmap(src, 0, 0, paint);
        canvas.restore();

        src.recycle();

        return bitmap;
    }
}
