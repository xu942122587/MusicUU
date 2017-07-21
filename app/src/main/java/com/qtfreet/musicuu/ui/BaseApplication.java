package com.qtfreet.musicuu.ui;

import android.app.Application;

import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.qtfreet.musicuu.model.Constant.Constants;
import com.tencent.bugly.crashreport.CrashReport;
import com.yanzhenjie.nohttp.InitializationConfig;
import com.yanzhenjie.nohttp.OkHttpNetworkExecutor;
import com.yanzhenjie.nohttp.NoHttp;

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
            boolean mkdirs = file.mkdirs();
        }
        Utils.init(this);
        InitializationConfig config = InitializationConfig.newBuilder(this)
                .readTimeout(15000).connectionTimeout(15000).networkExecutor(new OkHttpNetworkExecutor()).build();
        NoHttp.initialize(config);
        FileDownloader.init(getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker().connectionCreator(new FileDownloadUrlConnection.Creator(new FileDownloadUrlConnection.Configuration().connectTimeout(15_000).readTimeout(15_000).proxy(Proxy.NO_PROXY))));
        FileDownloader.setGlobalPost2UIInterval(1000);
    }
}
