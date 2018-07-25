package com.yunma.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.ServiceResultBean;
import com.yunma.jhuo.m.GoodsRefundInterface.OnCompleteClick;
import com.yunma.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class GoodsReturnCompleteAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private OnCompleteClick onAdapterClick;
    private List<ServiceResultBean.SuccessBean.ListBean> listBean;
    public GoodsReturnCompleteAdapter(Context mContext, OnCompleteClick onAdapterClick,
                                      List<ServiceResultBean.SuccessBean.ListBean> listBean) {
        this.mContext = mContext;
        this.listBean = listBean;
        this.onAdapterClick = onAdapterClick;
        inflater = LayoutInflater.from(mContext);
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
            view = inflater.inflate(R.layout.item_goods_reback_complete, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvOrderCode.setText(listBean.get(position).getOid());
        holder.tvTime.setText(DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)));
        holder.tvRealPay.setText("￥" + listBean.get(position).getOrder().getTotalcost());
        holder.tvReturnPrice.setText("￥" + listBean.get(position).getMoneyout());
        GoodsRuturnAdapter mAdapter = new GoodsRuturnAdapter(mContext, listBean.get(position).getServiceDetails());
        holder.lvGoodsList.setAdapter(mAdapter);

        holder.btnRebackDetial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onAdapterClick){
                    onAdapterClick.onLookDetial(listBean.get(position));
                }
            }
        });

        holder.btnRebackStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onAdapterClick){
                    onAdapterClick.onRefundStatus(listBean.get(position));
                }
            }
        });
        holder.btnMoneyGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onAdapterClick){
                    onAdapterClick.onLookMoney(listBean.get(position));
                    holder.layoutShow.setVisibility(View.VISIBLE);
                    holder.tvShow.setText("已退回“您的支付宝账户”" + "\u3000\u3000" +
                            DateTimeUtils.getTime(listBean.get(position).getRefundDate(),
                                    DateTimeUtils.DEFAULT_DATE_FORMAT) + "\n" + "￥" +
                    listBean.get(position).getMoneyout());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            holder.layoutShow.setVisibility(View.GONE);
                        }
                    },2500);
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
        Button btnRebackStatus;
        Button btnMoneyGo;
        View layoutShow;
        TextView tvShow;
        ListView lvGoodsList;

        public ViewHolder(View itemView) {
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTotalNums = itemView.findViewById(R.id.tvTotalNums);
            tvReturnPrice = itemView.findViewById(R.id.tvReturnPrice);
            btnRebackDetial = itemView.findViewById(R.id.btnRebackDetial);
            btnRebackStatus = itemView.findViewById(R.id.btnRebackStatus);
            btnMoneyGo = itemView.findViewById(R.id.btnMoneyGo);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvRealPay = itemView.findViewById(R.id.tvRealPay);
            layoutShow = itemView.findViewById(R.id.layoutShow);
            tvShow = itemView.findViewById(R.id.tvShow);
            lvGoodsList = itemView.findViewById(R.id.lvGoodsList);
        }
    }
}
