package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.boding.microscreen.adapter.ImagePagerAdapter;
import com.boding.microscreen.model.Goods;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.MyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by libq on 2015/12/25.
 * 商圈商品详情
 */
public class BusinessGoodsDetailFragment extends BaseFragment {
    private Goods goods;
    private HomeActivity mActivity;
    private static final String GOODS = "goods";


    @Bind(R.id.btn_goodsdetail_back)
    Button btnBack;
    @Bind(R.id.tv_business_detail)
    TextView tvDetailInfo;
    @Bind(R.id.iv_goods_thumbnail)
    ImageView ivGoodsThumbnail;
    @Bind(R.id.btn_goods_img_more)
    Button btnMoreImg;
    @Bind(R.id.iv_goods_qrcode)
    ImageView ivGoodsQrcode;

    private Fragment mFragment;


    public void setFatherFragment(Fragment mFragment) {
        this.mFragment = mFragment;
    }

    public BusinessGoodsDetailFragment getInstance() {

        BusinessGoodsDetailFragment f = new BusinessGoodsDetailFragment();

        return f;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this.goods = (Goods)getArguments().getSerializable(GOODS);

        MyLog.e("GoodsDetailFragment onCreate", "" + goods);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_business_detail, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {

        tvDetailInfo.setText(goods.getDescription());
        Glide.with(BusinessGoodsDetailFragment.this)
                .load(goods.getThumbnailUrl())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivGoodsThumbnail);
        /*String qrcodeUrl = goods.getQrCode();
        if (!TextUtils.isEmpty(qrcodeUrl)){
            Glide.with(BusinessGoodsDetailFragment.this)
                    .load(qrcodeUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                            return true;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return true;
                        }
                    })
                    .into(ivGoodsQrcode);
        }*/
        btnMoreImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMoreImgDialog();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((HomeActivity) getActivity()).hideFragment(BusinessGoodsDetailFragment.this);
                ((HomeActivity) getActivity()).showFragment(R.id.id_ll_fragment_container, mFragment);
            }
        });

    }

    private int curPageIndex;//当前page索引
    private Dialog dialog = null;

    private void showMoreImgDialog() {

        if (dialog == null) {
            AlertDialog.Builder b = new AlertDialog.Builder(mActivity, R.style.dialog);
            View v = LayoutInflater.from(mActivity).inflate(R.layout.dialog_goods_img, null);
            Button btnPre = (Button) v.findViewById(R.id.btn_goods_img_pre);
            Button btnNext = (Button) v.findViewById(R.id.btn_goods_img_next);
            final ViewPager vp = (ViewPager) v.findViewById(R.id.vp_goods_img_content);
            btnPre.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    curPageIndex--;
                    if (curPageIndex < 0) {
                        curPageIndex = 0;
                    }
                    vp.setCurrentItem(curPageIndex);
                }
            });
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    curPageIndex++;
                    if (curPageIndex > goods.getImageUrls().size() - 1) {
                        curPageIndex = goods.getImageUrls().size() - 1;
                    }
                    vp.setCurrentItem(curPageIndex);
                }
            });
            vp.setAdapter(new ImagePagerAdapter(goods.getImageUrls(), mActivity));
            vp.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    curPageIndex = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            b.setView(v);

            dialog = b.create();

            //dialog.getWindow().setContentView(v);
        }

        dialog.show();

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
}
