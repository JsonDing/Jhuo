package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.VolumeResultBean;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.ValueUtils;

import java.text.SimpleDateFormat;
import java.util.*;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-11
 *
 * @author Json.
 */

public class VolumeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder > {
    private static final int ITEM_TYPE_UNUSE= 0;
    private static final int ITEM_TYPE_TIMEOUT = 1;
    private Context mContext;
    private LayoutInflater inflater;
    private OnItemClick onItemClick;
    List<VolumeResultBean.SuccessBean> listBean = null;
    public VolumeListAdapter(Context context, OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
        this.mContext = context;
        this.inflater = LayoutInflater.from(mContext);
        this.listBean = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType ==ITEM_TYPE_UNUSE) {
            return new ViewHolderUnUse(inflater.inflate(
                    R.layout.item_volume_unuse_list, parent, false));
        } else if (viewType == ITEM_TYPE_TIMEOUT) {
            return new ViewHolderTimeOut(inflater.inflate(
                    R.layout.item_volume_timeout_list, parent, false));
        }
        return null;
    }

    //（1包邮 2限件包邮 3不限件包邮 4限消费商品 5不限消费商品）
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof ViewHolderUnUse) {
            ((ViewHolderUnUse)holder).tvMoney.setText(
                    ValueUtils.toTwoDecimal(listBean.get(position).getMoney()));
            ((ViewHolderUnUse)holder).tvVolumeUseRange.setText(
                    String.valueOf(listBean.get(position).getAstrict()));
            ((ViewHolderUnUse)holder).tvVolumeName.setText(listBean.get(position).getName());
            if(listBean.get(position).getType()==1){
                ((ViewHolderUnUse)holder).tvCondition.setText("包邮");
            }else if(listBean.get(position).getType()==2){
                ((ViewHolderUnUse)holder).tvCondition.setText("限件包邮");
            }else if(listBean.get(position).getType()==3){
                ((ViewHolderUnUse)holder).tvCondition.setText("不限件包邮");
            }else if(listBean.get(position).getType()==4){
                ((ViewHolderUnUse)holder).tvCondition.setText("限消费商品");
            }else if(listBean.get(position).getType()==5){
                ((ViewHolderUnUse)holder).tvCondition.setText("不限消费商品");
            }
            ((ViewHolderUnUse)holder).tvVolumeUseTime.setText(
                    DateTimeUtils.getTime(listBean.get(position).getDate(),
                            new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA)) + "-" +
                            DateTimeUtils.getTime(listBean.get(position).getDate() +
                                            listBean.get(position).getDay()*24*60*60*1000,
                            new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA)));
            ((ViewHolderUnUse)holder).tvUseIntro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClick){//24*60*60*1000
                        onItemClick.showVolumeIntro();
                    }
                }
            });
        }else if(holder instanceof ViewHolderTimeOut){
            ((ViewHolderTimeOut)holder).tvMoney.setText(
                    ValueUtils.toTwoDecimal(listBean.get(position).getMoney()));
            ((ViewHolderTimeOut)holder).tvVolumeUseRange.setText(
                    String.valueOf(listBean.get(position).getAstrict()));
            ((ViewHolderTimeOut)holder).tvVolumeName.setText(listBean.get(position).getName());
            if(listBean.get(position).getType()==1){
                ((ViewHolderTimeOut)holder).tvCondition.setText("包邮");
            }else if(listBean.get(position).getType()==2){
                ((ViewHolderTimeOut)holder).tvCondition.setText("限件包邮");
            }else if(listBean.get(position).getType()==3){
                ((ViewHolderTimeOut)holder).tvCondition.setText("不限件包邮");
            }else if(listBean.get(position).getType()==4){
                ((ViewHolderTimeOut)holder).tvCondition.setText("限消费商品");
            }else if(listBean.get(position).getType()==5){
                ((ViewHolderTimeOut)holder).tvCondition.setText("不限消费商品");
            }
            ((ViewHolderTimeOut)holder).tvVolumeUseTime.setText(
                    DateTimeUtils.getTime(listBean.get(position).getDate(),
                            new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA)) + "-" +
                            DateTimeUtils.getTime(listBean.get(position).getDate() +
                                            listBean.get(position).getDay()*24*60*60*1000,
                                    new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.CHINA)));
            ((ViewHolderTimeOut)holder).tvUseIntro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(null != onItemClick){
                        onItemClick.showVolumeIntro();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        if (listBean.get(position).getStatus()==0) {
            //未使用
            return ITEM_TYPE_UNUSE;
        } else if (listBean.get(position).getStatus()==1) {
            //已过期
            return ITEM_TYPE_TIMEOUT;
        }
        return -1;
    }

    public interface OnItemClick {
        void onItemClick();
        void showVolumeIntro();
    }

    public void setUnUseListBean(List<VolumeResultBean.SuccessBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }

    class ViewHolderUnUse extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMoney) TextView tvMoney;
        @BindView(R.id.tvCondition) TextView tvCondition;
        @BindView(R.id.tvVolumeName) TextView tvVolumeName;
        @BindView(R.id.tvVolumeUseRange) TextView tvVolumeUseRange;
        @BindView(R.id.tvVolumeUseTime) TextView tvVolumeUseTime;
        @BindView(R.id.imgsVolumeTags) ImageView imgsVolumeTags;
        @BindView(R.id.cbButton) CheckBox cbButton;
        @BindView(R.id.tvUseIntro) TextView tvUseIntro;
        ViewHolderUnUse(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolderTimeOut extends RecyclerView.ViewHolder {
        @BindView(R.id.tvMoney) TextView tvMoney;
        @BindView(R.id.tvCondition) TextView tvCondition;
        @BindView(R.id.tvVolumeName) TextView tvVolumeName;
        @BindView(R.id.tvVolumeUseRange) TextView tvVolumeUseRange;
        @BindView(R.id.tvVolumeUseTime) TextView tvVolumeUseTime;
        @BindView(R.id.imgsVolumeTags) ImageView imgsVolumeTags;
        @BindView(R.id.cbButton) CheckBox cbButton;
        @BindView(R.id.tvUseIntro) TextView tvUseIntro;
        ViewHolderTimeOut(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
