package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.adapter.GoodsTypeAdapter;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.listener.IItemClickListener;
import com.boding.microscreen.model.Goods;
import com.boding.microscreen.model.GoodsGroup;
import com.boding.microscreen.model.GoodsInfo;
import com.boding.microscreen.net.Request4GoodsList;
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
 * detail:
 * Created by libq
 * 2015/5/12#17:01
 */

public class GoodsFragment extends BaseFragment {
    private HomeActivity mActivity;
    private ImageButton btnImgPre, btnImgNext, btnImgStop;
    public static GoodsFragment newInstance(String params) {
        GoodsFragment fragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("params", params);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_good, container, false);
    }


    private RecyclerView recyclerView;
    private RecyclerView rcvGoods;
    private GoodsTypeAdapter gta;
    private TextView tvGoodsInfo;
    private ImageView ivGoodsQrCode;
    private FrameLayout mFLPicture;
    private ShowImageAdapter mShowImageAdapter;
    // 是否开始轮播图片
    private boolean isStart = true;

    private int PLAY_DELATE_TIME = 5000;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnImgNext = (ImageButton) view.findViewById(R.id.btn_goods_next);
        btnImgPre = (ImageButton) view.findViewById(R.id.btn_goods_pre);
        btnImgStop = (ImageButton) view.findViewById(R.id.btn_goods_stop);

        tvGoodsInfo = (TextView) view.findViewById(R.id.tv_goods_detail);
        ivGoodsQrCode = (ImageView) view.findViewById(R.id.iv_goods_qrcode);

        recyclerView = (RecyclerView) view.findViewById(R.id.rec_goods_type);
        rcvGoods = (RecyclerView) view.findViewById(R.id.vp_goods_photo);

        RecyclerView.LayoutManager llManager = new LinearLayoutManager(mActivity,
                LinearLayoutManager.HORIZONTAL, false);
        rcvGoods.setLayoutManager(llManager);
        rcvGoods.setItemAnimator(new DefaultItemAnimator());
        rcvGoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                setGoodsDetail();
            }
        });

        gta = new GoodsTypeAdapter(null);
        gta.setItemClickListener(new IItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Object obj, Object obj2) {
                if (obj == null || obj2 == null) {
                    Crouton.showText(mActivity, "没有商品信息", Style.ALERT);
                    return;
                }

                ArrayList<String> imgUrls = (ArrayList<String>) obj;
                ArrayList<Goods> goodses = (ArrayList<Goods>) obj2;
                // 删除mHandler图片播放对象
                mHandler.removeCallbacksAndMessages(null);
                // 刷新RecyclerViewAdapter数据
                mShowImageAdapter.updateAll(imgUrls, goodses);
                // 启动mHandler图片播放对象
                mCurrentPosition = 0;
                rcvGoods.scrollToPosition(0);
                if (isStart){
                    mHandler.postDelayed(mRunnable, PLAY_DELATE_TIME);
                }
            }
        });

        recyclerView.setAdapter(gta);
        loadGoodsTypeInfo();
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(mActivity, DividerItemDecoration.VERTICAL_LIST));
        //-------------------------------------------
        mFLPicture = (FrameLayout) view.findViewById(R.id.id_fl_picture);
        mFLPicture.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mFLPicture.getViewTreeObserver().removeOnPreDrawListener(this);
                int width = mFLPicture.getMeasuredWidth();
                int height = mFLPicture.getMeasuredHeight();
                mShowImageAdapter = new ShowImageAdapter(null, null);
                mShowImageAdapter.setItemWidth(width);
                mShowImageAdapter.setItemHeight(height);
                rcvGoods.setAdapter(mShowImageAdapter);
                return true;
            }
        });


        final LinearLayoutManager layoutManager = (LinearLayoutManager) rcvGoods.getLayoutManager();
        //添加图片控制点击事件
        btnImgNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 != mShowImageAdapter.getItemCount()){
                    int nextPosition = layoutManager.findFirstCompletelyVisibleItemPosition() + 1;
                    if (nextPosition != mShowImageAdapter.getItemCount()) {
                        mHandler.removeCallbacks(mRunnable);
    //                        ShowToast.Short("position2: " + mCurrentPosition);
                        rcvGoods.smoothScrollToPosition(nextPosition);
                        if (isStart) {// 如果允许自动播放，则开启自动播放
                            mHandler.postDelayed(mRunnable, PLAY_DELATE_TIME);
                        }
                    } else {
                        ShowToast.Short("已经是最后一张");
                    }
                }else{
                    Crouton.showText(mActivity, "抱歉没有商品信息", Style.ALERT);
                }
            }
        });
