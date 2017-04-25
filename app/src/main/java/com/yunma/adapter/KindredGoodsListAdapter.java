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
 * Created on 2017-01-04
 *
 * @author Json.
 */
public class KindredGoodsListAdapter extends RecyclerView.Adapter<KindredGoodsListAdapter.ViewHolder>{
    private Context context;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;
    private LayoutInflater inflater;
    private int[] tagColor = new int[]{R.drawable.subscript_1,R.drawable.subscript_2,
            R.drawable.subscript_3,R.drawable.subscript_4,R.drawable.subscript_5};
    public KindredGoodsListAdapter(Context context, List<GoodsInfoResultBean.SuccessBean.ListBean> listBean) {
        this.context = context;
        this.listBean = listBean;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_kingded_goods_manifest, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(position>4){
            holder.tvTags.setVisibility(View.GONE);
        }else{
            holder.tvTags.setVisibility(View.VISIBLE);
            holder.tvTags.setBackgroundResource(tagColor[position%tagColor.length]);
            holder.tvTags.setText(position+1+"");
        }
        if(position == listBean.size()-1){
            holder.layout.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.GONE);
        }else{
            holder.layout.setVisibility(View.GONE);
            holder.view.setVisibility(View.VISIBLE);
        }
        holder.tvGoodsName.setText(listBean.get(position).getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.get(position).getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(context,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(context,16),1,s.indexOf("."));
        holder.tvCurrPrice.setText(ss);
        holder.tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvOriPrice.setText("￥" +
                ValueUtils.toTwoDecimal(listBean.get(position).getSaleprice()));
        GlideUtils.glidNormle(context,holder.imgGoods,ConUtils.GOODS_IMAGE_URL +
                listBean.get(position).getPic());
        if(listBean.get(position).getSales()!=null){
            holder.tvMonthNums.setText(listBean.get(position).getSales());
        }else{
            holder.tvMonthNums.setText("0");
        }
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriPrice;
        TextView tvMonthNums;
        TextView tvTags;
        View layout,view;
        ViewHolder(View v) {
            super(v);
            imgGoods = (ImageView) v.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) v.findViewById(R.id.tvGoodsName);
            tvCurrPrice = (TextView) v.findViewById(R.id.tvCurrPrice);
            tvOriPrice = (TextView) v.findViewById(R.id.tvOriPrice);
            tvMonthNums = (TextView) v.findViewById(R.id.tvMonthNums);
            tvTags = (TextView) v.findViewById(R.id.tvTags);
            layout = v.findViewById(R.id.layout);
            view = v.findViewById(R.id.view);
        }
    }

    public List<GoodsInfoResultBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<GoodsInfoResultBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
    }
}
