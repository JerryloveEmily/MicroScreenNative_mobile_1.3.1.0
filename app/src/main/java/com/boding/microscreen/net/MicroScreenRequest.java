package com.boding.microscreen.net;

import com.android.volley.Request;
import com.boding.microscreen.model.ActivitiesInfo;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * 各个业务的网络请求
 * Created by Jerry on 2015/5/4.
 */
public class MicroScreenRequest {

    /**
     * 获取活动列表数据
     * @param url
     * @param listener
     * @return
     */
    public static Request getActivitiesInfos(String url, ResponseListener listener){
        return new GetObjectRequest(url,
                new TypeToken<ArrayList<ActivitiesInfo>>(){}.getType(),
                listener);
    }
}
