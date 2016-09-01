package com.qtfreet.musicuu.model.Bean.MusicUU;

/**
 * Created by qtfreet on 2016/4/6.
 */
public class RecomendResult {
    private String AlbumId;
    private String AlbumName;
    private String AlbumTime;
    private String AlbumDesp;
    private String AlnumPic;
    private int SubType;
    private String Tags;
    private String DownUrl;

    public void setAlbumId(String AlbumId) {
        this.AlbumId = AlbumId;
    }

    public String getAlbumId() {
        return this.AlbumId;
    }

    public void setAlbumName(String AlbumName) {
        this.AlbumName = AlbumName;
    }

    public String getAlbumName() {
        return this.AlbumName;
    }

    public void setAlbumTime(String AlbumTime) {
        this.AlbumTime = AlbumTime;
    }

    public String getAlbumTime() {
        return this.AlbumTime;
    }

    public void setAlbumDesp(String AlbumDesp) {
        this.AlbumDesp = AlbumDesp;
    }

    public String getAlbumDesp() {
        return this.AlbumDesp;
    }

    public void setAlnumPic(String AlnumPic) {
        this.AlnumPic = AlnumPic;
    }

    public String getAlnumPic() {
        return this.AlnumPic;
    }

    public void setSubType(int SubType) {
        this.SubType = SubType;
    }

    public int getSubType() {
        return this.SubType;
    }

    public void setTags(String Tags) {
        this.Tags = Tags;
    }

    public String getTags() {
        return this.Tags;
    }

    public void setDownUrl(String DownUrl) {
        this.DownUrl = DownUrl;
    }

    public String getDownUrl() {
        return this.DownUrl;
    }
}
