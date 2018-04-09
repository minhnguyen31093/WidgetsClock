package com.myst.kingdomheartsclock.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.myst.kingdomheartsclock.widget.ClockWidget;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by minh on 2/22/18.
 */

public class UpdateService extends Service {

    Timer timer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 1);
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                sendBroadcast(new Intent(UpdateService.this, ClockWidget.class));
            }
        }, calendar.getTimeInMillis() - System.currentTimeMillis(), 999);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
}
