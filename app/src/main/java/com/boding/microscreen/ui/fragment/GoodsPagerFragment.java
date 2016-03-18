package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.boding.microscreen.model.Goods;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.MyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by libq on 2015/12/21.
 * 每个商品的单页
 */
public class GoodsPagerFragment extends BaseFragment{//} implements SwipeRefreshLayout.OnRefreshListener{

    private static final String GOODS_DATA = "goods_data";
    private static final String CONTEXT = "context";
    private static final String ITEM_HEIGHT="item_height";
    private static final String COLUMN ="column";
    private static final String NEWSGOODSFRAGMENT ="newsgoodsfragment";
    private  int column;//行列
    private ArrayList<Goods> mPageData;
    @Bind(R.id.gv_pager_item)
    GridView mGridPager;
    @Bind(R.id.srl_refresh)
    SwipeRefreshLayout mSrlRefresh;
    private LayoutInflater inflater=null;
    private Context context;
    private int itemHeight;
    private NewGoodsFragment mFragment;
    public static GoodsPagerFragment newInstance(Context context,ArrayList<Goods> data,
                                                 int gridColumn,NewGoodsFragment pFragment) {
        GoodsPagerFragment fragment = new GoodsPagerFragment();
        Bundle args = new Bundle();
        MyLog.e("GoodsPagerFragment","newInstance");

        args.putSerializable(GOODS_DATA, data);
        args.putSerializable(NEWSGOODSFRAGMENT,pFragment);
        args.putInt(COLUMN,gridColumn);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mPageData = (ArrayList<Goods>) getArguments().getSerializable(GOODS_DATA);
            itemHeight = getArguments().getInt(ITEM_HEIGHT);
            column = getArguments().getInt(COLUMN);
            mFragment = (NewGoodsFragment)getArguments().getSerializable(NEWSGOODSFRAGMENT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        View root =inflater.inflate(R.layout.fragment_goods_grid_pager, container, false);
        ButterKnife.bind(this, root);
        mGridPager.setNumColumns(column);

        return root;
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
                itemHeight = view.getHeight() / 3 - 20;
                initGridListPager();
            }
        });


    }

    private HomeActivity mActivity;
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

    private MyGridAdapter adapter=null;
    private void initGridListPager(){

        adapter = new MyGridAdapter(mPageData);
        mGridPager.setAdapter(adapter);
        mGridPager.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((HomeActivity)getActivity()).hideFragment(mFragment);
                GoodsDetailFragment fag = new  GoodsDetailFragment().getInstance();
                fag.setGoods(mPageData.get(position));
                fag.setFatherFragment(mFragment);
                ((HomeActivity) getActivity()).showFragment(R.id.id_ll_fragment_container, fag);
                MyLog.e("goodsPagerF","goods=>"+mPageData.get(position));
            }
        });
    }

    /**
     * 初始化加载刷新视图
     *//*
    private void initRefreshView() {
        mSrlRefresh.setColorSchemeResources(android.R.color.holo_orange_light,
                android.R.color.holo_orange_dark,
                android.R.color.holo_orange_dark);
        mSrlRefresh.setOnRefreshListener(this);
        mSrlRefresh.setRefreshing(true);
        onRefresh();
    }*/

    /**
     * grid适配器
     */
     class MyGridAdapter  extends BaseAdapter{
        private List<Goods> goodsList;
        public MyGridAdapter(List<Goods> goodsList ){
            this.goodsList = goodsList;
        }
        @Override
        public int getCount() {
            return goodsList.size();
        }

        @Override
        public Object getItem(int position) {
            return goodsList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view != null) {
                holder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.item_goodspage, parent, false);

                ViewGroup.LayoutParams lp = view.getLayoutParams();
                lp.height = itemHeight;
                holder = new ViewHolder(view);
                view.setTag(holder);
            }
            Goods g = mPageData.get(position);
            holder.tvGoodsName.setText(g.getName());
            holder.tvPrice.setText("￥"+g.getPrice());
//            holder.ivImg.setImageResource(R.drawable.nbjn);
            Glide.with(GoodsPagerFragment.this)
                    .load(g.getThumbnailUrl())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new SimpleTarget<Bitmap>(400, 300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            holder.ivImg.setImageBitmap(resource);
                        }
                    });

            return view;
        }

        class ViewHolder{
            @Bind(R.id.iv_goodspage_img)
            ImageView ivImg;
            @Bind(R.id.tv_goodspage_name)
            TextView tvGoodsName;
            @Bind(R.id.tv_goodspage_price)
            TextView tvPrice;
            public ViewHolder(View view){
                ButterKnife.bind(this, view);
            }
        }
    }//-------------------end adapter----------------

   /* @Override
    public void onRefresh() {

    }//----end onRefresh-------------*/
}
