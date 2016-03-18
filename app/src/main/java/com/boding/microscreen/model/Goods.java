package com.boding.microscreen.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * detail:
 * Created by libq
 * 2015/5/18#13:59
 */

public class Goods implements Serializable{
    private String id;
    private String name;
    private String price;
    private String thumbnailUrl;//商品缩略图地址
    private String updateTime;//商品信息更新时间

    private ArrayList<String> imageUrls;//商品图片地址
    private String qrcode;//二维码图片地址
    private String description;//商品描述

    public Goods(){

    }
    public Goods(String id, String description, String name, String price, String thumbnailUrl, String updateTime, ArrayList<String> imageUrls, String qrCode) {
        this.id = id;
        this.description = description;
        this.name = name;
        this.price = price;
        this.thumbnailUrl = thumbnailUrl;
        this.updateTime = updateTime;
        this.imageUrls = imageUrls;
        this.qrcode = qrCode;
    }

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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getQrCode() {
        return qrcode;
    }

    public void setQrCode(String qrCode) {
        this.qrcode = qrCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
