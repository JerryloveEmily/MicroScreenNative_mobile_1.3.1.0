package com.boding.microscreen.widget.refreshview.adapter;

/**
 *抽象接口实现类
 *
 * Created by Administrator on 2015/8/14.
 */
public abstract class AbsAdapterDelegate<T> implements AdapterDelegate<T>{

    protected int viewType;

    public AbsAdapterDelegate(int viewType){
        this.viewType = viewType;
    }

    @Override
    public int getItemViewType() {
        return viewType;
    }
}
