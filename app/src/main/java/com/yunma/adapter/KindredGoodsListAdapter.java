package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.LocalImagePathBean;
import com.yunma.bean.PathBean;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.jhuo.activity.PicViewerActivity;
import com.yunma.jhuo.activity.homepage.GoodsDetialsActivity;
import com.yunma.utils.ConUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-03-15
 *
 * @author Json.
 */

public class KindredGoodsListAdapter extends RecyclerView.Adapter<KindredGoodsListAdapter.ViewHolder>{

    private Context mContext;
    private List<SelfGoodsListBean> listBean;
    private LayoutInflater inflater;
    private int[] tagColor = new int[]{R.drawable.subscript_1,R.drawable.subscript_2,
            R.drawable.subscript_3};
    public KindredGoodsListAdapter(Context mContext,
                                   List<SelfGoodsListBean> listBean) {
        this.mContext = mContext;
        this.listBean = listBean;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_kingded_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position>2){
            holder.tvTags.setVisibility(View.GONE);
        }else{
            holder.tvTags.setVisibility(View.VISIBLE);
            holder.tvTags.setBackgroundResource(tagColor[position%tagColor.length]);
            holder.tvTags.setText(position+1+"");
        }
        String s;
        if (listBean.get(position).getSpecialprice() != 0.00) {
            holder.imgSpecialOffer.setVisibility(View.VISIBLE);
            SpannableStringBuilder span = new SpannableStringBuilder(
                    "缩进 " + listBean.get(position).getName());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvGoodsName.setText(span);
            s = ValueUtils.toTwoDecimal(listBean.get(position).getSpecialprice());
        } else {
            holder.imgSpecialOffer.setVisibility(View.GONE);
            holder.tvGoodsName.setText(listBean.get(position).getName());
            s = ValueUtils.toTwoDecimal(listBean.get(position).getYmprice());
        }
        SpannableStringBuilder ss = ValueUtils.changeTextSize(mContext, s, 14, 0, s.indexOf("."));
        holder.tvCurrPrice.setText(ss);
        holder.tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvOriPrice.setText(ValueUtils.toTwoDecimal(listBean.get(position).getSaleprice()));
        final String url;
        if(listBean.get(position).getRepoid()==1){
            url = ConUtils.SElF_GOODS_IMAGE_URL;
        }else{
            url = ConUtils.GOODS_IMAGE_URL;
        }
        if(EmptyUtil.isNotEmpty(listBean.get(position).getPic())){
            final String str[] = listBean.get(position).getPic().split(",");
            GlideUtils.glidNormleFast(mContext,holder.imgGoods,url +
                    listBean.get(position).getPic().split(",")[0] + "/min");
            holder.imgGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalImagePathBean preBean = new LocalImagePathBean();
                    List<PathBean> pathBeenList = new ArrayList<>();
                    for (String value : str) {
                        PathBean pathBean = new PathBean();
                        pathBean.setImgsPath(url + value);
                        pathBeenList.add(pathBean);
                    }
                    preBean.setPathBeen(pathBeenList);
                    Intent intent = new Intent(mContext,PicViewerActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("path",preBean);
                    bundle.putInt("pos", 0);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });

        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
        }
        if(listBean.get(position).getLabel()!=null){
            holder.tvGoodsTags.setText(listBean.get(position).getLabel());
        }else{
            holder.tvGoodsTags.setText("");
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, GoodsDetialsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("isToEnd","yes");
                bundle.putInt("goodId", listBean.get(holder.getAdapterPosition()).getId());
                bundle.putSerializable("goodsDetials", listBean.get(holder.getAdapterPosition()));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgGoods,imgSpecialOffer;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriPrice;
        TextView tvGoodsTags;
        TextView tvTags;
        View view;
        ViewHolder(View v) {
            super(v);
            imgGoods = v.findViewById(R.id.imgGoods);
            imgSpecialOffer = v.findViewById(R.id.imgSpecialOffer);
            tvGoodsName = v.findViewById(R.id.tvGoodsName);
            tvCurrPrice = v.findViewById(R.id.tvCurrPrice);
            tvOriPrice = v.findViewById(R.id.tvOriPrice);
            tvGoodsTags = v.findViewById(R.id.tvGoodsTags);
            tvTags = v.findViewById(R.id.tvTags);
            view = v.findViewById(R.id.view);
        }
    }

    public List<SelfGoodsListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<SelfGoodsListBean> listBean) {
        this.listBean = listBean;
    }
}
