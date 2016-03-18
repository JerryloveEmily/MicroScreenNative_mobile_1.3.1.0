package com.boding.microscreen.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 快速适配器
 * @author Administrator
 *
 * @param <T> 实体类Bean泛型
 */
public abstract class QuickAdapter<T> extends
        QuickBaseAdapter<T, BaseViewHolder> {
	
	public QuickAdapter(Context context, int layoutResId, List<T> data) {
		super(context, layoutResId, data);
	}

	public QuickAdapter(Context context, int layoutResId) {
		super(context, layoutResId);
	}

	public QuickAdapter(Context context, int layoutResId, ArrayList<T> data,
			MultiItemTypeSupport<T> multiItemTypeSupport) {
		super(context, layoutResId, data, multiItemTypeSupport);
		
	}

	/**
	 * 当ListView调用getView的时候调用获得ViewHolder对象
	 */
	@Override
	protected BaseViewHolder getBaseViewHolder(int position, View convertView,
			ViewGroup parent) {
		// 有多个布局的item
		if (multiItemTypeSupport != null) { 
			// 获取ViewHolder对象
			return BaseViewHolder.get(context, convertView,
                    multiItemTypeSupport.getLayoutId(position, data.get(position)),
                    parent, position);
		}else{
			return BaseViewHolder.get(context, convertView, layoutResId, parent, position);
		}
	}
}
