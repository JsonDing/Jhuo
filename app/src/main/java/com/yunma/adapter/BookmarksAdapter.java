package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.mine.SimilarGoods;
import com.yunma.bean.GetCollectResultBean;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-01-16
 *
 * @author Json.
 */
public class BookmarksAdapter extends RecyclerView.Adapter<BookmarksAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private List<GetCollectResultBean.SuccessBean> success;
    public BookmarksAdapter(OnItemClick onItemClick, Context context) {
        this.onItemClick = onItemClick;
        this.success = new ArrayList<>();
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(inflater.inflate(R.layout.item_book_marks, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(success.get(position).getRepoid()==1){
            if(success.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        success.get(position).getPic().split(",")[0]);
            } else{
                holder.imgGoods.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.default_pic));
            }
        }else{
            if(success.get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        success.get(position).getPic());
            } else{
                holder.imgGoods.setImageDrawable(mContext
                        .getResources().getDrawable(R.drawable.default_pic));
            }

        }
        holder.tvGoodsName.setText(success.get(position).getName());
        holder.tvGoodsPrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(
                success.get(position).getYunmaprice())));

        holder.layoutDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClick!=null) {
                    onItemClick.onDelClick(position,success.get(position).getId());
                }
            }
        });

        holder.btnLooksimilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,SimilarGoods.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("goodsInfo",success.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });

        holder.layoutSeeDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onItemClick(holder.layoutSeeDetials,position,success.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return success.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.textview) TextView textview;
        @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
        @BindView(R.id.btnLooksimilar) Button btnLooksimilar;
        @BindView(R.id.layoutDelete) LinearLayout layoutDelete;
        @BindView(R.id.layoutSeeDetials) View layoutSeeDetials;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClick{
        void onItemClick(View view, int position, GetCollectResultBean.SuccessBean successBean);
        void onDelClick(int position,String goodId);
    }


    public void AddListBean(List<GetCollectResultBean.SuccessBean> success) {
        this.success = success;
        notifyItemInserted(success.size());
    }

    public void remove(int position) {
        success.remove(position);
        notifyItemRemoved(position);
    }
}
