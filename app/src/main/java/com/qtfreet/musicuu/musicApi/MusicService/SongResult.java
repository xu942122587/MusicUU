package com.qtfreet.musicuu.musicApi.MusicService;

/**
 * Created by qtfreet00 on 2017/2/3.
 */
public class SongResult {
    private String SongId;
    /// <summary>
    /// 曲名
    /// </summary>
    private String SongName;

    /// <summary>
    /// 艺术家ID
    /// </summary>
    private String ArtistId;
    /// <summary>
    /// 歌手名字
    /// </summary>
    private String ArtistName;

    /// <summary>
    /// 专辑ID
    /// </summary>
    private String AlbumId;
    /// <summary>
    /// 专辑名
    /// </summary>
    private String AlbumName;

    /// <summary>
    /// 歌曲链接/来源
    /// </summary>
    private String SongLink;
    /// <summary>
    /// 时长
    /// </summary>
    private String Length;

    /// <summary>
    /// 比特率
    /// </summary>
    private String BitRate;
    /// <summary>
    /// Flac无损地址
    /// </summary>
    private String FlacUrl;

    /// <summary>
    /// 320K
    /// </summary>
    private String SqUrl;
    /// <summary>
    /// 192K
    /// </summary>
    private String HqUrl;
    /// <summary>
    /// 128K
    /// </summary>
    private String LqUrl;

    /// <summary>
    /// 歌曲封面
    /// </summary>
    private String PicUrl;
    /// <summary>
    /// LRC歌词
    /// </summary>
    private String LrcUrl;

    /// <summary>
    /// MV Id
    /// </summary>
    private String MvId;
    /// <summary>
    /// 高清MV地址
    /// </summary>
    /// 
    private String MvHdUrl;
    /// <summary>
    /// 普清MV地址
    /// </summary>
    private String MvLdUrl;


    public String getSongId() {
        return SongId;
    }

    public void setSongId(String songId) {
        SongId = songId;
    }

    public String getSongName() {
        return SongName;
    }

    public void setSongName(String songName) {
        SongName = songName;
    }

    public String getArtistId() {
        return ArtistId;
    }

    public void setArtistId(String artistId) {
        ArtistId = artistId;
    }

    public String getArtistName() {
        return ArtistName;
    }

    public void setArtistName(String artistName) {
        ArtistName = artistName;
    }

    public String getAlbumId() {
        return AlbumId;
    }

    public void setAlbumId(String albumId) {
        AlbumId = albumId;
    }

    public String getAlbumName() {
        return AlbumName;
    }

    public void setAlbumName(String albumName) {
        AlbumName = albumName;
    }

    public String getSongLink() {
        return SongLink;
    }

    public void setSongLink(String songLink) {
        SongLink = songLink;
    }

    public String getLength() {
        return Length;
    }

    public void setLength(String length) {
        Length = length;
    }


    public String getBitRate() {
        return BitRate;
    }

    public void setBitRate(String bitRate) {
        BitRate = bitRate;
    }

    public String getFlacUrl() {
        return FlacUrl;
    }

    public void setFlacUrl(String flacUrl) {

        FlacUrl = flacUrl;
    }

    public String getSqUrl() {
        return SqUrl;
    }

    public void setSqUrl(String sqUrl) {

        SqUrl = sqUrl;
    }

    public String getHqUrl() {
        return HqUrl;
    }

    public void setHqUrl(String hqUrl) {

        HqUrl = hqUrl;
    }

    public String getLqUrl() {
        return LqUrl;
    }

    public void setLqUrl(String lqUrl) {

        LqUrl = lqUrl;
    }

    public String getPicUrl() {
        return PicUrl;
    }

    public void setPicUrl(String picUrl) {
        PicUrl = picUrl;
    }

    public String getLrcUrl() {
        return LrcUrl;
    }

    public void setLrcUrl(String lrcUrl) {
        LrcUrl = lrcUrl;
    }

    public String getMvId(){
        return MvId;
    }

    public void setMvId(String mvId) {
        MvId = mvId;
    }

    public String getMvHdUrl() {
        return MvHdUrl;
    }

    public void setMvHdUrl(String mvHdUrl) {
        MvHdUrl = mvHdUrl;
    }

    public String getMvLdUrl() {
        return MvLdUrl;
    }

    public void setMvLdUrl(String mvLdUrl) {
        MvLdUrl = mvLdUrl;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    /// <summary>
    /// 类型
    /// </summary>
    private String Type;
}
