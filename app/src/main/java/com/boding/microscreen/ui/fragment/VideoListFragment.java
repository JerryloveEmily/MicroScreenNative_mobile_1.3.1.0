package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.VideoInfo;
import com.boding.microscreen.net.Request4VideoInfos;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.JLog;
import com.boding.microscreen.util.Util;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 视频列表
 */
public class VideoListFragment extends BaseFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.vp_video_list_pager)
    ViewPager mVideoListPager;

    private VideoListPagerAdapter mPagerAdapter;

    private HomeActivity mActivity;

    private String mParam1;
    private String mParam2;


    public VideoListFragment() {
    }

    public static VideoListFragment newInstance(String param1, String param2) {
        VideoListFragment fragment = new VideoListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vedio_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        initVideoListPager();
        loadVideoListData();
    }

    private void initVideoListPager() {
        mPagerAdapter = new VideoListPagerAdapter(getChildFragmentManager(), null);
        mVideoListPager.setAdapter(mPagerAdapter);
    }

    /**
     * 请求视频地址
     */
    private void loadVideoListData() {
        String url = String.format(AppConstants.VIDEO_URL, mActivity.validateCode);
        executeRequest(new Request4VideoInfos(url,
                new Response.Listener<ArrayList<VideoInfo>>() {
                    @Override
                    public void onResponse(ArrayList<VideoInfo> data) {
                        for (VideoInfo vi :
                                data) {
                            JLog.e(vi.toString());
                        }
                        List<List<VideoInfo>> pagerData = Util.groupByColumnRow(data, 2, 3);
                        mPagerAdapter.setPagerDatas(pagerData);
                        mPagerAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Crouton.showText(mActivity, "加载出错，请重试！", Style.ALERT);
                    }
                }
        ));
    }

    private class VideoListPagerAdapter extends FragmentPagerAdapter {

        public void setPagerDatas(List<List<VideoInfo>> pagerDatas) {
            this.mPagerDatas = pagerDatas;
        }

        private List<List<VideoInfo>> mPagerDatas;

        public VideoListPagerAdapter(FragmentManager fm, List<List<VideoInfo>> pagerDatas) {
            super(fm);
            this.mPagerDatas = (pagerDatas == null) ? new ArrayList<List<VideoInfo>>() :
                    new ArrayList<>(pagerDatas);
        }

        @Override
        public Fragment getItem(int position) {
            return VideoPagerFragment.newInstance(mPagerDatas.get(position), mPagerDatas, position);
        }

        @Override
        public int getCount() {
            return mPagerDatas.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mVideoListPager.removeAllViews();
    }
}
