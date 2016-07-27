package com.qtfreet.musicuu.model;

import android.view.View;

import me.drakeet.uiview.UIButton;

/**
 * Created by qtfreet on 2016/3/23.
 */
public interface DownListener {
    void Download(View v, int position, String songId, String songName, String artist, String MusicUrl, String videoUrl, UIButton btn_down);
}
