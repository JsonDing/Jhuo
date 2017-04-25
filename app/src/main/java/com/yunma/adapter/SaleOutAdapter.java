package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-10
 *
 * @author Json.
 */

public class SaleOutAdapter extends RecyclerView.Adapter<SaleOutAdapter.ViewHolder>  {
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean = null;
    public SaleOutAdapter(OnItemClick onItemClick, Context context) {
        this.onItemClick = onItemClick;
        this.listBean = new ArrayList<>();
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_sale_out, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if(listBean.get(position).getPic()!=null){
            GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                    listBean.get(position).getPic().split(",")[0]);
        } else{
            holder.imgGoods.setImageDrawable(mContext
                    .getResources().getDrawable(R.drawable.default_pic));
        }
        holder.tvGoodsName.setText(listBean.get(position).getName());
        holder.tvGoodsPrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(
                listBean.get(position).getYmprice())));


        holder.layoutSeeDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onItemClick(position,listBean.get(position));
                }
            }
        });
        holder.btnSoldOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onSoldOut(listBean.get(position).getId(),position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.textview) TextView textview;
        @BindView(R.id.btnSoldOut) Button btnSoldOut;
        @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
        @BindView(R.id.layoutSeeDetials) View layoutSeeDetials;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface OnItemClick{
        void onItemClick(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean);
        void onSoldOut(int id,int position);
    }

    public void addListBean(List<GetSelfGoodsResultBean.SuccessBean.ListBean> mListBean) {
        this.listBean = mListBean;
        notifyItemRangeInserted(getItemCount(),getItemCount()+mListBean.size()-1);
    }

    public void setListBean(List<GetSelfGoodsResultBean.SuccessBean.ListBean> mListBean) {
        this.listBean = mListBean;
        notifyItemInserted(this.listBean.size());
    }

    public void remove(int position) {
        listBean.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }


}
