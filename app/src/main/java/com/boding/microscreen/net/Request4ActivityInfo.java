package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.model.ActivitiesInfo;
import com.boding.microscreen.model.ActivitiesItem;
import com.boding.microscreen.util.MyLog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 请求解析活动列表返回信息
 * Created by Administrator on 2015/4/21.
 */
public class Request4ActivityInfo extends Request<ActivitiesInfo> {
    public static final String TAG = "Request4ActivityInfo";
    private Response.Listener<ActivitiesInfo> mListener;

    public Request4ActivityInfo(String url, Response.Listener<ActivitiesInfo> listener,
                               Response.ErrorListener errorListener){
        super(Request.Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<ActivitiesInfo> parseNetworkResponse(NetworkResponse response) {

        try {
//            String result = new String(response.data, HttpHeaderParser.parseCharset(response.headers));

            /*String result = "{\n" +
                    "\"status\":1,\n" +
                    "\"result\":{\n" +
                    "\"messageNums\":4,\n" +
                    "\"activityMessages\":[\n" +
                    "{\n" +
                    "\"title\":\"活动标题1\",\n" +
                    "\"content\":\"活动内容描述信息11111\",\n" +
                    "\"image\":\"http://d.3987.com/ktlmp_140318/002.jpg\",\n" +
                    "\"time\":\"2015-05-13 20:20:10\"\n" +
                    "},\n" +
                    "{\n" +
                    "\"title\":\"活动标题2\",\n" +
                    "\"content\":\"活动内容描述信息22222\",\n" +
                    "\"image\":\"http://d.3987.com/ktlmp_140318/002.jpg\",\n" +
                    "\"time\":\"2015-05-14 10:50:20\"\n" +
                    "}\n" +
                    "]\n" +
                    "}\n" +
                    "}";*/
            String result = new String(response.data);
            MyLog.e(TAG, "jsonData: " + result);
            JSONObject jsonObj = new JSONObject(result);
            int status = jsonObj.optInt("status");
            if (status == 1){
                JSONObject dataObj = jsonObj.optJSONObject("result");
                ActivitiesInfo info = new ActivitiesInfo();
                info.setMessageNums(dataObj.optInt("messageNums"));
                String msgsString = dataObj.optJSONArray("activityMessages").toString();
                info.setActivityMessages((ArrayList<ActivitiesItem>)JSONParser.toObject(
                        msgsString,
                        new TypeToken<ArrayList<ActivitiesItem>>(){}.getType()));
                return Response.success(info,
                        HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new ParseError(new Exception("加载失败")));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(ActivitiesInfo response) {
        mListener.onResponse(response);
    }
}
