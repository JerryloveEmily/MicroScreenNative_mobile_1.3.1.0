package com.boding.microscreen.adapter;

/**
 * 支持多种Item布局类型接口
 * @author Administrator
 *
 * @param <T>
 */
public interface MultiItemTypeSupport<T> {
	
	/**
	 * 获取布局资源的Id
	 * @param position	item的位置
	 * @param t  		实体Bean
	 * @return
	 */
	int getLayoutId(int position, T t);
	
	/**
	 * 获取Item视图的类型
	 * @param position	item的位置
	 * @param t			实体Bean
	 * @return
	 */
	int getItemViewType(int position, T t);
	
	/**
	 * 获取Item视图类型的个数
	 * @return
	 */
	int getViewTypeCount();
}
