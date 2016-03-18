package com.boding.microscreen.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 基础数据
 * Created by Administrator on 2015/5/18.
 */
public class BaseDatas implements Serializable{
    /*private String sponIcon;// 主办方个人单位logo地址
    private String sponLogo;// 自定义logo地址
    private String sponName;// 主办方名称
    private String theme;
    private String qrCode;  // 二维码地址
    private int isScroll;   // 1表示留言墙开启滚动，0表示关闭滚动*/
    private String backgroundImage;         // 背景图片
    private MusicInfo backgroundMusic;            // 背景音乐
    private ArrayList<ModelInfo> showMenuBarModules;   // MenuBar模块
    private ArrayList<ModelInfo> promotionModules;  // 促销模块
    private ArrayList<ModelInfo> moreModules;  // 促销模块

    public ArrayList<ModelInfo> getShowMenuBarModules() {
        return showMenuBarModules;
    }

    public void setShowMenuBarModules(ArrayList<ModelInfo> showMenuBarModules) {
        this.showMenuBarModules = showMenuBarModules;
    }

    public ArrayList<ModelInfo> getMoreModules() {
        return moreModules;
    }

    public void setMoreModules(ArrayList<ModelInfo> moreModules) {
        this.moreModules = moreModules;
    }

    /*public String getSponIcon() {
        return sponIcon;
    }

    public void setSponIcon(String sponIcon) {
        this.sponIcon = sponIcon;
    }

    public String getSponLogo() {
        return sponLogo;
    }

    public void setSponLogo(String sponLogo) {
        this.sponLogo = sponLogo;
    }

    public String getSponName() {
        return sponName;
    }

    public void setSponName(String sponName) {
        this.sponName = sponName;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public int getIsScroll() {
        return isScroll;
    }

    public void setIsScroll(int isScroll) {
        this.isScroll = isScroll;
    }*/

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public MusicInfo getBackgroundMusic() {
        return backgroundMusic;
    }

    public void setBackgroundMusic(MusicInfo backgroundMusic) {
        this.backgroundMusic = backgroundMusic;
    }

    public ArrayList<ModelInfo> getPromotionModules() {
        return promotionModules;
    }

    public void setPromotionModules(ArrayList<ModelInfo> promotionModules) {
        this.promotionModules = promotionModules;
    }

    @Override
    public String toString() {
        return "BaseDatas{" +
                /*"sponIcon='" + sponIcon + '\'' +
                ", sponLogo='" + sponLogo + '\'' +
                ", sponName='" + sponName + '\'' +
                ", theme='" + theme + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", isScroll=" + isScroll +*/
                ", backgroundImage='" + backgroundImage + '\'' +
                ", backgroundMusic=" + backgroundMusic +
                ", showMenuBarModules=" + showMenuBarModules +
                ", promotionModules=" + promotionModules +
                ", moreModules=" + moreModules +
                '}';
    }
}
