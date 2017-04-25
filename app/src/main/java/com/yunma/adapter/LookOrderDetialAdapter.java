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
        String s ="￥" +  ValueUtils.toTwoDecimal(orderdetails.get(position).getMoney());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,14),1,s.indexOf("."));
        holder.tvPrice.setText(ss);
        holder.tvGoodsName.setText(orderdetails.get(position).getInfo());
        holder.tvGoodsInfo.setText("尺码：" + orderdetails.get(position).getSize());
        holder.tvGoodsNum.setText("x " + orderdetails.get(position).getNum());
        if (orderdetails.get(position).getRepoid() == 1) {
            if(orderdetails.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        orderdetails.get(position).getPic().split(",")[0]);
            }
        } else {
            GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                    orderdetails.get(position).getNumber() + ".jpg");
        }
        return convertView;
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvPrice;
        TextView tvGoodsInfo;

        ViewHolder(View view) {
            imgGoods = (ImageView) view.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
            tvGoodsNum = (TextView) view.findViewById(R.id.tvGoodsNum);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvGoodsInfo = (TextView) view.findViewById(R.id.tvGoodsInfo);
        }
    }
}
