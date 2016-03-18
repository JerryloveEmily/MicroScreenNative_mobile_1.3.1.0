package com.boding.microscreen.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.boding.microscreen.model.VideoInfo;
import com.boding.microscreen.ui.VideoPlayActivity;
import com.boding.microscreen.widget.quickadapter.BaseAdapterHelper;
import com.boding.microscreen.widget.quickadapter.QuickAdapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.Serializable;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 每一页的视频列表
 * Created by Jerry on 2015/12/18.
 */
public class VideoPagerFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;
    @Bind(R.id.gv_pager_item)
    GridView mPagerGridView;

    private QuickAdapter<VideoInfo> mPagerGridAdapter;

    private List<VideoInfo> mVideoInfos;
    private List<List<VideoInfo>> mPagerDatas;
    private int mPagerNum;

    public static final String VIDEO_INFOS = "videoInfos";
    public static final String PAGER_DATAS = "pagerDatas";
    public static final String SELECTED_VIDEO_POSITION = "selectedVideoPosition";
    public static final String PAGER_NUM = "pagerNum";    // 当前第几页

    public static VideoPagerFragment newInstance(List<VideoInfo> videoInfos,
                                                 List<List<VideoInfo>> pagerDatas,
                                                 int pagerNum) {
        VideoPagerFragment fragment = new VideoPagerFragment();
        Bundle args = new Bundle();
        args.putSerializable(VIDEO_INFOS, (Serializable) videoInfos);
        args.putSerializable(PAGER_DATAS, (Serializable) pagerDatas);
        args.putInt(PAGER_NUM, pagerNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mVideoInfos = (List<VideoInfo>) getArguments().getSerializable(VIDEO_INFOS);
            mPagerDatas = (List<List<VideoInfo>>) getArguments().getSerializable(PAGER_DATAS);
            mPagerNum = getArguments().getInt(PAGER_NUM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vedio_list_pager, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }


    private void initView(View view) {
        ButterKnife.bind(this, view);
        initVideoListPager();
        initRefreshView();
    }

    private void initEvent() {
        // 点击视频列表项进行播放
        mPagerGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putInt(SELECTED_VIDEO_POSITION, calculatePosition(position));
                bundle.putSerializable(PAGER_DATAS, (Serializable) mPagerDatas);
                VideoPlayActivity.startActivity(getActivity(), VideoPlayActivity.class,
                        bundle, 0);
            }
        });
    }

    /**
     * 计算所有数据中被点击的位置
     * @param position 当前页所在的位置
     * @return ...
     */
    private int calculatePosition(int position){
        int videoCount = 0;
        for (List<VideoInfo> videoInfos :
                mPagerDatas) {
            videoCount += videoInfos.size();
        }
        int tempPagerNum = mPagerNum + 1;
        int index = videoCount/6; // 完整一页数据的页数
        int tempPosition = 0;
        if (index > 0){
            if (tempPagerNum <= index){
                for (int i = 0; i < index; i++) {
                    if (i == index - 1){
                        tempPosition += position + 1;
                    }else {
                        tempPosition += 6;
                    }
                }
            }else {
                tempPosition = index * 6 + position + 1;
            }
        }else {
            tempPosition = position + 1;
        }
        return tempPosition - 1;
    }

    /**
     * 初始化加载刷新视图
     */
    private void initRefreshView() {
        mSrlRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_dark);
        mSrlRefresh.setOnRefreshListener(this);
        mSrlRefresh.setRefreshing(true);
        onRefresh();
    }

    @Override
    public void onRefresh() {
        upadteView(mVideoInfos);
    }

    /**
     * 初始化视频列表页视图
     */
    private void initVideoListPager() {
        mPagerGridAdapter = new QuickAdapter<VideoInfo>(getActivity(),
                R.layout.fragment_vedio_list_item2, null) {

            @Override
            protected void convert(BaseAdapterHelper helper, final int position, VideoInfo item) {
                ImageView ivPreViewImage = helper.getView(R.id.iv_video_preview);
                Glide.with(VideoPagerFragment.this)
                        .load(item.getThumbnailUrl())
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
                        .into(ivPreViewImage);
                TextView tvVideoName = helper.getView(R.id.tv_video_name);
                tvVideoName.setText(item.getName());
            }
        };

        mPagerGridView.setAdapter(mPagerGridAdapter);
    }

    private void upadteView(List<VideoInfo> videoInfos){
        mPagerGridAdapter.addAll(videoInfos);
    }
}
