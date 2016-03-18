package com.boding.microscreen.util;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class ApplicationUtil {
	
	/**
     * 获取Android系统当前可用内存大小
     * @param context
     * @return
     */
    public static long getAvailMemory(Context context){
        // 获取android当前可用内存大小
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        //mi.availMem; 当前系统的可用内存

        //return Formatter.formatFileSize(context, mi.availMem);// 将获取的内存大小规格化
        return mi.availMem/(1024*1024);
    }

    /**
     * 获取系统总内存大小
     * @param context
     * @return
     */
    public static long getTotalMemory(Context context){
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initialMemory = 0;

        try
        {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(
                    localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");
            for (String num : arrayOfString) {
                Log.i(str2, num + "\t");
            }

            initialMemory = Integer.valueOf(arrayOfString[1]).intValue() * 1024;// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close();

        } catch (IOException e) {
        }
        //return Formatter.formatFileSize(context, initial_memory);// Byte转换为KB或者MB，内存大小规格化
        return initialMemory/(1024*1024);
    }
	
	/**
	 * 清理内存
	 * @param context
	 */
    public static void clearMemory(Context context){
        ActivityManager activityManger=(ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list=activityManger.getRunningAppProcesses();
        if(list!=null){
            for(int i=0;i<list.size();i++) {
                ActivityManager.RunningAppProcessInfo apinfo=list.get(i);

                System.out.println("pid            "+apinfo.pid);
                System.out.println("processName              "+apinfo.processName);
                System.out.println("importance            "+apinfo.importance);
                String[] pkgList=apinfo.pkgList;

                /**
                 * IMPORTANCE_VISIBLE级别：非可见的后台进程和服务会被杀掉（力度和效果比较好）
                 * IMPORTANCE_SERVICE级别：杀掉那些长时间没用或者空进程
                 */
                if(apinfo.importance > ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    for(int j=0;j<pkgList.length;j++) {
                        if (Build.VERSION.SDK_INT <= 8){// SDK为2.2及其以下的
                            activityManger.restartPackage(pkgList[j]);
                        }else{
                            activityManger.killBackgroundProcesses(pkgList[j]);
                        }
                    }
                }
            }
        }
    }
}
