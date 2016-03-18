package com.boding.microscreen.net;

import com.android.volley.Response;

/**
 * 简化网络回调接口
 * Created by Administrator on 2015/5/4.
 */
public interface ResponseListener<T> extends Response.Listener<T>,Response.ErrorListener {
}
