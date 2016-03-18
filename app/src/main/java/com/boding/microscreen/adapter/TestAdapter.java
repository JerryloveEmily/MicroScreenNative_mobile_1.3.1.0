package com.boding.microscreen.adapter;

import android.widget.ListAdapter;

import com.boding.microscreen.model.TabInfo;

import java.util.List;

/**
 * Created by Administrator on 2015/4/24.
 */
public interface TestAdapter extends ListAdapter {

    void appendItems(List<TabInfo> newItems);

    void setItems(List<TabInfo> moreItems);
}
