package com.boding.microscreen.ui.fragment;

import android.support.v4.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.util.ShowToast;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/4/20.
 */
public class BaseFragment extends Fragment implements Serializable{

    /**
     * 执行请求
     */
    protected void executeRequest(Request request){
        RequestManager.addRequest(request, this);
    }

    /**
     * 请求报错监听
     */
    protected Response.ErrorListener errorListener(){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ShowToast.Short(error.getMessage());
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RequestManager.cancelAll(this);
    }
}
