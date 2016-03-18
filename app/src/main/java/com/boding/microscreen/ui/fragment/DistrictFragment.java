package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.ImageInfo;
import com.boding.microscreen.net.Request4ImageInfos;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.ShowToast;
import com.boding.microscreen.widget.refreshview.DividerItemDecoration;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 商圈
 * Created by Administrator on 2015/4/3.
 */
public class DistrictFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "PictureFragment";

    private static final String PARAMS_KEY = "params";

    private HomeActivity mActivity;
    private String params;

    private TextView mTvEmpty;
    private FrameLayout mFLPicture;
    private ImageButton mIBtnPre, mIBtnStart, mIBtnNext;
    private RecyclerView mRvShowList;
    private RecyclerView mRvShowImage;
    private ShowImageAdapter mShowImageAdapter;
    private ListInfoAdapter mListInfoAdapter;

    public DistrictFragment() {

    }

    /**
     * 实例化一个留言墙Fragment的对象
     *
     * @param params
     * @return
     */
    public static DistrictFragment newInstance(String params) {
        DistrictFragment fragment = new DistrictFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAMS_KEY, params);
        fragment.setArguments(bundle);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            params = getArguments().getString(PARAMS_KEY);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_fragment_district, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvEmpty = (TextView) view.findViewById(R.id.id_tv_empty);
        mFLPicture = (FrameLayout) view.findViewById(R.id.id_fl_picture);
        mFLPicture.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mFLPicture.getViewTreeObserver().removeOnPreDrawListener(this);
                int width = mFLPicture.getMeasuredWidth();
                int height = mFLPicture.getMeasuredHeight();
                mShowImageAdapter = new ShowImageAdapter(null);
                mShowImageAdapter.setItemWidth(width);
                mShowImageAdapter.setItemHeight(height);
                mRvShowImage.setAdapter(mShowImageAdapter);
                return true;
            }
        });
        mIBtnPre = (ImageButton) view.findViewById(R.id.id_btn_pre);
//        mIBtnPre.requestFocus();
        mIBtnPre.setOnClickListener(this);

        mIBtnPre.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()){
                    case KeyEvent.ACTION_UP:
                        break;
                    case KeyEvent.ACTION_DOWN:
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_UP){
                            if (mListInfoAdapter.getItemCount() != 0){
                                mListInfoAdapter.requestFocusItem();
                            }else {
                                return true;
                            }
                        }
                        break;
                }
                return false;
            }
        });
        mIBtnStart = (ImageButton) view.findViewById(R.id.id_btn_stop);
        mIBtnStart.setOnClickListener(this);
        mIBtnNext = (ImageButton) view.findViewById(R.id.id_btn_next);
        mIBtnNext.setOnClickListener(this);

        mRvShowList = (RecyclerView) view.findViewById(R.id.id_rcv_contain);
