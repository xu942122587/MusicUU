package com.qtfreet.musicuu.ui;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.pgyersdk.crash.PgyCrashManager;

import java.io.File;

/**
 * Created by qtfreet on 2016/3/21.
 */
public class APp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PgyCrashManager.register(this);
        File file = new File(Constant.lyricPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileDownloader.init(getApplicationContext());
        FileDownloader.setGlobalPost2UIInterval(1000);
    }
}
