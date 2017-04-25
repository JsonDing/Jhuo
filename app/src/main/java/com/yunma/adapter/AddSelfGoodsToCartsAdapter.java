package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.utils.ToastUtils;
import com.yunma.bean.StocksBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Json on 2017/3/1.
 */

public class AddSelfGoodsToCartsAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<StocksBean> stocks;
    private OnNumsSelectedClick onClick;
    public AddSelfGoodsToCartsAdapter(Context context,
                                      OnNumsSelectedClick onClick) {

        this.mContext = context;
        this.onClick = onClick;
        this.stocks =  new ArrayList<>();
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int position) {
        return stocks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_size_nums_selected, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvGoodsSize.setText(stocks.get(position).getSize());
        holder.tvGoodsRemain.setText(stocks.get(position).getNum() + "");
        holder.tvGoodsNums.setText(stocks.get(position).getBuyNum() + "");
        final int buyNums = Integer.valueOf(holder.tvGoodsNums.getText().toString());
        final int remainNums = Integer.valueOf(holder.tvGoodsRemain.getText().toString());
        holder.layoutAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    onClick.onAddMore(position,buyNums,remainNums);
                }
            }
        });

        holder.layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    if(buyNums!=0){
                        onClick.onAddToShoppingCarts(position,buyNums,stocks);
                    }else{
                        ToastUtils.showError(mContext,"数量不能为0");
                    }
                }
            }
        });

        holder.layoutMinusMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    onClick.onMinusMore(position,buyNums,remainNums);
                }
            }
        });
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tvGoodsSize)
        TextView tvGoodsSize;
        @BindView(R.id.layoutAddMore)
        LinearLayout layoutAddMore;
        @BindView(R.id.etGoodsNums)
        TextView tvGoodsNums;
        @BindView(R.id.tvGoodsRemain)
        TextView tvGoodsRemain;
        @BindView(R.id.layoutAdd)
        LinearLayout layoutAdd;
        @BindView(R.id.layoutMinusMore)
        LinearLayout layoutMinusMore;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnNumsSelectedClick{
        void onAddMore(int position,int buyNums,int goodsRemain);
        void onAddToShoppingCarts(int postion,int numbers,List<StocksBean> stocks);
        void onMinusMore(int position,int buyNums,int goodsRemain);
    }

    public void setStocks(List<StocksBean> stocks) {
        this.stocks = stocks;
        notifyDataSetChanged();
    }
}
