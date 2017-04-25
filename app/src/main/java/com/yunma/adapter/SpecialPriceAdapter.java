package com.yunma.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.Button;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.m.OnBuyClick;
import com.yunma.utils.*;
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
    private List<ListBean> goodsListBeen;

    public SpecialPriceAdapter(Context context, List<ListBean> goodsListBeen,OnBuyClick onBuyClick) {
        this.goodsListBeen = goodsListBeen;
        this.mContext = context;
        this.onBuyClick = onBuyClick;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.mainitem, parent, false);
            return new MainViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
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
        String s = "￥" + ValueUtils.toTwoDecimal(goodsListBeen.get(position).getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,16),1,s.indexOf("."));
        ((MainViewHolder)holder).tvPrice.setText(ss);
        ((MainViewHolder)holder).tvShopName.setText(goodsListBeen.get(position).getName());
        int remainNums = 0;
        for(int i=0;i<goodsListBeen.get(position).getStocks().size();i++){
            remainNums = remainNums + goodsListBeen.get(position).getStocks().get(i).getNum();
        }
        goodsListBeen.get(position).setRemain(remainNums);
        ((MainViewHolder)holder).tvRemainNums.setText(
                String.valueOf(remainNums));
        if(remainNums==0){
            ((MainViewHolder)holder).tvSaleOut.setVisibility(View.VISIBLE);
            ((MainViewHolder)holder).tvSaleOut.setEnabled(true);
            ((MainViewHolder)holder)
                    .btnBuy.setBackgroundResource(R.drawable.gray_button_background);
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
                        onBuyClick.onBuyClickListener(position, goodsListBeen.get(position));
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
        MainViewHolder(View itemView) {
            super(itemView);
            nestListView = (NestListView) itemView.findViewById(R.id.nestlistview);
            layoutSpace =  itemView.findViewById(R.id.layoutSpace);
            layout = itemView.findViewById(R.id.layout);
            tvShopName = (TextView)itemView.findViewById(R.id.tvShopName);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            tvRemainNums = (TextView)itemView.findViewById(R.id.tvRemainNums);
            tvSaleOut = (TextView)itemView.findViewById(R.id.tvSaleOut);
            btnBuy = (Button)itemView.findViewById(R.id.btnBuy);
        }
    }

    public void setGoodsListBeen(List<ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        notifyItemInserted(goodsListBeen.size());
        notifyDataSetChanged();
    }

    public List<ListBean> getGoodsInfo(){
        List<ListBean> listBeen = new ArrayList<>();
        if(goodsListBeen!=null){
            for(int i=0;i<goodsListBeen.size();i++){
                if(goodsListBeen.get(i).getTotalBuyNums()!=0){
                    goodsListBeen.get(i).setPosition(i);
                    listBeen.add(goodsListBeen.get(i));
                }
            }
            return listBeen;
        }else{
            return  null;
        }
    }
}
