package com.qtfreet.musicuu.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import me.drakeet.uiview.UIButton;
import okhttp3.Call;

/**
 * Created by qtfreet on 2016/3/26.
 */
public class DownloadUtil {
    public static void StartDownload(final Context context, final String name, final String url, final String tag) {
        final String path = Environment.getExternalStorageDirectory() + "/" + SPUtils.get("com.qtfreet.musicuu_preferences", context, "SavePath", "musicuu");

        final File file = new File(path + "/" + name);
        if (file.exists()) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setTitle("提示");
            dialog.setMessage("文件已存在，是否需要重新下载？");
            dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    download(context, name, url, path, tag);
                }
            });
            dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            dialog.show();
        } else {
            download(context, name, url, path, tag);
        }
    }

    private static void download(final Context context, String name, String url, final String path, String tag) {
        if (url.equals("")) {
            return;
        }
        OkHttpUtils.get().url(url).tag(tag).build().connTimeOut(10000).readTimeOut(30000).writeTimeOut(10000).execute(new FileCallBack(path, name) {
            @Override
            public void inProgress(float progress, long total) {

                if ((int) (100 * progress) == 100) {
                    Toast.makeText(context, "下载完成", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Call call, Exception e) {

            }

            @Override
            public void onResponse(File response) {

            }
        });
    }
}
