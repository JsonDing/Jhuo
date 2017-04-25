package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.VolumeResultBean;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.ValueUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-21
 *
 * @author Json.
 */

public class SelectVolumeAdapter extends RecyclerView.Adapter<SelectVolumeAdapter.ViewHolder> {
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    List<VolumeResultBean.SuccessBean> listBean = null;
    public SelectVolumeAdapter(Context context, OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.listBean = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(
                R.layout.item_volume_selete, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvMoney.setText(
                ValueUtils.toTwoDecimal(listBean.get(position).getMoney()));
        holder.tvVolumeUseRange.setText(
                String.valueOf(listBean.get(position).getAstrict()));
        holder.tvVolumeName.setText(listBean.get(position).getName());
        if(listBean.get(position).getType()==1){
            holder.tvCondition.setText("包邮");
        }else if(listBean.get(position).getType()==2){
            holder.tvCondition.setText("限件包邮");
        }else if(listBean.get(position).getType()==3){
            holder.tvCondition.setText("不限件包邮");
        }else if(listBean.get(position).getType()==4){
            holder.tvCondition.setText("限消费商品");
        }else if(listBean.get(position).getType()==5){
            holder.tvCondition.setText("不限消费商品");
        }
        holder.tvVolumeUseTime.setText(
                DateTimeUtils.getTime(listBean.get(position).getDate(),
                        new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA)) + "-" +
                        DateTimeUtils.getTime(listBean.get(position).getDate() +
                                        listBean.get(position).getDay()*24*60*60*1000,
                                new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA)));
        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onItemClick){
                    onItemClick.onItemClick(ValueUtils.toTwoDecimal(
                            listBean.get(position).getMoney()),listBean.get(position).getName());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }


    public interface OnItemClick {
        void onItemClick(String money, String name);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMoney) TextView tvMoney;
        @BindView(R.id.tvCondition) TextView tvCondition;
        @BindView(R.id.tvVolumeName) TextView tvVolumeName;
        @BindView(R.id.tvVolumeUseRange) TextView tvVolumeUseRange;
        @BindView(R.id.tvVolumeUseTime) TextView tvVolumeUseTime;
        @BindView(R.id.imgsVolumeTags) ImageView imgsVolumeTags;
        View root;
        ViewHolder(View view) {
            super(view);
            root = view;
            ButterKnife.bind(this, view);
        }
    }

    public void setUnUseListBean(List<VolumeResultBean.SuccessBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }
}
