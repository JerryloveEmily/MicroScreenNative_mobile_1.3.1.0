package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.model.MessageData;
import com.boding.microscreen.util.MyLog;

import org.json.JSONObject;

/**
 * 请求留言墙界面信息
 * Created by Administrator on 2015/5/22.
 */
public class Request4Message extends Request<MessageData> {

    private Response.Listener<MessageData> listener;

    public Request4Message(String url, Response.Listener<MessageData> listener, Response.ErrorListener errorListener) {
        super(Method.GET, url, errorListener);
        this.listener = listener;
    }

    @Override
    protected Response<MessageData> parseNetworkResponse(NetworkResponse response) {

        String jsonString = "{\n" +
                "    \"status\": 1,\n" +
                "    \"result\": {\n" +
                "        \"sponIcon\": \"http://pic.33.la/20130523tpxh/6494.jpg\",\n" +
                "        \"sponLogo\": \"http://pic.33.la/20130523tpxh/6494.jpg\",\n" +
                "        \"sponName\": \"aaa\",\n" +
                "        \"theme\": \"bbb\",\n" +
                "        \"qrCode\": \"http://pic.33.la/20130523tpxh/6494.jpg\",\n" +
                "        \"isScroll\": 1\n" +
                "   }\n" +
                "}";

        String result = new String(response.data);
        MyLog.e("Request4Message", "jsonString: " + result);
        try {
            JSONObject jsonObj = new JSONObject(result);
            int status = jsonObj.optInt("status");
            if (status == 1) {
                JSONObject dataObj = jsonObj.optJSONObject("result");
                /*MessageData baseDatas = new MessageData();
                baseDatas.setSponIcon(dataObj.optString("sponIcon"));
                baseDatas.setSponLogo(dataObj.optString("sponLogo"));
                baseDatas.setSponName(dataObj.optString("sponName"));
                baseDatas.setTheme(dataObj.optString("theme"));
                baseDatas.setQrCode(dataObj.optString("qrCode"));
                baseDatas.setIsScroll(dataObj.optInt("isScroll"));*/
                MessageData baseDatas = (MessageData) JSONParser.toObject(dataObj.toString(), MessageData.class);
                return Response.success(baseDatas, HttpHeaderParser.parseCacheHeaders(response));
            } else {
                return Response.error(new VolleyError("加载出错"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new VolleyError(e.getMessage()));
        }
    }

    @Override
    protected void deliverResponse(MessageData response) {
        listener.onResponse(response);
    }
}
