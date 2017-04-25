package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.bean.StocksBean;
import com.yunma.utils.*;
import com.yunma.widget.FlowTagLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Json on 2017/4/9.
 */

public class RepertoryManageAdapter extends RecyclerView.Adapter<RepertoryManageAdapter.ViewHolder>{
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private ArrayList<String> sizeList;
    private RecyclerView mRecyclerView;
    private int mSelectedPos = 0;//实现单选  变量保存当前选中的position
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen;
    public RepertoryManageAdapter(Context mContext,OnItemClick onItemClick,RecyclerView mRecyclerView) {
        this.mContext = mContext;
        this.onItemClick = onItemClick;
        this.mRecyclerView = mRecyclerView;
        this.goodsListBeen = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_publish_manage, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        GlideUtils.glidNormle(mContext, holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL
                + goodsListBeen.get(position).getPic().split(",")[0]);
        holder.tvGoodsName.setText(goodsListBeen.get(position).getName());
        holder.tvGoodsPrice.setText("￥" + ValueUtils.toTwoDecimal(
                goodsListBeen.get(position).getYmprice()));
        if(goodsListBeen.get(position).isShowBox()){
            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(goodsListBeen.get(position).isSelected());
            holder.checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //每次点击选择时，把看见的，看不见的的项目设置为非选择状态
                    ViewHolder mHolder = (ViewHolder) mRecyclerView
                            .findViewHolderForLayoutPosition(mSelectedPos);
                    if (mHolder != null) {
                        mHolder.checkbox.setChecked(false);
                    } else {
                        notifyItemChanged(mSelectedPos);
                    }
                    goodsListBeen.get(mSelectedPos).setSelected(false);//上次选中的条目，设置为false；
                    //更新默认选中的position；
                    mSelectedPos = position;
                    //最后设置要选中的那项；
                    goodsListBeen.get(mSelectedPos).setSelected(true);
                    holder.checkbox.setChecked(true);
                }
            });
        }else{
            holder.checkbox.setVisibility(View.GONE);
        }
        if(goodsListBeen.get(position).getStocks()!=null){
            List<StocksBean> stocksList =
                    goodsListBeen.get(position).getStocks();
            int lenth = goodsListBeen.get(position).getStocks().size();
            sizeList = new ArrayList<>();
            for(int i =0;i<lenth;i++){
                if(stocksList.get(i).getNum()!=0){
                    sizeList.add(stocksList.get(i).getSize());
                }
            }
            SizeAdapter mAdapter = new SizeAdapter(mContext);
            holder.tagSize.setAdapter(mAdapter);
            if(sizeList.size()>5){
                mAdapter.onlyAddAll(sizeList.subList(0,5));
            }else{
                mAdapter.onlyAddAll(sizeList);
            }
        }
        holder.layoutSeeDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null && !goodsListBeen.get(position).isShowBox()){
                    onItemClick.onItemClick(position,goodsListBeen.get(position));
                }else{
                    isShowBox(false);
                    onItemClick.onItemClick(position,null);
                }
            }
        });
        holder.layoutSeeDetials.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onItemLongClick(holder.layoutSeeDetials,position,goodsListBeen.get(position));
                }
                return false;
            }
        });

        holder.imgGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null){
                    onItemClick.OnReplaceImage(position,goodsListBeen.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return goodsListBeen.size();
    }

    public interface OnItemClick {
        void onItemClick(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean);
        void OnReplaceImage(int position, GetSelfGoodsResultBean.SuccessBean.ListBean listBean);
        void onItemLongClick(View view, int position, GetSelfGoodsResultBean.SuccessBean.ListBean successBean);
    }

    public void isShowBox(boolean b){
        for(int i=0;i<goodsListBeen.size();i++){
            goodsListBeen.get(i).setShowBox(b);
        }
        notifyDataSetChanged();
    }

    class ViewHolder  extends RecyclerView.ViewHolder {
        @BindView(R.id.imgGoods)
        ImageView imgGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.tagSize)
        FlowTagLayout tagSize;
        @BindView(R.id.checkbox)
        CheckBox checkbox;
        @BindView(R.id.layoutSeeDetials)
        RelativeLayout layoutSeeDetials;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void setGoodsListBeen(List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        isShowBox(this.goodsListBeen.get(0).isShowBox());
        notifyDataSetChanged();
    }

    public void remove() {
        goodsListBeen.remove(mSelectedPos);
        notifyItemRemoved(mSelectedPos);
        isShowBox(false);
    }

    public String getShelveIds(){
        String ids = "";
        for(int i=0;i<goodsListBeen.size();i++){
            if(goodsListBeen.get(i).isSelected()){
                mSelectedPos = i;
                ids = String.valueOf(goodsListBeen.get(i).getId()) ;
            }
        }
        return ids;
    }

    public int getSelectedPos(){
        return mSelectedPos;
    }

    private class SizeAdapter extends BaseAdapter {
        private List<String> mSize = new ArrayList<>();
        private LayoutInflater layoutInflater;

        SizeAdapter(Context mContext) {
            layoutInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mSize.size();
        }

        @Override
        public Object getItem(int position) {
            return mSize.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(R.layout.size_home_item, parent, false);
                viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_tag);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvName.setText(String.valueOf(mSize.get(position)));
            return convertView;
        }
        void onlyAddAll(List<String> list) {
            this.mSize = list;
            notifyDataSetChanged();
        }

        private class ViewHolder {
            private TextView tvName;
        }
    }

}
