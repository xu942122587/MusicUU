package com.qtfreet.musicuu.musicApi.MusicService;


import com.alibaba.fastjson.JSON;
import com.qtfreet.musicuu.musicApi.MusicBean.xm.XiamiDatas;
import com.qtfreet.musicuu.musicApi.MusicBean.xm.XiamiIds;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qtfreet on 2017/2/8.
 */
public class XmMusic implements IMusic {
    //虾米不支持mv和无损解析
    private static List<SongResult> search(String key, int page, int size) throws Exception {
        String url = "http://api.xiami.com/web?v=2.0&app_key=1&key=" + key + "&page=" + page + "&limit=" + size + "&r=search/songs";
        String s = NetUtil.GetHtmlWithRefer(url, "http://m.xiami.com");
        if (s.isEmpty()) {
            return null;  //搜索歌曲失败
        }
        XiamiDatas xiamiDatas = JSON.parseObject(s, XiamiDatas.class);
        if (xiamiDatas == null) {
            return null;//没有找到符合的歌曲
        }
        int totalsize = xiamiDatas.getData().getTotal();
        return GetListByJson(xiamiDatas.getData().getSongs());

    }

    private static List<SongResult> GetListByJson(List<XiamiDatas.DataBean.SongsBean> songs) throws Exception {
        List<SongResult> list = new ArrayList<>();
        int len = songs.size();
        if (len <= 0) {
            return null;
        }
        int i = 0;
        while (i < len) {
            XiamiDatas.DataBean.SongsBean songsBean = songs.get(i);
            String SongId = String.valueOf(songsBean.getSong_id());
            SongResult songResult = SearchSong(SongId);
            if (songResult != null) {
                list.add(songResult);
            }
            i++;
        }
        return list;
    }

    private static SongResult GetResultsByIds(String ids, int type) {
        String albumUrl = "http://www.xiami.com/song/playlist/id/" + ids + "/type/" + type + "/cat/json";
        String html = NetUtil.GetHtmlContent(albumUrl);
        if (html.isEmpty() || html.contains("应版权方要求，没有歌曲可以播放")) {
            return null;
        }
        try {
            XiamiIds xiamiIds = JSON.parseObject(html, XiamiIds.class);
            List<XiamiIds.DataBean.TrackListBean> trackList = xiamiIds.getData().getTrackList();
            if (trackList == null) {
                return null;
            }
            XiamiIds.DataBean.TrackListBean trackListBean = trackList.get(0);
            SongResult song = new SongResult();

            String songId = trackListBean.getSong_id();
            String songName = trackListBean.getSongName();
            String songLink = "http://www.xiami.com/song/" + songId;
            String artistId = String.valueOf(trackListBean.getArtistId());
            String artistName = trackListBean.getSingers();
            String ablum = String.valueOf(trackListBean.getAlbumId());
            String album = trackListBean.getAlbum_name();
            String length = Util.secTotime(trackListBean.getLength());
            String lyric = trackListBean.getLyric();
            String picUrl = trackListBean.getPic();//此处为小图
            //trackListBean.getAlbum_pic()//大图
            song.setSongId(songId);
            song.setSongName(songName);
            song.setSongLink(songLink);
            song.setArtistId(artistId);
            song.setArtistName(artistName);
            song.setAlbumId(ablum);
            song.setAlbumName(album);
            song.setLength(length);
            song.setLrcUrl(lyric);
            song.setPicUrl(picUrl);
            List<XiamiIds.DataBean.TrackListBean.AllAudiosBean> allAudios = trackListBean.getAllAudios();
            boolean hash320Kbite = false;
            for (XiamiIds.DataBean.TrackListBean.AllAudiosBean allAudiosBean : allAudios) {
                if (allAudiosBean.getFormat().equals("mp3")) {
                    if (allAudiosBean.getAudioQualityEnum().equals("HIGH")) {
                        hash320Kbite = true;
                        song.setSqUrl(allAudiosBean.getFilePath());
                        song.setHqUrl(allAudiosBean.getFilePath());
                    }
                    if (allAudiosBean.getAudioQualityEnum().equals("LOW")) {
                        song.setLqUrl(allAudiosBean.getFilePath());
                    }
                }
            }
            if (hash320Kbite) {
                song.setBitRate("320K");
            } else {
                song.setBitRate("128K");
            }
            song.setType("xm");

            return song;
        } catch (Exception ex) {

        }
        return null;
    }

    private static SongResult SearchSong(String songId) {
        SongResult song = GetResultsByIds(songId, 0);
        if (song == null) {
            return null;
        }
        return song;
    }

    //虾米网页版mv为m3u8
//    private static String GetMvUrl(String id, String quality, String format) {
//        if (format == "mp4" || format == "flv") {
//            String mvId;
//            String html;
//            if (Util.isNumber(id)) {
//                String url = "http://www.xiami.com/song/" + id;
//                html = NetUtil.GetHtmlContent(url);
//                if (html.isEmpty()) {
//                    return "";
//                }
//                mvId = Util.RegexString("(?<=href=\" \"/mv/)\\w+(?=\" \")", html);
//            } else {
//                mvId = id;
//            }
//            if (mvId.isEmpty()) {
//                return null;
//            }
//            html = NetUtil.GetHtmlContent("http://m.xiami.com/mv/" + mvId);
//            return html.isEmpty() ? "" : Util.RegexString("(?<=<video src=\"\")[^\"\"]+(?=\"\"\\s*poster=)", html);
//        }
//        return "";
//    }

    @Override
    public List<SongResult> SongSearch(String key, int page, int size) {
        try {
            return search(key, page, size);
        } catch (Exception e) {
            return null;  //解析歌曲失败
        }
    }
}
