package com.qtfreet.musicuu.musicApi.MusicService;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.qtfreet.musicuu.musicApi.MusicBean.kg.KugouLrc;
import com.qtfreet.musicuu.musicApi.MusicBean.kg.KugouMp3Url;
import com.qtfreet.musicuu.musicApi.MusicBean.kg.KugouMv;
import com.qtfreet.musicuu.musicApi.MusicBean.kg.KugouPic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qtfreet on 2017/2/7.
 */
public class KgMusic implements IMusic {
    //酷狗支持无损和mv解析
    private static List<SongResult> search(String key, int page, int size) throws Exception {
        String url = "http://ioscdn.kugou.com/api/v3/search/song?keyword=" + key + "&page=" + page + "&pagesize=" + size + "&showtype=10&plat=2&version=7910&tag=1&correct=1&privilege=1&sver=5";
        String s = NetUtil.GetHtmlContent(url);
        JSONObject kugouDatas = JSON.parseObject(s);
        if (kugouDatas == null) {
            return null;//搜索歌曲失败
        }
        if (kugouDatas.getJSONObject("data").getIntValue("total") == 0) {
            return null;//没有搜到歌曲
        }
        int totalsize = kugouDatas.getJSONObject("data").getIntValue("total");
        JSONArray data = kugouDatas.getJSONObject("data").getJSONArray("info");
        return GetListByJson(data);
    }

    //解析搜索时获取到的json，然后拼接成固定格式
    private static List<SongResult> GetListByJson(JSONArray songs) throws Exception {
        List<SongResult> list = new ArrayList<>();
        int len = songs.size();
        if (len <= 0) {
            return null;
        }
        int i = 0;
        while (i < len) {
            SongResult songResult = new SongResult();
            NetUtil.initNullElement(songResult);
            JSONObject songsBean = songs.getJSONObject(i);
            String SongId = songsBean.getString("hash");
            String SongName = songsBean.getString("songname");
            String AlbumId = songsBean.getString("album_id");
            String AlbumName = songsBean.getString("album_name").replace("+", ";");
            String artistName = songsBean.getString("singername").replace("+", ";");
            int length = songsBean.getIntValue("duration");
            songResult.setArtistName(artistName);
            songResult.setSongId(SongId);
            songResult.setSongName(SongName);
            songResult.setLength(Util.secTotime(length));
            if (!AlbumId.isEmpty() && Util.isNumber(AlbumId)) {
                songResult.setPicUrl(GetUrl(AlbumId, "320", "jpg"));
            } else {
                songResult.setPicUrl(GetUrl(SongId, "320", "jpg"));
            }
            String s20hash = songsBean.getString("320hash");
            String sqHash = songsBean.getString("sqhash");
            if (!s20hash.isEmpty()) {
                songResult.setBitRate("320K");
                String sq = GetUrl(s20hash, "320", "mp3");
                songResult.setSqUrl(sq);
                if (songResult.getHqUrl().isEmpty()) {
                    songResult.setHqUrl(sq);
                }
            }
            if (!sqHash.isEmpty()) {
                songResult.setBitRate("无损");
                songResult.setFlacUrl(GetUrl(sqHash, "1000", "mp3"));
            }
            if (!SongId.isEmpty()) {
                songResult.setBitRate("128K");
                songResult.setLqUrl(GetUrl(SongId, "128", "mp3"));
            }
//            GetUrl(songsBean.getValue320hash(),"320","mp3");
//            String Songlink = "http://h.dongting.com/yule/app/music_player_page.html?id=" + String.valueOf(songsBean.getSongId());
//            songResult.setSongLink(Songlink);
            String mvHash = songsBean.getString("mvhash");
            if (!mvHash.isEmpty()) {
                songResult.setMvId(mvHash);
                songResult.setMvHdUrl(GetUrl(mvHash, "hd", "mp4"));
                songResult.setMvLdUrl(GetUrl(mvHash, "ld", "mp4"));
            }
            songResult.setAlbumId(AlbumId);
            songResult.setAlbumName(AlbumName);

            songResult.setType("kg");
//            songResult.setPicUrl(songsBean.getPicUrl());
//            GetUrl(SongId,"320","lrc");
//            songResult.setLrcUrl(GetLrcUrl(SongId, SongName, artistName)); //暂不去拿歌曲，直接解析浪费性能
            NetUtil.removeNullElement(songResult);
            list.add(songResult);
            i++;
        }
        return list;
    }


