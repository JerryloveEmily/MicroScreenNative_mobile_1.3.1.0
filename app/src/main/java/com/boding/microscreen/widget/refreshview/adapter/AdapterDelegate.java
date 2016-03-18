package com.boding.microscreen.widget.refreshview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * 不同ViewHolder的代理接口
 *
 * Created by Administrator on 2015/8/14.
 */
public interface AdapterDelegate<T> {

    int getItemViewType();

    boolean isForViewType(@NonNull List<T> items, int position);

    @NonNull
    RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull T item, int position);
}
