package com.boding.microscreen.widget.quickadapter.recycler;

public interface MultiItemRcvTypeSupport<T> {

    int getLayoutId(int viewType);

    int getItemViewType(int position, T t);

}