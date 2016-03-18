package com.boding.microscreen;

import android.test.InstrumentationTestCase;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.app.MicroScreenApplication;
import com.boding.microscreen.model.ActivitiesInfo;
import com.boding.microscreen.model.MessageItem;
import com.boding.microscreen.net.Request4ActivityInfo;
import com.boding.microscreen.net.Request4MessageInfo;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.util.MyLog;

import java.util.ArrayList;

/**
 * 网络请求单元测试
 * Created by Administrator on 2015/5/14.
 */
public class NetTest extends InstrumentationTestCase {

    public static final String TAG = "NetTest";

    /**
     * 测试请求下载活动列表数据
     * @throws Exception
     */
    public void testGetActivityInfos() throws Exception {
       /* RequestManager.initialize(MicroScreenApplication.getContext());
        String url = "http://jandan.net/?oxwlxojflwblxbsapi=get_post&include=comments&id=" + 61245;
        executeRequest(new Request4ActivityInfo(url, new Response.Listener<ArrayList<ActivitiesInfo>>() {
            @Override
            public void onResponse(ArrayList<ActivitiesInfo> response) {
                for (ActivitiesInfo ai: response){
                    MyLog.e("testGetActivityInfos", ai.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, error.getMessage());
            }
        }));*/
    }

    /**
     * 测试请求下载留言墙数据
     * @throws Exception
     */
    public void testGetMessageInfos() throws Exception {
     /*   RequestManager.initialize(MicroScreenApplication.getContext());
        String url = "http://jandan.net/?oxwlxojflwblxbsapi=get_post&include=comments&id=" + 61245;
        executeRequest(new Request4MessageInfo(url, new Response.Listener<ArrayList<MessageItem>>() {
            @Override
            public void onResponse(ArrayList<MessageItem> response) {
                for (MessageItem mi : response) {
                    MyLog.e("testGetActivityInfos", mi.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, error.getMessage());
            }
        }));*/
    }

    protected void executeRequest(Request<?> request){
        RequestManager.addRequest(request, this);
    }
}
