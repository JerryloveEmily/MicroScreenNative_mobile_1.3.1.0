package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.GoodsGroup;
import com.boding.microscreen.model.GoodsInfo;
import com.boding.microscreen.util.MyLog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 请求商品分类列表
 * Created by libq on 2016/1/14.
 */
public class Request4GoodsGroup  extends Request<ArrayList<GoodsGroup>> {

    private Response.Listener<ArrayList<GoodsGroup>> listener;
    public Request4GoodsGroup(Response.ErrorListener errlistener,Response.Listener<ArrayList<GoodsGroup>> listener,String url) {
        super(Method.GET, url, errlistener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<GoodsGroup>> parseNetworkResponse(NetworkResponse response) {
        try {
            JSONObject jsonObj = new JSONObject(new String(response.data));
            MyLog.e("Request4GoodsGroup", new String(response.data));
            int status = jsonObj.optInt("status");


            if(status==1){
                JSONObject result = jsonObj.getJSONObject("result");
                JSONArray group = result.getJSONArray("goodsGroup");
                ArrayList<GoodsGroup> groups =(ArrayList<GoodsGroup>) JSONParser.toObject(group.toString(),
                        new TypeToken<ArrayList<GoodsGroup>>() {
                        }.getType());
                return Response.success(groups,HttpHeaderParser.parseCacheHeaders(response));
            }




        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void deliverResponse(ArrayList<GoodsGroup> response) {
        listener.onResponse(response);
    }
}
