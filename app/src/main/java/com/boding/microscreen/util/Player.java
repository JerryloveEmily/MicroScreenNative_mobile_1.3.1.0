package com.boding.microscreen.util;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;

/**
 * 音频播放
 * @author Administrator
 *
 */
public class Player implements OnBufferingUpdateListener,  
OnCompletionListener, OnPreparedListener{
	
	public static final String TAG = "Player";
	public MediaPlayer mediaPlayer;
	private PlayerListener mPlayerListener;
	
	public PlayerListener getPlayerListener() {
		return mPlayerListener;
	}

	public void setPlayerListener(PlayerListener playerListener) {
		this.mPlayerListener = playerListener;
	}

	public Player(){  
        try {
        	if (mediaPlayer == null) {				
        		mediaPlayer = new MediaPlayer();
        		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);  
        		mediaPlayer.setOnBufferingUpdateListener(this);  
        		mediaPlayer.setOnPreparedListener(this);  
        		mediaPlayer.setOnCompletionListener(this);
			}
        } catch (Exception e) {  
//            MyLog.e(TAG, "error", e);
        	e.printStackTrace();
        }  
    }
	
	/**
	 * 播放
	 */
	public void play(){  
        mediaPlayer.start();  
    }  
    
	/**
	 * 播放网络地址音频
	 * @param videoUrl
	 */
    public void playUrl(String videoUrl){  
        try {  
        	MyLog.e(TAG, "playUrl");
        	if (videoUrl != null && !"".equals(videoUrl)) {
//        		mediaPlayer.reset();
        		mediaPlayer.setDataSource(videoUrl);
        		mediaPlayer.prepareAsync();//prepare之后自动播放
			}
            //mediaPlayer.start();  
        } catch (IllegalArgumentException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IllegalStateException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (Exception e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }  
  
    /**
     * 暂停播放
     */
    public void pause(){  
    	if (mediaPlayer != null) {
    		if (mediaPlayer.isPlaying()) {
    			mediaPlayer.pause();  
			}
    	}
    }

	/**
	 * 音乐是否正在播放
	 * @return
	 */
	public boolean isPlaying(){
		if (mediaPlayer != null) {
			return mediaPlayer.isPlaying();
		}
		return false;
	}
      
    /**
     * 停止播放
     */
    public void stop(){  
        if (mediaPlayer != null) {   
        	mediaPlayer.stop();  
            mediaPlayer.release();   
            mediaPlayer = null;   
        }   
    }
	
	public void onPrepared(MediaPlayer mp) {
        if (mPlayerListener != null) {
        	mPlayerListener.onPrepared(this, mp);
		}
	}

	public void onCompletion(MediaPlayer mp) {
		if (mPlayerListener != null) {
        	mPlayerListener.onCompletion(this, mp);
		}
	}

	public void onBufferingUpdate(MediaPlayer mp, int percent) {
//		Log.e(TAG, "onBufferingUpdate");
		if (mPlayerListener != null) {
        	mPlayerListener.onBufferingUpdate(this,mp,percent);
		}
	}
	
	/**
	 * 设置是否循环播放
	 * @param isLoop
	 */
	public void setLoopingPlayer(boolean isLoop){
		if (mediaPlayer != null) {
			MyLog.e(TAG, "setLoopingPlayer");
			mediaPlayer.setLooping(isLoop);
		}
	}
	
	public interface PlayerListener{
		public void onPrepared(Player player, MediaPlayer mp);
		public void onCompletion(Player player, MediaPlayer mp);
		public void onBufferingUpdate(Player player, MediaPlayer mp, int percent);
	}
}
