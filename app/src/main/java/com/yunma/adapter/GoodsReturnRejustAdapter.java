package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.ServiceResultBean;
import com.yunma.jhuo.m.GoodsRefundInterface.*;
import com.yunma.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class GoodsReturnRejustAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private OnRejeckClick onAdapterClick;
    private GoodsRuturnAdapter mAdapter;
    private List<ServiceResultBean.SuccessBean.ListBean> listBean;
    public GoodsReturnRejustAdapter(Context mContext, OnRejeckClick onAdapterClick,
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
            view = inflater.inflate(R.layout.item_goods_reback_reject, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
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

        holder.btnReason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null!=onAdapterClick){
                    onAdapterClick.onRejectReason();
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
        Button btnReason;
        View layoutShow;
        TextView tvShow;
        ListView lvGoodsList;

        public ViewHolder(View itemView) {
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTotalNums = itemView.findViewById(R.id.tvTotalNums);
            tvReturnPrice = itemView.findViewById(R.id.tvReturnPrice);
            btnRebackDetial = itemView.findViewById(R.id.btnRebackDetial);
            btnReason = itemView.findViewById(R.id.btnReason);
            tvOrderCode = itemView.findViewById(R.id.tvOrderCode);
            tvRealPay = itemView.findViewById(R.id.tvRealPay);
            layoutShow = itemView.findViewById(R.id.layoutShow);
            tvShow = itemView.findViewById(R.id.tvShow);
            lvGoodsList = itemView.findViewById(R.id.lvGoodsList);
        }
    }
}
