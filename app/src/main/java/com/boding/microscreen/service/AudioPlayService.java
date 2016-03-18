package com.boding.microscreen.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;

import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.AudioPlayer;


/**
 * detail:
 * Created by libq
 * 2015/5/28#10:40
 */

public class AudioPlayService extends Service{

    public static boolean isPlaying;
    public final static String Tag = "AudioPlayService";
    public final static String AUDIO_ACTION = "boding.microscreen.service.audioplay";
    public final static String AUDIO_RECEIVER_ACTION = "boding.microscreen.service.audioplay.receievr";
    public final static int AUDIO_PAUSE =1;
    public final static int AUDIO_START =2;
    public final static int AUDIO_STOP=3;
    public final static int AUDIO_PARAM =4;
    public final static String DATA_URL="data_url";
    public final static String DATA_CODE="code";

    private BroadcastReceiver receiver = null;
    private AudioPlayer audioPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        audioPlayer = new AudioPlayer();
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                int code = intent.getIntExtra(DATA_CODE,-1);


                MyLog.e(Tag, "收到广播 "+code);
                switch (code){
                    case AUDIO_PAUSE:
                        isPlaying = false;
                        audioPlayer.pause();
                        break;
                    case AUDIO_START:

                        isPlaying = true;
                        audioPlayer.start();
                        break;
                    case AUDIO_STOP:
                        isPlaying = false;
                        audioPlayer.stop();
                        break;
                    case AUDIO_PARAM:
                        isPlaying = true;
                       String url = intent.getStringExtra(DATA_URL);
                        audioPlayer.setIsLoop(true);
                        audioPlayer.playUrl(url);
                        break;
                }

               // player.playUrl("http://192.168.1.105/龙卷风.mp3");
            }
        };

        MyLog.e(Tag, "开始 service create............");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AUDIO_RECEIVER_ACTION);
        registerReceiver(receiver, intentFilter);

        //audioPlayer.playUrl("http://192.168.1.105/龙卷风.mp3");


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
