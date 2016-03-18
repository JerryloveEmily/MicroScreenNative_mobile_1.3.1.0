package com.boding.microscreen.widget.refreshview.adapter;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 *
 * Created by Administrator on 2015/8/14.
 */
public abstract class BaseViewHolderDelegate<T> extends AbsAdapterDelegate<T> {

    private int mLayoutId;
    private OnItemClickListener<T> mOnItemClickListener = null;

    public BaseViewHolderDelegate(int layoutId, int viewType) {
        super(viewType);
        this.mLayoutId = layoutId;
    }

    @Override
    public boolean isForViewType(@NonNull List<T> items, int position) {
        return onIsForViewType(items, position);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                mLayoutId, parent, false);
        return BaseViewHolder.get(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, @NonNull final T item, final int position) {
        BaseViewHolder bvh = (BaseViewHolder)viewHolder;
        onBindViewDatas(bvh, item, position);
        bvh.mItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mOnItemClickListener){
                    mOnItemClickListener.onItemClick(v, position, item);
                }
            }
        });
    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder{

        private View mItemView;
        private SparseArray<View> mViews;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.mItemView = itemView;
            mViews = new SparseArray<>();
            itemView.setTag(this);
        }

        @NonNull
        public static BaseViewHolder get(@NonNull View itemView){
            BaseViewHolder viewHolder = (BaseViewHolder) itemView.getTag();
            if (viewHolder == null ) {
                viewHolder = new BaseViewHolder(itemView);
            }
            return viewHolder;
        }

        /**
         * 获取布局视图对象
         * @param viewId
         * @param <T>
         * @return
         */
        public <T extends View>T getView(@IdRes int viewId){
            return findView(viewId);
        }

        @SuppressWarnings("unchecked")
        private <T extends View>T findView(int viewId){
            View view = mViews.get(viewId);
            if (view == null){
                view = mItemView.findViewById(viewId);
                mViews.put(viewId, view);
            }
            return (T)view;
        }
    }

    public interface OnItemClickListener<T> {
        void onItemClick(View view, int position, T item);
    }

    public void setOnItemClickListener(OnItemClickListener<T> listener) {
        this.mOnItemClickListener = listener;
    }
    protected abstract void onBindViewDatas(@NonNull BaseViewHolder viewHolder, @NonNull T item, int position);

    protected abstract boolean onIsForViewType(@NonNull List<T> items, int position);
}
