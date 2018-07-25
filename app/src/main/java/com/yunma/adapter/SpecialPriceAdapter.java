package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.utils.PriceUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.NestListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Json on 2017/3/1.
 */

public class SpecialPriceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private LayoutInflater mInflater;
    private Context mContext;
    private OnBuyClick onBuyClick;
    private List<SelfGoodsListBean> goodsListBeen;
    private List<SelfGoodsListBean> listBeen ;

    public SpecialPriceAdapter(Context context,OnBuyClick onBuyClick) {
        this.mContext = context;
        this.onBuyClick = onBuyClick;
        this.goodsListBeen = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
        this.listBeen = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.mainitem, parent, false);
            return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        SubAdapterController mSubAdapterCrl = new SubAdapterController(
                goodsListBeen.get(position).getPic().split(","), mContext, goodsListBeen.get(position));
        ((MainViewHolder)holder).nestListView.setAdapter(mSubAdapterCrl.getAdapter());

        if(position == goodsListBeen.size()-1){
            ((MainViewHolder)holder).layout.setVisibility(View.VISIBLE);
            ((MainViewHolder)holder).layoutSpace.setVisibility(View.GONE);
        }else{
            ((MainViewHolder)holder).layout.setVisibility(View.GONE);
            ((MainViewHolder)holder).layoutSpace.setVisibility(View.VISIBLE);
        }
        String s;
        double yunPrice = goodsListBeen.get(position).getYmprice();
        double specialPrice = goodsListBeen.get(position).getSpecialprice();
        if (PriceUtils.isShowSpecialPrice(yunPrice, specialPrice)) {
            ((MainViewHolder)holder).imgSpecialOffer.setVisibility(View.VISIBLE);
            SpannableStringBuilder span = new SpannableStringBuilder(
                    "缩进 "+goodsListBeen.get(position).getName());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            ((MainViewHolder)holder).tvShopName.setText(span);
            s = PriceUtils.getPrice(yunPrice,specialPrice);
        }else{
            ((MainViewHolder)holder).imgSpecialOffer.setVisibility(View.GONE);
            ((MainViewHolder)holder).tvShopName.setText(goodsListBeen.get(position).getName());
            s = PriceUtils.getPrice(yunPrice,specialPrice);
        }
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.b4,s, Typeface.NORMAL,
                16,0,s.indexOf("."));
        ((MainViewHolder)holder).tvPrice.setText(ss);
        ((MainViewHolder)holder).tvRemainNums.setText(
                String.valueOf(goodsListBeen.get(position).getStock()));
        if(goodsListBeen.get(position).getStock() == 0){
            ((MainViewHolder)holder).tvSaleOut.setVisibility(View.VISIBLE);
            ((MainViewHolder)holder).tvSaleOut.setEnabled(true);
            ((MainViewHolder)holder)
                    .btnBuy.setBackgroundResource(R.drawable.layout_shape_dark_gray);
            ((MainViewHolder)holder).btnBuy.setEnabled(false);
            ((MainViewHolder)holder).nestListView.setEnabled(false);

        }else {
            ((MainViewHolder)holder).tvSaleOut.setVisibility(View.INVISIBLE);
            ((MainViewHolder)holder)
                        .btnBuy.setBackgroundResource(R.drawable.btn_selector_orange);
            ((MainViewHolder)holder).btnBuy.setEnabled(true);

            }
            ((MainViewHolder)holder).btnBuy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onBuyClick!=null){
                        onBuyClick.onBuyClickListener(holder.getAdapterPosition(),
                                goodsListBeen.get(holder.getAdapterPosition()));
                    }
                }
            });
    }


    @Override
    public int getItemCount() {
        return goodsListBeen.size();
    }

    private class MainViewHolder extends RecyclerView.ViewHolder{
        NestListView nestListView;
        View layoutSpace;
        View layout;
        TextView tvShopName;
        TextView tvPrice;
        Button btnBuy;
        TextView tvRemainNums;
        TextView tvSaleOut;
        ImageView imgSpecialOffer;
        MainViewHolder(View itemView) {
            super(itemView);
            nestListView = itemView.findViewById(R.id.nestlistview);
            layoutSpace =  itemView.findViewById(R.id.layoutSpace);
            layout = itemView.findViewById(R.id.layout);
            tvShopName = itemView.findViewById(R.id.tvShopName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRemainNums = itemView.findViewById(R.id.tvRemainNums);
            tvSaleOut = itemView.findViewById(R.id.tvSaleOut);
            btnBuy = itemView.findViewById(R.id.btnBuy);
            imgSpecialOffer = itemView.findViewById(R.id.imgSpecialOffer);
        }
    }

    public void addListBeen(List<SelfGoodsListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        notifyDataSetChanged();
    }

    //购物车列表
    public List<SelfGoodsListBean> getGoodsInfo(){
        int size = goodsListBeen.size();
            for(int i=0;i<size;i++){
                if(goodsListBeen.get(i).getTotalBuyNums()!=0){
                    goodsListBeen.get(i).setPosition(i);
                    listBeen.add(goodsListBeen.get(i));
                }
            }
            return listBeen;
    }

    public void refreshDatas(List<SelfGoodsListBean> goodsListBeen){
        this.goodsListBeen.clear();
        notifyDataSetChanged();
        this.goodsListBeen = goodsListBeen;
        notifyItemInserted(this.goodsListBeen.size());
    }
}
