package com.boding.microscreen.model;

import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;

import java.io.Serializable;

public class TabInfo implements Serializable {
	
	private String mTabTitle;			// tab标题
	private int mTabIcon;				// tab图标
	private Drawable mTabIconDrawable; 	// tab图标
	private String mType;				// tab的类型
	private String mTag;				//
    private Class<? extends Fragment> fragment;

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    public String getTag() {
		return mTag;
	}
	public void setTag(String tag) {
		this.mTag = tag;
	}
	public Drawable getTabIconDrawable() {
		return mTabIconDrawable;
	}
	public void setTabIconDrawable(Drawable tabIconDrawable) {
		this.mTabIconDrawable = tabIconDrawable;
	}
	public String getTabTitle() {
		return mTabTitle;
	}
	public void setTabTitle(String tabTitle) {
		this.mTabTitle = tabTitle;
	}
	public int getTabIcon() {
		return mTabIcon;
	}
	public void setTabIcon(int tabIcon) {
		this.mTabIcon = tabIcon;
	}
	public String getType() {
		return mType;
	}
	public void setType(String type) {
		this.mType = type;
	}

}
