package com.boding.microscreen.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 跑马灯TextView
 * Created by Administrator on 2015/4/8.
 */
public class MarqueeTextView extends TextView {

    // 是否拥有焦点，默认拥有
    private boolean mIsFocus = true;

    public boolean isFocus() {
        return mIsFocus;
    }

    /**
     * 设置是否拥有焦点
     * @param isFocus
     */
    public void setFocus(boolean isFocus) {
        this.mIsFocus = isFocus;
    }

    public MarqueeTextView(Context context) {
        super(context);
    }

    public MarqueeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return mIsFocus;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
//        super.onFocusChanged(focused, direction, previouslyFocusedRect);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(text, type);
    }
}
