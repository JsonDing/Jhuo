package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-01-05
 *
 * @author Json.
 */
public class SearchGoodsByCodeAdapter extends RecyclerView.Adapter<SearchGoodsByCodeAdapter.ViewHolder> {
    private String nums;
    private LayoutInflater mInflater;
    private Context mContext;
    private OnClickListener onClickListener;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsBean;
    public SearchGoodsByCodeAdapter(Context context, OnClickListener onClickListener) {
        this.mContext = context;
        this.onClickListener = onClickListener;
        this.goodsBean = new ArrayList<>();
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return goodsBean.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(mInflater.inflate(R.layout.item_code_list, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        String str = goodsBean.get(position).getNumber();
        int fstart = str.indexOf(nums);
        int fend = fstart + nums.length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.RED),
                fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.tvGoodsCode.setText(style);
        GlideUtils.glidNormle(mContext,holder.imgsPre, ConUtils.SElF_GOODS_IMAGE_URL +
                goodsBean.get(position).getPic().split(",")[0]);
        holder.tvGoodsName.setText(goodsBean.get(position).getName());
        holder.tvGoodsPrice.setText(String.valueOf("￥" +
                ValueUtils.toTwoDecimal(goodsBean.get(position).getYmprice())));
        holder.tvGoodsSalePrice.getPaint().setFlags(
                Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        holder.tvGoodsSalePrice.setText(String.valueOf("￥" +
                ValueUtils.toTwoDecimal(goodsBean.get(position).getSaleprice())));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onClickListener){
                    onClickListener.onSearchClick(goodsBean.get(position));
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvGoodsCode;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvGoodsSalePrice;
        ImageView imgsPre;
        ViewHolder(View view) {
            super(view);
            tvGoodsCode = (TextView) view.findViewById(R.id.tvGoodsCode);
            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = (TextView) view.findViewById(R.id.tvGoodsPrice);
            tvGoodsSalePrice = (TextView) view.findViewById(R.id.tvGoodsSalePrice);
            imgsPre = (ImageView)view.findViewById(R.id.imgsPre);
        }
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public interface OnClickListener{
        void onSearchClick(GetSelfGoodsResultBean.SuccessBean.ListBean number);
    }

    public void setGoodsBean(List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsBean) {
       /* if(EmptyUtil.isNotEmpty(this.goodsBean)){
            this.goodsBean.clear();
            notifyDataSetChanged();
        }*/
        this.goodsBean = goodsBean;
        notifyItemInserted(goodsBean.size());
    }

    public void clear(){
        this.goodsBean.clear();
        notifyDataSetChanged();
    }

}
