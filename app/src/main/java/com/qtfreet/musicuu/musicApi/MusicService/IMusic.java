package com.qtfreet.musicuu.musicApi.MusicService;

import java.util.List;

/**
 * Created by qtfreet00 on 2017/2/4.
 */
public interface IMusic {
    List<SongResult> SongSearch(String key, int page, int size);
}
