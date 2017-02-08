package com.qtfreet.musicuu.musicApi.MusicBean.kg;

/**
 * Created by qtfreet on 2017/2/7.
 */
public class KugouMp3Url {

    /**
     * fileHead : 100
     * q : 0
     * fileSize : 9999673
     * fileName : 邓紫棋 - 泡沫
     * status : 1
     * url : http://fs.pc.kugou.com/201702071504/922dc1cc7de4b828527743026ae920bd/G009/M07/11/13/qYYBAFUKnZyAXz_5AJiVOX_ICZY421.mp3
     * extName : mp3
     * bitRate : 320000
     * timeLength : 249
     */

    private int fileHead;
    private int q;
    private int fileSize;
    private String fileName;
    private int status;
    private String url;
    private String extName;
    private int bitRate;
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

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getExtName() {
        return extName;
    }

    public void setExtName(String extName) {
        this.extName = extName;
    }

    public int getBitRate() {
        return bitRate;
    }

    public void setBitRate(int bitRate) {
        this.bitRate = bitRate;
    }

    public int getTimeLength() {
        return timeLength;
    }

    public void setTimeLength(int timeLength) {
        this.timeLength = timeLength;
    }
}
