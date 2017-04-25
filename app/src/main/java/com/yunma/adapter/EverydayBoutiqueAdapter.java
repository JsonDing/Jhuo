package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.jhuo.activity.EntrepotGoodsDetial;
import com.yunma.jhuo.activity.EveryDayBoutique;
import com.yunma.bean.GoodsInfoResultBean.SuccessBean.ListBean;
import com.yunma.utils.*;
import com.yunma.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-01-05
 *
 * @author Json.
 */
public class EverydayBoutiqueAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ListBean> list;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private int mBottomCount=1;//底部View个数
    public EverydayBoutiqueAdapter(Context mContext) {
        this.list = new ArrayList<>();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        notifyDataSetChanged();
    }

    //内容长度
    private int getBodyItemCount(){
        return list.size();
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getBodyItemCount();
       if (mBottomCount != 0 && position >= dataItemCount) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return  new ViewHolder(mInflater.inflate(R.layout.item_entrepot_homepage, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(mInflater.inflate(R.layout.item_entrepot_see_more, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder) {
            GlideUtils.glidNormle(mContext, ((ViewHolder) holder).image, ConUtils.GOODS_IMAGE_URL + list.get(position).getPic());
            String s = "￥" + ValueUtils.toTwoDecimal(list.get(position).getUserPrice());
            SpannableStringBuilder ss = ValueUtils.changeText(mContext, R.color.color_b4, s, Typeface.NORMAL,
                    DensityUtils.sp2px(mContext, 12), 1, s.indexOf("."));
            ((ViewHolder) holder).tvCurrentPrice.setText(ss);
            ((ViewHolder) holder).tvOriginalPrice.setText(
                    String.valueOf("￥" + ValueUtils.toTwoDecimal(list.get(position).getSaleprice())));
            ((ViewHolder) holder).tvOriginalPrice.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);

            ((ViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EntrepotGoodsDetial.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("isToEnd", "no");
                    bundle.putSerializable("goodsBean", list.get(position));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }else if(holder instanceof BottomViewHolder){
            ((BottomViewHolder)holder).layoutSeeMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, EveryDayBoutique.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size() + mBottomCount;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tvSubscript;
        private TextView tvCurrentPrice;
        private TextView tvOriginalPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            tvSubscript = (TextView)itemView.findViewById(R.id.tvSubscript);
            tvCurrentPrice = (TextView)itemView.findViewById(R.id.tvCurrentPrice);
            tvOriginalPrice = (TextView)itemView.findViewById(R.id.tvOriginalPrice);
            tvSubscript.setBackgroundColor(Color.parseColor("#f44141"));
            tvSubscript.setText("精选");
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,width);
            image.setLayoutParams(params);
        }
    }

    private class BottomViewHolder extends RecyclerView.ViewHolder{
        private View layoutSeeMore;
        private TextView tvSeeMore;
        BottomViewHolder(View itemView) {
            super(itemView);
            layoutSeeMore = itemView.findViewById(R.id.layoutSeeMore);
            tvSeeMore = (TextView)itemView.findViewById(R.id.tvSeeMore);
            tvSeeMore.setText("查看完整精选商品");
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            layoutSeeMore.setLayoutParams(params);
        }
    }

    public void setList(List<ListBean> list) {
        this.list = list;
        notifyItemInserted(list.size());
    }
}
