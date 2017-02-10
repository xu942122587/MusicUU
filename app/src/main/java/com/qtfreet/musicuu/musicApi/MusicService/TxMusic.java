package com.qtfreet.musicuu.musicApi.MusicService;

import com.alibaba.fastjson.JSON;
import com.qtfreet.musicuu.musicApi.MusicBean.tx.TencentDatas;
import com.qtfreet.musicuu.musicApi.MusicBean.tx.TencentGetKey;
import com.qtfreet.musicuu.musicApi.MusicBean.tx.TencentMvData;
import com.qtfreet.musicuu.musicApi.MusicBean.tx.TencentMvKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


/**
 * Created by qtfreet on 2017/2/6.
 */
public class TxMusic implements IMusic {
    //腾讯支持无损和mv解析
    private static List<SongResult> search(String key, int page, int size) throws Exception {
        String url = "http://soso.music.qq.com/fcgi-bin/search_cp?aggr=0&catZhida=0&lossless=1&sem=1&w=" + key + "&n=" + size + "&t=0&p=" + page + "&remoteplace=sizer.yqqlist.song&g_tk=5381&loginUin=0&hostUin=0&format=jsonp&inCharset=GB2312&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0";
        String html = NetUtil.GetHtmlContent(url);
        if (html.isEmpty()) {
            return null;//获取信息失败
        }
        html = html.substring(0, html.length() - 1).replace("callback(", "");
        TencentDatas tencentDatas = JSON.parseObject(html, TencentDatas.class);
        TencentDatas.DataBean.SongBean songs = tencentDatas.getData().getSong();
        int totalsize = songs.getTotalnum();
        if (totalsize == 0) {
            return null;//没有找到符合的歌曲
        }
        return GetListByJson(songs.getList());
    }

    //解析搜索时获取到的json，然后拼接成固定格式
    private static List<SongResult> GetListByJson(List<TencentDatas.DataBean.SongBean.ListBean> songs) throws Exception {
        List<SongResult> list = new ArrayList<>();
        int len = songs.size();
        if (len <= 0) {
            return null;
        }
        for (TencentDatas.DataBean.SongBean.ListBean song : songs) {
            SongResult songResult = new SongResult();
            NetUtil.initNullElement(songResult);
            TencentDatas.DataBean.SongBean.ListBean songsBean = song;
            String SongId = String.valueOf(songsBean.getSongid());
            String SongName = songsBean.getSongname();
            String Songlink = "http://y.qq.com/#type=song&mid=" + String.valueOf(songsBean.getSongmid());
            String ArtistId = String.valueOf(songsBean.getSinger().get(0).getId());
            String AlbumId = String.valueOf(songsBean.getAlbumid());
            String AlbumName = songsBean.getAlbumname();
            String artistName = songsBean.getSinger().get(0).getName();
            songResult.setArtistName(artistName);
            songResult.setArtistId(ArtistId);
            songResult.setSongId(SongId);
            songResult.setSongName(SongName);
            songResult.setSongLink(Songlink);
            songResult.setAlbumId(AlbumId);
            songResult.setAlbumName(AlbumName);
            String mvId = songsBean.getVid();
            if (!mvId.isEmpty()) {
                songResult.setMvId(mvId);
                String hdMvUrl = GetMvUrl(songsBean.getVid(), "hd");
                songResult.setMvHdUrl(hdMvUrl);
                String ldMvUrl = GetMvUrl(songsBean.getVid(), "ld");
                songResult.setMvLdUrl(ldMvUrl);
            }
            String mid = !songsBean.getStrMediaMid().isEmpty() ? songsBean.getStrMediaMid() : songsBean.getMedia_mid();
            if (songsBean.getSize128() != 0) {
                songResult.setBitRate("128K");
                long v = new Random(System.currentTimeMillis()).nextLong();
                String key = GetKey(String.valueOf(v));
                if (!key.isEmpty()) {
                    songResult.setLqUrl("http://ws.stream.qqmusic.qq.com/M500" + mid + ".mp3?vkey=" + key + "&guid=" + v +
                            "&fromtag=0");
                }

            }
            if (songsBean.getSizeogg() != 0) {
                songResult.setBitRate("192K");
                long v = new Random(System.currentTimeMillis()).nextLong();
                String key = GetKey(String.valueOf(v));
                if (!key.isEmpty()) {
                    songResult.setHqUrl("http://ws.stream.qqmusic.qq.com/O600" + mid + ".ogg?vkey=" + key + "&guid=" + v +
                            "&fromtag=50");
                }

            }
            if (songsBean.getSize320() != 0) {

                songResult.setBitRate("320K");
                long v = new Random(System.currentTimeMillis()).nextLong();
                String key = GetKey(String.valueOf(v));
                if (!key.isEmpty()) {
                    songResult.setSqUrl("http://ws.stream.qqmusic.qq.com/M800" + mid + ".mp3?vkey=" + key + "&guid=" + v +
                            "&fromtag=50");
                }


            }
            if (songsBean.getSizeflac() != 0) {
                songResult.setBitRate("无损");
                songResult.setFlacUrl("http://dl.stream.qqmusic.qq.com/F000" + mid + ".flac?vkey=F7B5C260CB57AE3339B157A9443C33A01043A9AB6A8CFC7600535EEC4FDA13A31B1C94259C6A655FAB2A255A4C107F6D3A2FB1F2308ABE60&guid=YYFM&uin=123456&fromtag=53");

            }
            //ape为A000
            String albummid = songsBean.getAlbummid();
            songResult.setPicUrl("http://i.gtimg.cn/music/photo/mid_album_500/" + albummid.substring(albummid.length() - 2, albummid.length() - 1) + "/" + albummid.substring(albummid.length() - 1) + "/" + albummid + ".jpg");
            songResult.setLength(Util.secTotime(songsBean.getInterval()));

            songResult.setType("qq");
//            songResult.setLrcUrl(GetLrcUrl(SongId, SongName, artistName)); //暂不去拿歌词，直接解析浪费性能
            NetUtil.removeNullElement(songResult);
            list.add(songResult);
        }
        return list;
    }

