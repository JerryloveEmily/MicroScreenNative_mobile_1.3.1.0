package com.boding.microscreen.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.boding.microscreen.R;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.BaseDatas;
import com.boding.microscreen.model.MessageData;
import com.boding.microscreen.model.ModelInfo;
import com.boding.microscreen.model.ModelInfoComparator;
import com.boding.microscreen.net.Request4Message;
import com.boding.microscreen.net.RequestManager;
import com.boding.microscreen.service.AudioPlayService;
import com.boding.microscreen.ui.fragment.ActivitiesInfoFragment;
import com.boding.microscreen.ui.fragment.BusinessAreaListFragment;
import com.boding.microscreen.ui.fragment.CustomDialogFragment;
import com.boding.microscreen.ui.fragment.DistrictFragment;
import com.boding.microscreen.ui.fragment.MessageListFragment;
import com.boding.microscreen.ui.fragment.MoreFragment;
import com.boding.microscreen.ui.fragment.NewGoodsFragment;
import com.boding.microscreen.ui.fragment.PictureFragment;
import com.boding.microscreen.ui.fragment.PromotionFragment;
import com.boding.microscreen.ui.fragment.VideoListFragment;
import com.boding.microscreen.util.JLog;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.Util;
import com.boding.microscreen.widget.CustomDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.Collections;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class HomeActivity extends FragmentActivity {

    private static final String TAG = "HomeActivity";

    private ImageView mIvBg, mIvLogo;
    private TabLayout mTabContainer;
    @Bind(R.id.ibtn_menu)
    ImageButton mNavMneuView;
    public String validateCode = "";
    private ArrayList<ModelInfo> mCurrentSortModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_home);
        ButterKnife.bind(this);
        initView();
        initDatas();
        initEvent();
    }

    private void initEvent() {
        mNavMneuView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 显示退出应用对话框
            Util.exitDialog(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        }
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 初始化视图
     */
    private void initView() {
        mIvBg = (ImageView) findViewById(R.id.iv_bg);
        mIvLogo = (ImageView) findViewById(R.id.id_iv_logo);
        mTabContainer = (TabLayout) findViewById(R.id.tl_tab_container);
        mTabContainer.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // tab切换后的样式
                View customView = updateTabStyle(tab, true);

                // tab被选择后的内容数据
                updateContentDataForTab(tab, customView);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // 更新tab样式
                updateTabStyle(tab, false);
            }

            // tab被重新选择的方法
            @Override public void onTabReselected(TabLayout.Tab tab) {
                // tab切换后的样式
                View customView = updateTabStyle(tab, true);

                // tab被选择后的内容数据
                updateContentDataForTab(tab, customView);
            }

            /**
             * tab被选择后的内容数据
             * @param tab           ...
             * @param customView    ...
             */
            private void updateContentDataForTab(TabLayout.Tab tab, View customView){
                if (null != customView) {
                    int position = tab.getPosition();
                    ModelInfo modelInfo = mCurrentSortModels.get(position);
                    String tag = (String) tab.getTag();
                    if (modelInfo.getModuleType().equals(tag)) {
                        if (null != modelInfo.getFragment()) {
                            if ("more".equals(tag)) {// 点击的是更多模块，标记当前模块为更多模块
                                AppConstants.modelTag = "more";
                            }
                            replaceFragment(R.id.id_ll_fragment_container, modelInfo.getFragment().getName());
                        } else {
                            if ("qrcode".equals(tag)) {// 二维码
//                                setQRCodeDialog();
                                showDiaglog();
                            } else {// 没有设定Fragment和有url地址的
                                replaceFragment(R.id.id_ll_fragment_container,
                                        PromotionFragment.newInstance(modelInfo.getUrl()));
                            }
                        }
                    }
                }
            }
        });
    }

    private void initDatas() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        BaseDatas baseDatas = (BaseDatas) intent.getSerializableExtra("baseDatas");
        validateCode = intent.getStringExtra("validateCode");

        // 背景图片
        Glide.with(HomeActivity.this)
                .load(baseDatas.getBackgroundImage())
                .error(R.drawable.bg)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mIvBg);

        // 加载logo图标
        String url = String.format(AppConstants.MESSAGE_URL, validateCode);
        RequestManager.addRequest(new Request4Message(url, new Response.Listener<MessageData>() {
            @Override
            public void onResponse(MessageData response) {
                MyLog.e(TAG, response.toString());
                if (!"".equals(response.getSponLogo())) {
                    mIvLogo.setVisibility(View.VISIBLE);
                    Glide.with(HomeActivity.this)
                            .load(response.getSponLogo())
                            .error(R.drawable.logo_right_top)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(mIvLogo);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, error.getMessage());
                Crouton.showText(HomeActivity.this, "加载出错", Style.ALERT);
            }
        }), this);

