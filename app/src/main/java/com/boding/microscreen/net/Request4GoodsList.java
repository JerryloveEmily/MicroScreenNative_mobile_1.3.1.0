package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.Goods;
import com.boding.microscreen.model.GoodsGroup;
import com.boding.microscreen.model.GoodsInfo;
import com.boding.microscreen.util.MyLog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/19.
 */
public class Request4GoodsList extends Request<ArrayList<Goods>> {
    private static final String TAG = "Request4GoodsList";
    private Response.Listener<ArrayList<Goods>> listener;

    public Request4GoodsList(Response.Listener<ArrayList<Goods>> listener, Response.ErrorListener errorListener,String url) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<ArrayList<Goods>> parseNetworkResponse(NetworkResponse response) {

        try {

            MyLog.e("R4G",new String(response.data));
            //JSONObject jsonObj = new JSONObject(new String(response.data).replace("\\/","/"));
            JSONObject jsonObj = new JSONObject(new String(response.data));

            int status = jsonObj.optInt("status");
            MyLog.e("R4D","status="+status);
            if (status == 1) {

                JSONObject dataObj = jsonObj.optJSONObject("result");

                String goodsListStr = dataObj.optJSONArray("goods").toString();

                ArrayList<Goods> gList =(ArrayList <Goods>) JSONParser.toObject(goodsListStr,
                        new TypeToken<ArrayList<Goods>>() {
                        }.getType());

                return Response.success(gList,
                        HttpHeaderParser.parseCacheHeaders(response));
            }else {
                MyLog.e("R4G","gan ni mei");
                return Response.error(new VolleyError("加载出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(ArrayList<Goods> response) {
        listener.onResponse(response);
    }
}
