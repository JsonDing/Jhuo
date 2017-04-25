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
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBeen;
    private LayoutInflater mInflater;
    private boolean isLastPage;
    private FrameLayout.LayoutParams params;
    private OnKindredGoodsClick onKindredGoodsClick;
    private int[] tagColor = new int[]{R.drawable.subscript_1,R.drawable.subscript_2,
            R.drawable.subscript_3,R.drawable.subscript_4,R.drawable.subscript_5};

    public KindredGoodsAdapter(Context mContext, List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBeen,
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
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvSubscript.setBackgroundResource(tagColor[position%tagColor.length]);
        if(position!=listBeen.size()){
            holder.image.setLayoutParams(params);
            holder.layoutSeeMore.setVisibility(View.GONE);
            holder.tvSubscript.setText(String.valueOf(position+1));
            holder.tvCurrentPrice.setText(
                    String.valueOf("￥"+ ValueUtils.toTwoDecimal(listBeen.get(position).getYmprice())));
            holder.tvOriginalPrice.setText(
                    String.valueOf("￥"+ ValueUtils.toTwoDecimal(listBeen.get(position).getSaleprice())));
            if(listBeen.get(position).getPic()!=null&&
                    listBeen.get(position).getPic().split(",").length!=0){
                GlideUtils.glidNormleFast(mContext,holder.image,ConUtils.SElF_GOODS_IMAGE_URL +
                        listBeen.get(position).getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.image,R.drawable.default_pic);
            }
        }else{
            holder.layoutSeeMore.setLayoutParams(params);
            holder.tvSubscript.setText(String.valueOf(position+1));
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
                    onKindredGoodsClick.onSeeSelfGoodsDatialListener(position,listBeen.get(position));
                }
            }
        });

        holder.layoutSeeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onKindredGoodsClick!=null){
                    onKindredGoodsClick.onSeeSelfMoreListener(position+1,
                            String.valueOf(listBeen.get(position-1).getType()));
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
            image = (ImageView) itemView.findViewById(R.id.image);
            tvSubscript = (TextView)itemView.findViewById(R.id.tvSubscript);
            tvCurrentPrice = (TextView)itemView.findViewById(R.id.tvCurrentPrice);
            tvOriginalPrice = (TextView)itemView.findViewById(R.id.tvOriginalPrice);
            layoutSeeMore = itemView.findViewById(R.id.layoutSeeMore);
        }
    }
}
