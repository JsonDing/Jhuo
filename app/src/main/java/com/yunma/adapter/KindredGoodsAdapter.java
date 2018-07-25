package com.yunma.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.*;
import com.yunma.jhuo.m.OnKindredGoodsClick;
import com.yunma.utils.*;

import java.util.List;


/**
 * Created on 2016-12-30
 *
 * @author Json.
 */
public class KindredGoodsAdapter extends RecyclerView.Adapter<KindredGoodsAdapter.ViewHolder>{

    private Context mContext;
    private List<SelfGoodsListBean> listBeen;
    private LayoutInflater mInflater;
    private boolean isLastPage;
    private FrameLayout.LayoutParams params;
    private OnKindredGoodsClick onKindredGoodsClick;
    private int[] tagColor = new int[]{R.drawable.subscript_1,R.drawable.subscript_2,
            R.drawable.subscript_3,R.drawable.subscript_4,R.drawable.subscript_5};

    public KindredGoodsAdapter(Context mContext, List<SelfGoodsListBean> listBeen,
                               OnKindredGoodsClick onKindredGoodsClick, boolean isLastPage) {
        this.mContext = mContext;
        this.listBeen = listBeen;
        this.isLastPage = isLastPage;
        this.onKindredGoodsClick = onKindredGoodsClick;
        mInflater = LayoutInflater.from(mContext);
        int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
        params = new FrameLayout.LayoutParams(width,width);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.kindred_goods_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if(position != getItemCount()-1){
            holder.tvSubscript.setBackgroundResource(tagColor[position%tagColor.length]);
            holder.image.setLayoutParams(params);
            holder.layoutSeeMore.setVisibility(View.GONE);
            holder.tvSubscript.setText(String.valueOf(position+1));
            if(listBeen.get(position).getSpecialprice() != 0.0){
                holder.tvCurrentPrice.setText(
                        String.valueOf("￥"+ ValueUtils.toTwoDecimal(
                                listBeen.get(position).getSpecialprice())));
            }else{
                holder.tvCurrentPrice.setText(
                        String.valueOf("￥"+ ValueUtils.toTwoDecimal(
                                listBeen.get(position).getYmprice())));
            }
            holder.tvOriginalPrice.setText(
                    String.valueOf("￥"+ ValueUtils.toTwoDecimal(
                            listBeen.get(position).getSaleprice())));
            String url;
            if(listBeen.get(position).getRepoid()==1){
                url = ConUtils.SElF_GOODS_IMAGE_URL;
            }else{
                url = ConUtils.GOODS_IMAGE_URL;
            }
            if(listBeen.get(position).getPic()!=null&&
                    listBeen.get(position).getPic().split(",").length!=0){
                GlideUtils.glidNormleFast(mContext,holder.image,url +
                        listBeen.get(position).getPic().split(",")[0] + "/min");
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.image,R.drawable.default_pic);
            }
        }else{
            holder.tvSubscript.setVisibility(View.INVISIBLE);
            holder.layoutSeeMore.setLayoutParams(params);
            holder.tvCurrentPrice.setVisibility(View.GONE);
            holder.tvOriginalPrice.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
            holder.layoutSeeMore.setVisibility(View.VISIBLE);
        }
        holder.tvOriginalPrice.getPaint().setFlags(
                Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onKindredGoodsClick!=null){
                    onKindredGoodsClick.onSeeSelfGoodsDatialListener(holder.getAdapterPosition(),
                            listBeen.get(holder.getAdapterPosition()));
                }
            }
        });

        holder.layoutSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onKindredGoodsClick!=null){
                    onKindredGoodsClick.onSeeSelfMoreListener(
                            String.valueOf(listBeen.get(holder.getAdapterPosition()-1).getSex()),
                            String.valueOf(listBeen.get(holder.getAdapterPosition()-1).getType()));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(isLastPage){
            return listBeen.size();
        }else{
            return listBeen.size()+1;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView image;
        private TextView tvSubscript;
        private TextView tvCurrentPrice;
        private TextView tvOriginalPrice;
        private View layoutSeeMore;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            tvSubscript = itemView.findViewById(R.id.tvSubscript);
            tvCurrentPrice = itemView.findViewById(R.id.tvCurrentPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            layoutSeeMore = itemView.findViewById(R.id.layoutSeeMore);
        }
    }
}
