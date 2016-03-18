package com.boding.microscreen.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 活动信息
 * Created by Administrator on 2015/4/20.
 */
public class ActivitiesInfo implements Serializable{
    private int messageNums; // 屏幕上单页显示的条数， 默认是4条
    private ArrayList<ActivitiesItem> activityMessages;

    public int getMessageNums() {
        return messageNums;
    }

    public void setMessageNums(int messageNums) {
        this.messageNums = messageNums;
    }

    public ArrayList<ActivitiesItem> getActivityMessages() {
        return activityMessages;
    }

    public void setActivityMessages(ArrayList<ActivitiesItem> activityMessages) {
        this.activityMessages = activityMessages;
    }

    @Override
    public String toString() {
        return "ActivitiesInfo{" +
                "messageNums=" + messageNums +
                ", activityMessages=" + activityMessages +
                '}';
    }
}
