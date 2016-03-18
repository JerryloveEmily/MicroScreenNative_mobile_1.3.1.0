package com.boding.microscreen.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boding.microscreen.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by libq on 2016/1/20.
 */
public class ImagePagerAdapter extends PagerAdapter {


    private LayoutInflater inflater;
    private ArrayList<View> childViews;
    public ImagePagerAdapter(ArrayList<String> imgUris,Context mActivity) {
        inflater =LayoutInflater.from(mActivity);
        childViews = new ArrayList<View>();
        if(imgUris!=null){
            for (String url :imgUris) {
                View v = (View)inflater.inflate(R.layout.layout_goods_img,null);
                ImageView iv = (ImageView) v.findViewById(R.id.iv_goods_img);
                Glide.with(mActivity)
                        .load(url)
                        .crossFade()
                        .into(iv);
                childViews.add(v);
            }
        }
    }

    //viewpager中的组件数量
    @Override
    public int getCount() {
        return childViews.size();
    }
    //滑动切换的时候销毁当前的组件
    @Override
    public void destroyItem(ViewGroup container, int position,
                            Object object) {
        ((ViewPager) container).removeView(childViews.get(position));
    }
    //每次滑动的时候生成的组件
    @Override
    public Object instantiateItem(ViewGroup container, int position) {




        ((ViewPager) container).addView(childViews.get(position));
        return childViews.get(position);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return "";
    }




}
