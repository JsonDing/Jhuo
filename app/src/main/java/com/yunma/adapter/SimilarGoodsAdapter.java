package com.yunma.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-01-17
 *
 * @author Json.
 */
public class SimilarGoodsAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private List<SelfGoodsListBean> mListBean;
    public SimilarGoodsAdapter(Context mContext,OnItemClick onItemClick) {
        this.mContext = mContext;
        this.onItemClick = onItemClick;
        this.mListBean = new ArrayList<>();
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mListBean.size();
    }

    @Override
    public Object getItem(int position) {
        return mListBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_similar_goods, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(position == mListBean.size()-1){
            holder.view.setVisibility(View.GONE);
        }else{
            holder.view.setVisibility(View.VISIBLE);
        }
        String s =ValueUtils.toTwoDecimal(mListBean.get(position).getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.b4,s, Typeface.NORMAL,
                14,0,s.indexOf("."));
        holder.tvCurrPrice.setText(ss);
        holder.tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvOriPrice.setText(ValueUtils.toTwoDecimal(mListBean.get(position).getSaleprice()));
        holder.tvGoodsName.setText(mListBean.get(position).getName());
        holder.tvMonthNums.setText(String.valueOf(mListBean.get(position).getStock()));
        final String url;
        if(mListBean.get(position).getRepoid()==1){
            url = ConUtils.SElF_GOODS_IMAGE_URL;
        }else{
            url = ConUtils.GOODS_IMAGE_URL;
        }
        GlideUtils.glidNormle(mContext,holder.imgGoods, url +
                mListBean.get(position).getPic().split(",")[0]);
        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick != null){
                    onItemClick.onItemClick(mListBean.get(position));
                }
            }
        });
        return convertView;
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriPrice;
        TextView tvMonthNums;
        View view,rootView;
        ViewHolder(View v) {
            imgGoods = v.findViewById(R.id.imgGoods);
            tvGoodsName = v.findViewById(R.id.tvGoodsName);
            tvCurrPrice = v.findViewById(R.id.tvCurrPrice);
            tvOriPrice = v.findViewById(R.id.tvOriPrice);
            tvMonthNums = v.findViewById(R.id.tvMonthNums);
            view = v.findViewById(R.id.view);
            rootView = v;
        }
    }

    public interface OnItemClick{
        void onItemClick(SelfGoodsListBean mListBean);
    }

    public void setmListBean(List<SelfGoodsListBean> mListBean) {
        this.mListBean = mListBean;
        notifyDataSetChanged();
    }
}
