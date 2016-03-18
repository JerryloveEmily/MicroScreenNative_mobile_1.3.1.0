package com.boding.microscreen.ui.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.boding.microscreen.adapter.OtherAdapter;
import com.boding.microscreen.app.AppConstants;
import com.boding.microscreen.model.ModelInfo;
import com.boding.microscreen.model.TabInfo;
import com.boding.microscreen.ui.HomeActivity;
import com.boding.microscreen.util.MeasureUtil;
import com.boding.microscreen.util.MyLog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * 更多
 * Created by Administrator on 2015/4/15.
 */
public class MoreFragment extends BaseFragment{

    public static final String TAG = "MoreFragment";

    private HomeActivity mActivity;
    private GridLayout mGridLayout;
    private OtherAdapter<TabInfo> mOtherAdapter;
//    private List<TabInfo> mTabInfos;
    // 列
    private static final int COLUMN_COUNT = 6;
    // 行
    private static final int ROW_COUNT = 3;

    public static MoreFragment newInstance(String params){
        MoreFragment fragment = new MoreFragment();
        Bundle b = new Bundle();
        b.putString("params", params);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activity instanceof HomeActivity){
            mActivity = (HomeActivity) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_more, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MeasureUtil.Size size = MeasureUtil.getScreenSize(mActivity);
        final LinearLayout container = (LinearLayout) view.findViewById(R.id.ll_container);
        mGridLayout = (GridLayout) view.findViewById(R.id.id_ever_change_gridview);
        mGridLayout.setColumnCount(COLUMN_COUNT);
        mGridLayout.setRowCount(ROW_COUNT);
        container.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                container.getViewTreeObserver().removeOnPreDrawListener(this);
                int width = container.getMeasuredWidth();
                int height = container.getMeasuredHeight();
                int itemWidth = width / COLUMN_COUNT;
                int itemHeight = height / ROW_COUNT;
                ArrayList<View> views = new ArrayList<>();
                for (int row = 0; row < mGridLayout.getRowCount(); row++) {
                    for (int col = 0; col < mGridLayout.getColumnCount(); col++) {
                        if ((row == 0 && col == 3) || (row == 1 && col == 2) || (row == 1 && col == 3)
                                || (row == 0 && col == 5) || (row == 2 && col == 1)) continue;
                        View itemView = LayoutInflater.from(mActivity).inflate(R.layout.fragment_more_item, null);
                        ImageView ivBackground = (ImageView) itemView.findViewById(R.id.id_iv_background);

                        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                        views.add(itemView);
                        params.width = itemWidth;
                        params.height = itemHeight;
                        String color = AppConstants.COLORS[2];
                        if ((row == 0 && col == 2)) {
                            params.rowSpec = GridLayout.spec(row, 2);
                            params.columnSpec = GridLayout.spec(col, 2);
                            color = AppConstants.COLORS[2];
                        } else if ((row == 0 && col == 4)) {
                            params.rowSpec = GridLayout.spec(row);
                            params.columnSpec = GridLayout.spec(col, 2);
                            color = AppConstants.COLORS[3];
                        } else if ((row == 2 && col == 0)) {
                            params.rowSpec = GridLayout.spec(row);
                            params.columnSpec = GridLayout.spec(col, 2);
                            color = AppConstants.COLORS[8];
                        } else if (row == 0 && col == 0) {
                            // 第一行，第一个
                            color = AppConstants.COLORS[0];
                        } else if (row == 0 && col == 1) {
                            // 第一行，第二个
                            color = AppConstants.COLORS[1];
                        } else if (row == 1 && col == 0) {
                            // 第二行，第一个
                            color = AppConstants.COLORS[4];
                        } else if (row == 1 && col == 1) {
                            // 第二行，第一个
                            color = AppConstants.COLORS[5];
                        } else if (row == 1 && col == 4) {
                            // 第二行，第四个
                            color = AppConstants.COLORS[6];
                        } else if (row == 1 && col == 5) {
                            // 第二行，第五个
                            color = AppConstants.COLORS[7];
                        } else if (row == 2 && col == 2) {
                            // 第三行，第二个
                            color = AppConstants.COLORS[9];
                        } else if (row == 2 && col == 3) {
                            // 第三行，第三个
                            color = AppConstants.COLORS[10];
                        } else if (row == 2 && col == 4) {
                            // 第三行，第三个
                            color = AppConstants.COLORS[11];
                        } else if (row == 2 && col == 5) {
                            // 第三行，第四个
                            color = AppConstants.COLORS[2];
                        }
                        ivBackground.setBackgroundColor(Color.parseColor(color));
                        params.setMargins(0,0,0,0);
                        params.setGravity(Gravity.FILL);
                        mGridLayout.addView(itemView, params);
                    }
                }
                initView(views);
                return true;
            }
        });

    }

    private void initView(ArrayList<View> views){
        ArrayList<ModelInfo> modelInfos = new ArrayList<>();
        if ("more".equals(AppConstants.modelTag)){
            modelInfos.addAll(AppConstants.mMoreModels);
        }
        /*if ("promotion".equals(AppConstants.modelTag)){
            modelInfos.addAll(AppConstants.mPromotionModels);
        }*/
        for (int i=0; i<modelInfos.size(); i++){
            final ModelInfo modelInfo = modelInfos.get(i);
            String modelType = modelInfo.getModuleType();
            View itemView = views.get(i);
            ImageView icon = (ImageView) itemView.findViewById(R.id.id_iv_icon);
            Glide.with(this)
                    .load(modelInfo.getIconUrl())
                    .override(100,100)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(icon);
            /*Matrix matrix = new Matrix();
            matrix.postScale(2.5f,2.5f);
            icon.setImageMatrix(matrix);*/
            TextView title = (TextView) itemView.findViewById(R.id.id_tv_title);
            FrameLayout contain = (FrameLayout) itemView.findViewById(R.id.id_ll_fragment_container);
            if ("picture".equals(modelType)||"video".equals(modelType)
                    ||"goods".equals(modelType) || "myDistrict".equals(modelType)){
                if ("picture".equals(modelType)){
                    modelInfo.setFragment(PictureFragment.class);
                }
                if ("video".equals(modelType)){
                    modelInfo.setFragment(VideoListFragment.class);
//                    modelInfo.setFragment(VideoPlayByFFmpegFragment.class);
                }
                if ("goods".equals(modelType)){
                    modelInfo.setFragment(NewGoodsFragment.class);
                }
                // 2015.08.13新增
                if ("myDistrict".equals(modelType)){// 商圈
                    modelInfo.setFragment(BusinessAreaListFragment.class);
                }
                contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyLog.e(TAG, "itemView_click_picture");
                        mActivity.replaceFragment(R.id.id_ll_fragment_container, modelInfo.getFragment().getName());
                    }
                });
            }
            if ("shake".equals(modelType)||"lottery".equals(modelType)
                    ||"smashEgg".equals(modelType)){
                if ("shake".equals(modelType)){
//                    icon.setImageBitmap(scaleImage(R.drawable.ic_shake, matrix));
                }
                if ("lottery".equals(modelType)){
//                    icon.setImageBitmap(scaleImage(R.drawable.ic_sweepstakes, matrix));
                }
                if ("smashEgg".equals(modelType)){
//                    icon.setImageBitmap(scaleImage(R.drawable.ic_sweepstakes, matrix));
                }
                modelInfo.setFragment(PromotionFragment.class);
                contain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyLog.e(TAG, "itemView_click_picture");
                        mActivity.replaceFragment(R.id.id_ll_fragment_container,
                                PromotionFragment.newInstance(modelInfo.getUrl()));
                    }
                });
            }
        }
    }

    /**
     * 缩放图片
     * @param resId
     * @param matrix
     * @return
     */
    private Bitmap scaleImage(int resId, Matrix matrix){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                resId);
        Bitmap reSizeBitmap = Bitmap.createBitmap(bitmap,0,0,
                bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        return reSizeBitmap;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
