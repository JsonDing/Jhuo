package com.yunma.adapter;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.OrderDetialActivity;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean;
import com.yunma.jhuo.m.OrderHadConfirmInterFace.OnAdapterClick;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.MyListView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToReceiveAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<ListBean> listBean;
    private OnAdapterClick onAdapterClick;
    private Animation push_left_in,push_right_in;
    public OrderWaitToReceiveAdapter(Context mContext, List<ListBean> listBean,
                                     OnAdapterClick onAdapterClick) {
        this.mContext = mContext;
        this.listBean = listBean ;
        this.onAdapterClick = onAdapterClick;
        push_left_in= AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
        push_right_in=AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
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
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_wait_to_receive, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if (position % 2 == 0) {
            push_left_in.setDuration(500);
            view.setAnimation(push_left_in);
        } else {
            push_right_in.setDuration(500);
            view.setAnimation(push_right_in);
        }
        holder.tvTime.setText(DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        holder.tvTotalPrice.setText(ValueUtils.toTwoDecimal(listBean.get(position).getTotalcost()));
        int totalNums = 0 ;
        for(int i=0;i<listBean.get(position).getOrderdetails().size();i++){
            totalNums = totalNums + listBean.get(position).getOrderdetails().get(i).getNum();
        }
        holder.tvTotalNums.setText(String.valueOf(totalNums));
        holder.tvOrderId.setText(/*String.valueOf("Jh"+ DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyMMdd", Locale.CHINA)) + */listBean.get(position).getId() + "");
        OrderGoodsAdapter mAdapter = new OrderGoodsAdapter(mContext, listBean.get(position).getOrderdetails());
        holder.rvGoodsList.setAdapter(mAdapter);
        holder.btnOrderTrace
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null != onAdapterClick){
                            onAdapterClick.onTraceClickListener(
                                    listBean.get(position).getId(),
                                    listBean.get(position).getOrderdetails().get(0).getExpresscode(),
                                    listBean.get(position).getOrderdetails().get(0).getExpressname(),
                                    listBean.get(position).getOrderdetails().get(0).getExpressnumber());
                        }
                    }
                });

        holder.btnDeteleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onAdapterClick){
                    onAdapterClick.onDeleteClickListener(position,
                            String.valueOf(listBean.get(position).getId()));
                }
            }
        });

        holder.btnTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onAdapterClick){
                    onAdapterClick.onGetTicketListener(position,listBean.get(position));
                }
            }
        });

        final int finalTotalNums = totalNums;
        holder.btnApplyHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onAdapterClick){
                    onAdapterClick.onApplyClickListener(position,listBean.get(position), finalTotalNums);
                }
            }
        });
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetialActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderDetial",listBean.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tvTime,tvOrderId;
        TextView tvTotalNums;
        TextView tvTotalPrice;
        TextView btnDeteleOrder;
        TextView btnApplyHelp;
        TextView btnOrderTrace;
        TextView btnTicket;
        View layout;
        MyListView rvGoodsList;
        public ViewHolder(View itemView) {
            tvTime = itemView.findViewById(R.id.tvTime);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalNums = itemView.findViewById(R.id.tvTotalNums);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnOrderTrace = itemView.findViewById(R.id.btnOrderTrace);
            btnDeteleOrder = itemView.findViewById(R.id.btnDeteleOrder);
            btnApplyHelp = itemView.findViewById(R.id.btnApplyHelp);
            btnTicket = itemView.findViewById(R.id.btnTicket);
            rvGoodsList = itemView.findViewById(R.id.rvGoodsList);
            layout = itemView.findViewById(R.id.layout);
        }
    }

    public List<ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<ListBean> listBean) {
        this.listBean = listBean;
    }
}
