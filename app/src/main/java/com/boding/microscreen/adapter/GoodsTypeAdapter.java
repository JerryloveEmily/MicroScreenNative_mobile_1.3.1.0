package com.boding.microscreen.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.boding.microscreen.R;
import com.boding.microscreen.listener.IItemClickListener;
import com.boding.microscreen.model.Goods;
import com.boding.microscreen.model.GoodsGroup;

import java.util.ArrayList;

/**
 * detail:
 * Created by libq
 * 2015/5/21#11:32
 */

public class GoodsTypeAdapter extends RecyclerView.Adapter<GoodsTypeViewHolder>{

    private int curPosition=-1;
    private ArrayList<GoodsGroup> goodsGroups;//在此当前只有type数据,其他数据为空
    private IItemClickListener itemClickListener;
    private ArrayList<String> urls;
    private GoodsTypeViewHolder mViewHolder;

    public GoodsTypeAdapter(ArrayList<GoodsGroup> goodsGroups){
        this.goodsGroups = (goodsGroups ==null?new ArrayList<GoodsGroup>():new ArrayList<GoodsGroup>(goodsGroups));
    }

    public void setItemClickListener(IItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public void updateDataSet(ArrayList<GoodsGroup> goodesList){
        if(this.urls!=null){
            this.urls.clear();
        }
        this.goodsGroups.clear();
        this.goodsGroups.addAll(goodesList);
        notifyDataSetChanged();
    }

    public ArrayList<String> getAllImgUrl(GoodsGroup gList){
        ArrayList<String> urls = new ArrayList<String>();
       /* for(Goods g : gList.getGoods()){
            urls.add(g.getImageUrl());
        }*/
        return urls;
    }


    @Override
    public void onBindViewHolder(final GoodsTypeViewHolder holder, final int position) {
        if(goodsGroups ==null)return;
        final GoodsGroup group = goodsGroups.get(position);
        holder.tvGoodsType.setText("");
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != itemClickListener&&curPosition!=position) {
                    /*if(null!=group.getGoods()){
                        itemClickListener.onItemClick(view, position,getAllImgUrl(group),group.getGoods());
                    }else{
                        itemClickListener.onItemClick(view, position,null,null);
                    }*/

                    curPosition = position;

                }
            }
        });
        // 分类列表item遥控器按键焦点处理
        holder.root.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (event.getAction()){
                    case KeyEvent.ACTION_UP:
                        break;
                    case KeyEvent.ACTION_DOWN:
                        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT ||
                                keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
                            holder.root.setNextFocusRightId(mIBtnPre.getId());
                            if (goodsGroups.size() - 1 == position){
                                holder.root.setNextFocusDownId(mIBtnPre.getId());
                            }
                        }
                        break;
                }
                return false;
            }
        });
    }

    private ImageButton mIBtnPre;
    public void requestFocusViewCallBack(ImageButton imageButton){
        mIBtnPre = imageButton;
    }

    public void requestFocusItem(){
        mViewHolder.root.requestFocus();
    }

    @Override
    public GoodsTypeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pic_item, parent, false);
        mViewHolder = new GoodsTypeViewHolder(v);

        return mViewHolder;
    }

    @Override
    public int getItemCount() {
        return goodsGroups.size();
    }




}
