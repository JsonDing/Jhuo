package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.MainActivity;
import com.yunma.jhuo.activity.homepage.TomorrowPreSaleActivity;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2017-04-13
 *
 * @author Json.
 */

public class TomorrowUpdateAdapter  extends RecyclerView.Adapter<TomorrowUpdateAdapter.ViewHolder>{

    private Context mContext;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen;
    private LayoutInflater mInflater;
    public TomorrowUpdateAdapter(Context mContext,
                                 List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return  new ViewHolder(mInflater.inflate(R.layout.item_tomorrow_good, parent, false));
    }

    @Override
    public int getItemCount() {
        return  goodsListBeen.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position==goodsListBeen.size()-1){
            holder.viewLine.setVisibility(View.GONE);
        }else{
            holder.viewLine.setVisibility(View.VISIBLE);
        }
        GlideUtils.glidNormle(mContext, holder.imgsGoods, ConUtils.SElF_GOODS_IMAGE_URL
                + goodsListBeen.get(position).getPic().split(",")[0]);
        holder.tvGoodsName.setText(goodsListBeen.get(position).getName());
        String s = "￥" + ValueUtils.toTwoDecimal(goodsListBeen.get(position).getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext, R.color.color_b4, s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext, 12), 1, s.indexOf("."));
        holder.tvAppointPrice.setText(ss);
        holder.tvSalePrice.setText(
                String.valueOf("￥" + ValueUtils.toTwoDecimal(goodsListBeen.get(position).getSaleprice())));
        holder.tvSalePrice.getPaint().setFlags(
                Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.btnLookAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.mainContext, TomorrowPreSaleActivity.class);
                MainActivity.mainContext.startActivity(intent);
            }
        });

    }


    public void setListBean(List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        notifyItemInserted(goodsListBeen.size());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgsGoods;
        private TextView tvGoodsName;
        private TextView tvSalePrice;
        private TextView tvAppointPrice;
        private Button btnLookAll;
        private View viewLine;
        public ViewHolder(View itemView) {
            super(itemView);
            imgsGoods = (ImageView) itemView.findViewById(R.id.imgsGoods);
            tvGoodsName = (TextView)itemView.findViewById(R.id.tvGoodsName);
            tvSalePrice = (TextView)itemView.findViewById(R.id.tvSalePrice);
            tvAppointPrice = (TextView)itemView.findViewById(R.id.tvAppointPrice);
            btnLookAll = (Button)itemView.findViewById(R.id.btnLookAll);
            viewLine = itemView.findViewById(R.id.viewLine);
        }
    }

}
