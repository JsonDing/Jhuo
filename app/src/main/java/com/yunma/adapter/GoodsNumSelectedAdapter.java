package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GoodsInfoResultBean.SuccessBean.ListBean.StocksBean;

import java.util.List;

import butterknife.*;


/**
 * Created on 2017-01-09
 *
 * @author Json.
 */
public class GoodsNumSelectedAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<StocksBean> stocks;
    private OnNumsSelectedClick onClick;
    public GoodsNumSelectedAdapter(Context mContext, List<StocksBean> stocks,
                                   OnNumsSelectedClick onClick) {
        this.onClick = onClick;
        this.stocks =  stocks;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return stocks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
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
            view = mInflater.inflate(R.layout.item_size_nums, parent, false);
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
        @BindView(R.id.layoutMinusMore)
        LinearLayout layoutMinusMore;
        @BindView(R.id.tvGoodsRemain)
        TextView tvGoodsRemain;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnNumsSelectedClick{
        void onAddMore(int position,int buyNums,int goodsRemain);
        void onMinusMore(int position,int buyNums,int goodsRemain);
    }
}
