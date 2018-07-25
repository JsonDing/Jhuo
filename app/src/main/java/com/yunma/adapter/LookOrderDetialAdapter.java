package com.yunma.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.LookOrderBean;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2017-03-04
 *
 * @author Json.
 */

public class LookOrderDetialAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<LookOrderBean.SuccessBean.OrderdetailsBean> orderdetails;
    public LookOrderDetialAdapter( Context mContext,
                                  List<LookOrderBean.SuccessBean.OrderdetailsBean> orderdetails) {
        this.mContext = mContext;
        this.orderdetails = orderdetails;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return orderdetails.size();
    }

    @Override
    public Object getItem(int position) {
        return orderdetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_goods_manifest, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s =ValueUtils.toTwoDecimal(orderdetails.get(position).getMoney());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.b4,s, Typeface.NORMAL,
                14,0,s.indexOf("."));
        holder.tvPrice.setText(ss);
        holder.tvGoodsName.setText(orderdetails.get(position).getInfo());
        holder.tvGoodsInfo.setText(orderdetails.get(position).getSize());
        holder.tvGoodsNum.setText(String.valueOf(orderdetails.get(position).getNum()));
        if (orderdetails.get(position).getRepoid() == 1) {
                GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        orderdetails.get(position).getPic().split(",")[0]);
        } else {
            GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                    orderdetails.get(position).getPic().split(",")[0]);
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imgGoods,imgSpecialOffer;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvPrice;
        TextView tvGoodsInfo;

        ViewHolder(View view) {
            imgGoods = view.findViewById(R.id.imgGoods);
            imgSpecialOffer = view.findViewById(R.id.imgSpecialOffer);
            tvGoodsName = view.findViewById(R.id.tvGoodsName);
            tvGoodsNum = view.findViewById(R.id.tvGoodsNum);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvGoodsInfo = view.findViewById(R.id.tvGoodsInfo);
            imgSpecialOffer.setVisibility(View.INVISIBLE);
        }
    }
}
