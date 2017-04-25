package com.yunma.adapter;

import android.content.Context;
import android.graphics.*;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.utils.*;

/**
 * Created on 2017-01-17
 *
 * @author Json.
 */
public class SimilarGoodsAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;

    public SimilarGoodsAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 10;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_similar_goods, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(position == 9){
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
        }

        String s ="￥" +  ValueUtils.toTwoDecimal(699);
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,16),1,s.indexOf("."));
        holder.tvCurrPrice.setText(ss);
        holder.tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        return convertView;
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriPrice;
        TextView tvMonthNums;
        View layout,view;
        ViewHolder(View v) {
            imgGoods = (ImageView) v.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) v.findViewById(R.id.tvGoodsName);
            tvCurrPrice = (TextView) v.findViewById(R.id.tvCurrPrice);
            tvOriPrice = (TextView) v.findViewById(R.id.tvOriPrice);
            tvMonthNums = (TextView) v.findViewById(R.id.tvMonthNums);
            layout = v.findViewById(R.id.layout);
            view = v.findViewById(R.id.view);
        }
    }
}