//        mRvShowList.requestFocus();
        mRvShowList.setHasFixedSize(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mRvShowList.setLayoutManager(layoutManager);
        mRvShowList.setItemAnimator(new DefaultItemAnimator());
        mRvShowList.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        mListInfoAdapter = new ListInfoAdapter(null);
        mRvShowList.setAdapter(mListInfoAdapter);
        mListInfoAdapter.setOnClickItemListener(new OnClickItemListener() {
            @Override
            public void onClickItem(View view, int position, ImageInfo imageInfo) {
                // 删除mHandler图片播放对象
                mHandler.removeCallbacks(mRunnable);
                // 刷新RecyclerViewAdapter数据
                MyLog.e(TAG, "position: " + (position+1) + ", imageInfo: " + imageInfo.toString());
                if (null == imageInfo.getUrls() || imageInfo.getUrls().isEmpty()){
                    mTvEmpty.setVisibility(View.VISIBLE);
                    mTvEmpty.setText("没有图片信息");
                }else {
                    mTvEmpty.setVisibility(View.GONE);
                }
                mShowImageAdapter.updateAll(imageInfo.getUrls());
                // 启动mHandler图片播放对象
//                mCurrentPosition = 0;
                mRvShowImage.scrollToPosition(0);
                if(isStart){// 如果允许自动播放，则开启自动播放
                    mHandler.postDelayed(mRunnable, DELAY_TIME);
                }
            }
        });
        String url = String.format(AppConstants.DISTRICT_URL,mActivity.validateCode);
        // 网络请求加载数据
        mListInfoAdapter.loadData(url);
        mRvShowImage = (RecyclerView) view.findViewById(R.id.id_vp_photo);
        mRvShowImage.setHasFixedSize(true);
        RecyclerView.LayoutManager llManager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.HORIZONTAL, false);
        mRvShowImage.setLayoutManager(llManager);
        mRvShowImage.setItemAnimator(new DefaultItemAnimator());
        mRvShowImage.setFocusable(false);
    }

    /**
     * 指定的View获取焦点
     */
    public int requestFocuseView(){
        return mIBtnPre.getId();
    }

    // 是否开始轮播图片
    private boolean isStart = true;

    @Override
    public void onClick(View v) {
        if (0 != mShowImageAdapter.getItemCount()){
            LinearLayoutManager llManager = (LinearLayoutManager) mRvShowImage.getLayoutManager();
            switch (v.getId()) {// 上一张
                case R.id.id_btn_pre:
                    int prePosition = llManager.findFirstCompletelyVisibleItemPosition() - 1;
                    if (prePosition != -1) {
                        mHandler.removeCallbacks(mRunnable);
                        mRvShowImage.smoothScrollToPosition(prePosition);
                        if (isStart){// 如果允许自动播放，则开启自动播放
                            mHandler.postDelayed(mRunnable, DELAY_TIME);
                        }
                    } else {
                        ShowToast.Short("已经是第一张");
                    }
                    break;
                case R.id.id_btn_stop:// 自动播放开关
                    int resId;
                    if (isStart) {
                        mHandler.removeCallbacks(mRunnable);
                        resId = R.drawable.start_play;
                        isStart = false;
                    } else {
                        mHandler.post(mRunnable);
                        resId = R.drawable.stop_play;
                        isStart = true;
                    }
                    mIBtnStart.setImageDrawable(getResources().getDrawable(resId));

                    break;
                case R.id.id_btn_next:// 下一张
                    int nextPosition = llManager.findFirstCompletelyVisibleItemPosition() + 1;
                    if (nextPosition != mShowImageAdapter.getItemCount()) {
                        mHandler.removeCallbacks(mRunnable);
                        mRvShowImage.smoothScrollToPosition(nextPosition);
                        if (isStart){// 如果允许自动播放，则开启自动播放
                            mHandler.postDelayed(mRunnable, DELAY_TIME);
                        }
                    } else {
                        ShowToast.Short("已经是最后一张");
                    }
                    break;
            }
        }else {
            Crouton.showText(mActivity, "抱歉没有图片信息", Style.ALERT);
        }
    }

    /**
     * 循环自动滚动活动列表
     **/
    private Handler mHandler = new Handler();
    private MyRunnable mRunnable = new MyRunnable(this);

    private static class MyRunnable implements Runnable {
        WeakReference<DistrictFragment> weakActivity;

        public MyRunnable(DistrictFragment fragment) {
            weakActivity = new WeakReference<>(fragment);
        }

        @Override
        public void run() {
            DistrictFragment outClass = weakActivity.get();
            if (outClass != null) {
                outClass.recycleListItem();
            }
        }
    }

    private static final int DELAY_TIME = 5000;
    private void recycleListItem() {

        int itemCount = mShowImageAdapter.getItemCount();
        LinearLayoutManager llManager = (LinearLayoutManager) mRvShowImage.getLayoutManager();
        int nextPosition = llManager.findFirstCompletelyVisibleItemPosition() + 1;
        if (nextPosition <= itemCount - 1) {
            mRvShowImage.smoothScrollToPosition(nextPosition);
        } else {
            mRvShowImage.scrollToPosition(0);
        }
        mHandler.postDelayed(mRunnable, DELAY_TIME);
    }

    /**
     * 分类列表适配器
     */
    private class ListInfoAdapter extends RecyclerView.Adapter<ViewHolder> {

        private ArrayList<ImageInfo> mImageInfos;
        private ViewHolder mViewHolder;

        public ListInfoAdapter(ArrayList<ImageInfo> imageInfos) {
            this.mImageInfos = (imageInfos == null ? new ArrayList<ImageInfo>()
                    : new ArrayList<>(imageInfos));
        }

        public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
            this.mOnClickItemListener = onClickItemListener;
        }

        private OnClickItemListener mOnClickItemListener;


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_district_list_item, parent, false);
            mViewHolder = new ViewHolder(view);
            return mViewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final ImageInfo imageInfo = mImageInfos.get(position);
            holder.mBtnChannelItem.setText(imageInfo.getTypeName());
            holder.mLLChannelItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnClickItemListener != null) {
                        mOnClickItemListener.onClickItem(view, position, imageInfo);
                    }
                }
            });
            // 分类列表item遥控器按键焦点处理
            holder.mLLChannelItem.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    switch (event.getAction()){
                        case KeyEvent.ACTION_UP:
                            break;
                        case KeyEvent.ACTION_DOWN:
                            if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                                    keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                                holder.mLLChannelItem.setNextFocusRightId(mIBtnPre.getId());
                                if (mImageInfos.size() - 1 == position){
                                    holder.mLLChannelItem.setNextFocusDownId(mIBtnPre.getId());
                                }
                            }
                            break;
                    }
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mImageInfos.size();
        }

        private void requestFocusItem(){
            mViewHolder.mLLChannelItem.requestFocus();
        }

        private void loadData(String url) {
            executeRequest(new Request4ImageInfos(url, new Response.Listener<ArrayList<ImageInfo>>() {
                @Override
                public void onResponse(ArrayList<ImageInfo> response) {
                    mImageInfos.addAll(response);
                    notifyDataSetChanged();
                    if (response.isEmpty()){
                        Crouton.showText(mActivity, "没有图片信息", Style.INFO);
                        mTvEmpty.setVisibility(View.VISIBLE);
                        mTvEmpty.setText("没有图片信息");
                        return;
                    }
                    if (null == response.get(0).getUrls() || response.get(0).getUrls().isEmpty()){
                        mTvEmpty.setVisibility(View.VISIBLE);
                        mTvEmpty.setText("没有图片信息");
                    }else {
                        mTvEmpty.setVisibility(View.GONE);
                    }
                    boolean isAutoPlay = response.get(0).getIsAutoPlay() == 1;
                    initShowImage(isAutoPlay, response.get(0).getUrls(), response.get(0).getSwitcherWay());
                }
            }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    MyLog.e(TAG, "error：" + error.getMessage());
                    Crouton.showText(mActivity, "网络出错", Style.ALERT);
                }
            }));
        }
    }

    /**
     * 初始化图片轮播视图，默认显示第一项分类图片
     */
    private void initShowImage(boolean isAutoScroll, ArrayList<String> urls, String switcherWay) {
        // 判断后台配置的图片切换方式
        LinearLayoutManager llManager = (LinearLayoutManager) mRvShowImage.getLayoutManager();
        if ("up".equals(switcherWay)) { // 上下切换
            llManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        if ("left".equals(switcherWay)) { // 左右切换
            llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        mShowImageAdapter.updateAll(urls);
        isStart = isAutoScroll;
        if (isAutoScroll){
            mHandler.postDelayed(mRunnable, DELAY_TIME);
        }
    }

    public interface OnClickItemListener {
        void onClickItem(View view, int position, ImageInfo imageInfo);
    }

    /**
     * 分类列表ViewHolder
     */
    private static class ViewHolder extends RecyclerView.ViewHolder {

        private Button mBtnChannelItem;
        private LinearLayout mLLChannelItem;

        public ViewHolder(View itemView) {
            super(itemView);
            mBtnChannelItem = (Button) itemView.findViewById(R.id.id_btn_channel_item);
            mLLChannelItem = (LinearLayout) itemView.findViewById(R.id.id_ll_channel_item);
        }
    }

    /**
     * 轮播图片列表适配器
     */
    private class ShowImageAdapter extends RecyclerView.Adapter<ShowImageViewHolder> {

        private ArrayList<String> mImageUrls;
        private int mItemWidth;
        private int mItemHeight;
        private LinearLayoutManager mLLManager;

        public void setItemHeight(int itemHeight) {
            this.mItemHeight = itemHeight;
        }

        public void setItemWidth(int itemWidth) {
            this.mItemWidth = itemWidth;
        }

        public ShowImageAdapter(ArrayList<String> imageUrls) {
            this.mImageUrls = (imageUrls == null ? new ArrayList<String>()
                    : new ArrayList<>(imageUrls));
            mLLManager = (LinearLayoutManager) mRvShowImage.getLayoutManager();
        }

        @Override
        public ShowImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_district_list_show_imge_item, parent, false);

            return new ShowImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ShowImageViewHolder holder, int position) {
            int orientation = mLLManager.getOrientation();
            if (LinearLayoutManager.HORIZONTAL == orientation) {
                holder.mLLImage.setMinimumWidth(mItemWidth);
            } else if (LinearLayoutManager.VERTICAL == orientation) {
                holder.mLLImage.setMinimumHeight(mItemHeight);
            }
            if (null != mImageUrls && 0 != mImageUrls.size()) {
                String imageUrl = mImageUrls.get(position);
//                MyLog.e(TAG, "imageUrl=" + imageUrl);
//                Uri uri = Uri.parse(imageUrl);
                /*if(imageUrl.endsWith(".gif") || imageUrl.endsWith(".GIF")){
                    MyLog.e(TAG, "imageUrl: " + imageUrl);
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setAutoPlayAnimations(true)
                    .build();
                    holder.mIvImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    holder.mIvImage.setController(controller);
                }else {
                    holder.mIvImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                    holder.mIvImage.setImageURI(uri);
                }*/
                if(imageUrl.endsWith(".gif") || imageUrl.endsWith(".GIF")){
                    MyLog.e(TAG, "imageUrl: " + imageUrl);
                    Glide.with(DistrictFragment.this)
                            .load(imageUrl)
                            .crossFade()
                            .fitCenter()
                            .into(holder.mIvImage);
                }else {
                    Glide.with(DistrictFragment.this)
                            .load(imageUrl)
                            .override(mItemWidth, mItemHeight)
                            .crossFade()
                            .fitCenter()
                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                            .into(holder.mIvImage);
                }
            }
        }

        @Override
        public int getItemCount() {
            return mImageUrls.size();
        }

        /**
         * 更新所有的数据列表
         *
         * @param elements 更新的数据
         */
        public void updateAll(List<String> elements) {
            this.mImageUrls.clear();
            this.mImageUrls.addAll(elements);
            notifyDataSetChanged();
        }
    }

    /**
     * 轮播图片的ViewHolder
     */
    private class ShowImageViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLLImage;
        private ImageView mIvImage;
//        private SimpleDraweeView mIvImage;

        public ShowImageViewHolder(View itemView) {
            super(itemView);
            mLLImage = (LinearLayout) itemView.findViewById(R.id.id_ll_show_image);
            mIvImage = (ImageView) itemView.findViewById(R.id.id_iv_image);
//            mIvImage = (SimpleDraweeView) itemView.findViewById(R.id.id_iv_image);
        }
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
    public void onDestroyView() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (mRunnable != null) {
            mRunnable = null;
        }
        if (mListInfoAdapter != null){
            mListInfoAdapter = null;
        }
        if(mRvShowList != null){
            mRvShowList.removeAllViews();
            mRvShowList = null;
        }
        if (mShowImageAdapter != null){
            mShowImageAdapter = null;
        }
        if(mRvShowImage != null){
            mRvShowImage.removeAllViews();
            mRvShowImage = null;
        }
        RequestManager.cancelAll(this);
        System.gc();
        super.onDestroyView();
    }
}