package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.BaseDatas;
import com.boding.microscreen.model.ModelInfo;
import com.boding.microscreen.model.MusicInfo;
import com.boding.microscreen.util.JLog;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/5/18.
 */
public class Request4BaseDatas extends Request<BaseDatas> {

    private Response.Listener<BaseDatas> listener;

    public Request4BaseDatas(String url, Response.Listener<BaseDatas> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<BaseDatas> parseNetworkResponse(NetworkResponse response) {

        String result = new String(response.data);
        JLog.e("jsonString: " + result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            int status = jsonObj.optInt("status");
            if (status == 1) {
                JSONObject dataObj = jsonObj.optJSONObject("result");
                // 微屏幕
                int screenStatus = dataObj.optInt("screenStatus");
                if (2 == screenStatus){
                    BaseDatas baseDatas = new BaseDatas();
                    baseDatas.setBackgroundImage(dataObj.optString("backgroundImage"));
                    String bgMusic = dataObj.optJSONObject("backgroundMusic").toString();
                    baseDatas.setBackgroundMusic((MusicInfo) JSONParser.toObject(bgMusic, MusicInfo.class));

                    // 显示在MenuBar导航栏的模块
                    String showMenuBarModulesString = dataObj.optJSONArray("showMenuBarModules").toString();
                    baseDatas.setShowMenuBarModules((ArrayList<ModelInfo>) JSONParser.toObject(
                            showMenuBarModulesString, new TypeToken<ArrayList<ModelInfo>>() {
                            }.getType()));
                    // 显示在更多导航栏的模块
                    String moreModulesString = dataObj.optJSONArray("moreModules").toString();
                    baseDatas.setMoreModules((ArrayList<ModelInfo>) JSONParser.toObject(
                            moreModulesString, new TypeToken<ArrayList<ModelInfo>>() {
                            }.getType()));
                    // 显示在促销导航栏的模块
                    /*String promotionModulesString = dataObj.optJSONArray("promotionModules").toString();
                    baseDatas.setPromotionModules((ArrayList<ModelInfo>) JSONParser.toObject(
                            promotionModulesString, new TypeToken<ArrayList<ModelInfo>>() {
                    }.getType()
                    ));*/

                    return Response.success(baseDatas, HttpHeaderParser.parseCacheHeaders(response));
                }else if (1 == screenStatus){
                    return Response.error(new VolleyError(AppConstants.VALIDATE_CODE_OVER_INFO));
                }
            } else {
                return Response.error(new VolleyError("加载出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }
        return null;
    }

    @Override
    protected void deliverResponse(BaseDatas response) {
        listener.onResponse(response);
    }
}
