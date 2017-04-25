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
 * Created on 2017-04-11
 *
 * @author Json.
 */

public class OffTheShelfAdapter extends RecyclerView.Adapter<OffTheShelfAdapter.ViewHolder>  {
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean = null;
    public OffTheShelfAdapter(OnItemClick onItemClick, Context context) {
        this.onItemClick = onItemClick;
        this.listBean = new ArrayList<>();
        this.mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_reshelf, parent, false));
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

        holder.btnReShelf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onReShelf(
                            listBean.get(position).getId(),position,listBean.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    public interface OnItemClick{
        void onItemClick(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean);
        void onReShelf(int id,int position,GetSelfGoodsResultBean.SuccessBean.ListBean listBean);
    }

    public List<GetSelfGoodsResultBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<GetSelfGoodsResultBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
        //notifyItemInserted(listBean.size());
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.imgGoods)
        ImageView imgGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.textview) TextView textview;
        @BindView(R.id.btnReShelf)
        Button btnReShelf;
        @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
