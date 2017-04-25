package com.yunma.adapter;

import android.content.Context;
import android.graphics.*;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2017-01-06
 *
 * @author Json.
 */
public class BoutiqueAdapter extends RecyclerView.Adapter<BoutiqueAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private LayoutOnClick layoutOnClick;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;
    public BoutiqueAdapter(Context context, List<GoodsInfoResultBean.SuccessBean.ListBean> listBean,
                           LayoutOnClick layoutOnClick) {
        this.mContext = context;
        this.listBean = listBean;
        this.layoutOnClick = layoutOnClick;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_boutique_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position == listBean.size()-1){
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
        }else{
            holder.layout.setVisibility(View.GONE);
            holder.view.setVisibility(View.VISIBLE);
        }
        holder.tvGoodsName.setText(listBean.get(position).getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.get(position).getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,16),1,s.indexOf("."));
        holder.tvCurrPrice.setText(ss);
        holder.tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvOriPrice.setText("￥" +
                ValueUtils.toTwoDecimal(listBean.get(position).getSaleprice()));
        GlideUtils.glidNormle(mContext,holder.imgGoods,ConUtils.GOODS_IMAGE_URL +
                listBean.get(position).getPic());
        if(listBean.get(position).getSales()!=null){
            holder.tvMonthNums.setText(listBean.get(position).getSales());
        }else{
            holder.tvMonthNums.setText("0");
        }

        holder.layoutItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutOnClick!=null){
                    layoutOnClick.onLayoutClick(listBean.get(position));
                }
            }
        });
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriPrice;
        TextView tvMonthNums;
        View layout,view;
        View layoutItem;
        ViewHolder(View v) {
            super(v);
            imgGoods = (ImageView) v.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) v.findViewById(R.id.tvGoodsName);
            tvCurrPrice = (TextView) v.findViewById(R.id.tvCurrPrice);
            tvOriPrice = (TextView) v.findViewById(R.id.tvOriPrice);
            tvMonthNums = (TextView) v.findViewById(R.id.tvMonthNums);
            layout = v.findViewById(R.id.layout);
            view = v.findViewById(R.id.view);
            layoutItem = v.findViewById(R.id.layoutItem);
        }
    }

    public interface LayoutOnClick{
        void onLayoutClick(GoodsInfoResultBean.SuccessBean.ListBean bean);
    }
}
