package com.boding.microscreen.net;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.boding.microscreen.model.MessageInfo;
import com.boding.microscreen.model.MessageItem;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 请求解析留言墙列表返回信息
 * Created by Administrator on 2015/4/21.
 */
public class Request4MessageInfo extends Request<MessageInfo> {

    private Response.Listener<MessageInfo> mListener;

    public Request4MessageInfo(String url, Response.Listener<MessageInfo> listener,
                               Response.ErrorListener errorListener){
        super(Request.Method.GET, url, errorListener);
        mListener = listener;
    }

    @Override
    protected Response<MessageInfo> parseNetworkResponse(NetworkResponse response) {

        try {
//            String result = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            if(200 == response.statusCode){
                String result = new String(response.data);
                JSONObject jsonObj = new JSONObject(result);
                int status = jsonObj.optInt("status");
                if (status == 1){
                    JSONObject dataObj = jsonObj.optJSONObject("result");
                    MessageInfo messageInfo = new MessageInfo();
                    messageInfo.setIsCheck(dataObj.optInt("isCheck"));
                    messageInfo.setMessageNums(dataObj.optInt("messageNums"));
                    String msgListString = dataObj.optJSONArray("messageList").toString();
                    messageInfo.setMessageList((ArrayList<MessageItem>) JSONParser.toObject(
                            msgListString,
                            new TypeToken<ArrayList<MessageItem>>() {
                            }.getType()));
                    return Response.success(messageInfo,
                        HttpHeaderParser.parseCacheHeaders(response));
                } else {
                    return Response.error(new ParseError(new Exception("加载失败")));
                }
            }else {
                return Response.error(new ParseError(new Exception("加载失败: " + response.statusCode)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Response.error(new ParseError(new Exception("加载失败")));
        }
    }

    @Override
    protected void deliverResponse(MessageInfo response) {
        mListener.onResponse(response);
    }
}
