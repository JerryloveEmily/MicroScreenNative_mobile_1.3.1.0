package com.boding.microscreen.model;

import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * 模块信息
 * Created by Administrator on 2015/5/18.
 */
public class ModelInfo implements Serializable {
    private String moduleName;
    private String moduleType;
    private int isDefault;
    private String url;
    private String iconUrl;
    private int icon;
    private int iconPress;     // 选中的图标
    private int index;

    public int getIconPress() {
        return iconPress;
    }

    public void setIconPress(int iconPress) {
        this.iconPress = iconPress;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private Class<? extends Fragment> fragment;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Class<? extends Fragment> getFragment() {
        return fragment;
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        this.fragment = fragment;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public int getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(int isDefault) {
        this.isDefault = isDefault;
    }

    public String getModuleType() {
        return moduleType;
    }

    public void setModuleType(String moduleType) {
        this.moduleType = moduleType;
    }

    @Override
    public String toString() {
        return "ModelInfo{" +
                "moduleName='" + moduleName + '\'' +
                ", moduleType='" + moduleType + '\'' +
                ", isDefault=" + isDefault +
                ", url='" + url + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                ", icon=" + icon +
                ", index=" + index +
                ", fragment=" + fragment +
                '}';
    }
}
