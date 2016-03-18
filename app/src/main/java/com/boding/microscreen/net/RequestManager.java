package com.boding.microscreen.net;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.boding.microscreen.util.MyLog;

/**
 * 请求任务管理
 * Created by Jerry on 2015/4/20.
 */
public class RequestManager {
    private static final String TAG = "RequestManager";
    private static RequestQueue mRequestQueue;

    private RequestManager(){}

    /**
     * 初始化请求队列
     * @param context
     */
    public static void initialize(Context context){
        if (mRequestQueue == null){
            synchronized (RequestManager.class){
                if (mRequestQueue == null){
                    mRequestQueue = Volley.newRequestQueue(context);
                }
            }
        }
    }

    /**
     * 新增请求任务到队列中
     * @param request
     * @param tag
     */
    public static void addRequest(Request<?> request, Object tag){
        if (mRequestQueue == null){
            throw new RuntimeException("请先使用\"RequestManager.initialize(context)\"初始化请求队列");
        }
        if (tag != null){
            request.setTag(tag);
        }

        MyLog.d(TAG, "addRequest: " + request.getUrl());
        mRequestQueue.add(request);
    }

    /**
     * 获取请求队列
     * @return
     */
    public static RequestQueue getRequestQueue(){
        if (mRequestQueue == null){
            throw new RuntimeException("请先使用\"RequestManager.initialize(context)\"初始化请求队列");
        }

        return mRequestQueue;
    }

    /**
     * 取消请求任务
     * @param tag
     */
    public static void cancelAll(Object tag){
        if (mRequestQueue == null){
            throw new RuntimeException("请先使用\"RequestManager.initialize(context)\"初始化请求队列");
        }
        mRequestQueue.cancelAll(tag);
    }
}
