package com.myst.kingdomheartsclock.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import rapid.decoder.BitmapDecoder;

/**
 * Created by minh on 1/30/18.
 */

public class ClockApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        // Allocate 2MB for memory cache
        BitmapDecoder.initMemoryCache(2 * 1024 * 1024);
        // Allocate proper amount proportional to screen size for memory cache
        BitmapDecoder.initMemoryCache(this);

        // Allocate default 8MB for disk cache
        BitmapDecoder.initDiskCache(this);
        // Allocate 32MB for disk cache
        BitmapDecoder.initDiskCache(this, 32 * 1024 * 1024);
    }
}
