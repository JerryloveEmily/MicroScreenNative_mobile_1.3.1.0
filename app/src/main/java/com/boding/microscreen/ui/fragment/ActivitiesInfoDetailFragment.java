package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.boding.microscreen.model.ActivitiesItem;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.ShowToast;
import com.boding.microscreen.widget.MarqueeTextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * 活动信息详情
 * Created by Administrator on 2015/4/20.
 */
public class ActivitiesInfoDetailFragment extends Fragment {

    public static final String TAG = "ActivitiesInfoDetailFragment";

    private ActivitiesItem mItem;

    private HomeActivity mActivity;
//    private Button mBtnBack;
    private TextView mTVTitle, mTVDate;
    private MarqueeTextView mTVContent;
    private ImageView mSDVImage;

    public static ActivitiesInfoDetailFragment newInstance(ActivitiesItem item){
        ActivitiesInfoDetailFragment fragment = new ActivitiesInfoDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", item);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mItem = (ActivitiesItem) getArguments().getSerializable("item");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof HomeActivity){
            mActivity = (HomeActivity)activity;
        } else {
            ShowToast.Long("切换显示出错，请重启应用！");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_activities_info_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnBack = (Button) view.findViewById(R.id.id_btn_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.hideFragment(ActivitiesInfoDetailFragment.this);
                mActivity.showFragment(R.id.id_ll_fragment_container, ActivitiesInfoFragment.newInstance(""));
            }
        });

        btnBack.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()){
                    case KeyEvent.ACTION_UP:
                        break;
                    case KeyEvent.ACTION_DOWN:
                        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                            mActivity.requestFocuseToTab(0);
                        }
                        break;
                }
                return false;
            }
        });

        mTVTitle = (TextView) view.findViewById(R.id.id_tv_activity_title);
        mTVDate = (TextView) view.findViewById(R.id.id_tv_activity_date);
        mTVContent = (MarqueeTextView) view.findViewById(R.id.id_tv_activity_content);
        mSDVImage = (ImageView) view.findViewById(R.id.id_sdv_image);
        initView();
    }

    private void initView() {
        mTVTitle.setText(mItem.getTitle().trim());
        mTVDate.setText(mItem.getTime().trim());
        mTVContent.setText(mItem.getContent().trim());
        Glide.with(this)
                .load(mItem.getImage())
                .error(R.drawable.bg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mSDVImage);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // 低内存的时候清理Glide图片内存缓存:bitmapPool、memoryCache
        Glide.get(mActivity).clearMemory();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
