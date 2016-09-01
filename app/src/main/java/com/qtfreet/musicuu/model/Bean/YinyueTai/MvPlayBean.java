package com.qtfreet.musicuu.model.Bean.YinyueTai;

import java.util.List;

/**
 * Created by qtfreet00 on 2016/9/1.
 */
public class MvPlayBean {

    /**
     * code : 0
     * msg : SUCCESS
     * now : 1472711109059
     * data : {"specialType":"normal","status":200,"hdUrl":"http://hc.yinyuetai.com/uploads/videos/common/3F0C0156D46EC81677FFD7F2A4C9B062.flv?sc=5d8e92c35c4a2955&br=773&rd=Android","videoSize":17072645,"url":"http://dd.yinyuetai.com/uploads/videos/common/B8F70156D473A892DB87D95898710256.mp4?sc=f04329f7d7ce18a6&br=573&rd=Android","videoTypes":[1,2,3,4],"uhdUrl":"http://hd.yinyuetai.com/uploads/videos/common/57170156D473A8A812BC7EE2B2A6D8ED.flv?sc=37b45958f5419577&br=1095&rd=Android","shdUrl":"http://he.yinyuetai.com/uploads/videos/common/8AEB0156D473A8A1F99CA4357C24E070.flv?sc=2ffe3d4e4ee818c6&br=3130&rd=Android","shdVideoSize":93148553,"title":"Side To Side","hdVideoSize":23018489,"duration":238,"videoId":2664421,"ad":false,"uhdVideoSize":32597184}
     * cost : 0
     */

    private String code;
    private String msg;
    private long now;
    /**
     * specialType : normal
     * status : 200
     * hdUrl : http://hc.yinyuetai.com/uploads/videos/common/3F0C0156D46EC81677FFD7F2A4C9B062.flv?sc=5d8e92c35c4a2955&br=773&rd=Android
     * videoSize : 17072645
     * url : http://dd.yinyuetai.com/uploads/videos/common/B8F70156D473A892DB87D95898710256.mp4?sc=f04329f7d7ce18a6&br=573&rd=Android
     * videoTypes : [1,2,3,4]
     * uhdUrl : http://hd.yinyuetai.com/uploads/videos/common/57170156D473A8A812BC7EE2B2A6D8ED.flv?sc=37b45958f5419577&br=1095&rd=Android
     * shdUrl : http://he.yinyuetai.com/uploads/videos/common/8AEB0156D473A8A1F99CA4357C24E070.flv?sc=2ffe3d4e4ee818c6&br=3130&rd=Android
     * shdVideoSize : 93148553
     * title : Side To Side
     * hdVideoSize : 23018489
     * duration : 238
     * videoId : 2664421
     * ad : false
     * uhdVideoSize : 32597184
     */

    private DataBean data;
    private int cost;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public static class DataBean {
        private String specialType;
        private int status;
        private String hdUrl;
        private int videoSize;
        private String url;
        private String uhdUrl;
        private String shdUrl;
        private int shdVideoSize;
        private String title;
        private int hdVideoSize;
        private int duration;
        private int videoId;
        private boolean ad;
        private int uhdVideoSize;
        private List<Integer> videoTypes;

        public String getSpecialType() {
            return specialType;
        }

        public void setSpecialType(String specialType) {
            this.specialType = specialType;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getHdUrl() {
            return hdUrl;
        }

        public void setHdUrl(String hdUrl) {
            this.hdUrl = hdUrl;
        }

        public int getVideoSize() {
            return videoSize;
        }

        public void setVideoSize(int videoSize) {
            this.videoSize = videoSize;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUhdUrl() {
            return uhdUrl;
        }

        public void setUhdUrl(String uhdUrl) {
            this.uhdUrl = uhdUrl;
        }

        public String getShdUrl() {
            return shdUrl;
        }

        public void setShdUrl(String shdUrl) {
            this.shdUrl = shdUrl;
        }

        public int getShdVideoSize() {
            return shdVideoSize;
        }

        public void setShdVideoSize(int shdVideoSize) {
            this.shdVideoSize = shdVideoSize;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getHdVideoSize() {
            return hdVideoSize;
        }

        public void setHdVideoSize(int hdVideoSize) {
            this.hdVideoSize = hdVideoSize;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getVideoId() {
            return videoId;
        }

        public void setVideoId(int videoId) {
            this.videoId = videoId;
        }

        public boolean isAd() {
            return ad;
        }

        public void setAd(boolean ad) {
            this.ad = ad;
        }

        public int getUhdVideoSize() {
            return uhdVideoSize;
        }

        public void setUhdVideoSize(int uhdVideoSize) {
            this.uhdVideoSize = uhdVideoSize;
        }

        public List<Integer> getVideoTypes() {
            return videoTypes;
        }

        public void setVideoTypes(List<Integer> videoTypes) {
            this.videoTypes = videoTypes;
        }
    }
}
