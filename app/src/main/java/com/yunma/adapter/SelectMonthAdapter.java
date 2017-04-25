package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.utils.ScreenUtils;

/**
 * Created by Json on 2017/1/17.
 */
public class SelectMonthAdapter extends RecyclerView.Adapter<SelectMonthAdapter.ViewHolder> {
    private Context mContext;
    private String[] mMonth;
    private String currentMonth;
    private LayoutInflater mInflater;
    public SelectMonthAdapter(Context mContext, String[] mMonth, String currentMonth) {
        this.mContext = mContext;
        this.mMonth = mMonth;
        this.mInflater = LayoutInflater.from(mContext);
        this.currentMonth = currentMonth;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_selected_month, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        int width = ScreenUtils.getScreenWidth(mContext)/4;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        holder.layout.setLayoutParams(params);
        holder.tvMonth.setText(mMonth[position]);
        if(mMonth[position].equals(currentMonth)){
            holder.tvMonth.setTextSize(14);
            holder.tvMonth.getPaint().setFakeBoldText(true);
            holder.imgDot.setVisibility(View.VISIBLE);
            holder.layoutMonth.setBackgroundDrawable(mContext.getResources()
                    .getDrawable(R.drawable.location_month));
        }else{
            holder.tvMonth.getPaint().setFakeBoldText(false);
            holder.imgDot.setVisibility(View.INVISIBLE);
            holder.layoutMonth.setBackgroundDrawable(null);
        }

    }

    @Override
    public int getItemCount() {
        return mMonth.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvMonth;
        ImageView imgDot;
        View layout,layoutMonth;
        public ViewHolder(View itemView) {
            super(itemView);
            tvMonth = (TextView)itemView.findViewById(R.id.tvMonth);
            imgDot = (ImageView)itemView.findViewById(R.id.imgDot);
            layout = itemView.findViewById(R.id.layout);
            layoutMonth = itemView.findViewById(R.id.layoutMonth);
        }
    }
}
