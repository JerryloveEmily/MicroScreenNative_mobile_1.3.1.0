package com.boding.microscreen.util;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;

/**
 * 淡入淡出动画
 * Created by Administrator on 2015/4/10.
 */
public class FadeInOutPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
//                Log.e(TAG, "position: " + position);
        int pageWidth = view.getWidth();

        if (position < -1) { // [-Infinity,-1)
            // This page is way off-screen to the left.
            ViewHelper.setAlpha(view, 0);

        } else if (position <= 0) { // [-1,0]
            // Use the default slide transition when moving to the left page
            // 0.0 ~ -1.0
            ViewHelper.setAlpha(view, 1 - Math.abs(position));
            ViewHelper.setTranslationX(view, pageWidth * -position);
            ViewHelper.setScaleX(view, 1);
            ViewHelper.setScaleY(view, 1);
        } else if (position <= 1) { // (0,1]
            // 1.0 ~ 0.0
            // Fade the page out.
            ViewHelper.setAlpha(view, 1 - position);
            // Counteract the default slide transition
            ViewHelper.setTranslationX(view, pageWidth * -position);

        } else { // (1,+Infinity]
            // This page is way off-screen to the right.
            ViewHelper.setAlpha(view, 0);
        }
    }
}
