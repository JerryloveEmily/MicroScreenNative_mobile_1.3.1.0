package com.boding.microscreen.app;

import android.content.Intent;
import android.util.Log;

import com.boding.microscreen.ui.SplashActivity;

/**
 * deal with Crash
 * Created by Jerry on 2015/9/6.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "CrashHandler";
    private static CrashHandler mInstance;
    private MicroScreenApplication mApplication;

    private CrashHandler(){}

    public static CrashHandler getInstance(){
        if (mInstance == null){
            synchronized (CrashHandler.class){
                if (mInstance == null){
                    mInstance = new CrashHandler();
                }
            }
        }
        return mInstance;
    }

    public void init(MicroScreenApplication application){
        this.mApplication = application;
        // �����쳣������
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // �жϵ�ǰ�߳��Ƿ�ΪUI�߳�
        if (thread.getId() == 1){// ΪUI�߳�
            // �����е�Activity����Activity�б��У���һ����ɾ���б���˳���Activity
            Log.e(TAG, "Application crashed is in UI thread. Now, restarting app...");
            // ��������app
            Intent intent = new Intent(mApplication, SplashActivity.class);
            mApplication.startActivity(intent);
        }else{
            Log.e(TAG, " Application crashed isn't in UI thread. Now, restarting app...");
            // ��ǰ�̲߳���UI�̵߳Ĵ���
            new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();
        }
    }
}
