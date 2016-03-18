package com.boding.microscreen.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.boding.microscreen.util.JLog;

/**
 * 自启动广播
 * Created by Jerry on 2016/2/19.
 */
public class AutoBootBroadcastReceiver extends BroadcastReceiver {



    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (Intent.ACTION_BOOT_COMPLETED.equals(action)){
            autoBootApp(context);
        }
    }

    /**
     * 开机自动启动app
     * @param context   ...
     */
    private void autoBootApp(Context context){
        Toast.makeText(context, "自启动app", Toast.LENGTH_SHORT).show();
        JLog.e("autoBootApp...");
        Intent intent = new Intent(context, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
//        String packageName = context.getPackageName();
//        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
//        context.startActivity(intent );
    }
}
