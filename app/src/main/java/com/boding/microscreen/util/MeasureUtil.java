package com.boding.microscreen.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Administrator on 2015/1/23.
 */
public class MeasureUtil {

    /**
     * 获取屏幕的大小
     * @param context
     * @return
     */
    public static Size getScreenSize(Context context){
        Size size = new Size();
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        size.width = metrics.widthPixels;
        size.height = metrics.heightPixels;
        return size;
    }

    public static class Size {
        public int width;
        public int height;
    }
}
