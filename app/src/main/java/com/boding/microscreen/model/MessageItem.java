package com.boding.microscreen.model;

import java.io.Serializable;

/**
 * 留言墙信息列表item
 *
 * @author Administrator
 */
public class MessageItem implements Serializable {

    private String name;        // 发布消息者
    private String iconUrl;        // 发布消息者的头像地址
    private String time;        // 消息发布时间
    private String onwalltime;  // 消息审核时间
    private String contentType; // 消息类型：txt-文本 ，img –图片，audio – 音频
    private String content;        // 消息内容

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContentType() {
        return contentType;
    }


    public String getOnwalltime() {
        return onwalltime;
    }

    public void setOnwalltime(String onwalltime) {
        this.onwalltime = onwalltime;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "MessageItem{" +
                "name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", time='" + time + '\'' +
                ", onwalltime='" + onwalltime + '\'' +
                ", contentType='" + contentType + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
