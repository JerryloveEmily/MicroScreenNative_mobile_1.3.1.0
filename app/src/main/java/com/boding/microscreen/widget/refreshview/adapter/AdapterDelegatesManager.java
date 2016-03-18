package com.boding.microscreen.widget.refreshview.adapter;

import android.support.annotation.NonNull;
import android.support.v4.util.SparseArrayCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * 不同item布局的适配器代理组合管理类
 *
 * Created by Administrator on 2015/8/14.
 */
public class AdapterDelegatesManager<T> {

    /**
     * 根据viewType存储AdapterDeleage对象
     */
    SparseArrayCompat<AdapterDelegate<T>> delegates = new SparseArrayCompat();

    public AdapterDelegatesManager<T> addDelegate(@NonNull AdapterDelegate<T> delegate) {
        return addDelegate(delegate, false);
    }

    public AdapterDelegatesManager<T> addDelegate(@NonNull AdapterDelegate<T> delegate,
                                                  boolean allowReplacingDelegate) {

        int viewType = delegate.getItemViewType();
        if (!allowReplacingDelegate && delegates.get(viewType) != null) {
            throw new IllegalArgumentException(
                    "An AdapterDelegate is already registered for the viewType = " + viewType
                            + ". Already registered AdapterDelegate is " + delegates.get(viewType));
        }

        delegates.put(viewType, delegate);
        Log.e("AdapterDelegatesManager", "addDelegate_viewType:" + viewType);
        return this;
    }

    public AdapterDelegatesManager<T> removeDelegate(@NonNull AdapterDelegate<T> delegate) {

        AdapterDelegate<T> queried = delegates.get(delegate.getItemViewType());
        if (queried != null && queried == delegate) {
            delegates.remove(delegate.getItemViewType());
        }
        return this;
    }

    public AdapterDelegatesManager<T> removeDelegate(int viewType) {
        delegates.remove(viewType);
        return this;
    }

    public int getItemViewType(@NonNull List<T> items, int position) {

        int delegatesCount = delegates.size();
        for (int i = 0; i < delegatesCount; i++) {
            AdapterDelegate<T> delegate = delegates.valueAt(i);
            if (delegate.isForViewType(items, position)) {
                return delegate.getItemViewType();
            }
        }

        throw new IllegalArgumentException(
                "No AdapterDelegate added that matches position=" + position + " in data source");
    }

    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterDelegate<T> delegate = delegates.get(viewType);
        if (delegate == null) {
            throw new NullPointerException("没有对应viewType的AdapterDelegate " + viewType);
        }

        return delegate.onCreateViewHolder(parent, viewType);
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull T item,  int position) {

        AdapterDelegate<T> delegate = delegates.get(viewHolder.getItemViewType());
        if (delegate == null) {
            throw new NullPointerException(
                    "没有对应viewType的AdapterDelegate " + viewHolder.getItemViewType());
        }

        delegate.onBindViewHolder(viewHolder, item, position);
    }
}
