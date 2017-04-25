package com.yunma.publish;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.StocksBeanList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-04-14
 *
 * @author Json.
 */

public class SizeNumsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM_TYPE_CONTENT = 0;
    private static final int ITEM_TYPE_BOTTOM = 1;
    private int mBottomCount=1;//底部View个数
    private Context mContext;
    private LayoutInflater inflater;
    private OnClickListener onClick;
    private List<StocksBeanList> stocksBeenList;

    public SizeNumsAdapter(Context context,OnClickListener onClick) {
        this.mContext = context;
        this.onClick = onClick;
        this.stocksBeenList =new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    //内容长度
    private int getBodyItemCount(){
        if(stocksBeenList==null){
            return 0;
        }else{
            return stocksBeenList.size();
        }
    }

    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getBodyItemCount();
        if (mBottomCount != 0 && position >=  dataItemCount){
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
            return  new BodyViewHolder(inflater.inflate(R.layout.item_goods_nums_size, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(inflater.inflate(R.layout.item_add_new_goods, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof BodyViewHolder) {
            ((BodyViewHolder) holder).tvGoodsSize.setText(stocksBeenList.get(position).getSize());
            ((BodyViewHolder) holder).tvGoodsNums.setText(String.valueOf(
                    stocksBeenList.get(position).getNum()));
            final int currNums = Integer.valueOf(((BodyViewHolder) holder).
                    tvGoodsNums.getText().toString().trim());
            ((BodyViewHolder) holder).layoutRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClick!=null){
                        onClick.onRemoveSize(position);
                    }
                }
            });
            ((BodyViewHolder) holder).layoutAddMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClick!=null){
                        onClick.onAddNumber(position,currNums);
                    }
                }
            });
            ((BodyViewHolder) holder).layoutMinusMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClick!=null){
                        onClick.onMinusNumbers(position,currNums);
                    }
                }
            });
        }else if (holder instanceof BottomViewHolder) {

            ((BottomViewHolder)holder).layoutAddNewSize.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onClick!=null){
                        onClick.onAddNewSize();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return getBodyItemCount() + mBottomCount;
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder{
        private View layoutRemove,layoutMinusMore,layoutAddMore;
        private TextView tvGoodsSize,tvGoodsNums;
        public BodyViewHolder(View itemView) {
            super(itemView);
            layoutAddMore = itemView.findViewById(R.id.layoutAddMore);
            layoutMinusMore = itemView.findViewById(R.id.layoutMinusMore);
            layoutRemove = itemView.findViewById(R.id.layoutRemove);
            tvGoodsSize = (TextView)itemView.findViewById(R.id.tvGoodsSize);
            tvGoodsNums = (TextView)itemView.findViewById(R.id.tvGoodsNums);
        }
    }

    public class BottomViewHolder extends RecyclerView.ViewHolder{
        private View layoutAddNewSize;
        public BottomViewHolder(View itemView) {
            super(itemView);
            layoutAddNewSize = itemView.findViewById(R.id.layoutAddNewSize);
        }
    }


    public interface OnClickListener{
        void onAddNewSize();
        void onRemoveSize(int position);
        void onAddNumber(int position,int currNums);
        void onMinusNumbers(int position,int currNums);
    }

    public void setStocksBeanList(List<StocksBeanList> stocksBeenList) {
        this.stocksBeenList = stocksBeenList;
    }
}
