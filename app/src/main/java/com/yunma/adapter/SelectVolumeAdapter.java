package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.VolumeListBean;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-21
 *
 * @author Json.
 */

public class SelectVolumeAdapter extends RecyclerView.Adapter<SelectVolumeAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    private List<VolumeListBean> listBean = null;
    private final static long VALUE = 24 * 60 * 60 * 1000;

    public SelectVolumeAdapter(Context context, OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        this.inflater = LayoutInflater.from(context);
        this.listBean = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(
                R.layout.item_volume_selete, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvMoney.setText(
                ValueUtils.toTwoDecimal(listBean.get(position).getMoney()));
        holder.tvVolumeUseRange.setText(
                String.valueOf(listBean.get(position).getAstrict()));
        holder.tvVolumeName.setText(listBean.get(position).getName());

        if(listBean.get(position).getType()==1){
            holder.tvCondition.setText("包邮");
            holder.tvPriceTag.setVisibility(View.GONE);
            holder.tvMoney.setText("包邮");
            holder.tvCondition.setText("邮费抵扣券");
            holder.tvVolumeUseRange.setVisibility(View.INVISIBLE);
        }else if(listBean.get(position).getType()==2){
            holder.tvCondition.setText(listBean.get(position).getAstrict() + "件内可用");
            holder.tvVolumeUseRange.setText("邮费抵扣券");
        }else if(listBean.get(position).getType()==3){
            holder.tvCondition.setText("不限件包邮");
            holder.tvVolumeUseRange.setText("邮费抵扣券");
        }else if(listBean.get(position).getType()==4){
            holder.tvCondition.setText(
                    "满" + listBean.get(position).getAstrict() + "可用");
            holder.tvVolumeUseRange.setText("满减券");
        }else if(listBean.get(position).getType()==5){
            holder.tvCondition.setText("不限消费金额");
            holder.tvVolumeUseRange.setText("满减券");
        }
        long l1 = listBean.get(position).getDate();
        long l2 = listBean.get(position).getDay() * VALUE;
        long endTime = l1 + l2;
        String endDate = DateTimeUtils.getTime(endTime, DateTimeUtils.DATE_FORMAT_DATE);
        String startDate = DateTimeUtils.getTime(listBean.get(position).getDate(),
                DateTimeUtils.DATE_FORMAT_DATE);
        holder.tvVolumeUseTime.setText(
                String.valueOf(startDate + " - " + endDate));
        long curTime = System.currentTimeMillis();
        if (endTime-curTime<2*VALUE) {
            holder.imgsVolumeTags.setVisibility(View.VISIBLE);
        } else {
            holder.imgsVolumeTags.setVisibility(View.INVISIBLE);
        }
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onItemClick){
                    onItemClick.onItemClick(ValueUtils.toTwoDecimal(
                            listBean.get(holder.getAdapterPosition()).getMoney()),
                            String.valueOf(listBean.get(holder.getAdapterPosition()).getId()),
                            listBean.get(holder.getAdapterPosition()).getType());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }


    public interface OnItemClick {
        void onItemClick(String money, String couponUserid,int type);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMoney) TextView tvMoney;
        @BindView(R.id.tvCondition) TextView tvCondition;
        @BindView(R.id.tvVolumeName) TextView tvVolumeName;
        @BindView(R.id.tvVolumeUseRange) TextView tvVolumeUseRange;
        @BindView(R.id.tvVolumeUseTime) TextView tvVolumeUseTime;
        @BindView(R.id.imgsVolumeTags) ImageView imgsVolumeTags;
        @BindView(R.id.tvPriceTag) TextView tvPriceTag;
        View root;
        ViewHolder(View view) {
            super(view);
            root = view;
            ButterKnife.bind(this, view);
        }
    }

    public void setUnUseListBean(List<VolumeListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }
}
