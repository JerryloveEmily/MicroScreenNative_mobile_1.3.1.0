package com.boding.microscreen.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 图片信息
 * Created by Administrator on 2015/5/18.
 */
public class ImageInfo implements Serializable {
    private String typeName;
    private String switcherWay;
    private int isAutoPlay;
    private ArrayList<String> urls;

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getSwitcherWay() {
        return switcherWay;
    }

    public void setSwitcherWay(String switcherWay) {
        this.switcherWay = switcherWay;
    }

    public int getIsAutoPlay() {
        return isAutoPlay;
    }

    public void setIsAutoPlay(int isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public ArrayList<String> getUrls() {
        return urls;
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
    }

    @Override
    public String toString() {
        return "ImageInfo{" +
                "typeName='" + typeName + '\'' +
                ", switcherWay='" + switcherWay + '\'' +
                ", isAutoPlay=" + isAutoPlay +
                ", urls=" + urls +
                '}';
    }
}
