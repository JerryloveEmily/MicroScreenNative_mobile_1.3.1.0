package com.boding.microscreen.model;

import java.util.Comparator;

/**
 * Created by Administrator on 2015/5/22.
 */
public class ModelInfoComparator implements Comparator<ModelInfo>{
    @Override
    public int compare(ModelInfo t1, ModelInfo t2) {
        int index = t1.getIndex();
        int index2 = t1.getIndex();

        if (index > index2){
            return -1;
        }

        return 1;
    }
}