    private static String GetUrl(String id, String quality, String format) {
        try {
            String html;
            if (format.equals("jpg") && Util.isNumber(id)) {
                html = NetUtil.GetHtmlContent("http://ioscdn.kugou.com/api/v3/album/info?albumid=" + id + "&version=7910");
                if (html.isEmpty()) {
                    return "";
                }
                KugouPic kugouPic = JSON.parseObject(html, KugouPic.class);
                if (kugouPic.getData() == null) {
                    return "";
                }
                return kugouPic.getData().getImgurl().replace("{size}", "480");
            } else if (format.equals("jpg")) {
                html = NetUtil.GetHtmlContent("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + id);
                if (html.isEmpty() || html.contains("hash error")) {
                    return "";
                }
                JSONObject json = JSON.parseObject(html);
                String songName = json.getString("fileName");
                html = NetUtil.GetHtmlContent("http://m.kugou.com/app/i/getSingerHead_new.php?singerName=" + songName.split("-")[0].trim().toString() + "&size=480");
                JSONObject imgJson = JSON.parseObject(html);
                String imgUrl = imgJson.getString("url");
                if (imgUrl.isEmpty()) {
                    return "";
                }
                return imgUrl;
            }

            if (format.equals("lrc")) {
                html = NetUtil.GetHtmlContent("http://m.kugou.com/app/i/getSongInfo.php?cmd=playInfo&hash=" + id);

                if (html.contains("hash error") || html.isEmpty()) {
                    return "";
                }
                KugouLrc kugouLrc = JSON.parseObject(html, KugouLrc.class);
                String songName = kugouLrc.getSongName();
                String len = kugouLrc.getTimeLength() + "000";
                if (format.equals("lrc")) {
                    html = NetUtil.GetHtmlContent("http://m.kugou.com/app/i/krc.php?cmd=100&keyword=" + songName +
                            "&hash=" + id + "&timelength=" + len + "&d=0.38664927426725626", false);
                    if (html.isEmpty()) {
                        return "";
                    }
//                System.out.println(html);
                    return "[ti:" + songName + "]\n[by: FM]\n" + html;
                }

            }
            if (format.equals("mp3")) {
                String url = "http://trackercdn.kugou.com/i/?key=" + Util.getMD5(id + "kgcloud") + "&cmd=4&acceptMp3=1&hash=" + id + "&pid=1";
                html = NetUtil.GetHtmlContent(url);
                if (html.contains("Bad key") || html.contains("The Resource Needs to be Paid")) {
                    return "";
                }
                //付费歌曲无法解析 {"status":0,"error":"The Resource Needs to be Paid"}
                KugouMp3Url kugouMp3Url = JSON.parseObject(html, KugouMp3Url.class);
                return kugouMp3Url.getUrl();
            }
            if (format.equals("mp4")) {
                String key = Util.getMD5(id + "kugoumvcloud");
                html = NetUtil.GetHtmlContent("http://trackermv.kugou.com/interface/index?cmd=100&pid=6&ext=mp4&hash=" + id +
                        "&quality=-1&key=" + key + "&backupdomain=1");
//            /interface/index?cmd=100&pid=6&ext=mp4&hash=1f1668e15ee298b4d3ee630cef0c6a90&quality=-1&key=0cda6579ff6a8822d5d5a9e504bbcc57&backupdomain=1
                if (html.contains("Bad key")) {
                    return "";
                }
                KugouMv kugouMv = JSON.parseObject(html, KugouMv.class);
                KugouMv.MvdataBean mvdata = kugouMv.getMvdata();
                if (quality.equals("hd")) {
                    String rq = mvdata.getRq().getDownurl();
                    if (rq != null && !rq.isEmpty()) {
                        return rq;
                    }
                    String sq = mvdata.getSq().getDownurl();
                    if (sq != null && !sq.isEmpty()) {
                        return sq;
                    }
                    String hd = mvdata.getHd().getDownurl();
                    if (hd != null && !hd.isEmpty()) {
                        return hd;
                    }
                    String sd = mvdata.getSd().getDownurl();
                    if (sd != null && !sd.isEmpty()) {
                        return sd;
                    }
                } else {
                    String sq = mvdata.getSq().getDownurl();
                    if (sq != null && !sq.isEmpty()) {
                        return sq;
                    }
                    String hd = mvdata.getHd().getDownurl();
                    if (hd != null && !hd.isEmpty()) {
                        return hd;
                    }
                    String sd = mvdata.getSd().getDownurl();
                    if (sd != null && !sd.isEmpty()) {
                        return sd;
                    }
                }
            }
        } catch (Exception e) {

        }
        return "";
    }

    @Override
    public List<SongResult> SongSearch(String key, int page, int size) {
        try {
            return search(key, page, size);
        } catch (Exception e) {
            return null;
        }
    }
}
