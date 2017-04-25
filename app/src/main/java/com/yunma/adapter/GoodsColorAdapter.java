package com.yunma.adapter;

import android.content.*;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.GoodsInfoBean;
import com.yunma.utils.*;

import java.util.*;

/**
 * Created by Json on 2017/1/8.
 */
public class GoodsColorAdapter extends RecyclerView.Adapter<GoodsColorAdapter.ViewHolder>{
    private Context mContext;
    private OnColorClick onClick;
    private GoodsInfoBean color;
    private LayoutInflater mInflater;
    private List<HashMap<String,Integer>> status;

    public GoodsColorAdapter(Context mContext, GoodsInfoBean color, OnColorClick onClick) {
        this.color = color;
        this.mContext = mContext;
        this.onClick = onClick;
        mInflater = LayoutInflater.from(mContext);
        initStatus();
    }

    private void initStatus() {
        status = new ArrayList<>();
        for (int i=0;i<color.getData().size();i++){
            HashMap<String,Integer> sia = new HashMap<>();
            sia.put("status",0);
            status.add(sia);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_goods_color, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,80))/3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                width,DensityUtils.dp2px(mContext,46));
        holder.tvColor.setLayoutParams(params);
        holder.tvColor.setText(color.getData().get(position).getColor());
        if(status.get(position).get("status") == 0){
            holder.tvColor.setTextColor(mContext.getResources().getColor(R.color.color_b1));
            holder.tvColor.setTextSize(13);
        }else if(status.get(position).get("status") == 1){
            holder.tvColor.setTextColor(mContext.getResources().getColor(R.color.color_b3));
            holder.tvColor.setTextSize(17);
        }

        holder.tvColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onClick ){
                    onClick.onSelectedColor(holder.getAdapterPosition(),holder.tvColor);
                }
            }
        });

    }



    @Override
    public int getItemCount() {
        return color.getData().size();
    }

    public void clearPreStatus(int pos){
        status.clear();
        status = new ArrayList<>();
        for (int i=0;i<color.getData().size();i++){
            HashMap<String,Integer> sia = new HashMap<>();
            if(i==pos){
                sia.put("status",1);
            }else{
                sia.put("status",0);
            }
            status.add(sia);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvColor;
        public ViewHolder(View itemView) {
            super(itemView);
            tvColor = (TextView)itemView.findViewById(R.id.tvColor);
        }
    }

    public interface OnColorClick{
        void onSelectedColor(int pos,TextView textView);
    }

}
