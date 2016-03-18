package com.boding.microscreen.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类
 * @author Administrator
 *
 */
public class Util {
	
	/**
     * 把Unix时间戳转换成指定格式日期字符串
     * @param timestamp
     * @param formats
     * @return
     */
    @SuppressLint("SimpleDateFormat")
	public static String timeStamp2Date(long timestamp, String formats){
    	if (formats == null || "".equals(formats)) {
    		formats = "yyyy-MM-dd HH:mm:ss";
		}
        String date = new java.text.SimpleDateFormat(formats).format(new java.util.Date(timestamp));
        return date;
    }
	
	/**
	 * 判断SD卡是否存在
	 * 
	 * @return
	 */
	public static boolean isSDCardExist() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 功能说明：判断是否有网络连接
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
	/**
	 * 退出应用对话框
	 */
	public static void exitDialog(final Context context, int theme){
		AlertDialog.Builder builder = new AlertDialog.Builder(context, theme);
		builder.setTitle("提示");
        builder.setMessage("您要退出应用吗？");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				((Activity) context).finish();
//				android.os.Process.killProcess(Process.myPid());
			}
		});
		builder.setNegativeButton("取消", null);
		builder.create().show();
	}
	
	/**
	 * 功能：获取App包的信息
	 * 
	 * @param context
	 * @return 包信息对象
	 */
	public static PackageInfo getAppPackageInfo(Context context) {
		PackageManager packageManager = context.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return packageInfo;
	}

    /**
     * 将unicode字符转化为汉字
     * @param utfString
     * @return
     */
    public static String convertUnicode(String utfString){
        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while((i=utfString.indexOf("\\u", pos)) != -1){
            sb.append(utfString.substring(pos, i));
            if(i+5 < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }

        return sb.toString();
    }

	/**
	 * 给一个集合数据分组
	 *
	 * @param data  需要分组的数据
	 * @param row    需要分成该行的组
	 * @param column 需要分成该列的组
	 * @param <T>    被分组数据的实体
	 * @return 分组后的结合列表
	 */
	public static  <T> List<List<T>> groupByColumnRow(List<T> data, int row, int column) {
		List<List<T>> result = new ArrayList<>();
		ArrayList<T> eachGroupData = null;
		for (int i = 0; i < data.size(); i++) {
			if (i % (row * column) == 0) {
				eachGroupData = new ArrayList<>();
				result.add(eachGroupData);
				eachGroupData.add(data.get(i));
			} else {
				if (eachGroupData != null) {
					eachGroupData.add(data.get(i));
				}
			}
		}

		return result;
	}
}
