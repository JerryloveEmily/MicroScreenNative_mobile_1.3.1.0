package com.boding.microscreen.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速适配器基础类
 * @author Administrator
 *
 * @param <T> 实体Bean
 * @param <H> ViewHolder类
 */
public abstract class QuickBaseAdapter<T, H extends BaseViewHolder> extends BaseAdapter {
	
	protected final Context context;
	protected int layoutResId;
	protected final List<T> data;
	protected boolean displayIndeterminateProgress = false;
	protected MultiItemTypeSupport<T> multiItemTypeSupport;
	
	public QuickBaseAdapter(Context context,int layoutResId){
		this(context, layoutResId, null);
	}
	
	public QuickBaseAdapter(Context context,int layoutResId, List<T> data){
		this.context = context;
		this.layoutResId = layoutResId;
		this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
	}
	
	public QuickBaseAdapter(Context context, int layoutResId, ArrayList<T> data,
			MultiItemTypeSupport<T> multiItemTypeSupport){
		this.context = context;
		this.layoutResId = layoutResId;
		this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
		this.multiItemTypeSupport = multiItemTypeSupport;
	}

	@Override
	public int getCount() {
		int extra = displayIndeterminateProgress?1:0;
		return data.size() + extra;
	}

	@Override
	public T getItem(int position) {
		if(position >= data.size()) return null;
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getItemViewType(int position) {
		if (displayIndeterminateProgress) {
			if (multiItemTypeSupport != null) {
				return position >= data.size() ? 0 : multiItemTypeSupport.getItemViewType(position, data.get(position));
			}
		}else {
			if (multiItemTypeSupport != null){
				return multiItemTypeSupport.getItemViewType(position, data.get(position));
			}
		}
		return position >= data.size() ? 0 : 1;
	}

	@Override
	public int getViewTypeCount() {
		if (multiItemTypeSupport != null) {
			return multiItemTypeSupport.getViewTypeCount() + 1;
		}
		return 2;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (getItemViewType(position) == 0) {
			return createIndeterminateProgressView(convertView, parent);
		}
		// 获得ViewHolder对象
		final H holder = getBaseViewHolder(position, convertView, parent);
		// 获得Item的Bean
		T item = getItem(position);
		holder.setAssociatedObject(item);
		// 给每个item设置对应的holder以及item的其它处理
		convert(holder, item, position);
		// 返回每个item的view对象即convertView对象
		return holder.getView();
	}
	
	@Override
	public boolean isEnabled(int position) {
		return position < data.size();
	}
	
	private View createIndeterminateProgressView(View convertView, ViewGroup parent) {
		if (convertView == null) {
			FrameLayout container = new FrameLayout(context);
			container.setForegroundGravity(Gravity.CENTER);
			ProgressBar pb = new ProgressBar(context);
			container.addView(pb);
			convertView = container;
		}
		return convertView;
	}
	
	public void showIndeterminateProgress(boolean display) {
		if (display == displayIndeterminateProgress)
			return;
		displayIndeterminateProgress = display;
		notifyDataSetChanged();
	}

	/**
	 * Implement this method and use the helper to adapt the view to the given
	 * item.
	 * 
	 * @param holder
	 *            A fully initialized helper.
	 * @param item
	 *            The item that needs to be displayed.
	 */
	protected abstract void convert(H holder, T item, int position);
	
	protected abstract H getBaseViewHolder(int position, View convertView,
			ViewGroup parent);
	
	/**
	 * 添加一个元素
	 * @param elem
	 */
	public void add(T elem){
		data.add(elem);
		notifyDataSetChanged();
	}
	
	/**
	 * 添加新的list元素到原有的list中
	 * @param elem
	 */
	public void addAll(List<T> elem){
		data.addAll(elem);
		notifyDataSetChanged();
	}
	
	/**
	 * 删除某元素
	 * @param elem
	 */
	public void remove(T elem){
		data.remove(elem);
		notifyDataSetChanged();
	}
	
	/**
	 * 给指定位置添加元素
	 * @param index
	 * @param elem
	 */
	public void set(int index, T elem){
		data.set(index, elem);
		notifyDataSetChanged();
	}
	
	/**
	 * 给指定元素设置成新的元素
	 * @param oldElem
	 * @param newElem
	 */
	public void set(T oldElem, T newElem){
		set(data.indexOf(oldElem), newElem);
	}
	
	/**
	 * 替换所有的list数据
	 * @param elem
	 */
	public void replaceAll(List<T> elem){
		data.clear();
		data.addAll(elem);
		notifyDataSetChanged();
	}
	
	/**
	 * 判断是否包含该元素
	 * @param elem
	 * @return
	 */
	public boolean container(T elem){
		return data.contains(elem);
	}
	
	/** clear list data */
	public void clear(){
		data.clear();
		notifyDataSetChanged();
	}
}
