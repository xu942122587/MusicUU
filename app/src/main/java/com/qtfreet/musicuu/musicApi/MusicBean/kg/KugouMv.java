package com.qtfreet.musicuu.musicApi.MusicBean.kg;

/**
 * Created by qtfreet on 2017/2/7.
 */
public class KugouMv {

    /**
     * status : 1
     * singer : 徐薇
     * songname : 泡沫
     * track : 3
     * type : 2
     * mvdata : {"hd":{"hash":"3fc30c68a2d6fba77f8cd3c727e32ed1","filesize":37166942,"timelength":263059,"bitrate":1113478,"downurl":"http://fs.mv.web.kugou.com/201702071515/cb6415d6694adbacb966c8888b5e2197/G036/M0A/10/17/BJQEAFXxIY6AFv6GAjcfXoSeLhY625.mp4"},"sd":{"hash":"6848a2c1c538add01bda9fd10368197f","filesize":20745215,"timelength":263059,"bitrate":614081,"downurl":"http://fs.mv.web.kugou.com/201702071515/e9aa07d2a2e58821b86bdf01e44bfc74/G039/M00/1B/00/x4YBAFXxIY2AVKO5ATyL_xUi4TU451.mp4"},"sq":{"hash":"bcd3a1bddbb551ca98ef616ba6f06530","filesize":102971701,"timelength":263059,"bitrate":3131243,"downurl":"http://fs.mv.web.kugou.com/201702071515/ab75919375840153ab38fe7a503df02b/G030/M09/03/17/Xg0DAFXxIZeAYM6VBiM5NZsGHxs635.mp4"},"rq":{}}
     */

    private int status;
    private String singer;
    private String songname;
    private int track;
    private int type;
    private MvdataBean mvdata;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }

    public int getTrack() {
        return track;
    }

    public void setTrack(int track) {
        this.track = track;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MvdataBean getMvdata() {
        return mvdata;
    }

    public void setMvdata(MvdataBean mvdata) {
        this.mvdata = mvdata;
    }

    public static class MvdataBean {
        /**
         * hd : {"hash":"3fc30c68a2d6fba77f8cd3c727e32ed1","filesize":37166942,"timelength":263059,"bitrate":1113478,"downurl":"http://fs.mv.web.kugou.com/201702071515/cb6415d6694adbacb966c8888b5e2197/G036/M0A/10/17/BJQEAFXxIY6AFv6GAjcfXoSeLhY625.mp4"}
         * sd : {"hash":"6848a2c1c538add01bda9fd10368197f","filesize":20745215,"timelength":263059,"bitrate":614081,"downurl":"http://fs.mv.web.kugou.com/201702071515/e9aa07d2a2e58821b86bdf01e44bfc74/G039/M00/1B/00/x4YBAFXxIY2AVKO5ATyL_xUi4TU451.mp4"}
         * sq : {"hash":"bcd3a1bddbb551ca98ef616ba6f06530","filesize":102971701,"timelength":263059,"bitrate":3131243,"downurl":"http://fs.mv.web.kugou.com/201702071515/ab75919375840153ab38fe7a503df02b/G030/M09/03/17/Xg0DAFXxIZeAYM6VBiM5NZsGHxs635.mp4"}
         * rq : {}
         */

        private HdBean hd;
        private SdBean sd;
        private SqBean sq;
        private RqBean rq;

        public HdBean getHd() {
            return hd;
        }

        public void setHd(HdBean hd) {
            this.hd = hd;
        }

        public SdBean getSd() {
            return sd;
        }

        public void setSd(SdBean sd) {
            this.sd = sd;
        }

        public SqBean getSq() {
            return sq;
        }

        public void setSq(SqBean sq) {
            this.sq = sq;
        }

        public RqBean getRq() {
            return rq;
        }

        public void setRq(RqBean rq) {
            this.rq = rq;
        }

        public static class HdBean {
            /**
             * hash : 3fc30c68a2d6fba77f8cd3c727e32ed1
             * filesize : 37166942
             * timelength : 263059
             * bitrate : 1113478
             * downurl : http://fs.mv.web.kugou.com/201702071515/cb6415d6694adbacb966c8888b5e2197/G036/M0A/10/17/BJQEAFXxIY6AFv6GAjcfXoSeLhY625.mp4
             */

            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

        public static class SdBean {
            /**
             * hash : 6848a2c1c538add01bda9fd10368197f
             * filesize : 20745215
             * timelength : 263059
             * bitrate : 614081
             * downurl : http://fs.mv.web.kugou.com/201702071515/e9aa07d2a2e58821b86bdf01e44bfc74/G039/M00/1B/00/x4YBAFXxIY2AVKO5ATyL_xUi4TU451.mp4
             */

            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

        public static class SqBean {
            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }

        public static class RqBean {
            private String hash;
            private int filesize;
            private int timelength;
            private int bitrate;
            private String downurl;

            public String getHash() {
                return hash;
            }

            public void setHash(String hash) {
                this.hash = hash;
            }

            public int getFilesize() {
                return filesize;
            }

            public void setFilesize(int filesize) {
                this.filesize = filesize;
            }

            public int getTimelength() {
                return timelength;
            }

            public void setTimelength(int timelength) {
                this.timelength = timelength;
            }

            public int getBitrate() {
                return bitrate;
            }

            public void setBitrate(int bitrate) {
                this.bitrate = bitrate;
            }

            public String getDownurl() {
                return downurl;
            }

            public void setDownurl(String downurl) {
                this.downurl = downurl;
            }
        }
    }
}
