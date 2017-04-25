package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;

import java.util.List;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderGoodsAdapter extends BaseAdapter {
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
        holder.tvGoodsInfo.setText("颜色：如图" + "  尺码: "+orderdetailsBeen.get(position).getSize());
        holder.tvPrice.setText("￥" + orderdetailsBeen.get(position).getMoney());
        holder.tvGoodsNum.setText("x " + orderdetailsBeen.get(position).getNum());

        if(orderdetailsBeen.get(position).getRepoid()==1){
            if(orderdetailsBeen.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        orderdetailsBeen.get(position).getPic().split(",")[0]);
            }else{
                holder.imgGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
            }
        }else{
            if(orderdetailsBeen.get(position).getNumber()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        orderdetailsBeen.get(position).getNumber() +".jpg");
            }else{
                holder.imgGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
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
        public ViewHolder(View itemView) {
             imgGoods = (ImageView)itemView.findViewById(R.id.imgGoods);
             tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
             tvGoodsInfo = (TextView) itemView.findViewById(R.id.tvGoodsInfo);
             tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
             tvGoodsNum = (TextView) itemView.findViewById(R.id.tvGoodsNum);
        }
    }
}
