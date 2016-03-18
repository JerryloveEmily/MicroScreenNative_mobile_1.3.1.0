package com.boding.microscreen.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 商品信息
 * Created by Administrator on 2015/5/19.
 */
public class GoodsInfo implements Serializable{
    private String switcherWay;
    private int isAutoPlay;
    private String layoutStyle;
    private ArrayList<GoodsGroup> goodsGroup;

    public String getSwitcherWay() {
        return switcherWay;
    }

    public void setSwitcherWay(String switcherWay) {
        this.switcherWay = switcherWay;
    }

    public int getIsAutoPlay() {
        return isAutoPlay;
    }

    public void setIsAutoPlay(int isAutoPlay) {
        this.isAutoPlay = isAutoPlay;
    }

    public String getLayoutStyle() {
        return layoutStyle;
    }

    public void setLayoutStyle(String layoutStyle) {
        this.layoutStyle = layoutStyle;
    }

    public ArrayList<GoodsGroup> getGoodsGroup() {
        return goodsGroup;
    }

    public void setGoodsGroup(ArrayList<GoodsGroup> goodsGroup) {
        this.goodsGroup = goodsGroup;
    }

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "switcherWay='" + switcherWay + '\'' +
                ", isAutoPlay=" + isAutoPlay +
                ", layoutStyle='" + layoutStyle + '\'' +
                ", goodsGroup=" + goodsGroup +
                '}';
    }
}
