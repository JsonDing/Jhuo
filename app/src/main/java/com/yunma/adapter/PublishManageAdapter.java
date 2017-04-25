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
 * Created on 2017-04-06
 *
 * @author Json.
 */

public class PublishManageAdapter extends RecyclerView.Adapter<PublishManageAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private ArrayList<String> sizeList;
    private List<RemoveBean> removeBeanList;
    private SizeAdapter mAdapter;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen;

    public PublishManageAdapter(OnItemClick onItemClick, Context context) {
        this.onItemClick = onItemClick;
        this.mContext = context;
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
        holder.tvGoodsPrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(
                goodsListBeen.get(position).getYmprice())));
        if(goodsListBeen.get(position).isShowBox()){
            holder.checkbox.setVisibility(View.VISIBLE);
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
            mAdapter = new SizeAdapter(mContext);
            holder.tagSize.setAdapter(mAdapter);
            if(sizeList.size()>5){
                mAdapter.onlyAddAll(sizeList.subList(0,5));
            }else{
                mAdapter.onlyAddAll(sizeList);
            }
            holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        goodsListBeen.get(position).setSelected(true);
                    }
                }
            });
        }
        holder.layoutSeeDetials.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(onItemClick!=null){
                    onItemClick.onItemLongClick(holder.layoutSeeDetials,position,goodsListBeen.get(position));
                }
                return false;
            }
        });
        holder.layoutSeeDetials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClick!=null&&!goodsListBeen.get(position).isShowBox()){
                    onItemClick.onItemClick(holder.layoutSeeDetials,position,goodsListBeen.get(position));
                }else{
                    isSelect(false);
                    isShowBox(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return goodsListBeen.size();
    }

    public interface OnItemClick {
        void onItemClick(View view, int position, GetSelfGoodsResultBean.SuccessBean.ListBean successBean);
        void onItemLongClick(View view, int position, GetSelfGoodsResultBean.SuccessBean.ListBean successBean);
    }


    public void setGoodsListBeen(List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        //notifyItemInserted(goodsListBeen.size());
        notifyDataSetChanged();
    }

    public void remove() {
        for(int i = 0;i<removeBeanList.size();i++){
            goodsListBeen.remove(removeBeanList.get(i).getPosition());
            notifyItemRemoved(removeBeanList.get(i).getPosition());
        }
    }

    public String getDeleteIds(){
        String ids = "";
        for(int i=0;i<goodsListBeen.size();i++){
            if(goodsListBeen.get(i).isSelected()){
                ids = ids + goodsListBeen.get(i).getId() +",";
            }
        }
        if(EmptyUtil.isNotEmpty(ids)){
            return ids.substring(0,ids.length()-1);
        }else{
            return null;
        }
    }

    public void isShowBox(boolean b){
        for(int i=0;i<goodsListBeen.size();i++){
            goodsListBeen.get(i).setShowBox(b);
        }
        notifyDataSetChanged();
    }

    public void isSelect(boolean b){
        for(int i=0;i<goodsListBeen.size();i++){
            goodsListBeen.get(i).setSelected(b);
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

    private class RemoveBean{
        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }
    }
}
