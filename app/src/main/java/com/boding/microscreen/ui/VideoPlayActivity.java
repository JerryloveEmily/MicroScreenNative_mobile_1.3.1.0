package com.boding.microscreen.ui;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.VideoInfo;
import com.boding.microscreen.service.AudioPlayService;
import com.boding.microscreen.ui.fragment.VideoPagerFragment;
import com.boding.microscreen.util.JLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class VideoPlayActivity extends AppCompatActivity {

    @Bind(R.id.id_iv_img_bg)
    ImageView mImgBg;
    @Bind(R.id.video_view)
    VideoView videoView;
    @Bind(R.id.id_ibtn_play_video)
    ImageButton mIBtnPlayVideo;

    private int mPlayCurrentMsec = 0;
    private boolean isFirstPlay = true;
    private boolean isComplete = false;

    private String mParam;
    private static final String POSITION = "position";
    private static final String VIDEO_INFO_DATAS = "videoInfoDatas";
    private int mPosition;
    private List<VideoInfo> mVideoInfoDatas = new ArrayList<>();
    private VideoInfo mCurVideoInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_play);
        initView();
        initEvent();

    }

    private void initView() {
        ButterKnife.bind(this);
        videoView.requestFocus();
        loadDatas();
    }

    private void loadDatas(){
        Intent intent = getIntent();
        if (null != intent){
            mPosition = intent.getBundleExtra("bundle").getInt(VideoPagerFragment.SELECTED_VIDEO_POSITION);
            JLog.e("当前播放位置: " + mPosition);
            List<List<VideoInfo>> mPagerDatas = new ArrayList<>();
            mPagerDatas = (List<List<VideoInfo>>) intent.getBundleExtra("bundle")
                    .getSerializable(VideoPagerFragment.PAGER_DATAS);
            for (List<VideoInfo> videoInfos: mPagerDatas) {
                mVideoInfoDatas.addAll(videoInfos);
            }
            for (VideoInfo vi :
                    mVideoInfoDatas) {
                JLog.e(vi.toString());
            }
            startVideoPlay2(mPosition, mVideoInfoDatas);
        }
    }

    private void initEvent() {
        mIBtnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    mIBtnPlayVideo.setVisibility(View.GONE);
                    videoView.seekTo(mPlayCurrentMsec);
                    videoView.start();
                    videoView.setFocusable(true);
                    if (AudioPlayService.isPlaying) {
                        Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                        intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
                        sendBroadcast(intent);
                    }
                }
            }
        });
        mIBtnPlayVideo.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                    mIBtnPlayVideo.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    private int mCurrPlayVideoIndex = 0;

    /**
     * 初始化VideoView 播放视频
     */
    private void startVideoPlay2(final int position, @NonNull final List<VideoInfo> videoInfos) {
        mCurrPlayVideoIndex = position;
        final int videoCount = videoInfos.size();
        if (!videoInfos.isEmpty()) {
            VideoInfo videoInfo = videoInfos.get(position);
            String url = videoInfo.getVideoUrl();
            if (TextUtils.isEmpty(url)) {
                Crouton.showText(this, "没有可播放视频", Style.ALERT);
                updateView();
            } else {
                if (AudioPlayService.isPlaying) {
                    Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                    intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
                    sendBroadcast(intent);
                }

                final Uri uri = Uri.parse(url);
                videoView.setMediaController(null);
                videoView.setVideoURI(uri);
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoView.isPlaying()) {
                            videoView.pause();
                            mPlayCurrentMsec = videoView.getCurrentPosition();
                            if (View.GONE == mIBtnPlayVideo.getVisibility()) {
                                mIBtnPlayVideo.setVisibility(View.VISIBLE);
                                videoView.setFocusable(false);
                                mIBtnPlayVideo.requestFocus();
                            }
                            if (!AudioPlayService.isPlaying) {
                                if (AppConstants.isHandClose && !AppConstants.isHandOpen) {
                                    return;
                                }
                                Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                                intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                                sendBroadcast(intent);
                            }
                        }
                    }
                });
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        videoView.setFocusable(true);
                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if (mCurrPlayVideoIndex >= 0 && mCurrPlayVideoIndex < videoCount - 1) {
                            // 当前播放位置在 0 ~ size-1的时候轮播下一个
                            mCurrPlayVideoIndex++;
                        } else {
                            mCurrPlayVideoIndex = 0;
                        }
                        videoView.setVideoURI(Uri.parse(videoInfos.get(mCurrPlayVideoIndex).getVideoUrl()));
                        videoView.start();
                    }
                });
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mp.stop();
                        mPlayCurrentMsec = 0;
                        mImgBg.setVisibility(View.VISIBLE);
                        mIBtnPlayVideo.setVisibility(View.VISIBLE);
                        if (!AudioPlayService.isPlaying) {
                            if (AppConstants.isHandClose && !AppConstants.isHandOpen) {

                            } else {
                                Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                                intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                                sendBroadcast(intent);
                            }
                        }
                        return true;
                    }
                });
            }
            videoView.start();
        } else {
            Crouton.showText(this, "没有可播放视频", Style.ALERT);
        }
    }

    private void updateView() {
        if (View.VISIBLE == mImgBg.getVisibility()) {
            mImgBg.setVisibility(View.GONE);
            mIBtnPlayVideo.setVisibility(View.GONE);
        } else {
            mImgBg.setVisibility(View.VISIBLE);
            mIBtnPlayVideo.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(mCurVideoInfo.getThumbnailUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mImgBg);
        }
    }

    /**
     * 启动Activity
     * @param context   当前Activity上下文
     * @param cls       跳转到的Activity
     * @param bundle    携带的数据
     * @param flags     不同界面的调整标识
     */
    public static void startActivity(@NonNull Context context, @NonNull Class<?> cls,
                                     @Nullable Bundle bundle, int flags){
        Intent intent = new Intent(context, cls);
        if (null != bundle){
            intent.putExtra("bundle", bundle);
        }
        if (0 != flags){
            intent.setFlags(flags);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Glide.clear(mImgBg);
        if (videoView != null) {
            videoView.stopPlayback();
            videoView = null;
        }
        if (!AudioPlayService.isPlaying) {
            if (AppConstants.isHandClose && !AppConstants.isHandOpen) {
                return;
            }
            Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
            intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
            sendBroadcast(intent);
        }
    }
}
