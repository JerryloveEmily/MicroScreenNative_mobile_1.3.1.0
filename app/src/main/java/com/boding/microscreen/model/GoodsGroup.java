package com.boding.microscreen.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * detail:商品列表
 * Created by libq
 * 2015/5/26#11:13
 */
public class GoodsGroup implements Serializable {
    private String id;
    private String name;

    public GoodsGroup(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "GoodsGroup{" +
                "id='" + id + '\'' +
                ", name=" + name +
                '}';
    }
}
