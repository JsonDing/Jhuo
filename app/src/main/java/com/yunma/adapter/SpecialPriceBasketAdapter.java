package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SpecialPriceActivity;
import com.yunma.bean.*;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.utils.*;
import com.yunma.widget.MyListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-03-08
 *
 * @author Json.
 */

public class SpecialPriceBasketAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private NumberPickerAdapter numberPickerAdapter;
    private List<ListBean> goodsInfo;
    private SpecialPriceActivity specialPrice;
    public SpecialPriceBasketAdapter(Context mContext, List<ListBean> goodsInfo, SpecialPriceActivity specialPrice) {
        this.mContext = mContext;
        this.specialPrice = specialPrice;
        this.goodsInfo = goodsInfo;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return goodsInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsInfo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_nums_list, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        if(goodsInfo.get(position).getPic()!=null){
            GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                    goodsInfo.get(position).getPic().split(",")[0]);
        }else{
            holder.imgGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
        }
        holder.tvPrice.setText("￥" + goodsInfo.get(position).getYmprice());
        List<StocksBean>
                stocksBeanListInfo = new ArrayList<>();

        for(int i=0;i<goodsInfo.get(position).getStocks().size();i++){
            if(goodsInfo.get(position).getStocks().get(i).getBuyNum()!=0){
                goodsInfo.get(position).getStocks().get(i).setPosition(i);
                stocksBeanListInfo.add(goodsInfo.get(position).getStocks().get(i));
            }
        }

        numberPickerAdapter =
                new NumberPickerAdapter(mContext, stocksBeanListInfo,goodsInfo.get(position).getPosition());
        holder.lvSize.setAdapter(numberPickerAdapter);

        return convertView;
    }

    public class ViewHolder {
        ImageView imgGoods;
        TextView tvPrice;
        View layout;
        MyListView lvSize;
        public ViewHolder(View itemView) {
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            lvSize = (MyListView)itemView.findViewById(R.id.lvSize);
            imgGoods = (ImageView) itemView.findViewById(R.id.imgGoods);
        }
    }

    private class NumberPickerAdapter extends BaseAdapter{
        private LayoutInflater mInflater;
        private Context context;
        private int pos;
        private List<StocksBean> stocksInfo;
        private NumberPickerAdapter(Context context, List<StocksBean> stocksBeanListInfo, int position) {
            this.stocksInfo = stocksBeanListInfo;
            this.context = context;
            this.pos=position;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return stocksInfo.size();
        }

        @Override
        public Object getItem(int position) {
            return stocksInfo.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            final NumViewHolder holder;
            if (view != null) {
                holder = (NumViewHolder) view.getTag();
            } else {
                view = mInflater.inflate(R.layout.item_size_nums, parent, false);
                holder = new NumViewHolder(view);
                view.setTag(holder);
            }


            holder.tvGoodsSize.setText(stocksInfo.get(position).getSize());

            holder.tvGoodsRemain.setText(stocksInfo.get(position).getNum()+"");

            holder.etGoodsNums.setText(stocksInfo.get(position).getBuyNum()+"");

            final int buyNums = Integer.valueOf(holder.etGoodsNums.getText().toString());
            final int remainNums = Integer.valueOf(holder.tvGoodsRemain.getText().toString());
            holder.layoutAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(remainNums<=0){
                        ToastUtils.showShort(mContext,"库存不足，无法继续添加");
                    }else{
                        specialPrice.getGoodsListBeen().get(pos)
                                .getStocks().get(stocksInfo.get(position).getPosition())
                                .setBuyNum(buyNums +1);
                        specialPrice.getGoodsListBeen().get(pos)
                                .getStocks().get(stocksInfo.get(position).getPosition())
                                .setNum(remainNums-1);
                        specialPrice.getGoodsListBeen().get(pos)
                                .setTotalBuyNums(specialPrice.getGoodsListBeen()
                                        .get(pos).getTotalBuyNums()+1);
                        specialPrice.addDatas(specialPrice.getGoodsListBeen()
                                .get(pos).getYmprice());
                        specialPrice.updataAdapter();
                    }
                }
            });

            holder.layoutMinusMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(buyNums==0){
                        ToastUtils.showShort(context,"无法继续减少");
                    }else {
                        specialPrice.getGoodsListBeen().get(pos).getStocks()
                                .get(stocksInfo.get(position).getPosition())
                                .setBuyNum(buyNums -1);
                        specialPrice.getGoodsListBeen().get(pos).getStocks()
                                .get(stocksInfo.get(position).getPosition())
                                .setNum(remainNums+1);
                        specialPrice.getGoodsListBeen().get(pos)
                                .setTotalBuyNums(specialPrice.getGoodsListBeen()
                                        .get(pos).getTotalBuyNums()-1);
                        specialPrice.minusDatas(specialPrice.getGoodsListBeen()
                                .get(pos).getYmprice());
                        specialPrice.updataAdapter();
                    }
                }
            });

            return view;
        }

        class NumViewHolder {
                TextView tvGoodsSize;
                LinearLayout layoutAddMore;
                TextView etGoodsNums;
                LinearLayout layoutMinusMore;
                TextView tvGoodsRemain;

             NumViewHolder(View itemView) {
                tvGoodsSize = (TextView)itemView.findViewById(R.id.tvGoodsSize);
                 etGoodsNums = (TextView)itemView.findViewById(R.id.etGoodsNums);
                tvGoodsRemain = (TextView) itemView.findViewById(R.id.tvGoodsRemain);
                layoutAddMore = (LinearLayout)itemView.findViewById(R.id.layoutAddMore);
                layoutMinusMore = (LinearLayout)itemView.findViewById(R.id.layoutMinusMore);
            }
        }
    }

    public List<GetSelfGoodsResultBean.SuccessBean.ListBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
        notifyDataSetChanged();
    }



}
