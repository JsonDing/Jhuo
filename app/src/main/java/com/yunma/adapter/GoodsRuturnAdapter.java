package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.ServiceResultBean.SuccessBean.ListBean.ServiceDetailsBean;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

class GoodsRuturnAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private Animation push_left_in,push_right_in;
    private List<ServiceDetailsBean> serviceDetails;
    public GoodsRuturnAdapter(Context mContext, List<ServiceDetailsBean> serviceDetails) {
        this.mContext = mContext;
        this.serviceDetails = serviceDetails;
        inflater = LayoutInflater.from(mContext);
        push_left_in= AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
        push_right_in=AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
    }

    @Override
    public int getCount() {
        return serviceDetails.size();
    }

    @Override
    public Object getItem(int position) {
        return serviceDetails.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_goods_return, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if (position % 2 == 0) {
            push_left_in.setDuration(800);
            view.setAnimation(push_left_in);
        } else {
            push_right_in.setDuration(800);
            view.setAnimation(push_right_in);
        }
        holder.tvGoodsName.setText(serviceDetails.get(position).getOrderdetail().getInfo());
        holder.tvGoodsInfo.setText("颜色：如图 " + " 尺码：" + serviceDetails.get(position).getSize());
        holder.tvPrice.setText("￥" + serviceDetails.get(position).getOrderdetail().getUserprice());
        holder.tvGoodsNum.setText("x" + serviceDetails.get(position).getNum());

        if(serviceDetails.get(position).getOrderdetail().getRepoid()==1){
            if(EmptyUtil.isNotEmpty(serviceDetails.get(position).getOrderdetail().getPic())){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.SElF_GOODS_IMAGE_URL +
                        serviceDetails.get(position).getOrderdetail().getPic().split(",")[0]);
            }else{

                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }else{
            if(EmptyUtil.isNotEmpty(serviceDetails.get(position).getOrderdetail().getPic())){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.GOODS_IMAGE_URL +
                        serviceDetails.get(position).getOrderdetail().getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
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
            imgGoods = itemView.findViewById(R.id.imgGoods);
            tvGoodsName = itemView.findViewById(R.id.tvGoodsName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvGoodsInfo = itemView.findViewById(R.id.tvGoodsInfo);
            tvGoodsNum = itemView.findViewById(R.id.tvGoodsNum);
        }
    }
}
