package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-01-09
 *
 * @author Json.
 */
public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.ViewHolder> {
    private Context mContext;
    private SelectGoods selectGoodsClick;
    private OnNumsSelectedClick onClick;
    private OnItemClickListener onItemClickListener;
    private boolean isEditor = false;
    private List<GetShoppingListBean.SuccessBean> shoppingCartsList;
    public BasketAdapter(Context mContext, SelectGoods selectGoodsClick,
                         OnNumsSelectedClick onClick, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.onClick = onClick;
        this.onItemClickListener = onItemClickListener;
        this.selectGoodsClick = selectGoodsClick;
        this.shoppingCartsList = new ArrayList<>();
        initSelectedPosition(0);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void initSelectedPosition(int states) {
        int  size = shoppingCartsList.size();
        for (int i = 0; i < size; i++) {
            if(shoppingCartsList.get(i).getStock()==0){
                if(isEditor){
                    shoppingCartsList.get(i).setIsSelected(states);
                }else{
                    shoppingCartsList.get(i).setIsSelected(0);//0:未选择 1：选中
                }
            }else{
                int cartNum = shoppingCartsList.get(i).getCartNum();
                int stockNum = shoppingCartsList.get(i).getStock();
                if(cartNum > stockNum){
                    shoppingCartsList.get(i).setCartNum(stockNum);
                }
                shoppingCartsList.get(i).setIsSelected(states);//0:未选择 1：选中
            }
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_basket, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int pos = holder.getLayoutPosition();
        holder.tvRemain.setText(String.valueOf(shoppingCartsList.get(pos).getStock()));
        if(shoppingCartsList.get(pos).getStock()==0 || shoppingCartsList.get(pos).getIssue() == 0){
            holder.layoutSaleOut.setVisibility(View.VISIBLE);
            if (onItemClickListener != null) {
                holder.layoutSaleOut.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        onItemClickListener.onItemLongClick(pos,
                                shoppingCartsList.get(pos).getId());
                        return false;
                    }
                });
            }
        }else{
            holder.layoutSaleOut.setVisibility(View.GONE);
        }
        holder.tvGoodsSize.setText(shoppingCartsList.get(pos).getCartSize());
        String s;
        if (shoppingCartsList.get(pos).getSpecialprice() != 0.00) {
            holder.imgSpecialOffer.setVisibility(View.VISIBLE);
            SpannableStringBuilder span = new SpannableStringBuilder("缩进 "+shoppingCartsList.get(pos).getName());
            span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.tvGoodsName.setText(span);
            s = ValueUtils.toTwoDecimal(shoppingCartsList.get(pos).getSpecialprice());
        } else {
            holder.imgSpecialOffer.setVisibility(View.GONE);
            holder.tvGoodsName.setText(shoppingCartsList.get(pos).getName());
            s = ValueUtils.toTwoDecimal(shoppingCartsList.get(pos).getYunmaprice());
        }
        SpannableStringBuilder ss = ValueUtils.changeTextSize(mContext,s,14,0,s.indexOf("."));
        holder.tvGoodsPrice.setText(ss);
        holder.tvGoodsNum.setText(String.valueOf(shoppingCartsList.get(pos).getCartNum()));
        if(shoppingCartsList.get(pos).getPic() != null){
            if (shoppingCartsList.get(pos).getRepoid() == 1) {
                GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        shoppingCartsList.get(pos).getPic().split(",")[0] + "/min");
            } else {
                GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        shoppingCartsList.get(pos).getPic().split(",")[0] + "/min");
            }
        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
        }
        if(shoppingCartsList.get(pos).getIsSelected()==1){
            holder.imgSelect.setImageDrawable(
                    ContextCompat.getDrawable(mContext,R.drawable.pitch_on));
        }else{
            holder.imgSelect.setImageDrawable(
                    ContextCompat.getDrawable(mContext,R.drawable.unchecked));
        }
        holder.layoutSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != selectGoodsClick){
                    selectGoodsClick.selectedGoods(holder.getLayoutPosition(),shoppingCartsList);
                }
            }
        });
        final int buyNums = shoppingCartsList.get(holder.getLayoutPosition()).getCartNum();
        final int remainNums = shoppingCartsList.get(holder.getLayoutPosition()).getStock();
        holder.layMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    if(shoppingCartsList.get(holder.getLayoutPosition()).getIsSelected()==1){
                        onClick.onMinusMore(holder.getLayoutPosition(),buyNums,remainNums,shoppingCartsList);
                    }
                }
            }
        });
        holder.layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    if(shoppingCartsList.get(holder.getLayoutPosition()).getIsSelected()==1){
                        onClick.onAddMore(holder.getLayoutPosition(),buyNums,remainNums,shoppingCartsList);
                    }
                }
            }
        });
        if (onItemClickListener != null) {
            holder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder.getLayoutPosition());
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return shoppingCartsList.size();
    }

    public void removeSaleOutGoods(int position){
        shoppingCartsList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void removeMore() {
        String ids[] = getSelectedId().split(",");
        for (String id : ids) {
            for (int j = 0; j < shoppingCartsList.size(); j++) {
                if (shoppingCartsList.get(j).getId() == Integer.valueOf(id)) {
                    shoppingCartsList.remove(j);
                    notifyItemRemoved(j);
                    break;
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getRemainDatas() {
        return shoppingCartsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgSelect) ImageView imgSelect;
        @BindView(R.id.layoutSelected) LinearLayout layoutSelected;
        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvGoodsSize) TextView tvGoodsSize;
        @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
        @BindView(R.id.tvGoodsNum) TextView tvGoodsNum;
        @BindView(R.id.layMinus) View layMinus;
        @BindView(R.id.layoutAdd) View layoutAdd;
        @BindView(R.id.tvRemain) TextView tvRemain;
        @BindView(R.id.layoutSaleOut) View layoutSaleOut;
        @BindView(R.id.imgSpecialOffer) ImageView imgSpecialOffer;
        private View rootView;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            rootView = view;
        }
    }

    public interface SelectGoods{
        void selectedGoods(int position,List<GetShoppingListBean.SuccessBean> shoppingCartsList);
    }

    public List<GetShoppingListBean.SuccessBean> getShoppingCartsList() {
        return shoppingCartsList;
    }

    public void addShoppingCartsList(List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        this.shoppingCartsList = shoppingCartsList;
        notifyItemInserted(shoppingCartsList.size());
    }

    public void setCartsList(int position,List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        this.shoppingCartsList = shoppingCartsList;
        notifyDataSetChanged();
    }

    public void selectAll()  {
        initSelectedPosition(1);
    }

    public void clearAll() {
        shoppingCartsList.clear();
        notifyDataSetChanged();
    }

    public void resetAll() {
        initSelectedPosition(0);
        notifyDataSetChanged();
    }

    public String getSelectedId() {
        String selectedId = "";
        int size = shoppingCartsList.size();
        for (int i = 0; i < size; i++) {
            if (shoppingCartsList.get(i).getIsSelected()==1) {
                selectedId = selectedId + String.valueOf("," + shoppingCartsList.get(i).getId());
            }
        }
        if (!selectedId.isEmpty())
            return selectedId.substring(1);
        else
            return selectedId;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }

    public List<GetShoppingListBean.SuccessBean> getPaylist(){
        List<GetShoppingListBean.SuccessBean> listBean = new ArrayList<>();
        int size = shoppingCartsList.size();
        for (int i = 0; i < size; i++) {
            if (shoppingCartsList.get(i).getIsSelected() == 1) {
                listBean.add(shoppingCartsList.get(i));
            }
        }
        return listBean;
    }

    public interface OnNumsSelectedClick{
        void onAddMore(int position,int buyNums,int remain,
                       List<GetShoppingListBean.SuccessBean> shoppingCartsList);
        void onMinusMore(int position,int buyNums,int remain,
                         List<GetShoppingListBean.SuccessBean> shoppingCartsList);
    }

    public interface OnItemClickListener {
        void onItemClick( int position);
        void onItemLongClick(int position,int id);
    }
}


