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
public class HotGoodsListAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private int mHeaderCount=1;//头部View个数
    private int mBottomCount=1;//底部View个数
    private Context context;
    private LayoutOnClick layoutOnClick;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;
    public HotGoodsListAdapter(Context context, List<GoodsInfoResultBean.SuccessBean.ListBean> listBean,
                               LayoutOnClick layoutOnClick) {
        this.context = context;
        this.listBean = listBean;
        this.layoutOnClick = layoutOnClick;
    }


    //内容长度
    private int getBodyItemCount(){
        return listBean.size();
    }

    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (mHeaderCount + getBodyItemCount());
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position == mHeaderCount;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getBodyItemCount();
        if (mHeaderCount != 0 && position == mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        } else if (mBottomCount != 0 && position >= (mHeaderCount + dataItemCount)) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType ==ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_hot_goods_new,parent,false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return  new ViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_hot_goods,parent,false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(LayoutInflater.from(context)
                    .inflate(R.layout.item_hot_goods_new,parent,false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder)holder).imgsGoods.setImageDrawable(context
                    .getResources().getDrawable(R.drawable.bg_one));
            ((HeaderViewHolder)holder).tvIntro.setText("已精选" + listBean.size() + "件商品");

        }else if(holder instanceof ViewHolder) {
            final int pos;
            if(position==0){
                pos = 0;
                ((ViewHolder)holder).tvTags.setText(String.valueOf(position ));
            }else {
                pos = position -1;
                if(position<4){
                    ((ViewHolder)holder).tvTags.setText(String.valueOf(position ));
                }else{
                    ((ViewHolder)holder).tvTags.setVisibility(View.GONE);
                }
            }
            ((ViewHolder)holder).tvGoodsName.setText(listBean.get(pos).getName());
            String price = "￥" + listBean.get(pos).getUserPrice();
            SpannableStringBuilder ss = ValueUtils.changeText(context,R.color.color_b4,price,
                    Typeface.NORMAL, DensityUtils.sp2px(context,14),1,price.indexOf("."));
            ((ViewHolder)holder).tvCurrPrice.setText(ss);
            ((ViewHolder)holder).tvOriginalPrice.setText("￥" + listBean.get(pos).getSaleprice());
            ((ViewHolder)holder).tvOriginalPrice.getPaint().setFlags(
                    Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            if(listBean.get(pos).getPic()!=null){
                GlideUtils.glidNormle(context,((ViewHolder)holder).imgsGoods,ConUtils.GOODS_IMAGE_URL +
                        listBean.get(pos).getPic());
            }else{
                ((ViewHolder)holder).imgsGoods.setImageDrawable(context.getResources()
                        .getDrawable(R.drawable.default_pic));
            }
            if(listBean.get(pos).getSales()==null){
                ((ViewHolder)holder).tvSaleNums.setText("销售量 0");
            }else{
                ((ViewHolder)holder).tvSaleNums.setText("销售量 " + listBean.get(pos).getSales());
            }
            ((ViewHolder)holder).layoutClick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(layoutOnClick!=null){
                        layoutOnClick.onLayoutClick(listBean.get(pos));
                    }
                }
            });
        }else if(holder instanceof BottomViewHolder){
            ((BottomViewHolder)holder).imgsGoods.setImageDrawable(context
                    .getResources().getDrawable(R.drawable.bg_two));
        }
    }

    @Override
    public int getItemCount() {
        return mHeaderCount + getBodyItemCount() + mBottomCount;
    }

    private class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgsGoods;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriginalPrice;
        TextView tvSaleNums;
        TextView tvTags;
        View layout;
        View layoutPrice;
        View view1,view2,view3,layoutClick;
        public ViewHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.layout);
            layoutPrice = itemView.findViewById(R.id.layoutPrice);
            imgsGoods = (ImageView) itemView.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            tvCurrPrice = (TextView) itemView.findViewById(R.id.tvCurrPrice);
            tvOriginalPrice = (TextView) itemView.findViewById(R.id.tvOriginalPrice);
            tvSaleNums = (TextView) itemView.findViewById(R.id.tvSaleNums);
            tvTags = (TextView)itemView.findViewById(R.id.tvTags);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            layoutClick = itemView.findViewById(R.id.layoutClick);
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,25))/2;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
            imgsGoods.setLayoutParams(params);
        }
    }

    private class HeaderViewHolder extends RecyclerView.ViewHolder{

        ImageView imgsGoods;
        TextView tvIntro;
        View view1,view2,view3;
        HeaderViewHolder(View itemView) {
            super(itemView);
             imgsGoods = (ImageView) itemView.findViewById(R.id.imgGoods);
             tvIntro = (TextView)itemView.findViewById(R.id.tvIntro);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
        }
    }

    private class BottomViewHolder extends RecyclerView.ViewHolder{

        ImageView imgsGoods;
        TextView tvIntro;
        View view1,view2,view3;
        BottomViewHolder(View itemView) {
            super(itemView);
            imgsGoods = (ImageView) itemView.findViewById(R.id.imgGoods);
            tvIntro = (TextView)itemView.findViewById(R.id.tvIntro);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
        }
    }

    public interface LayoutOnClick{
        void onLayoutClick(GoodsInfoResultBean.SuccessBean.ListBean bean);
    }
}
