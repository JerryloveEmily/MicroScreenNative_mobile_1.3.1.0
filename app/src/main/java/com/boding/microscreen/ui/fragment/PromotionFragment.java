package com.boding.microscreen.ui.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.boding.microscreen.R;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.MyLog;
import com.boding.microscreen.util.Util;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

/**
 * 促销应用界面
 */
public class PromotionFragment extends Fragment {
    public static final String TAG = "MoreFragment";

    private HomeActivity mActivity;
    private static final String URL = "url";

    private String mUrl;
    private WebView webView;
    private LinearLayout llPromotion;

    /**
     * 初始化促销应用对象
     *
     * @param url 访问促销应用的地址
     */
    public static PromotionFragment newInstance(String url) {
        PromotionFragment fragment = new PromotionFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }

    public PromotionFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString(URL);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof HomeActivity) {
            mActivity = (HomeActivity) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_promotion, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        llPromotion = (LinearLayout) view.findViewById(R.id.id_ll_promotion);
        llPromotion.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {

                llPromotion.getViewTreeObserver().removeOnPreDrawListener(this);
//                Crouton.showText(mActivity, "高度是：" + llPromotion.getMeasuredHeight(), Style.ALERT);
                webView = new WebView(mActivity);
                webView.setBackgroundColor(0xff4500);
                // 加载的进度条
                final ProgressBar progressBar = new ProgressBar(mActivity);
                progressBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,5));
                webView.addView(progressBar);
                llPromotion.addView(webView, 0);

                webView.setVerticalScrollBarEnabled(false);
//                webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
                webView.setVerticalScrollBarEnabled(false);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setAppCacheEnabled(false);
                webView.getSettings().setBuiltInZoomControls(false);
                webView.getSettings().setSaveFormData(true);
                webView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//        webView.getSettings().setUseWideViewPort(true);
//        webView.getSettings().setLoadWithOverviewMode(true);
                DisplayMetrics metrics = new DisplayMetrics();
                mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int mDensity = metrics.densityDpi;
                MyLog.e(TAG, "density: " + mDensity);
                if (mDensity == 120) {
                    webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
                } else if (mDensity == 160) {
                    webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
                } else if (mDensity == 240) {
                    webView.getSettings().setDefaultZoom(WebSettings.ZoomDensity.FAR);
                }

                webView.setWebChromeClient(new WebChromeClient(){
                    @Override
                    public void onProgressChanged(WebView view, int newProgress) {
                        if (100 == newProgress){
                            progressBar.setVisibility(View.GONE);
                        }else {
                            if (View.GONE == progressBar.getVisibility()){
                                progressBar.setVisibility(View.VISIBLE);
                            }
                            progressBar.setProgress(newProgress);
                        }
                        super.onProgressChanged(view, newProgress);
                    }
                });

                if (Util.isNetworkConnected(mActivity)) {
                    webView.loadUrl(mUrl);
                    webView.setWebViewClient(new WebViewClient() {
                        @Override
                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    view.loadUrl(url);
                            return super.shouldOverrideUrlLoading(view, url);
                        }
                    });
                } else {
                    Crouton.showText(mActivity, "请检查网络，重试", Style.ALERT);
                }

                return true;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webView != null) {
            llPromotion.removeView(webView);
            webView.clearCache(true);
            webView.removeAllViews();
            webView.destroy();
            webView = null;
        }
    }
}
