package com.boding.microscreen.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CustomDialogFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CustomDialogFragment extends DialogFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    @Bind(R.id.id_tv_name)
    TextView tvName;
    @Bind(R.id.id_tv_msg_content)
    TextView tvContent;
    @Bind(R.id.id_iv_qrcode)
    ImageView bigImage;
    @Bind(R.id.id_btn_close)
    Button cancelButton;

    private String mParam1;
    private String mParam2;


    public CustomDialogFragment() {
        // Required empty public constructor
    }

    public static CustomDialogFragment newInstance(String param1, String param2) {
        CustomDialogFragment fragment = new CustomDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getDialog().getWindow();
        window.setWindowAnimations(R.style.DialogWindowAnim);
        window.setBackgroundDrawableResource(R.color.translusent);
//        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.DialogWindowAnim);
        return inflater.inflate(R.layout.layout_dialog_qrcode, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initEvent();
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        Glide.with(CustomDialogFragment.this)
                .load(mParam1)
                .override((int) getResources().getDimension(R.dimen.x720),
                        (int) getResources().getDimension(R.dimen.x550))
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(bigImage);
    }

    private void initEvent() {
        cancelButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
