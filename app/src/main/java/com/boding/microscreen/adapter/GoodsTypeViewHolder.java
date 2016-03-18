package com.boding.microscreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.boding.microscreen.R;
import com.boding.microscreen.listener.IItemClickListener;

/**
 * detail:��Ʒ����ViewHolder
 * Created by libq
 * 2015/5/21#10:44
 */

public class GoodsTypeViewHolder extends RecyclerView.ViewHolder{

    public TextView tvGoodsType;
    public View root;
    public GoodsTypeViewHolder(View itemView){
        super(itemView);
        tvGoodsType = (TextView)itemView.findViewById(R.id.id_btn_channel_item);
        root = itemView.findViewById(R.id.id_ll_channel_item);
    }


}



