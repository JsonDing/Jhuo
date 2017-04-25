package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.yunma.R;
import com.yunma.bean.YunmasBeanList;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.ScreenUtils;

import java.util.List;

/**
 * Created by Json on 2017/3/5.
 */
public class UploadListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private int mHeaderCount=0;//头部View个数
    private int mBottomCount=1;//底部View个数
    private Context context;
    private OnAddGoodClick onAddGoodClick;
    private LayoutInflater inflater;
    private List<YunmasBeanList> goodBeanList;
    public UploadListAdapter(Context context, List<YunmasBeanList> goodBeanList,
                             OnAddGoodClick onAddGoodClick) {
        this.context = context;
        this.onAddGoodClick = onAddGoodClick;
        this.goodBeanList = goodBeanList;
        inflater = LayoutInflater.from(context);
    }

    //内容长度
    private int getBodyItemCount(){
        return goodBeanList.size();
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return mHeaderCount != 0 && position < mHeaderCount;
    }

    //判断当前item是否是FooterView
    public boolean isBottomView(int position) {
        return mBottomCount != 0 && position >= (mHeaderCount + getBodyItemCount());
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getBodyItemCount();
       if (mBottomCount != 0 && position >=  + dataItemCount) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return  new BodyViewHolder(inflater.inflate(R.layout.item_goods, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(inflater.inflate(R.layout.item_add_goods, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BodyViewHolder) {
            ((BodyViewHolder) holder).tvGoodsName.setText(goodBeanList.get(position).getName());
            String goodsInfo = "";
            for(int i=0;i<goodBeanList.get(position).getStocks().size();i++){
                String goodsSize =  goodBeanList.get(position).getStocks().get(i).getSize();
                String goodsNums = goodBeanList.get(position).getStocks().get(i).getNum();
                goodsInfo = goodsInfo + ";" + goodsSize +" x " + goodsNums;
            }
            ((BodyViewHolder) holder).tvGoodsSize.setText("尺码：" +goodsInfo.substring(1,goodsInfo.length()));
            ((BodyViewHolder) holder).tvGoodsPrice.setText("售价：" + goodBeanList.get(position).getYmprice());
            ((BodyViewHolder) holder).tvOriginalPrice.setText("吊牌价：" + goodBeanList.get(position).getSaleprice());
            Glide.with(context)
                    .load(goodBeanList.get(position).getPic().split(",")[0])
                    .centerCrop()
                    .into(((BodyViewHolder) holder).imgGoods);

        }else if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder)holder).imgsAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onAddGoodClick!=null){
                        onAddGoodClick.toAddGoodInfos();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return  getBodyItemCount() + mBottomCount;
    }

    public interface OnAddGoodClick{
        void toAddGoodInfos();
    }

    private class BodyViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgGoods;
        private TextView tvGoodsName;
        private TextView tvGoodsSize;
        private TextView tvGoodsPrice;
        private TextView tvOriginalPrice;
        View view1,view2,view3,layout;

        BodyViewHolder(View itemView) {
            super(itemView);
            imgGoods = (ImageView) itemView.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) itemView.findViewById(R.id.tvGoodsName);
            tvGoodsSize = (TextView) itemView.findViewById(R.id.tvGoodsSize);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tvGoodsPrice);
            tvOriginalPrice = (TextView) itemView.findViewById(R.id.tvOriginalPrice);
            view1 = itemView.findViewById(R.id.view1);
            view2 = itemView.findViewById(R.id.view2);
            view3 = itemView.findViewById(R.id.view3);
            layout = itemView.findViewById(R.id.layout);
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,35))/2;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }

    //底部 ViewHolder
    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgsAdd;
        BottomViewHolder(View itemView) {
            super(itemView);
            imgsAdd = (ImageView) itemView.findViewById(R.id.imgsAdd);
        }
    }

    public List<YunmasBeanList> getGoodBeanList() {
        return goodBeanList;
    }

    public void setGoodBeanList(List<YunmasBeanList> goodBeanList) {
        this.goodBeanList = goodBeanList;
    }
}
