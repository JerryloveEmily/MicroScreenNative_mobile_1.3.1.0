package com.boding.microscreen.model;

import java.io.Serializable;

/**
 * 留言墙界面信息
 * Created by Administrator on 2015/5/22.
 */
public class MessageData implements Serializable{
    private String sponIcon;// 主办方个人单位logo地址
    private String sponLogo;// 自定义logo地址
    private String sponName;// 主办方名称
    private String theme;
    private String qrCode;  // 二维码地址
    private int isScroll;   // 1表示留言墙开启滚动，0表示关闭滚动
    private int isCheck;    // 1表示留言墙开启审核，0表示关闭审核

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public String getSponIcon() {
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
    }

    @Override
    public String toString() {
        return "MessageData{" +
                "sponIcon='" + sponIcon + '\'' +
                ", sponLogo='" + sponLogo + '\'' +
                ", sponName='" + sponName + '\'' +
                ", theme='" + theme + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", isScroll=" + isScroll +
                '}';
    }
}
