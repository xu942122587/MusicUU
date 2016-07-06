package com.qtfreet.musicuu.utils;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;

public class SystemUtil {

    public static int getMaxVolume(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int max = manager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        return max;
    }

    public static void setVolume(int volume, Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }

    public static int getVolume(Context context) {
        AudioManager manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return manager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds);
    }


    public static void setScreeBrightness(int progress, Activity activity) {
        android.view.WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (progress < 10) {
            progress = 10;
        } else if (progress >= 100) {
            progress = 99;
        }
        float f = progress * 1.0f / 100.0f;
        lp.screenBrightness = f;
        activity.getWindow().setAttributes(lp);
    }

}
