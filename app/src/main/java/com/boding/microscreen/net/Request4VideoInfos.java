package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.model.VideoInfo;
import com.boding.microscreen.util.JLog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 视频列表请求
 * Created by Administrator on 2015/5/25.
 */
public class Request4VideoInfos extends Request<ArrayList<VideoInfo>>{


    private Response.Listener<ArrayList<VideoInfo>> listener;

    public Request4VideoInfos(String url,Response.Listener<ArrayList<VideoInfo>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<VideoInfo>> parseNetworkResponse(NetworkResponse response) {

        String result = new String(response.data);
        JLog.e("result: " + result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            int status = jsonObj.optInt("status");
            if (status == 1) {
                JSONObject dataObj = jsonObj.optJSONObject("result");
                String videoListStr = dataObj.optJSONArray("videos").toString();
                return Response.success((ArrayList<VideoInfo>)JSONParser.toObject(
                                videoListStr,
                                new TypeToken<ArrayList<VideoInfo>>(){}.getType()),
                        HttpHeaderParser.parseCacheHeaders(response));
            }else {
                return Response.error(new VolleyError("request error"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<VideoInfo> response) {
        listener.onResponse(response);
    }
}
