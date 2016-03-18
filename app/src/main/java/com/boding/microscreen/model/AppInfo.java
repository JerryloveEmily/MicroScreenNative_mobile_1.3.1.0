package com.boding.microscreen.model;

import java.io.Serializable;

/**
 * App的信息
 * 
 * @author Administrator
 *
 */
public class AppInfo implements Serializable {
	private boolean mIsForce; // 是否强制更新
	private String mLatestVersion;
	private String mUrl;
	private String mDescripte;
	private boolean mIsUpdate; // 是否有更新
	
	public boolean isIsUpdate() {
		return mIsUpdate;
	}
	public void setIsUpdate(boolean isUpdate) {
		this.mIsUpdate = isUpdate;
	}
	public boolean isForce() {
		return mIsForce;
	}
	public void setIsForce(boolean isForce) {
		this.mIsForce = isForce;
	}
	public String getLatestVersion() {
		return mLatestVersion;
	}
	public void setLatestVersion(String latestVersion) {
		this.mLatestVersion = latestVersion;
	}
	public String getUrl() {
		return mUrl;
	}
	public void setUrl(String url) {
		this.mUrl = url;
	}
	public String getDescripte() {
		return mDescripte;
	}
	public void setDescripte(String descripte) {
		this.mDescripte = descripte;
	}
}
