package com.yunma.adapter;

import android.content.*;
import android.graphics.*;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.SelfGoodsDetials;
import com.yunma.bean.*;
import com.yunma.utils.*;
import com.yunma.publish.*;

import java.util.*;

/**
 * Created on 2017-03-15
 *
 * @author Json.
 */

public class SelfKindredGoodsListAdapter extends RecyclerView.Adapter<SelfKindredGoodsListAdapter.ViewHolder>{

    private Context mContext;
    List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean;
    private LayoutInflater inflater;
    private int[] tagColor = new int[]{R.drawable.subscript_1,R.drawable.subscript_2,
            R.drawable.subscript_3,R.drawable.subscript_4,R.drawable.subscript_5};
    public SelfKindredGoodsListAdapter(Context mContext,
                                       List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean) {
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
        return new ViewHolder(inflater.inflate(R.layout.item_kingded_goods_self, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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
        String s ="￥" +  ValueUtils.toTwoDecimal(listBean.get(position).getYmprice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,16),1,s.indexOf("."));
        holder.tvCurrPrice.setText(ss);
        holder.tvOriPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.tvOriPrice.setText("￥" +
                ValueUtils.toTwoDecimal(listBean.get(position).getSaleprice()));
        if(EmptyUtil.isNotEmpty(listBean.get(position).getPic())){
            final String str[] = listBean.get(position).getPic().split(",");
            GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.SElF_GOODS_IMAGE_URL +
                    listBean.get(position).getPic().split(",")[0]);

            holder.imgGoods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocalImagePathBean preBean = new LocalImagePathBean();
                    List<PathBean> pathBeenList = new ArrayList<>();
                    for (String value : str) {
                        PathBean pathBean = new PathBean();
                        pathBean.setImgsPath(ConUtils.SElF_GOODS_IMAGE_URL + value);
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
                Intent intent = new Intent(mContext, SelfGoodsDetials.class);
                Bundle bundle = new Bundle();
                bundle.putString("isToEnd","yes");
                bundle.putString("goodId", String.valueOf(listBean.get(position).getId()));
                bundle.putSerializable("goodsDetials", listBean.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvCurrPrice;
        TextView tvOriPrice;
        TextView tvGoodsTags;
        TextView tvTags;
        View layout,view;
        ViewHolder(View v) {
            super(v);
            imgGoods = (ImageView) v.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) v.findViewById(R.id.tvGoodsName);
            tvCurrPrice = (TextView) v.findViewById(R.id.tvCurrPrice);
            tvOriPrice = (TextView) v.findViewById(R.id.tvOriPrice);
            tvGoodsTags = (TextView) v.findViewById(R.id.tvGoodsTags);
            tvTags = (TextView) v.findViewById(R.id.tvTags);
            layout = v.findViewById(R.id.layout);
            view = v.findViewById(R.id.view);
        }
    }

    public List<GetSelfGoodsResultBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
    }
}
