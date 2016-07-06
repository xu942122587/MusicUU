package com.qtfreet.musicuu.ui;

import android.app.Application;

import com.pgyersdk.crash.PgyCrashManager;

/**
 * Created by qtfreet on 2016/3/21.
 */
public class APp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
    }
}
