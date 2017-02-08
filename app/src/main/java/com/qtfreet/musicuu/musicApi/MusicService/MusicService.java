package com.qtfreet.musicuu.musicApi.MusicService;

/**
 * Created by qtfreet00 on 2017/2/4.
 */
public class MusicService {
    public static IMusic GetMusic(String type) {
        switch (type) {
            case "wy":
                return new WyMusic();
            case "tt":
                return new TtMusic();
            case "qq":
                return new TxMusic();
            case "kg":
                return new KgMusic();
            case "xm":
                return new XmMusic();
            default:
                return new TtMusic();
        }
    }
}
