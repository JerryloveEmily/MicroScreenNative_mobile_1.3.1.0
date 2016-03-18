package com.boding.microscreen.model;

import java.io.Serializable;

/**��Ϣitem��
 * Created by Administrator on 2015/5/29.
 */
public class ActivitiesItem implements Serializable {

    private String title;
    private String content;
    private String image;
    private String time;

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    private int sort;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "ActivitiesItem{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", image='" + image + '\'' +
                ", time='" + time + '\'' +
                ", sort=" + sort +
                '}';
    }
}
