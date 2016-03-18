package com.boding.microscreen.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2015/4/23.
 */
public abstract  class OtherAdapter<T> extends QuickAdapter<T>{

    public OtherAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public OtherAdapter(Context context, int layoutResId, List<T> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected final void convert(BaseViewHolder holder, T item, int position) {
        convert(holder, item, position, true);
    }

    protected abstract void convert(BaseViewHolder helper, T item, int position, boolean flag);
}
