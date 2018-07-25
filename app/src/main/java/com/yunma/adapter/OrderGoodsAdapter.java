package com.yunma.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.List;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderGoodsAdapter extends BaseAdapter{
    private Context mContext;
    private List<OrderdetailsBean> orderdetailsBeen;
    private LayoutInflater mInflater;
    private Animation push_left_in,push_right_in;
    public OrderGoodsAdapter(Context mContext, List<OrderdetailsBean> orderdetailsBeen) {
        this.mContext = mContext;
        this.orderdetailsBeen = orderdetailsBeen;
        mInflater = LayoutInflater.from(mContext);
        push_left_in= AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
        push_right_in=AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
    }

    @Override
    public int getCount() {
        return orderdetailsBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return orderdetailsBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_good_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if (position % 2 == 0) {
            push_left_in.setDuration(600);
            view.setAnimation(push_left_in);
        } else {
            push_right_in.setDuration(600);
            view.setAnimation(push_right_in);
        }
        holder.tvGoodsName.setText(orderdetailsBeen.get(position).getInfo());
        holder.tvGoodsInfo.setText(String.format("颜色：如图  尺码: %s",
                orderdetailsBeen.get(position).getSize()));
        holder.tvPrice.setText(String.format("￥%s", orderdetailsBeen.get(position).getUserprice()));
        holder.tvGoodsNum.setText(String.valueOf("x " + orderdetailsBeen.get(position).getNum()));

        if(orderdetailsBeen.get(position).getRepoid()==1){
            if(orderdetailsBeen.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        orderdetailsBeen.get(position).getPic().split(",")[0]);
            }else{
                holder.imgGoods.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.default_pic));
            }
        }else{
            if(orderdetailsBeen.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        orderdetailsBeen.get(position).getPic().split(",")[0]);
            }else{
                holder.imgGoods.setImageDrawable(ContextCompat.getDrawable(mContext,R.drawable.default_pic));
            }

        }
        String expressNumber = orderdetailsBeen.get(position).getExpressnumber();
        if (ValueUtils.equals(expressNumber,"00000000")){
            int status = holder.mNoDataLo.getVisibility();
            if (status != View.VISIBLE) {
                holder.mNoDataLo.setVisibility(View.VISIBLE);
            }
        } else {
            int status = holder.mNoDataLo.getVisibility();
            if (status != View.GONE) {
                holder.mNoDataLo.setVisibility(View.GONE);
            }
        }

        return view;
    }

    public class ViewHolder {
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvGoodsInfo;
        TextView tvPrice;
        TextView tvGoodsNum;
        LinearLayout mNoDataLo;
        public ViewHolder(View itemView) {
             imgGoods = itemView.findViewById(R.id.imgGoods);
             tvGoodsName = itemView.findViewById(R.id.tvGoodsName);
             tvGoodsInfo = itemView.findViewById(R.id.tvGoodsInfo);
             tvPrice = itemView.findViewById(R.id.tvPrice);
             tvGoodsNum = itemView.findViewById(R.id.tvGoodsNum);
             mNoDataLo = itemView.findViewById(R.id.mNoDataLo);
        }
    }
}
