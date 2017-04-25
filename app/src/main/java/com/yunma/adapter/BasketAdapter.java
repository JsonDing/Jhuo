package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.utils.*;

import java.util.*;

import butterknife.*;

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
        for (int i = 0; i < shoppingCartsList.size(); i++) {
            if(shoppingCartsList.get(i).getStock()==0){
                if(isEditor){
                    shoppingCartsList.get(i).setIsSelected(states);
                }else{
                    shoppingCartsList.get(i).setIsSelected(0);//0:未选择 1：选中
                }
            }else{
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
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvRemain.setText("库存:" + shoppingCartsList.get(position).getStock());
        if(shoppingCartsList.get(position).getStock()==0){
            holder.layoutSaleOut.setVisibility(View.VISIBLE);
        }else{
            holder.layoutSaleOut.setVisibility(View.GONE);
        }
        holder.tvGoodsName.setText(shoppingCartsList.get(position).getName());
        holder.tvGoodsType.setText("颜色：如图 "+" 尺码：" + shoppingCartsList.get(position).getCartSize());
        holder.tvGoodsPrice.setText("￥" + shoppingCartsList.get(position).getYunmaprice());
        holder.tvGoodsNum.setText(shoppingCartsList.get(position).getCartNum() + "");
        if (shoppingCartsList.get(position).getRepoid() == 1) {
            GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                    shoppingCartsList.get(position).getPic().split(",")[0]);
        } else {
            GlideUtils.glidNormleFast(mContext, holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                    shoppingCartsList.get(position).getNumber() + ".jpg");
        }

        if(shoppingCartsList.get(position).getIsSelected()==1){
            holder.imgSelect.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.pitch_on));
        }else{
            holder.imgSelect.setImageDrawable(
                    mContext.getResources().getDrawable(R.drawable.unchecked));
        }

        holder.layoutSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != selectGoodsClick){
                    selectGoodsClick.selectedGoods(position,shoppingCartsList);
                }
            }
        });
        final int buyNums = Integer.valueOf(holder.tvGoodsNum.getText().toString());
        final int remainNums = shoppingCartsList.get(position).getStock();
        holder.layMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    if(shoppingCartsList.get(position).getIsSelected()==1){
                        onClick.onMinusMore(position,buyNums,remainNums,shoppingCartsList);
                    }
                }
            }
        });
        holder.layoutAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onClick){
                    if(shoppingCartsList.get(position).getIsSelected()==1){
                        onClick.onAddMore(position,buyNums,remainNums,shoppingCartsList);
                    }
                }
            }
        });
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return shoppingCartsList.size();
    }

    public void remove() {
        String ids[] = getSelectedId().split(",");
        for(int i=0;i<ids.length;i++){
            for(int j=0;j<shoppingCartsList.size();j++){
                 if(shoppingCartsList.get(j).getId()==Integer.valueOf(ids[i])){
                     shoppingCartsList.remove(j);
                     notifyItemRemoved(j);
                     break;
                 }
            }
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgSelect) ImageView imgSelect;
        @BindView(R.id.layoutSelected) LinearLayout layoutSelected;
        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvGoodsType) TextView tvGoodsType;
        @BindView(R.id.tvGoodsPrice) TextView tvGoodsPrice;
        @BindView(R.id.tvGoodsNum) TextView tvGoodsNum;
        @BindView(R.id.layMinus) View layMinus;
        @BindView(R.id.layoutAdd) View layoutAdd;
        @BindView(R.id.tvRemain) TextView tvRemain;
        @BindView(R.id.layoutSaleOut) View layoutSaleOut;
        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public interface SelectGoods{
        void selectedGoods(int position,List<GetShoppingListBean.SuccessBean> shoppingCartsList);
    }

    public List<GetShoppingListBean.SuccessBean> getShoppingCartsList() {
        return shoppingCartsList;
    }

    public void setShoppingCartsList(List<GetShoppingListBean.SuccessBean> shoppingCartsList) {
        this.shoppingCartsList.clear();
        this.shoppingCartsList = shoppingCartsList;
        notifyItemInserted(this.shoppingCartsList.size());
    }

    public void selectAll() {
        initSelectedPosition(1);
    }

    public void clearAll() {
        initSelectedPosition(0);
    }

    public void selectOpposite() {//反选
        for (int i = 0; i < shoppingCartsList.size(); i++) {
            if (shoppingCartsList.get(i).getIsSelected()==0) {
                shoppingCartsList.get(i).setIsSelected(1);
            } else {
                shoppingCartsList.get(i).setIsSelected(0);
            }
        }
    }

    public String getSelectedId() {
        String selectedId = "";
        for (int i = 0; i < shoppingCartsList.size(); i++) {
            if (shoppingCartsList.get(i).getIsSelected()==1) {
                selectedId = selectedId + String.valueOf("," + shoppingCartsList.get(i).getId());
            }
        }
        if (!selectedId.isEmpty())
            return selectedId.substring(1);
        else
            return selectedId;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean editor) {
        isEditor = editor;
    }

    public List<GetShoppingListBean.SuccessBean> getPaylist(){
            List<GetShoppingListBean.SuccessBean> listBean = new ArrayList<>();
            for (int i = 0; i < shoppingCartsList.size(); i++) {
                if (shoppingCartsList.get(i).getIsSelected()==1) {
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
        void onItemClick(View view, int position);
    }
}


