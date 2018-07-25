package com.yunma.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.List;

/**
 * Created by Json on 2017/1/5.
 */
public class OrderDetialAdapter extends BaseAdapter{
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
        holder.imgSpecialOffer.setVisibility(View.GONE);
        String s = ValueUtils.toTwoDecimal(orderdetails.get(position).getMoney());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.b4,s, Typeface.NORMAL,
                14,0,s.indexOf("."));
        holder.tvPrice.setText(ss);
        holder.tvGoodsName.setText(orderdetails.get(position).getInfo());
        holder.tvGoodsInfo.setText(orderdetails.get(position).getSize());
        holder.tvGoodsNum.setText(String.valueOf(orderdetails.get(position).getNum()));
        String url;
        if(orderdetails.get(position).getRepoid()==1){
            url = ConUtils.SElF_GOODS_IMAGE_URL;
        }else{
            url = ConUtils.GOODS_IMAGE_URL;
        }
        if(EmptyUtil.isNotEmpty(orderdetails.get(position).getPic())){
            GlideUtils.glidNormleFast(mContext,holder.imgGoods,url +
            orderdetails.get(position).getPic().split(",")[0]);
        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
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
            tvGoodsName = view.findViewById(R.id.tvGoodsName);
            tvGoodsNum = view.findViewById(R.id.tvGoodsNum);
            tvPrice = view.findViewById(R.id.tvPrice);
            tvGoodsInfo = view.findViewById(R.id.tvGoodsInfo);
            imgSpecialOffer = view.findViewById(R.id.imgSpecialOffer);
        }
    }

}
