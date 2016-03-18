package com.boding.microscreen.model;

import java.io.Serializable;

/**
 * 视频信息
 * Created by Administrator on 2015/5/18.
 */
public class VideoInfo implements Serializable {
    private String id;
    private String name;
    private int serialNum;
    private String thumbnailUrl;
    private int isLooper;       // 1表示循环播放，0表示只播放一次
    private String videoUrl;
    private String updateTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(int serialNum) {
        this.serialNum = serialNum;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public int getIsLooper() {
        return isLooper;
    }

    public void setIsLooper(int isLooper) {
        this.isLooper = isLooper;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "VideoInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", serialNum=" + serialNum +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                ", isLooper=" + isLooper +
                ", videoUrl='" + videoUrl + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
