package com.yunma.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.*;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created by Json on 2017/1/5.
 */
public class OrderDetialAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<OrderdetailsBean> orderdetails;
    public OrderDetialAdapter(Context mContext, List<OrderdetailsBean> orderdetails) {
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
        holder.tvGoodsInfo.setText("颜色：如图" + " 尺码：" + orderdetails.get(position).getSize());
        holder.tvGoodsNum.setText("x " + orderdetails.get(position).getNum());
        if(EmptyUtil.isNotEmpty(orderdetails.get(position).getPic())){
            GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.SElF_GOODS_IMAGE_URL +
            orderdetails.get(position).getPic().split(",")[0]);
        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
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
