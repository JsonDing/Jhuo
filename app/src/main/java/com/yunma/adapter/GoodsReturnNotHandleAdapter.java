package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.ServiceResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.m.GoodsRefundInterface.OnUnHandleClick;
import com.yunma.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class GoodsReturnNotHandleAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private OnUnHandleClick onAdapterClick;
    private GoodsRuturnAdapter mAdapter;
    private List<ListBean> listBean;
    private Animation slide_bottom_to_top;
    public GoodsReturnNotHandleAdapter(Context mContext, OnUnHandleClick onAdapterClick,
                                       List<ListBean> listBean) {
        this.mContext = mContext;
        this.listBean = listBean;
        this.onAdapterClick = onAdapterClick;
        inflater = LayoutInflater.from(mContext);
        slide_bottom_to_top= AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_to_top);
    }

    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_goods_reback_not_handle, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        slide_bottom_to_top.setDuration(200);
        view.setAnimation(slide_bottom_to_top);
        holder.tvOrderCode.setText(listBean.get(position).getOid());
        holder.tvTime.setText(DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
        holder.tvRealPay.setText("￥" + listBean.get(position).getOrder().getTotalcost());
        holder.tvReturnPrice.setText("￥" + listBean.get(position).getMoneyout());

        mAdapter = new GoodsRuturnAdapter(mContext,listBean.get(position).getServiceDetails());
        holder.lvGoodsList.setAdapter(mAdapter);

        holder.btnRebackDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onAdapterClick){
                    onAdapterClick.onLookDetial(listBean.get(position));
                }
            }
        });
        holder.btnRebackCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onAdapterClick){
                    onAdapterClick.onCancleRefund(position,listBean.get(position).getId());
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView tvOrderCode;
        TextView tvTime;
        TextView tvRealPay;
        TextView tvTotalNums;
        TextView tvReturnPrice;
        Button btnRebackDetial;
        Button btnRebackCancle;
        View layoutShow;
        TextView tvShow;
        ListView lvGoodsList;

        public ViewHolder(View itemView) {
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
            tvTotalNums = (TextView) itemView.findViewById(R.id.tvTotalNums);
            tvReturnPrice = (TextView) itemView.findViewById(R.id.tvReturnPrice);
            btnRebackDetial = (Button) itemView.findViewById(R.id.btnRebackDetial);
            btnRebackCancle = (Button) itemView.findViewById(R.id.btnRebackCancle);
            tvOrderCode = (TextView) itemView.findViewById(R.id.tvOrderCode);
            tvRealPay = (TextView) itemView.findViewById(R.id.tvRealPay);
            layoutShow = itemView.findViewById(R.id.layoutShow);
            tvShow = (TextView)itemView.findViewById(R.id.tvShow);
            lvGoodsList = (ListView)itemView.findViewById(R.id.lvGoodsList);
        }
    }


    public void delRefund(int position){
        listBean.remove(position);
        notifyDataSetChanged();
    }

    public List<ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<ListBean> listBean) {
        this.listBean = listBean;
    }
}
