package com.qtfreet.musicuu.musicApi.MusicBean.kg;

/**
 * Created by qtfreet on 2017/2/7.
 */
public class KugouPic {

    /**
     * status : 1
     * error :
     * data : {"albumname":"徐薇翻唱作品合集","publishtime":"2016-05-25 00:00:00","singername":"徐薇","intro":"徐薇·翻唱作品合集。","songcount":0,"imgurl":"http://imge.kugou.com/stdmusic/{size}/20160529/20160529210310343262.jpg","collectcount":0,"singerid":180071,"albumid":1918047,"sextype":0,"privilege":0}
     * errcode : 0
     */

    private int status;
    private String error;
    private DataBean data;
    private int errcode;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public static class DataBean {
        /**
         * albumname : 徐薇翻唱作品合集
         * publishtime : 2016-05-25 00:00:00
         * singername : 徐薇
         * intro : 徐薇·翻唱作品合集。
         * songcount : 0
         * imgurl : http://imge.kugou.com/stdmusic/{size}/20160529/20160529210310343262.jpg
         * collectcount : 0
         * singerid : 180071
         * albumid : 1918047
         * sextype : 0
         * privilege : 0
         */

        private String albumname;
        private String publishtime;
        private String singername;
        private String intro;
        private int songcount;
        private String imgurl;
        private int collectcount;
        private int singerid;
        private int albumid;
        private int sextype;
        private int privilege;

        public String getAlbumname() {
            return albumname;
        }

        public void setAlbumname(String albumname) {
            this.albumname = albumname;
        }

        public String getPublishtime() {
            return publishtime;
        }

        public void setPublishtime(String publishtime) {
            this.publishtime = publishtime;
        }

        public String getSingername() {
            return singername;
        }

        public void setSingername(String singername) {
            this.singername = singername;
        }

        public String getIntro() {
            return intro;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public int getSongcount() {
            return songcount;
        }

        public void setSongcount(int songcount) {
            this.songcount = songcount;
        }

        public String getImgurl() {
            return imgurl;
        }

        public void setImgurl(String imgurl) {
            this.imgurl = imgurl;
        }

        public int getCollectcount() {
            return collectcount;
        }

        public void setCollectcount(int collectcount) {
            this.collectcount = collectcount;
        }

        public int getSingerid() {
            return singerid;
        }

        public void setSingerid(int singerid) {
            this.singerid = singerid;
        }

        public int getAlbumid() {
            return albumid;
        }

        public void setAlbumid(int albumid) {
            this.albumid = albumid;
        }

        public int getSextype() {
            return sextype;
        }

        public void setSextype(int sextype) {
            this.sextype = sextype;
        }

        public int getPrivilege() {
            return privilege;
        }

        public void setPrivilege(int privilege) {
            this.privilege = privilege;
        }
    }
}
