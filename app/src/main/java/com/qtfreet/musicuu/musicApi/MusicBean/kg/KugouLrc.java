package com.qtfreet.musicuu.musicApi.MusicBean.kg;

/**
 * Created by qtfreet on 2017/2/7.
 */
public class KugouLrc {

    /**
     * fileHead : 100
     * q : 0
     * extra : {"320filesize":0,"sqfilesize":0,"sqhash":"","128hash":"0FBCE9388DE01C661965FBEC403804CA","320hash":"","128filesize":4266906}
     * fileSize : 4266906
     * hash : 0FBCE9388DE01C661965FBEC403804CA
     * mvhash :
     * error :
     * topic_remark :
     * url : http://fs.open.kugou.com/df3032f8e360074cb89a0f818a73fc4c/58995f72/G012/M09/08/14/TA0DAFUIp5yAT7jwAEEbmo3iupE047.mp3
     * bitRate : 128
     * choricSinger : 初音ミク
     * songName : 泡沫
     * req_hash : 0FBCE9388DE01C661965FBEC403804CA
     * singerHead :
     * privilege : 0
     * status : 1
     * stype : 11323
     * ctype : 1009
     * singerName : 初音ミク
     * fileName : 初音ミク - 泡沫
     * singerId : 83837
     * topic_url :
     * intro :
     * errcode : 0
     * extName : mp3
     * imgUrl : http://singerimg.kugou.com/uploadpic/softhead/{size}/20140814/20140814125653689354.jpg
     * timeLength : 266
     */

    private int fileHead;
    private int q;
    private ExtraBean extra;
    private int fileSize;
    private String hash;
    private String mvhash;
    private String error;
    private String topic_remark;
    private String url;
    private int bitRate;
    private String choricSinger;
    private String songName;
    private String req_hash;
    private String singerHead;
    private int privilege;
    private int status;
    private int stype;
    private int ctype;
    private String singerName;
    private String fileName;
    private int singerId;
    private String topic_url;
    private String intro;
    private int errcode;
    private String extName;
    private String imgUrl;
    private int timeLength;

    public int getFileHead() {
        return fileHead;
    }

    public void setFileHead(int fileHead) {
        this.fileHead = fileHead;
    }

    public int getQ() {
        return q;
    }

    public void setQ(int q) {
        this.q = q;
    }

    public ExtraBean getExtra() {
        return extra;
    }

    public void setExtra(ExtraBean extra) {
        this.extra = extra;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getMvhash() {
        return mvhash;
    }

    public void setMvhash(String mvhash) {
        this.mvhash = mvhash;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getTopic_remark() {
        return topic_remark;
    }

    public void setTopic_remark(String topic_remark) {
        this.topic_remark = topic_remark;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public String getChoricSinger() {
        return choricSinger;
    }

    public void setChoricSinger(String choricSinger) {
        this.choricSinger = choricSinger;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getReq_hash() {
        return req_hash;
    }

    public void setReq_hash(String req_hash) {
        this.req_hash = req_hash;
    }

    public String getSingerHead() {
        return singerHead;
    }

    public void setSingerHead(String singerHead) {
        this.singerHead = singerHead;
    }

    public int getPrivilege() {
        return privilege;
    }

    public void setPrivilege(int privilege) {
        this.privilege = privilege;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStype() {
        return stype;
    }

    public void setStype(int stype) {
        this.stype = stype;
    }

    public int getCtype() {
        return ctype;
    }

    public void setCtype(int ctype) {
        this.ctype = ctype;
    }

    public String getSingerName() {
        return singerName;
    }

    public void setSingerName(String singerName) {
        this.singerName = singerName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getSingerId() {
        return singerId;
    }

    public void setSingerId(int singerId) {
        this.singerId = singerId;
    }

    public String getTopic_url() {
        return topic_url;
    }

    public void setTopic_url(String topic_url) {
        this.topic_url = topic_url;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }

    public static class ExtraBean {
        /**
         * 320filesize : 0
         * sqfilesize : 0
         * sqhash :
         * 128hash : 0FBCE9388DE01C661965FBEC403804CA
         * 320hash :
         * 128filesize : 4266906
         */


        private int value320filesize;
        private int sqfilesize;
        private String sqhash;

        private String value128hash;

        private String value320hash;

        private int value128filesize;

        public int getValue320filesize() {
            return value320filesize;
        }

        public void setValue320filesize(int value320filesize) {
            this.value320filesize = value320filesize;
        }

        public int getSqfilesize() {
            return sqfilesize;
        }

        public void setSqfilesize(int sqfilesize) {
            this.sqfilesize = sqfilesize;
        }

        public String getSqhash() {
            return sqhash;
        }

        public void setSqhash(String sqhash) {
            this.sqhash = sqhash;
        }

        public String getValue128hash() {
            return value128hash;
        }

        public void setValue128hash(String value128hash) {
            this.value128hash = value128hash;
        }

        public String getValue320hash() {
            return value320hash;
        }

        public void setValue320hash(String value320hash) {
            this.value320hash = value320hash;
        }

        public int getValue128filesize() {
            return value128filesize;
        }

        public void setValue128filesize(int value128filesize) {
            this.value128filesize = value128filesize;
        }
    }
}
