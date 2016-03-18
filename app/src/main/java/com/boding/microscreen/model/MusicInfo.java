package com.boding.microscreen.model;

import java.io.Serializable;

/**
 * 音频信息
 * Created by Administrator on 2015/5/18.
 */
public class MusicInfo implements Serializable {
    private int isOpen;     // 1表示开启, 0表示不开启
    private String musicUrl;

    public int getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(int isOpen) {
        this.isOpen = isOpen;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.musicUrl = musicUrl;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "isOpen=" + isOpen +
                ", musicUrl='" + musicUrl + '\'' +
                '}';
    }
}
