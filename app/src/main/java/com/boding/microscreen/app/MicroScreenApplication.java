package com.boding.microscreen.app;

import android.app.Application;
import android.content.Context;

import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.util.JLog;
import com.boding.microscreen.util.MyLog;

/**
 * Application类
 * Created by Administrator on 2015/4/3.
 */
public class MicroScreenApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        // Crash后的处理
//        CrashHandler.getInstance().init(this);
        super.onCreate();
        mContext = getApplicationContext();
        // 初始化网络请求队列
        RequestManager.initialize(mContext);
        // 初始化fresco框架
//        Fresco.initialize(mContext);
        // 是否打印日志
        MyLog.DEBUG = true;
        JLog.isAllowDebug = true;
    }

    public static Context getContext(){
        return mContext;
    }

}