//        boolean isOpenBgMusic = baseDatas.getBackgroundMusic().getIsOpen() == 1;
        // 背景音乐地址为空串就不播放，不为空字符串就播放
        boolean isOpenBgMusic = !TextUtils.isEmpty(baseDatas.getBackgroundMusic().getMusicUrl());
        //开始背景音乐
        if (isOpenBgMusic) {
            Intent i2 = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
            i2.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PARAM);
            i2.putExtra(AudioPlayService.DATA_URL, baseDatas.getBackgroundMusic().getMusicUrl());
            sendBroadcast(i2);
        }
        JLog.e("是否打开背景音乐" + isOpenBgMusic + "   ," + baseDatas.getBackgroundMusic().getIsOpen());

        // 促销应用模块列表
//        AppConstants.mPromotionModels = baseDatas.getPromotionModules();
        // 更多模块列表
        AppConstants.mMoreModels = baseDatas.getMoreModules();

        // 初始化tab的数据
        initTabDatas(baseDatas);
    }

    /**
     * 初始化tab的数据
     */
    private void initTabDatas(BaseDatas baseDatas) {
        mCurrentSortModels = baseDatas.getShowMenuBarModules();
        mCurrentSortModels = setUIDatasToModelInfo(mCurrentSortModels);
        ModelInfoComparator comparator = new ModelInfoComparator();
        Collections.sort(mCurrentSortModels, comparator);
        setTabButton(mCurrentSortModels);
    }

    /**
     * tab上显示的数据
     */
    private ArrayList<ModelInfo> setUIDatasToModelInfo(ArrayList<ModelInfo> datas) {
        for (ModelInfo mi : datas) {
            if ("activities".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_activity);
                mi.setIconPress(R.drawable.ic_activity_press);
                mi.setFragment(ActivitiesInfoFragment.class);
                mi.setIndex(0);
                continue;
            }
            if ("messageList".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_message);
                mi.setIconPress(R.drawable.ic_message_press);
                mi.setFragment(MessageListFragment.class);
                mi.setIndex(1);
                continue;
            }
            if ("picture".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_picture);
                mi.setIconPress(R.drawable.ic_picture_press);
                mi.setFragment(PictureFragment.class);
                mi.setIndex(2);
                continue;
            }
            // 2015.08.13新增
            if ("myDistrict".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_district);
                mi.setIconPress(R.drawable.ic_district_press);
                mi.setFragment(BusinessAreaListFragment.class);
                mi.setIndex(3);
                continue;
            }
            if ("goods".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_goods);
                mi.setIconPress(R.drawable.ic_goods_press);
               // mi.setFragment(GoodsFragment.class);
                mi.setFragment(NewGoodsFragment.class);
                mi.setIndex(4);
                continue;
            }
            if ("video".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_video);
                mi.setIconPress(R.drawable.ic_video_press);
                mi.setFragment(VideoListFragment.class);
                mi.setIndex(5);
                continue;
            }
            if ("qrcode".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_qrcode);
                mi.setIconPress(R.drawable.ic_qrcode_press);
                mi.setIndex(6);
                continue;
            }
            if ("help".equals(mi.getModuleType())) {
//                mi.setFragment();
                mi.setIcon(R.drawable.ic_help);
                mi.setIconPress(R.drawable.ic_help_press);
                mi.setIndex(datas.size() - 1);
                continue;
            }
            // 删除了背景音乐开关 1.0.5版本
            /*if ("music".equals(mi.getModuleType())) {
                if (isOpenBgMusic) {
                    mi.setIcon(R.drawable.ic_music);
                } else {
                    mi.setIcon(R.drawable.ic_music_close);
                }
                mi.setIndex(datas.size() - 2);
                continue;
            }*/
            if ("more".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_more);
                mi.setIconPress(R.drawable.ic_more_press);
                mi.setFragment(MoreFragment.class);
                mi.setIndex(datas.size() - 2);
                continue;
            }
            /*if ("promotion".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_promotion);
                mi.setFragment(MoreFragment.class);
                mi.setIndex(datas.size() - 3);
                continue;
            }*/
            if ("shake".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_shake);
                mi.setIconPress(R.drawable.ic_shake_press);
                mi.setIndex(datas.size() - 4);
                continue;
            }
            if ("lottery".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_sweepstakes);
                mi.setIconPress(R.drawable.ic_sweepstakes_press);
                mi.setIndex(datas.size() - 4);
                continue;
            }
            if ("smashEgg".equals(mi.getModuleType())) {
                mi.setIcon(R.drawable.ic_smash_egg);
                mi.setIconPress(R.drawable.ic_smash_egg_press);
                mi.setIndex(datas.size() - 4);
            }
        }
        return datas;
    }

    private void setTabButton(ArrayList<ModelInfo> tabDatas) {
        mTabContainer.removeAllTabs();
        boolean defaultInTab = false;
        for (int i = 0; i < tabDatas.size(); i++) {
            // 设置tab的数据和样式
            ModelInfo modelInfo = tabDatas.get(i);
            TabLayout.Tab tab = mTabContainer.newTab();
            tab.setTag(modelInfo.getModuleType());
            tab.setText(modelInfo.getModuleName());
            mTabContainer.addTab(tab, modelInfo.getIsDefault() == 1);
            tab.setCustomView(R.layout.activity_home_tab_item);
            LinearLayout tabStrip = (LinearLayout) mTabContainer.getChildAt(0);
            // 每个tab选项卡视图
            final View tabItemView = tabStrip.getChildAt(i);
            try {
                if (null != tabItemView) {
                    boolean isSelected = false;
                    if (modelInfo.getIsDefault() == 1) {
                        tabItemView.requestFocus();
                        isSelected = true;  //  设置默认项被选中状态
                    }
                    // 更新tab样式
                    updateTabStyle(tab, isSelected);
                    // 处理遥控器的焦点事件问题
                    tabItemView.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            switch (event.getAction()) {
                                case KeyEvent.ACTION_UP:
                                    break;
                                case KeyEvent.ACTION_DOWN:
                                    if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                                        int key = curFragmentArray.keyAt(0);
                                        // 当前是留言墙栏目
                                        if (key == MessageListFragment.class.getName().hashCode()) {
                                            MessageListFragment fragment = (MessageListFragment) curFragmentArray.valueAt(0);
                                            tabItemView.setNextFocusUpId(fragment.requestFocuseView());
                                        }
                                        // 当前是相册栏目
                                        if (key == PictureFragment.class.getName().hashCode()) {
                                            PictureFragment fragment = (PictureFragment) curFragmentArray.valueAt(0);
                                            tabItemView.setNextFocusUpId(fragment.requestFocuseView());
                                        }
                                        // 当前是商品栏目
                                       /* if (key == GoodsFragment.class.getName().hashCode()) {
                                            GoodsFragment fragment = (GoodsFragment) curFragmentArray.valueAt(0);
                                            tabItemView.setNextFocusUpId(fragment.requestFocuseView());
                                        }*/
                                        if (key == NewGoodsFragment.class.getName().hashCode()) {
                                            NewGoodsFragment fragment = (NewGoodsFragment) curFragmentArray.valueAt(0);
                                            //tabItemView.setNextFocusUpId(fragment.requestFocuseView());
                                        }
                                        // 当前是商圈栏目
                                        if (key == DistrictFragment.class.getName().hashCode()) {
                                            BusinessAreaListFragment fragment = (BusinessAreaListFragment) curFragmentArray.valueAt(0);
                                            //tabItemView.setNextFocusUpId(fragment.requestFocuseView());
                                        }
                                        // 当前是视频列表
                                        /*if (key == VideoListFragment.class.getName().hashCode()) {
                                            VideoListFragment fragment = (VideoListFragment) curFragmentArray.valueAt(0);
//                                            tabItemView.setNextFocusUpId();
                                            fragment.requestFocuseView();
                                        }*/
                                    }
                                    break;
                            }
                            return false;
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 设置内容区域的内容, 默认显示的界面在tab里
            if (modelInfo.getIsDefault() == 1) {
                defaultInTab = true;
                if (null != modelInfo.getFragment()) {
                    replaceFragment(R.id.id_ll_fragment_container, modelInfo.getFragment().getName());
                } else {
                    String typeTemp = modelInfo.getModuleType();
                    if (!"qrcode".equals(typeTemp)) {
                        // 没有设定Fragment和有url地址的，是促销应用游戏模块
                        replaceFragment(R.id.id_ll_fragment_container,
                                PromotionFragment.newInstance(modelInfo.getUrl()));
                    }
                }
            }
        }

        // 默认显示的界面不在tab菜单栏中,表示是在更多的模块里，则遍历更多列表数据，显示出来
        if (!defaultInTab) {
            AppConstants.mMoreModels = setUIDatasToModelInfo(AppConstants.mMoreModels);
            for (ModelInfo modelInfo : AppConstants.mMoreModels) {
                if (1 == modelInfo.getIsDefault()) {
                    // 默认打开的模块
                    if (null != modelInfo.getFragment()) {
                        replaceFragment(R.id.id_ll_fragment_container, modelInfo.getFragment().getName());
                        break;
                    } else {// 没有设置Fragment的为null的, 且url不是空的
                        if (!TextUtils.isEmpty(modelInfo.getUrl())) {
                            replaceFragment(R.id.id_ll_fragment_container,
                                    PromotionFragment.newInstance(modelInfo.getUrl()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 更新tab的样式
     * @param tab           当前需要更新的tab
     * @param isSelected    tab是否被选中
     * @return tab的自定义视图
     */
    private View updateTabStyle(TabLayout.Tab tab, boolean isSelected) {
        View customView = tab.getCustomView();
        if (null != customView) {
            int position = tab.getPosition();
            LinearLayout tabStrip = (LinearLayout) mTabContainer.getChildAt(0);
            View tabItemView = tabStrip.getChildAt(position);
            ModelInfo modelInfo = mCurrentSortModels.get(position);
            int textColor = Color.WHITE, iconId = modelInfo.getIcon(),
                    backgroundColor = R.drawable.tab_item_bg_normal;
            if (isSelected) { // 如果是选中的，这修改tab的样式
                textColor = Color.BLACK;
                iconId = modelInfo.getIconPress();
                backgroundColor = R.drawable.tab_item_bg_press;
            }
            // 标题
            ((TextView) tabItemView.findViewById(android.R.id.text1)).setTextColor(textColor);
            // 图标
            tab.setIcon(iconId);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)
                    tabItemView.findViewById(android.R.id.icon).getLayoutParams();
            params.bottomMargin = 0;
            // 背景颜色
            tabItemView.setBackgroundResource(backgroundColor);
        }
        return customView;
    }

    public void requestFocuseToTab(int index) {
        /*if (mRadioGroup.getChildCount() != 0) {
            mRadioGroup.getChildAt(index).requestFocus();
        }*/
    }

    public void requestCheckToTab() {
        /*if (mRadioGroup.getChildCount() != 0) {
            mRadioGroup.check(mRadioGroup.getChildAt(0).getId());
        }*/
    }

    private SparseArray<Fragment> curFragmentArray = new SparseArray<>(1);

    /**
     * 根据Fragment的名字替换
     *
     * @param containerViewId 容器id
     * @param fragmentName    Fragment类名字
     */
    public void replaceFragment(@IdRes int containerViewId, @NonNull String fragmentName) {
        try {
            Fragment fragment = (Fragment) Class.forName(
                    fragmentName).newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(containerViewId, fragment);
            transaction.commit();
            curFragmentArray.clear();
            curFragmentArray.put(fragmentName.hashCode(), fragment);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 替换Fragment
     *
     * @param containerViewId Fragment放置的容器
     * @param fragment        替换上的Fragment
     */
    public void replaceFragment(@IdRes int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, fragment);
        curFragmentArray.clear();
        curFragmentArray.put(fragment.getClass().getName().hashCode(), fragment);
        transaction.commit();
    }

    /**
     * 显示Fragment
     *
     * @param fragment 显示上的Fragment
     */
    public void showFragment(@IdRes int containerViewId, Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(containerViewId, fragment);
        transaction.show(fragment);
        curFragmentArray.clear();
        curFragmentArray.put(fragment.getClass().getName().hashCode(), fragment);
        transaction.commit();
    }

    /**
     * 隐藏Fragment
     *
     * @param fragment 被隐藏的Fragment
     */
    public void hideFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(fragment);
        transaction.remove(fragment);
        transaction.commit();
    }

    private void showDiaglog(){
        String url = String.format(AppConstants.MESSAGE_URL, validateCode);
        RequestManager.addRequest(new Request4Message(url, new Response.Listener<MessageData>() {
            @Override
            public void onResponse(MessageData response) {
                MyLog.e(TAG, response.toString());
                CustomDialogFragment dialogFragment =
                        CustomDialogFragment.newInstance(response.getQrCode(), "");
                dialogFragment.show(getSupportFragmentManager(), "customDialog");
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, error.getMessage());
                Crouton.showText(HomeActivity.this, "加载出错", Style.ALERT);
            }
        }), this);
    }

    /**
     * 设置二维码显示对话框
     */
    private void setQRCodeDialog() {

        final CustomDialog dialog = new CustomDialog(this, false);
        dialog.showDialog(R.layout.layout_dialog_qrcode, R.style.DialogWindowAnim);
        Button button = (Button) dialog.findViewById(R.id.id_btn_close);
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        // 加载和设置二维码
        final ImageView ivQrCode = (ImageView) dialog.findViewById(R.id.id_iv_qrcode);
        String url = String.format(AppConstants.MESSAGE_URL, validateCode);
        RequestManager.addRequest(new Request4Message(url, new Response.Listener<MessageData>() {
            @Override
            public void onResponse(MessageData response) {
                MyLog.e(TAG, response.toString());
                Glide.with(HomeActivity.this)
                        .load(response.getQrCode())
                        .override((int) getResources().getDimension(R.dimen.x500),
                                (int) getResources().getDimension(R.dimen.x550))
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(ivQrCode);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyLog.e(TAG, error.getMessage());
                Crouton.showText(HomeActivity.this, "加载出错", Style.ALERT);
            }
        }), this);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
        Log.e(TAG, TAG + "_低内存...");
        System.gc();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        Glide.get(this).trimMemory(level);
    }

    @Override
    protected void onRestart() {
        if (!AudioPlayService.isPlaying) {
            if (AppConstants.isHandClose && !AppConstants.isHandOpen) {
                return;
            }
            Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
            intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_START);
            sendBroadcast(intent);
        }
        super.onRestart();
    }

    @Override
    protected void onStop() {
        if (AudioPlayService.isPlaying) {
            Intent intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
            intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_PAUSE);
            sendBroadcast(intent);
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (mTabContainer != null) {
            mTabContainer.removeAllTabs();
            mTabContainer.removeAllViews();
            mTabContainer = null;
        }
        if (mCurrentSortModels != null) {
            mCurrentSortModels.clear();
            mCurrentSortModels = null;
        }
        Glide.clear(mIvLogo);
        Glide.get(this).clearMemory();
        Crouton.cancelAllCroutons();
        RequestManager.cancelAll(this);
        Intent _intent = new Intent(AudioPlayService.AUDIO_RECEIVER_ACTION);
        _intent.putExtra(AudioPlayService.DATA_CODE, AudioPlayService.AUDIO_STOP);
        sendBroadcast(_intent);
        Intent i = new Intent(this, AudioPlayService.class);
        i.setAction(AudioPlayService.AUDIO_ACTION);
        stopService(i);
        System.gc();
        super.onDestroy();
    }
}
