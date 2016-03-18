package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.VideoView;

import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.VideoInfo;
import com.boding.microscreen.service.AudioPlayService;
import com.boding.microscreen.ui.HomeActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;


public class VideoPlayFragment extends BaseFragment {

    private static final String TAG = "VideoPlayFragment";

    private FrameLayout mFLcontain;
    private ImageView mImgBg;
    private VideoView videoView = null;
    private ImageButton mIBtnPlayVideo;
    private int mPlayCurrentPostion = 0;
    private boolean isFirstPlay = true;
    private boolean isComplete = false;

    private HomeActivity mActivity;
    private String mParam;
    private static final String POSITION = "position";
    private static final String VIDEO_INFO_DATAS = "videoInfoDatas";
    private int mPosition;
    private List<VideoInfo> mVideoInfoDatas;
    private VideoInfo mCurVideoInfo;

    public static VideoPlayFragment newInstance(int position, List<VideoInfo> videoInfoDatas) {
        VideoPlayFragment fragment = new VideoPlayFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putSerializable("videoInfoDatas", (Serializable) videoInfoDatas);
        fragment.setArguments(bundle);
        return fragment;
    }

    public VideoPlayFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(POSITION);
            mVideoInfoDatas = (List<VideoInfo>) getArguments().getSerializable(VIDEO_INFO_DATAS);
            mCurVideoInfo = mVideoInfoDatas.get(mPosition);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //初始化videoview
        mImgBg = (ImageView) view.findViewById(R.id.id_iv_img_bg);
        videoView = (VideoView) view.findViewById(R.id.video_view);
        mIBtnPlayVideo = (ImageButton) view.findViewById(R.id.id_ibtn_play_video);
        mIBtnPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!videoView.isPlaying()) {
                    /*if (!isFirstPlay) {
                        Log.e(TAG, "canPause...");
                        if (View.VISIBLE == mImgBg.getVisibility()) {
                            mImgBg.setVisibility(View.GONE);
                        }

                    } else {
                        startVideoPlay2(mCurrVideoIndex, mVideoInfoDatas);
                        isFirstPlay = false;
                    }*/
                    /*if (isComplete) {
                        isComplete = false;
                        startVideoPlay2(mCurrVideoIndex, mVideoInfoDatas);
                    } else {

                    }*/
                    mIBtnPlayVideo.setVisibility(View.GONE);
                    videoView.seekTo(mPlayCurrentPostion);
                    videoView.start();
                    videoView.setFocusable(true);
                    if (AudioPlayService.isPlaying) {
                        Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                        intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
                        mActivity.sendBroadcast(intent);
                    }
                    Log.e(TAG, "canPause: " + videoView.canPause());
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
//        updateView();
//        mIBtnPlayVideo.requestFocus();
        videoView.requestFocus();
        startVideoPlay2(mPosition, mVideoInfoDatas);
    }

    /**
     * 初始化背景图片
     */
    private void updateView() {
        if (View.VISIBLE == mImgBg.getVisibility()) {
            mImgBg.setVisibility(View.GONE);
            mIBtnPlayVideo.setVisibility(View.GONE);
        } else {
            mImgBg.setVisibility(View.VISIBLE);
            mIBtnPlayVideo.setVisibility(View.VISIBLE);
            Glide.with(VideoPlayFragment.this)
                    .load(mCurVideoInfo.getThumbnailUrl())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(mImgBg);
        }
    }

    /**
     * 初始化VideoView 播放视频
     */
    /*private void startVideoPlay(int position, @NonNull final List<VideoInfo> videoInfos) {
        mCurrVideoIndex = 0;

        if (!videoInfos.isEmpty()) {
            VideoInfo videoInfo = videoInfos.get(position);
            final int videoCount = videoInfos.size();
            Log.e(TAG, "videoInfo: " + videoInfo.toString());
            String url = videoInfo.getVideoUrl();
            if (TextUtils.isEmpty(url)) {
                Crouton.showText(mActivity, "没有可播放视频", Style.ALERT);
                updateView();
//                return;
            } else {
                if (AudioPlayService.isPlaying) {
                    Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                    intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
                    mActivity.sendBroadcast(intent);
                }
            }
//            String urlTemp = "http://1279.vod.myqcloud.com/1279_b035cb0031f911e5be00d51f0439f0b3.f0.mp4";
//            String urlTemp = "http://192.168.1.105/5.avi";
//            String urlTemp = "http://content.12530.com/media/v/20100429/002323.flv";
            final Uri uri = Uri.parse(url);
            final boolean isLoop = videoInfo.getIsLooper() == 1;

            videoView.setMediaController(null);
            videoView.setVideoURI(uri);
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (videoView.isPlaying()) {
                        videoView.pause();
                        mPlayCurrentPostion = videoView.getCurrentPosition();
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
                            mActivity.sendBroadcast(intent);
                        }
                    }
                }
            });
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
//                    mp.setLooping(isLoop);
                    videoView.setFocusable(true);
                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mCurrVideoIndex++;

                    if (isLoop) {
                        if (mCurrVideoIndex >= videoCount) {
                            mCurrVideoIndex = 0;
                        }
                        if (videoCount == 1) {
                            mCurrVideoIndex = 0;
                        }
                        mp.reset();
                        mp.start();
                    } else if (mCurrVideoIndex >= videoCount) {
                        mCurrVideoIndex = -1;
                    }

                    if (mCurrVideoIndex >= 0) {
                        videoView.setVideoURI(Uri.parse(videoInfos.get(mCurrVideoIndex).getVideoUrl()));
                        videoView.start();
                    }
                    if (!isLoop) {
                        Crouton.showText(mActivity, "播放完了", Style.INFO);
                        videoView.stopPlayback();
                        mPlayCurrentPostion = 0;
                        isComplete = true;
                        mImgBg.setVisibility(View.VISIBLE);
                        mIBtnPlayVideo.setVisibility(View.VISIBLE);
                        videoView.setFocusable(false);
                        if (!AudioPlayService.isPlaying) {
                            if (AppConstants.isHandClose && !AppConstants.isHandOpen) {
                                return;
                            }
                            Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                            intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                            mActivity.sendBroadcast(intent);
                        }
                    }
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    mp.stop();
                    mPlayCurrentPostion = 0;
                    mImgBg.setVisibility(View.VISIBLE);
                    mIBtnPlayVideo.setVisibility(View.VISIBLE);
                    if (!AudioPlayService.isPlaying) {
                        if (AppConstants.isHandClose && !AppConstants.isHandOpen) {

                        } else {
                            Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                            intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                            mActivity.sendBroadcast(intent);
                        }
                    }
                    mActivity.replaceFragment(R.id.id_ll_fragment_container, MoreFragment.class.getName());
                    return true;
                }
            });
        } else {
            Crouton.showText(mActivity, "没有可播放视频", Style.ALERT);
        }

        videoView.start();
    }*/

    private int mCurrVideoIndex = 0;

    /**
     * 初始化VideoView 播放视频
     */
    private void startVideoPlay2(final int position, @NonNull final List<VideoInfo> videoInfos) {
        mCurrVideoIndex = position;
        final int videoCount = videoInfos.size();
        if (!videoInfos.isEmpty()) {
            VideoInfo videoInfo = videoInfos.get(position);
            String url = videoInfo.getVideoUrl();
            if (TextUtils.isEmpty(url)) {
                Crouton.showText(mActivity, "没有可播放视频", Style.ALERT);
                updateView();
//                return;
            } else {
                if (AudioPlayService.isPlaying) {
                    Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                    intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
                    mActivity.sendBroadcast(intent);
                }

                final Uri uri = Uri.parse(url);
                videoView.setMediaController(null);
                videoView.setVideoURI(uri);
                videoView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (videoView.isPlaying()) {
                            videoView.pause();
                            mPlayCurrentPostion = videoView.getCurrentPosition();
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
                                mActivity.sendBroadcast(intent);
                            }
                        }
                    }
                });
                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
