package com.boding.microscreen.util;

import android.util.Log;

/**
 * 日志管理
 * 
 * @author Administrator
 *
 */
public class MyLog {
	public static boolean DEBUG = false;
	
	public static void d(String tag, String message){
		if (DEBUG) {
			Log.d(tag, message);
		}
	}
	
	public static void v(String tag, String message){
		if (DEBUG) {
			Log.v(tag, message);
		}
	}
	
	public static void e(String tag, String message){
		if (DEBUG) {
			Log.e(tag, message);
		}
	}
	
	public static void i(String tag, String message){
		if (DEBUG) {
			Log.i(tag, message);
		}
	}
}
