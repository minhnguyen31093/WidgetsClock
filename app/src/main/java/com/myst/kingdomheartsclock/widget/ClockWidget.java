package com.myst.kingdomheartsclock.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

import com.jni.bitmap_operations.JniBitmapHolder;
import com.myst.kingdomheartsclock.service.AppWidgetAlarm;
import com.myst.kingdomheartsclock.utils.BitmapUtils;
import com.myst.kingdomheartsclock.R;

/**
 * Created by Myst on 1/28/2018.
 */

public class ClockWidget extends AppWidgetProvider {

    public static final String ACTION_AUTO_UPDATE = "com.myst.kingdomheartsclock.widget.ClockWidget.AUTO_UPDATE";

    private static RemoteViews remoteViews;
    private static int currentM = -1, currentBg = R.drawable.bg;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateTime(context);
    }

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        // start alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.startAlarm();
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        // stop alarm
        AppWidgetAlarm appWidgetAlarm = new AppWidgetAlarm(context.getApplicationContext());
        appWidgetAlarm.stopAlarm();
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(ACTION_AUTO_UPDATE)) {
            updateTime(context);
        }
    }

    public static void updateTime(Context context) {
        ComponentName mComponentName = new ComponentName(context, ClockWidget.class);
        AppWidgetManager mAppWidgetManager = AppWidgetManager.getInstance(context);

        if (remoteViews == null) {
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_clock);
            remoteViews.setImageViewBitmap(R.id.imgFrame, BitmapUtils.getFrameBitmap(context));
            remoteViews.setImageViewBitmap(R.id.imgBg, BitmapUtils.getBitmap(context, currentBg));
            remoteViews.setImageViewBitmap(R.id.imgFg, BitmapUtils.getBitmap(context, R.drawable.fg));

            remoteViews.setImageViewBitmap(R.id.imgDot, BitmapUtils.getBitmapWithShadowTask(context, R.drawable.dot, 0));
            remoteViews.setImageViewBitmap(R.id.imgDot2, BitmapUtils.getBitmapWithShadowTask(context, R.drawable.dot2, 0));
        }

        Time mCalendar = new Time();
        mCalendar.setToNow();
        int newSecond = mCalendar.second;
        int newMinute = mCalendar.minute;
        int newHour = mCalendar.hour;

        long start = System.currentTimeMillis();
        Log.e("Time start", String.valueOf(System.currentTimeMillis()));
        try {

            if (currentM != newMinute) {
                currentM = newMinute;
                float m = newMinute * 360f / 60f;

                Bitmap bitmap = BitmapUtils.getBitmapWithShadowTask(context, R.drawable.m, m);
                JniBitmapHolder bitmapHolder = new JniBitmapHolder(bitmap);
                bitmap.recycle();

                remoteViews.setImageViewBitmap(R.id.imgM, bitmapHolder.getBitmapAndFree());

                float h = (newHour + newMinute / 60f) * 360f / 12f;

                Bitmap bitmapH = BitmapUtils.getBitmapWithShadowTask(context, R.drawable.h, h);
                JniBitmapHolder bitmapHolderH = new JniBitmapHolder(bitmapH);
                bitmapH.recycle();

                remoteViews.setImageViewBitmap(R.id.imgH, bitmapHolderH.getBitmapAndFree());
            }

            if (newHour >= 7 && newHour < 22 && currentBg == R.drawable.bgn) {
                currentBg = R.drawable.bg;
                remoteViews.setImageViewBitmap(R.id.imgBg, BitmapUtils.getBitmap(context, currentBg));
            } else if (newHour < 7 || newHour >= 22 && currentBg == R.drawable.bg) {
                currentBg = R.drawable.bgn;
                remoteViews.setImageViewBitmap(R.id.imgBg, BitmapUtils.getBitmap(context, currentBg));
            }
        } catch (OutOfMemoryError e) {
            Log.e("Error", e.getMessage());
        }
        long end = System.currentTimeMillis();
        Log.e("Time end", String.valueOf(end));
        Log.e("Time render", String.valueOf(end - start) + "ms");

        mAppWidgetManager.updateAppWidget(mComponentName, remoteViews);
    }
}
