package com.boding.microscreen.app;

import com.boding.microscreen.model.ModelInfo;

import java.util.ArrayList;

/**
 * EventBus的事件类
 * Created by Administrator on 2015/5/26.
 */
public class Event {

    // 传递更多模块数据的事件
    public static class MoreMoelEvent{
        public ArrayList<ModelInfo> moreModels;
    }
}
