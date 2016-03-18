package com.boding.microscreen.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.boding.microscreen.util.MyLog;

public class RoundImageView extends ImageView {
	
	private static final String TAG = "RoundImageView";
	
	private Paint mPaint;
	private Xfermode mXfermode;

	public RoundImageView(Context context) {
		this(context, null);
	}
	
	public RoundImageView(Context context, AttributeSet attrs) {
		this(context, null, 0);
	}
	
	public RoundImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		init();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.RED);
//		mPaint.setStyle(Style.STROKE);
//		mPaint.setStrokeWidth(5);
		// src_in模式  取两层图像的交集，显示上层图像，也就是后面画的图像
		mXfermode = new PorterDuffXfermode(Mode.SRC_IN);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// 圆形图片
//		int width = Math.min(getMeasuredWidth(), getMeasuredHeight());
//		setMeasuredDimension(width, width);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Drawable drawable = getDrawable();
		if (drawable == null) {  
            return;
		}
		if(getWidth() == 0 || getHeight() == 0){
			return;
		}
		
		Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
		int width = getWidth();
		int height = getHeight();
		MyLog.d(TAG, "width:" + width + ", height:" + height);
		
		int saveFlags = Canvas.MATRIX_SAVE_FLAG
				| Canvas.CLIP_SAVE_FLAG
				| Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
				| Canvas.FULL_COLOR_LAYER_SAVE_FLAG
				| Canvas.CLIP_TO_LAYER_SAVE_FLAG;
		Rect rect = new Rect(0, 0, width, height);
		int radius = Math.min(width, height)/2;
		canvas.drawCircle(width/2, height/2, radius, mPaint);
		canvas.saveLayer(0, 0, width, width, null, saveFlags);
		mPaint.setXfermode(mXfermode);
		canvas.drawBitmap(bitmap, width, height, mPaint);
		canvas.restore();
	}
}
