package com.boding.microscreen.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

import java.io.IOException;

/**
 * 音频播放
 * @author Administrator
 *
 */
public class AudioPlayer implements OnBufferingUpdateListener,
OnCompletionListener, OnPreparedListener{

	public  final String TAG = "Player";
	private  MediaPlayer mediaPlayer;
	private  boolean isLoop;

	public AudioPlayer(){
		mediaPlayer = new MediaPlayer();

    }

	public  void setIsLoop(boolean pIsLoop){
		isLoop = pIsLoop;
	}

	/**
	 * 播放音频
	 * @param audioUrl 音频地址或者路径
	 */
	public  void playUrl(String audioUrl){
		try
		{
			if(mediaPlayer!=null) {
				//mediaPlayer.reset();
                mediaPlayer.setOnCompletionListener(this);
                //mediaPlayer.setOnErrorListener(this);
                mediaPlayer.setOnBufferingUpdateListener(this);
                mediaPlayer.setOnPreparedListener(this);
				mediaPlayer.setLooping(isLoop);
				mediaPlayer.setDataSource(audioUrl);
				mediaPlayer.prepareAsync();// 开始在后台缓冲音频文件并返回
			}
		} catch (IOException e)
		{
			MyLog.v("AUDIOHTTPPLAYER", e.getMessage());
		}
	}

	/**
	 * 暂停
	 */
	public  void pause(){
		if(mediaPlayer!=null)mediaPlayer.pause();
	}

	/**
	 * 开始播放
	 */
	public  void start(){
		if(mediaPlayer!=null)mediaPlayer.start();
	}

	/**
	 * 停止播放
	 *
	 */
	public  void stop(){
		if(mediaPlayer!=null)mediaPlayer.stop();
	}




	@Override
	public void onPrepared(MediaPlayer mp)
	{
		if(mediaPlayer!=null)mediaPlayer.start();

	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent)
	{

	}

	@Override
	public void onCompletion(MediaPlayer mp)
	{

	}



}
