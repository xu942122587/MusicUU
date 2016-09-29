package com.qtfreet.musicuu.ui;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.tencent.bugly.Bugly;

import java.io.File;

/**
 * Created by qtfreet on 2016/3/21.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(this, "900055380", false);
        File file = new File(Constants.lyricPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        FileDownloader.init(getApplicationContext());
        FileDownloader.setGlobalPost2UIInterval(1000);
    }
}
