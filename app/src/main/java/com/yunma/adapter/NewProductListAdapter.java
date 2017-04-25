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
public class NewProductListAdapter extends RecyclerView.Adapter<NewProductListAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private LayoutOnClick layoutOnClick;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;
    public NewProductListAdapter(Context context, List<GoodsInfoResultBean.SuccessBean.ListBean> listBean,
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
        return new ViewHolder(inflater.inflate(R.layout.item_new_product, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if(position%2==0){
            holder.view2.setVisibility(View.VISIBLE);
            holder.view1.setVisibility(View.GONE);
        }else{
            holder.view2.setVisibility(View.GONE);
            holder.view1.setVisibility(View.VISIBLE);
        }

        if(listBean.get(position).getPic()!=null){
            GlideUtils.glidNormle(mContext,holder.imgGoods,
                    ConUtils.GOODS_IMAGE_URL + listBean.get(position).getPic());
        }else{
            holder.imgGoods.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.default_pic));
        }

        holder.tvGoodsName.setText(listBean.get(position).getName());
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.get(position).getUserPrice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,16),1,s.indexOf("."));

        holder.tvGoodsPrice.setText(ss);
        holder.tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvOriginalPrice.setText("￥" +
                ValueUtils.toTwoDecimal(listBean.get(position).getSaleprice()));

        holder.layoutClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutOnClick!=null){
                    layoutOnClick.onLayoutClick(listBean.get(position));
                }
            }
        });
        holder.imgsAddtoBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutOnClick!=null){
                    layoutOnClick.onAddBasketClick(listBean.get(position));
                }
            }
        });
    }

     class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvTags;
        TextView tvGoodsPrice;
        TextView tvOriginalPrice;
        ImageView imgsAddtoBasket;
        View view1,view2,view3,layoutClick;

        ViewHolder(View view) {
            super(view);
            imgGoods = (ImageView) view.findViewById(R.id.imgGoods);
            tvTags = (TextView)view.findViewById(R.id.tvTags);
            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = (TextView) view.findViewById(R.id.tvGoodsPrice);
            tvOriginalPrice = (TextView) view.findViewById(R.id.tvOriginalPrice);
            imgsAddtoBasket = (ImageView) view.findViewById(R.id.imgsAddtoBasket);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view3 = view.findViewById(R.id.view3);
            layoutClick = view.findViewById(R.id.layoutClick);
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,25))/2;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }

    public interface LayoutOnClick{
        void onLayoutClick(GoodsInfoResultBean.SuccessBean.ListBean bean);
        void onAddBasketClick(GoodsInfoResultBean.SuccessBean.ListBean bean);
    }
}
