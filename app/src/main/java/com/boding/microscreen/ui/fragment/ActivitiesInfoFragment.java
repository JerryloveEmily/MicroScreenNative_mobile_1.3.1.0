package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.ActivitiesInfo;
import com.boding.microscreen.model.ActivitiesItem;
import com.boding.microscreen.net.Request4ActivityInfo;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.ShowToast;
import com.boding.microscreen.widget.MarqueeTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 活动信息
 *
 * @author Jerry
 */
public class ActivitiesInfoFragment extends BaseFragment {
    private static final String TAG = "ActivitiesInfoFragment";

    private static final String PARAMS_KEY = "params";

    private String params;

    private HomeActivity mActivity;
    private RecyclerView mRecyclerView;

    private InfoAdapter mInfoAdapter;

    public ActivitiesInfoFragment() {

    }

    /**
     * 实例化一个活动信息Fragment的对象
     *
     * @param params
     * @return
     */
    public static ActivitiesInfoFragment newInstance(String params) {
        ActivitiesInfoFragment fragment = new ActivitiesInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_KEY, params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            params = getArguments().getString(PARAMS_KEY);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HomeActivity) {
            mActivity = (HomeActivity) activity;
        } else {
            ShowToast.Long("切换显示出错，请重启应用！");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_activities_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.id_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                mInfoAdapter = new InfoAdapter(null);
                mInfoAdapter.mItemWidth = mRecyclerView.getMeasuredWidth();
                mRecyclerView.setAdapter(mInfoAdapter);
                // 载入数据
                String url = String.format(AppConstants.ACTIVITIES_URL, mActivity.validateCode, -1);
                mInfoAdapter.loadData(url);
                return true;
            }
        });
    }

    /**
     * 循环自动滚动活动列表
     **/
    private Handler mHandler = new Handler();
    private MyRunnable mRunnable = new MyRunnable(this);

    private static class MyRunnable implements Runnable {
        WeakReference<ActivitiesInfoFragment> weakActivity;

        public MyRunnable(ActivitiesInfoFragment fragment) {
            weakActivity = new WeakReference<>(fragment);
        }

        @Override
        public void run() {
            ActivitiesInfoFragment outClass = weakActivity.get();
            if (outClass != null) {
                outClass.recycleListItem();
            }
        }
    }

    // 15s自动播放到下一个活动信息
    private static final int DELAY_TIME = 15000;
    private void recycleListItem() {

        int itemCount = mInfoAdapter.getItemCount();
        LinearLayoutManager llManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        int nextPosition = llManager.findFirstCompletelyVisibleItemPosition() + 1;
        if (nextPosition <= itemCount - 1) {
            mRecyclerView.smoothScrollToPosition(nextPosition);
        } else {
            mRecyclerView.scrollToPosition(0);
        }
        mHandler.postDelayed(mRunnable, DELAY_TIME);
    }

    public class InfoAdapter extends RecyclerView.Adapter<ViewHolder> {

        public ArrayList<ActivitiesItem> mActivitiesInfos;
        private ViewHolder mViewHolder;
        private int mItemWidth;

        public InfoAdapter(ArrayList<ActivitiesItem> datas) {
            this.mActivitiesInfos = (null == datas ?
                    new ArrayList<ActivitiesItem>() : new ArrayList<>(datas));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_activities_info_item, parent, false);
            mViewHolder = new ViewHolder(v);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ActivitiesItem itemInfo = mActivitiesInfos.get(position);
            holder.mLLItem.setMinimumWidth(mItemWidth);
//            LogUtils.d(itemInfo.toString());
            holder.mTvTitle.setText(itemInfo.getTitle().trim());
            holder.mMarqueeTextView.setText(itemInfo.getContent().trim());
            Glide.with(ActivitiesInfoFragment.this)
                    .load(itemInfo.getImage())
                    .error(R.drawable.module_bg)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.mIvImage);
        }

        private void requestFocusItem(){
            mViewHolder.mLLItem.requestFocus();
        }

        @Override
        public int getItemCount() {
            return mActivitiesInfos.size();
        }

        /**
         * 载入数据
         */
        public void loadData(String url) {
            executeRequest(new Request4ActivityInfo(url, new Response.Listener<ActivitiesInfo>() {
                @Override
                public void onResponse(ActivitiesInfo response) {
                    if (response == null || response.getActivityMessages().isEmpty()){
                        Crouton.showText(mActivity, "没有内容", Style.INFO);
                        return;
                    }
                    /*for (ActivitiesItem ai : response.getActivityMessages()) {
                        JLog.d(ai.toString());
                    }*/
                    mActivitiesInfos.clear();
                    mActivitiesInfos.addAll(response.getActivityMessages());
                    notifyDataSetChanged();
                    // 如果允许自动播放，则开启自动播放
                    mHandler.postDelayed(mRunnable, DELAY_TIME);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    MyLog.e(TAG, error.getMessage());
                    Crouton.showText(mActivity, "网络出错", Style.ALERT);
                }
            }));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTitle;
        private LinearLayout mLLItem;
        private MarqueeTextView mMarqueeTextView;
        private ImageView mIvImage;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_activity_title);
            mLLItem = (LinearLayout) itemView.findViewById(R.id.id_ll_item);
            mMarqueeTextView = (MarqueeTextView) itemView.findViewById(R.id.id_tv_activity_content);
            mIvImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存的时候清理Glide图片内存缓存:bitmapPool、memoryCache
        Glide.get(mActivity).clearMemory();
    }

    @Override
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mRunnable != null) {
            mRunnable = null;
        }
        if (mInfoAdapter != null) {
            mInfoAdapter = null;
        }
        if (mRecyclerView != null) {
            mRecyclerView.removeAllViews();
            mRecyclerView = null;
        }
        RequestManager.cancelAll(this);
        System.gc();
        super.onDestroyView();
    }
}
