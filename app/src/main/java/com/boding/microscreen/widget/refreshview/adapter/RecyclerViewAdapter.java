package com.boding.microscreen.widget.refreshview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/7/17.
 */
public class RecyclerViewAdapter<T> extends RecyclerView.Adapter {

    private List<T> mDatas;
    private AdapterDelegatesManager<T> delegatesManager;

    @SafeVarargs
    public RecyclerViewAdapter(List<T> datas, @NonNull AbsAdapterDelegate<T>... delegates) {
        mDatas = (datas == null) ? new ArrayList<T>() : new ArrayList<>(datas);
        delegatesManager = new AdapterDelegatesManager<>();
        for (AbsAdapterDelegate<T> delegate :
                delegates) {
            delegatesManager.addDelegate(delegate);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return delegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public int getItemViewType(int position) {
        return delegatesManager.getItemViewType(mDatas, position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        T item = mDatas.get(position);
        delegatesManager.onBindViewHolder(viewHolder, item, position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    /**
     * 添加一组数据
     *
     * @param elements
     */
    public void addDatas(List<T> elements) {
        mDatas.addAll(elements);
        notifyDataSetChanged();
    }

    /**
     * 添加一条数据
     *
     * @param item
     */
    public void addElement(T item, int index) {
        if (index >= 0 && index <= mDatas.size()) {
            mDatas.add(item);
            notifyItemInserted(index);
        }
    }

    /**
     * 添加一条数据
     *
     * @param item
     */
    public void addElement(T item) {
        mDatas.add(item);
        notifyDataSetChanged();
    }

    public List<T> getDatas(){
        return mDatas;
    }

    public void replaceElement(T item, int index){
        if (index >= 0 && index <= mDatas.size()) {
            mDatas.set(index, item);
            notifyItemChanged(index);
        }
    }

    public void removeElement(int index){
        if (index >= 0 && index <= mDatas.size()) {
            mDatas.remove(index);
            notifyDataSetChanged();
        }
    }
}
