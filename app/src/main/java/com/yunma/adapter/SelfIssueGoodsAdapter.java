package com.yunma.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.TomorrowUpDataResultBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.LogUtils;
import com.yunma.utils.ScreenUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-17
 *
 * @author Json.
 */

public class SelfIssueGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //item类型
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int mHeaderCount = 1;//头部View个数
    private LayoutInflater inflater;
    private Activity mActivity;
    private OnClickListener onClickListener;
    private List<TomorrowUpDataResultBean.SuccessBean.YunmaBean> listBean = null;
    public SelfIssueGoodsAdapter(Activity mActivity, OnClickListener onClickListener) {
        this.mActivity = mActivity;
        this.onClickListener = onClickListener;
        this.listBean = new ArrayList<>();
        inflater = LayoutInflater.from(mActivity);
    }

    //判断当前item是否是HeadView
    public boolean isHeaderView(int position) {
        return position < mHeaderCount;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(inflater.inflate(
                    R.layout.tommorrow_issue_item, parent, false));
        }else if(viewType == ITEM_TYPE_CONTENT){
            return new BodyViewHolder(inflater.inflate(
                    R.layout.item_issue_goods, parent, false));
        }
        return null;
    }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        if (position < mHeaderCount) {
            //头部View
            return ITEM_TYPE_HEADER;
        }else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BodyViewHolder) {
            String url;
            if(listBean.get(position).getRepoid()==1){
                url = ConUtils.SElF_GOODS_IMAGE_URL;
            }else{
                url = ConUtils.GOODS_IMAGE_URL;
            }
            GlideUtils.glidNormleFast(mActivity, ((BodyViewHolder) holder).imgsGoods, url +
                    listBean.get(position - 1).getPic().split(",")[0]);
            ((BodyViewHolder) holder).tvGoodsName.setText(listBean.get(position - 1).getName());
            ((BodyViewHolder) holder).tvTime.setText(String.valueOf(DateTimeUtils.getTime(
                    listBean.get(position - 1).getDate() + 24 * 60 * 60 * 1000,
                    new SimpleDateFormat("MM月dd日", Locale.CHINA)) + "上新"));
            if(listBean.get(position-1).getNewRemindId()!=null){
                ((BodyViewHolder) holder).btnRemindMe.setEnabled(false);
                ((BodyViewHolder) holder).btnRemindMe.setText("已设置上架提醒");
            }else{
                ((BodyViewHolder) holder).btnRemindMe.setEnabled(true);
                ((BodyViewHolder) holder).btnRemindMe.setText("上架提醒我");
            }

            ((BodyViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.lookDetials(holder.getAdapterPosition() - 1,
                                listBean.get(holder.getAdapterPosition() - 1));
                    }
                }
            });
            ((BodyViewHolder) holder).btnRemindMe.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != onClickListener) {
                        onClickListener.remindMeClick(String.valueOf(
                                listBean.get(holder.getAdapterPosition() - 1).getNumber()));
                    }
                }
            });
        } else {
            LogUtils.json("---------------> " + position);
        }
    }

    @Override
    public int getItemCount() {
        return listBean.size() + mHeaderCount;
    }

    public void setListBean(List<TomorrowUpDataResultBean.SuccessBean.YunmaBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }

    public interface OnClickListener{
        void lookDetials(int position,TomorrowUpDataResultBean.SuccessBean.YunmaBean detialsBean);
        void remindMeClick(String goodsNums);
    }

    public class BodyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgsGoods) ImageView imgsGoods;
        @BindView(R.id.btnRemindMe) TextView btnRemindMe;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvTime) TextView tvTime;
        public BodyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int width = (ScreenUtils.getScreenWidth(mActivity)- DensityUtils.dp2px(mActivity,5))/2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,width);
            imgsGoods.setLayoutParams(params);
        }
    }

    //头部 ViewHolder
    private class HeaderViewHolder extends RecyclerView.ViewHolder {

        HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }
}
