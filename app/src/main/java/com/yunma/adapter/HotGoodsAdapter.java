package com.yunma.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class HotGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Activity mContext;
    private int pos;
    private List<ListBean> listBean;
    private LayoutInflater mInflater;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private int mBottomCount=1;//底部View个数
    private static int[] tagColor = new int[]{R.drawable.subscript_1,R.drawable.subscript_2,
            R.drawable.subscript_3,R.drawable.subscript_4,R.drawable.subscript_5};

    public HotGoodsAdapter(Activity mContext) {
        this.listBean = new ArrayList<>();
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    //内容长度
    private int getBodyItemCount(){
        return listBean.size();
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
            return  new ViewHolder(mInflater.inflate(R.layout.item_entrepot_homepage_1, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(mInflater.inflate(R.layout.item_entrepot_see_more, parent, false));
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof ViewHolder) {
            ((ViewHolder) holder).tvSubscript.setBackgroundResource(
                    tagColor[position % tagColor.length]);
                ((ViewHolder) holder).tvSubscript.setText(String.valueOf(position + 1));
                String s = "￥" + ValueUtils.toTwoDecimal(listBean.get(position).getUserPrice());
                SpannableStringBuilder ss = ValueUtils.changeText(mContext, R.color.color_b4,
                        s, Typeface.NORMAL,
                        DensityUtils.sp2px(mContext, 12), 1, s.indexOf("."));
                ((ViewHolder) holder).tvCurrentPrice.setText(ss);
                ((ViewHolder) holder).tvSaleNums.setText("销量20");
                GlideUtils.glidNormle(mContext, ((ViewHolder) holder).image, ConUtils.GOODS_IMAGE_URL +
                        listBean.get(position).getPic());
                ((ViewHolder) holder).image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext, EntrepotGoodsDetial.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("isToEnd", "no");
                        bundle.putSerializable("goodsBean", listBean.get(position));
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });
        } else if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder) holder).layoutSeeMore.setOnClickListener(new View.OnClickListener() {
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
        return listBean.size() + mBottomCount;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView tvSubscript;
        private TextView tvCurrentPrice;
        private TextView tvSaleNums;
        private View layout;
        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            tvSubscript = (TextView)itemView.findViewById(R.id.tvSubscript);
            tvCurrentPrice = (TextView)itemView.findViewById(R.id.tvCurrentPrice);
            tvSaleNums = (TextView)itemView.findViewById(R.id.tvSaleNums);
            layout = itemView.findViewById(R.id.layout);
            tvSubscript.setBackgroundColor(Color.parseColor("#f44141"));
            tvSubscript.setText("热销");
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            layout.setLayoutParams(params);
        }
    }

    class BottomViewHolder extends RecyclerView.ViewHolder{
        private View layoutSeeMore;
        private TextView tvSeeMore;

        public BottomViewHolder(View itemView) {
            super(itemView);
            layoutSeeMore = itemView.findViewById(R.id.layoutSeeMore);
            tvSeeMore = (TextView)itemView.findViewById(R.id.tvSeeMore);
            tvSeeMore.setText("查看完整热销榜单");
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            layoutSeeMore.setLayoutParams(params);
        }
    }

    public void setListBean(List<ListBean> listBean) {
        this.listBean = listBean;
        notifyItemInserted(listBean.size());
    }
}
