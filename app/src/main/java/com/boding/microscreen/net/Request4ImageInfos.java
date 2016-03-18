package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.model.ImageInfo;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jerry on 2015/5/18.
 */
public class Request4ImageInfos extends Request<ArrayList<ImageInfo>> {
    private Response.Listener<ArrayList<ImageInfo>> listener;
    public Request4ImageInfos(String url, Response.Listener<ArrayList<ImageInfo>> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<ImageInfo>> parseNetworkResponse(NetworkResponse response) {

        try {
            String result = new String(response.data);
            JSONObject jsonObj = new JSONObject(result);
            int status = jsonObj.optInt("status");
            if (status == 1) {
                JSONObject dataObj = jsonObj.optJSONObject("result");
                String imageListStr = dataObj.optJSONArray("imageList").toString();
                return Response.success((ArrayList<ImageInfo>)JSONParser.toObject(
                                imageListStr,
                        new TypeToken<ArrayList<ImageInfo>>(){}.getType()),
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
    protected void deliverResponse(ArrayList<ImageInfo> response) {
        listener.onResponse(response);
    }
}