//        btnImgPre.requestFocus();
        gta.requestFocusViewCallBack(btnImgPre);
        btnImgPre.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()){
                    case KeyEvent.ACTION_UP:
                        break;
                    case KeyEvent.ACTION_DOWN:
                        if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT || keyCode == KeyEvent.KEYCODE_DPAD_UP){
                            if (gta.getItemCount() != 0){
                                gta.requestFocusItem();
                            }else {
                                return true;
                            }
                        }
                        break;
                }
                return false;
            }
        });

        btnImgPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 != mShowImageAdapter.getItemCount()){
                    int prePosition = layoutManager.findFirstCompletelyVisibleItemPosition() - 1;
                    if (prePosition != -1) {
                        mHandler.removeCallbacks(mRunnable);
    //                        ShowToast.Short("position1: " + mCurrentPosition);
                        rcvGoods.smoothScrollToPosition(prePosition);
                        if (isStart) {// 如果允许自动播放，则开启自动播放
                            mHandler.postDelayed(mRunnable, PLAY_DELATE_TIME);
                        }
                    } else {
                        ShowToast.Short("已经是第一张");
                    }
                }else{
                    Crouton.showText(mActivity, "抱歉没有商品信息", Style.ALERT);
                }
            }
        });

        btnImgStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (0 != mShowImageAdapter.getItemCount()){
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
                    ((ImageButton) view).setImageDrawable(getResources().getDrawable(resId));
                }else{
                    Crouton.showText(mActivity, "抱歉没有商品信息", Style.ALERT);
                }
            }
        });
    }

    /**
     * 指定的View获取焦点
     */
    public int requestFocuseView(){
        return btnImgPre.getId();
    }

    /**
     * 加载商品类型数据
     */
    private void loadGoodsTypeInfo() {
      /*  MyLog.e("Gf", "loadGoodsTypeInfo");
        String url = String.format(AppConstants.GOODS_DATA_URL, mActivity.validateCode);
        executeRequest(new Request4GoodsList(
                url,
                new Response.Listener<GoodsInfo>() {
                    @Override
                    public void onResponse(GoodsInfo response) {
                        ArrayList<GoodsGroup> goodsGroups = response.getGoodsGroup();
                        if (goodsGroups == null){
                            Crouton.showText(mActivity, "没有图片信息", Style.INFO);
                        }
                        gta.updateDataSet(response.getGoodsGroup());
                        //默认获取第一个类型商品进行加载
                        if (goodsGroups != null && !goodsGroups.isEmpty()) {
                            boolean isAutoPlay = response.getIsAutoPlay()==1;
                            loadDefaultGoodsData(isAutoPlay, response.getSwitcherWay(), goodsGroups.get(0));
                        }
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Crouton.showText(mActivity, "网络出错，请重试！", Style.ALERT);
                        MyLog.e("GoodaFragment", "error" + error.getMessage());
                    }
                }
        ));
*/
    }

    /**
     * 设置商品信息描述
     */
    private void setGoodsDetail() {
        String showing = tvGoodsInfo.getText().toString();
        LinearLayoutManager llManager = (LinearLayoutManager) rcvGoods.getLayoutManager();
        int index = llManager.findFirstVisibleItemPosition();
       // String ready = mShowImageAdapter.getGoodsList().get(index).getDetail();
        String ready = "";
        if (!showing.equals(ready)) {
            tvGoodsInfo.setText(ready);
        }

        String qrCodeUrl = mShowImageAdapter.getGoodsList().get(index).getQrCode();
        Glide.with(GoodsFragment.this)
                .load(qrCodeUrl)
                .fitCenter()
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivGoodsQrCode);
    }

    /**
     * 加载默认商品信息（取第一个）
     *
     * @param group
     */
    private void loadDefaultGoodsData(boolean isAutoPlay, String switcherWay, GoodsGroup group) {

        // 判断后台配置的图片切换方式
        LinearLayoutManager llManager = (LinearLayoutManager) rcvGoods.getLayoutManager();
        if ("up".equals(switcherWay)) { // 上下切换
            llManager.setOrientation(LinearLayoutManager.VERTICAL);
        }
        if ("left".equals(switcherWay)) { // 左右切换
            llManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        }
        ArrayList<String> imageUrl = gta.getAllImgUrl(group);
        // 删除mHandler图片播放对象
        mHandler.removeCallbacksAndMessages(null);
        // 刷新RecyclerViewAdapter数据
        //mShowImageAdapter.updateAll(imageUrl, group.getGoods());
        // 启动mHandler图片播放对象
        mCurrentPosition = 0;
        isStart = isAutoPlay;
        if (isAutoPlay){
            mHandler.postDelayed(mRunnable, PLAY_DELATE_TIME);
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
    public void onDetach() {
        super.onDetach();
    }


    /**
     * 循环自动滚动活动列表
     **/
    private Handler mHandler = new Handler();
    private MyRunnable mRunnable = new MyRunnable(this);

    private static class MyRunnable implements Runnable {
        WeakReference<GoodsFragment> weakActivity;

        public MyRunnable(GoodsFragment fragment) {
            weakActivity = new WeakReference<GoodsFragment>(fragment);
        }

        @Override
        public void run() {
            GoodsFragment outClass = weakActivity.get();
            if (outClass != null) {
                outClass.recycleListItem();
            }
        }
    }

    int scrollDx = 0;
    int mCurrentPosition = 0;
    int currIndex = 0;

    /*private void recycleListItem() {

        int itemCount = mShowImageAdapter.getItemCount();
        int delayeTime = 5000;
        if (mCurrentPosition <= itemCount - 1) {
            rcvGoods.smoothScrollToPosition(mCurrentPosition);
            currIndex = mCurrentPosition;
            mCurrentPosition += 1;
        } else {
            rcvGoods.scrollToPosition(0);
            currIndex = 0;
            mCurrentPosition = 1;
        }
        mHandler.postDelayed(mRunnable, delayeTime);
    }*/

    private void recycleListItem() {

        int itemCount = mShowImageAdapter.getItemCount();
        LinearLayoutManager llManager = (LinearLayoutManager) rcvGoods.getLayoutManager();
        int nextPosition = llManager.findFirstCompletelyVisibleItemPosition() + 1;
        if (nextPosition <= itemCount - 1) {
            rcvGoods.smoothScrollToPosition(nextPosition);
        } else {
            rcvGoods.scrollToPosition(0);
        }
        mHandler.postDelayed(mRunnable, PLAY_DELATE_TIME);
    }

    /**
     * 轮播图片列表适配器
     */
    private class ShowImageAdapter extends RecyclerView.Adapter<ShowImageViewHolder> {

        private ArrayList<String> mImageUrls;
        private int mItemWidth;
        private int mItemHeight;
        private final LinearLayoutManager mLLManager;
        private ArrayList<Goods> mGoodsList;

        public void setItemHeight(int itemHeight) {
            this.mItemHeight = itemHeight;
        }

        public void setItemWidth(int itemWidth) {
            this.mItemWidth = itemWidth;
        }

        public ShowImageAdapter(ArrayList<String> imageUrls, ArrayList<Goods> goodsList) {
            this.mImageUrls = (imageUrls == null ? new ArrayList<String>() : new ArrayList<String>(imageUrls));
            this.mLLManager = (LinearLayoutManager) rcvGoods.getLayoutManager();
            this.mGoodsList = (goodsList == null ? new ArrayList<Goods>() : new ArrayList<Goods>(goodsList));
        }

        public ArrayList<Goods> getGoodsList() {
            return mGoodsList;
        }

        @Override
        public ShowImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.fragment_pic_show_imge_item, parent, false);

            return new ShowImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ShowImageViewHolder holder, int position) {
            int orientation = this.mLLManager.getOrientation();
            if (LinearLayoutManager.HORIZONTAL == orientation) {
                holder.mLLImage.setMinimumWidth(mItemWidth);
            } else if (LinearLayoutManager.VERTICAL == orientation) {
                holder.mLLImage.setMinimumHeight(mItemHeight);
            }
            if (null != mImageUrls && 0 != mImageUrls.size()) {
                String imageUrl = mImageUrls.get(position);

                if(imageUrl.endsWith(".gif") || imageUrl.endsWith(".GIF")){
                    Glide.with(GoodsFragment.this)
                            .load(imageUrl)
                            .crossFade()
                            .fitCenter()
                            .into(holder.mIvImage);
                }else {
                    Glide.with(GoodsFragment.this)
                            .load(imageUrl)
                            .override(mItemWidth, mItemHeight)
                            .fitCenter()
                            .crossFade()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(holder.mIvImage);
                }

                /*Uri uri = Uri.parse(imageUrl);
                if(imageUrl.endsWith(".gif") || imageUrl.endsWith(".GIF")){
                    ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                            .build();
                    DraweeController controller = Fresco.newDraweeControllerBuilder()
                            .setImageRequest(request)
                            .setAutoPlayAnimations(true)
                            .build();
                    holder.mIvImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    holder.mIvImage.setController(controller);
                }else {
                    holder.mIvImage.setImageURI(uri);
                }*/
                tvGoodsInfo.setText("");
               // MyLog.e("onBindViewHolder", "position =" + position + ";de=" + mGoodsList.get(position).getDetail());
            }
        }

        @Override
        public int getItemCount() {
            return mImageUrls.size();
        }

        /**
         * 更新所有的数据列表
         *
         * @param elements
         */
        public void updateAll(List<String> elements, ArrayList<Goods> goodses) {
            this.mImageUrls.clear();
            this.mImageUrls.addAll(elements);
            this.mGoodsList.clear();
            this.mGoodsList.addAll(goodses);
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
        if (gta != null){
            gta = null;
        }
        if(recyclerView != null){
            recyclerView.removeAllViews();
            recyclerView = null;
        }
        if (mShowImageAdapter != null){
            mShowImageAdapter = null;
        }
        if(rcvGoods != null){
            rcvGoods.removeAllViews();
            rcvGoods = null;
        }
        RequestManager.cancelAll(this);
        System.gc();
        super.onDestroyView();
    }
}
