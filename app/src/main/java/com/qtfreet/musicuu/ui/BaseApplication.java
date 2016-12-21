package com.qtfreet.musicuu.ui;

import android.app.Application;

import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.tencent.bugly.crashreport.CrashReport;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yolanda.nohttp.NoHttp;

import java.io.File;
import java.net.Proxy;

/**
 * Created by qtfreet on 2016/3/21.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashReport.initCrashReport(getApplicationContext(), "900055380", false);
        File file = new File(Constants.lyricPath);
        if (!file.exists()) {
            file.mkdirs();
        }
        NoHttp.initialize(this, new NoHttp.Config().setConnectTimeout(30000).setReadTimeout(30000).setNetworkExecutor(new OkHttpNetworkExecutor()));
        FileDownloader.init(getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker().connectionCreator(new FileDownloadUrlConnection.Creator(new FileDownloadUrlConnection.Configuration().connectTimeout(15_000).readTimeout(15_000).proxy(Proxy.NO_PROXY))));
        FileDownloader.setGlobalPost2UIInterval(1000);
    }
}