//                        mp.setLooping(isLoop);
                        videoView.setFocusable(true);
                    }
                });
                videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        /*if (isLoop) {
                            if (currVideoIndex >= videoCount) {
                                currVideoIndex = 0;
                            }
                            if (videoCount == 1) {
                                currVideoIndex = 0;
                            }
                            mp.reset();
                            mp.start();
                        } else if (currVideoIndex >= videoCount) {
                            currVideoIndex = -1;
                        }*/

                        if (mCurrVideoIndex >= 0 && mCurrVideoIndex < videoCount - 1) {
                            // 当前播放位置在 0 ~ size-1的时候轮播下一个
                            mCurrVideoIndex++;

                        } else {
                            mCurrVideoIndex = 0;
                        }
                        videoView.setVideoURI(Uri.parse(videoInfos.get(mCurrVideoIndex).getVideoUrl()));
                        videoView.start();
                        /*if (!isLoop) {
                            Crouton.showText(mActivity, "播放完了", Style.INFO);
                            videoView.stopPlayback();
                            mPlayCurrentPostion = 0;
                            isComplete = true;
                            mImgBg.setVisibility(View.VISIBLE);
                            mIBtnPlayVideo.setVisibility(View.VISIBLE);
                            videoView.setFocusable(false);
                            if (!AudioPlayService.isPlaying) {
                                if (AppConstants.isHandClose && !AppConstants.isHandOpen) {
                                    return;
                                }
                                Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                                intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                                mActivity.sendBroadcast(intent);
                            }
                        }*/
                    }
                });
                videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        mp.stop();
                        mPlayCurrentPostion = 0;
                        mImgBg.setVisibility(View.VISIBLE);
                        mIBtnPlayVideo.setVisibility(View.VISIBLE);
                        if (!AudioPlayService.isPlaying) {
                            if (AppConstants.isHandClose && !AppConstants.isHandOpen) {

                            } else {
                                Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
                                intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
                                mActivity.sendBroadcast(intent);
                            }
                        }
                        mActivity.replaceFragment(R.id.id_ll_fragment_container, MoreFragment.class.getName());
                        return true;
                    }
                });
            }
//            String urlTemp = "http://1279.vod.myqcloud.com/1279_b035cb0031f911e5be00d51f0439f0b3.f0.mp4";
//            String urlTemp = "http://192.168.1.105/5.avi";
//            String urlTemp = "http://content.12530.com/media/v/20100429/002323.flv";
            videoView.start();
        } else {
            Crouton.showText(mActivity, "没有可播放视频", Style.ALERT);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            if (activity instanceof HomeActivity)
                mActivity = (HomeActivity) activity;

        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存的时候清理Glide图片内存缓存:bitmapPool、memoryCache
        Glide.get(mActivity).clearMemory();
        Log.e(TAG, TAG + "_低内存...");
        System.gc();
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
            mActivity.sendBroadcast(intent);
        }
    }
}