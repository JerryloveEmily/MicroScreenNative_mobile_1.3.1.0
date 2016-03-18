package com.boding.microscreen.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import com.boding.microscreen.R;

/**
 * 自定义对话框
 * @author Administrator
 *
 */
public class CustomDialog extends Dialog {

	/**
	 * 初始化对话框对象
	 * @param context		对话框上下文
	 * @param isShowTitle	是否显示标题头
	 */
	public CustomDialog(Context context, boolean isShowTitle) {
		super(context);
		if (!isShowTitle) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
	}

	/**
	 * 显示对话框
	 * @param layoutResID		自定义布局
	 * @param animationResID 	自定义显示动画 :0表示使用默认动画
	 */
	public void showDialog(int layoutResID, int animationResID){
		setContentView(layoutResID);
		// 窗口显示
		setWindowDialog(animationResID);
		// 显示对话框
		show();
	}
	
	/**
	 * 显示对话框
	 * @param view  			自定义视图
	 * @param animationResID 	自定义显示动画 :0表示使用默认动画
	 */
	public void showDialog(View view, int animationResID){
		setContentView(view);
		// 窗口显示
		setWindowDialog(animationResID);
		// 显示对话框
		show();
	}
	
	private void setWindowDialog(int animationResID){
		Window window = getWindow();
		window.setWindowAnimations(animationResID);
		window.setBackgroundDrawableResource(R.color.translusent);
		LayoutParams params = window.getAttributes();
		params.gravity = Gravity.CENTER;
		window.setAttributes(params);
	}
}
