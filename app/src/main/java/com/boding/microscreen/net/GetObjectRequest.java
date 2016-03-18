package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

/**
 * Get方式网络请求
 * Created by Jerry on 2015/5/4.
 */
public class GetObjectRequest<T> extends Request<T> {

    private Gson mGson;
    private ResponseListener mListener;
    private Type mClazz;

    public GetObjectRequest(String url, Type type, ResponseListener listener) {
        super(Method.GET, url, listener);
        mListener = listener;
        mClazz = type;
        mGson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            T result;
//            String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            String jsonString = "{\n" +
                    "\"status\":\1,\n" +
                    "\"result\":{\n" +
                    "\"messageNums\":4,  // 屏幕上单页显示的条数， 默认是4条\n" +
                    "\"activityMessages\":[\n" +
                    "       {\n" +
                    "\"title\":\"活动标题\",\n" +
                    "\"content\":\"活动内容描述信息\",\n" +
                    "\"image\":\"http://www.xxxx.com/jklfsdjlfsdfdsdf.jpg\",  // 活动图片\n" +
                    "\"time\":\"2015-02-13 20:20:10\"\n" +
                    "},\n" +
                    "       {\n" +
                    "\"title\":\"活动标题\",\n" +
                    "\"content\":\"活动内容描述信息\",\n" +
                    "\"image\":\"http://www.xxxx.com/jklfsdjlfsdfdsdf.jpg\",  // 活动图片\n" +
                    "\"time\":\"2015-02-13 20:20:10\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n" +
                    "}";
            result = mGson.fromJson(jsonString, mClazz);
            return Response.success(result, HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            return Response.error(new VolleyError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}
