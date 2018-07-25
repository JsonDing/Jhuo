package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.List;


/**
 * Created by Json on 2017/1/1.
 */
public class GoodsManifestAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<GetShoppingListBean.SuccessBean> resultBean;
    public GoodsManifestAdapter(Context mContext,
                                List<GetShoppingListBean.SuccessBean> resultBean) {
        this.mContext = mContext;
        this.resultBean = resultBean;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return resultBean.size();
    }

    @Override
    public Object getItem(int position) {
        return resultBean.get(position);
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
        String s;
        if (resultBean.get(position).getSpecialprice() != 0.00) {
            holder.imgSpecialOffer.setVisibility(View.VISIBLE);
            SpannableStringBuilder span = new SpannableStringBuilder(
                    "缩进 "+resultBean.get(position).getName());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvGoodsName.setText(span);
            s = ValueUtils.toTwoDecimal(resultBean.get(position).getSpecialprice());
        } else {
            holder.imgSpecialOffer.setVisibility(View.GONE);
            holder.tvGoodsName.setText(resultBean.get(position).getName());
            s = ValueUtils.toTwoDecimal(resultBean.get(position).getYunmaprice());
        }
        SpannableStringBuilder ss = ValueUtils.changeTextSize(mContext,s,14,0,s.indexOf("."));
        holder.tvPrice.setText(ss);
        holder.tvGoodsInfo.setText(resultBean.get(position).getCartSize());
        holder.tvGoodsNum.setText(String.valueOf(resultBean.get(position).getCartNum()));
        if(resultBean.get(position).getRepoid()==1){
            if(resultBean.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.SElF_GOODS_IMAGE_URL+
                        resultBean.get(position).getPic().split(",")[0] + "/min");
            }else{
                holder.imgGoods.setImageDrawable(
                        ContextCompat.getDrawable(mContext,R.drawable.default_pic));
            }
        }else {
            if(resultBean.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.GOODS_IMAGE_URL +
                        resultBean.get(position).getPic().split(",")[0] + "/min");
            }else{
                holder.imgGoods.setImageDrawable(
                        ContextCompat.getDrawable(mContext,R.drawable.default_pic));
            }
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvPrice;
        TextView tvGoodsInfo;
        ImageView imgSpecialOffer;
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
