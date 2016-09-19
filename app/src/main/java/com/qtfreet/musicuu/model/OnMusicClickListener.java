package com.qtfreet.musicuu.model;

import android.view.View;


public interface OnMusicClickListener {
    void Music(View itemView, int position);

    void downLoadMusic(View itemView, int position);

    void playMV(View itemView, int position);
}
