package com.qtfreet.musicuu.musicApi;


import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.qtfreet.musicuu.musicApi.MusicBean.SearchResult;
import com.qtfreet.musicuu.musicApi.MusicService.MusicService;
import com.qtfreet.musicuu.musicApi.MusicService.SongResult;

import java.util.List;

/**
 * Created by qtfreet00 on 2017/2/5.
 */
public class SearchSong {
    public static SearchResult Search(String type, String key, int page, int size) {
        List<SongResult> wy = MusicService.GetMusic(type).SongSearch(key, page, size);
        SearchResult searchResult = new SearchResult();
        if (wy == null) {
            searchResult.setStatus(404);
            searchResult.setSongs(null);
        } else {
            searchResult.setStatus(200);
            searchResult.setSongs(wy);
        }
        return searchResult;
    }
}
