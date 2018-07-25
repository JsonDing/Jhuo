package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ScreenUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Json on 2017/1/4.
 */
public class CompletedPayMentAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClickListener onItemClickListener;
    private List<SelfGoodsListBean> listBean;
    public CompletedPayMentAdapter(Context context,OnItemClickListener onItemClickListener) {
        this.mContext = context;
        this.onItemClickListener = onItemClickListener;
        this.listBean = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_intro, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String url ;
        if(listBean.get(position).getRepoid()==1){
            url = ConUtils.SElF_GOODS_IMAGE_URL;
        }else{
            url = ConUtils.GOODS_IMAGE_URL;
        }
        if(EmptyUtil.isNotEmpty(listBean.get(position).getPic())){
            GlideUtils.glidNormleFast(mContext,holder.imgGoods, url +
            listBean.get(position).getPic().split(",")[0] + "/min");
        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
        }

        if(listBean.get(position).getSpecialprice() != 0.0){
            holder.imgSpecialOffer.setVisibility(View.VISIBLE);
            SpannableStringBuilder span = new SpannableStringBuilder(
                    "缩进 "+listBean.get(position).getName());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvGoodsName.setText(span);
        }else{
            holder.imgSpecialOffer.setVisibility(View.GONE);
            holder.tvGoodsName.setText(listBean.get(position).getName());
        }

        holder.tvGoodsPrice.setText(ValueUtils.toTwoDecimal(listBean.get(position).getYmprice()));
       // if(EmptyUtil.isNotEmpty(listBean.get(position).getStock())){
            holder.tvSaleNums.setText(String.valueOf("库存 "+listBean.get(position).getStock() + " 件"));
        //}else {
           // holder.tvSaleNums.setText(String.valueOf("库存 "+"0"+ " 件"));
      //  }
        if((position+1)%2==0){
            holder.view1.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.GONE);
        }else{
            holder.view2.setVisibility(View.VISIBLE);
            holder.view1.setVisibility(View.GONE);
        }
        if(onItemClickListener != null){
            holder.imgGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(position,listBean.get(position));
                }
            });
        }

        return convertView;
    }

    public interface OnItemClickListener{
        void onItemClick(int position,SelfGoodsListBean listBean);
    }

    private class ViewHolder{
        ImageView imgGoods,imgSpecialOffer;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvSaleNums;
        View view1,view2,view3;

        ViewHolder(View view) {
            imgGoods = view.findViewById(R.id.imgGoods);
            imgSpecialOffer = view.findViewById(R.id.imgSpecialOffer);
            tvGoodsName = view.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = view.findViewById(R.id.tvGoodsPrice);
            tvSaleNums = view.findViewById(R.id.tvSaleNums);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view3 = view.findViewById(R.id.view3);
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,30))/2;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }

    public void setListBean(List<SelfGoodsListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }
}