    private static String GetMvUrl(String id, String quality) {
        try {
            String html = NetUtil.GetHtmlContent("http://vv.video.qq.com/getinfo?vid=" + id + "&platform=11&charge=1&otype=json");
            html = html.substring(0, html.length() - 1).replace("QZOutputJson=", "");
            TencentMvData tencentMvData = JSON.parseObject(html, TencentMvData.class);
            if (tencentMvData.getFl() == null) {
                return "";
            }
            List<TencentMvData.FlBean.FiBean> fi = tencentMvData.getFl().getFi();
            HashMap<String, Integer> dic = new HashMap<>();
            int count = fi.size();
            for (TencentMvData.FlBean.FiBean fiBean : fi) {
                dic.put(fiBean.getName(), fiBean.getId());
            }
            int mvID;
            if (quality.equals("hd")) switch (count) {
                case 4:
                    mvID = dic.get("fhd");
                    break;
                case 3:
                    mvID = dic.get("shd");

                    break;
                case 2:
                    mvID = dic.get("hd");
                    break;
                default:
                    mvID = dic.get("sd");
                    break;
            }
            else {
                switch (count) {
                    case 4:
                        mvID = dic.get("shd");
                        break;
                    case 3:
                        mvID = dic.get("hd");
                        break;

                    default:
                        mvID = dic.get("sd");
                        break;
                }
            }
            String vkey = GetVkey(mvID, id);
            String fn = id + ".p" + (mvID - 10000) + ".1.mp4";
            return tencentMvData.getVl().getVi().get(0).getUl().getUi().get(0).getUrl() + fn + "?vkey=" + vkey;
        } catch (Exception e) {
            return "";
        }

    }

    private static String GetVkey(int id, String videoId) {
        try {
            String fn = videoId + ".p" + (id - 10000) + ".1.mp4";
            String url = "http://vv.video.qq.com/getkey?format=" + id + "&otype=json&vid=" + videoId +
                    "&platform=11&charge=1&filename=" + fn;

            String html = NetUtil.GetHtmlContent(url);
            if (html.isEmpty()) {
                return "";
            }
            html = html.substring(0, html.length() - 1).replace("QZOutputJson=", "");
            TencentMvKey tencentMvKey = JSON.parseObject(html, TencentMvKey.class);
            return tencentMvKey.getKey();
        } catch (Exception e) {
            return "";
        }
    }

    private static String GetKey(String time) {
        try {
            String html =
                    NetUtil.GetHtmlContent("http://base.music.qq.com/fcgi-bin/fcg_musicexpress.fcg?json=3&guid=" + time);
            html = html.replace("jsonCallback(", "").replace(");", "");
            TencentGetKey tencentGetKey = JSON.parseObject(html, TencentGetKey.class);
            return tencentGetKey.getKey();
        } catch (Exception e) {
            return "";
        }

    }

    @Override
    public List<SongResult> SongSearch(String key, int page, int size) {
        try {
            return search(key, page, size);
        } catch (Exception e) {
            return null;//解析歌曲时失败
        }
    }
}
