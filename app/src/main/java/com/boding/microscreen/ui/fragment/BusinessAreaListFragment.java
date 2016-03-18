package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.Goods;
import com.boding.microscreen.model.GoodsGroup;
import com.boding.microscreen.net.Request4GoodsGroup;
import com.boding.microscreen.net.Request4GoodsList;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.widget.pagerecycler.PageIndicatorView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 商圈列表
 * Created by libq on 2015/12/25.
 */
public class BusinessAreaListFragment extends BaseFragment {
    private HomeActivity mActivity;
    @Bind(R.id.tl_tab_container)
    TabLayout mTab;
    @Bind(R.id.vp_goods)ViewPager mGoodsPager;
    @Bind(R.id.piv_indicator)PageIndicatorView mIndicator;
    //页面Fragment容器
    private ArrayList<Fragment> pagerFragment = new ArrayList<Fragment>();

    public static BusinessAreaListFragment newInstance(String params) {
        BusinessAreaListFragment fragment = new BusinessAreaListFragment();
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
        View view = inflater.inflate(R.layout.fragment_new_goods, container, false);
        ButterKnife.bind(this, view);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getBusinessGroup();
        initViewPager();

    }

    //商品分类id
    private String goodsGroupId = NONE_VALUE;
    private static final String NONE_VALUE = "-1";
    /**
     * 获取商圈分类列表
     */
    private void getBusinessGroup() {

        Response.Listener<ArrayList<GoodsGroup>> listener = new Response.Listener<ArrayList<GoodsGroup>>() {
            @Override
            public void onResponse(ArrayList<GoodsGroup> response) {
                setTabButton(response);
                //默认获取第一个分类，并请求数据
                if(!goodsGroupId.equals(NONE_VALUE)){
                    requestBusinessList(goodsGroupId);

                }
                MyLog.e("NewGoodsFragment", "goodsGroupId@@@= " + goodsGroupId);
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crouton.showText(mActivity, "加载出错", Style.ALERT);
            }
        };


        RequestManager.addRequest(new Request4GoodsGroup(errorListener, listener, String.format(AppConstants.BUSINESS_CATEORY,mActivity.validateCode)), mActivity);
    }

    private GoodsPagerAdapter adapter = null;
    /**
     * 获取商品列表
     *
     * @param id    ...
     */
    private void requestBusinessList(String id) {
        Response.Listener<ArrayList<Goods>> listener = new Response.Listener<ArrayList<Goods>>() {
            @Override
            public void onResponse(ArrayList<Goods> response) {
                if (response.size() == 0){
                    Crouton.showText(mActivity, "暂无商圈列表信息!", Style.ALERT);
                }
                if (adapter != null) {
                    adapter.clear();

                }
                adapter = new GoodsPagerAdapter(getChildFragmentManager(), response, 3, 3);
                mGoodsPager.setAdapter(adapter);

            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Crouton.showText(mActivity, "加载出错", Style.ALERT);
            }
        };
        RequestManager.addRequest(new Request4GoodsList(listener, errorListener, String.format(AppConstants.BUSINESS_DATA,mActivity.validateCode,id)), mActivity);

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


    private void initViewPager() {
        mGoodsPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mIndicator.setSelectedPage(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }

    private void setTabButton(ArrayList<GoodsGroup> goodsGroups) {

        //过滤为空情况
        if(goodsGroups!=null&&!goodsGroups.isEmpty()&& goodsGroupId.equals(NONE_VALUE)){
            goodsGroupId = goodsGroups.get(0).getId();
        }

        mTab.setTabTextColors(Color.WHITE, Color.WHITE);
        mTab.setHorizontalScrollBarEnabled(false);

        if (goodsGroups != null){
            for (int i = 0; i < goodsGroups.size(); i++) {

                GoodsGroup g = goodsGroups.get(i);

                TabLayout.Tab tab = mTab.newTab().setText(g.getName());
                tab.setTag("" + g.getId());
                if(goodsGroupId.equals(g.getId())){
                    mTab.addTab(tab,true);
                }else{
                    mTab.addTab(tab);
                }

                // 设置Tablayout的每个tab的样式
                tab.setCustomView(R.layout.fragment_top_tab_item);
                LinearLayout tabStrip = (LinearLayout) mTab.getChildAt(0);
                View tabItemView = tabStrip.getChildAt(i);
                tabItemView.setBackgroundResource(R.drawable.tab_item_bg_normal);
                TextView tvTitle = (TextView) tabItemView.findViewById(android.R.id.text1);
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tvTitle.getLayoutParams();
                params.gravity = Gravity.CENTER;
                tvTitle.setLayoutParams(params);
                tvTitle.setTextSize(30);
                tvTitle.setTextColor(Color.WHITE);
                tvTitle.setSingleLine(true);
                tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                tabItemView.findViewById(android.R.id.icon).setVisibility(View.GONE);
            }
        }
        mTab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String id = (String) tab.getTag();
                goodsGroupId = id;
                clearPageFragment();
                requestBusinessList(id);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    /**
     * 清理页面中的Fragment对象
     */
    private void clearPageFragment(){
        FragmentManager fm = getChildFragmentManager();
        if(!pagerFragment.isEmpty()){
            FragmentTransaction t = fm.beginTransaction();
            for (Fragment f:pagerFragment) {
                t.remove(f);
            }
            t.commit();
            fm.executePendingTransactions();


        }
    }





    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        RequestManager.cancelAll(this);
        System.gc();
        super.onDestroyView();
    }



    /**
     * viewPager适配器
     */
    class GoodsPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Goods> mAllGoods;
        private ArrayList<ArrayList<Goods>> mGoodsGroupPagersData;
        private int column = 3;
        private int row = 3;
        public GoodsPagerAdapter(FragmentManager fm, @NonNull ArrayList<Goods> pagerDatas,int column,int row){
            super(fm);
            this.mAllGoods = pagerDatas!=null?pagerDatas:new ArrayList<Goods>();
            this.column =column;
            this.row = row;

            groupByColumnRow();
            mIndicator.initIndicator(mGoodsGroupPagersData.size());

        }

        public void update(ArrayList<Goods> pagerDatas) {
            this.mAllGoods = pagerDatas;
            clear();
            groupByColumnRow();
            notifyDataSetChanged();
        }

        public void clear() {
            if (mGoodsGroupPagersData != null) {
                mGoodsGroupPagersData.clear();
            }
            if (mAllGoods != null) {
                mAllGoods.clear();
            }

        }

        /**
         * 根据行和列，计算分组
         */
        private void groupByColumnRow(){

            mGoodsGroupPagersData = new ArrayList<ArrayList<Goods>>();
            ArrayList<Goods> goodsGroup=null;
            for (int i = 0; i < mAllGoods.size(); i++) {
                if(i%(column*row)==0){
                    goodsGroup =new ArrayList<Goods>();
                    mGoodsGroupPagersData.add(goodsGroup);
                    goodsGroup.add(mAllGoods.get(i));
                }else{
                    goodsGroup.add(mAllGoods.get(i));
                }
            }
        }

        @Override
        public Fragment getItem(int position) {
            Fragment item =BusinessAreaPageFragment.newInstance(mActivity,mGoodsGroupPagersData.get(position),  3,BusinessAreaListFragment.this);
            pagerFragment.add(item);
            return item;
        }

        @Override
        public int getCount() {
            return mGoodsGroupPagersData.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);

        }
    }

}
