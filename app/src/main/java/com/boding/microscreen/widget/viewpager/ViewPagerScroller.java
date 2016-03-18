package com.boding.microscreen.widget.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by Administrator on 2015/4/7.
 */
public class ViewPagerScroller extends Scroller {

    // 滚动的时间默认是2秒
    private int mScrollDuration= 2000;

    public int getScrollDuration() {
        return mScrollDuration;
    }

    public void setScrollDuration(int scrollduration) {
        this.mScrollDuration = scrollduration;
    }

    public ViewPagerScroller(Context context) {
        super(context);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }

    public ViewPagerScroller(Context context, Interpolator interpolator, boolean flywheel) {
        super(context, interpolator, flywheel);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    /**
     * 初始化Scroller对象
     * @param viewPager
     */
    public void initViewPagerScroller(ViewGroup viewPager){
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, this);
            /*if (viewPager instanceof ViewPager){
                // 把当前的scroller对象设置给从viewPager对象中反射出来的“mScroller”对象
                mScroller.set((ViewPager)viewPager, this);
            }
            if (viewPager instanceof VerticalViewPager){
                // 把当前的scroller对象设置给从viewPager对象中反射出来的“mScroller”对象
                mScroller.set((VerticalViewPager)viewPager, this);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
