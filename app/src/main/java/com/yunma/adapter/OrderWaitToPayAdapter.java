package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.jhuo.activity.homepage.OrderDetialActivity;
import com.yunma.jhuo.activity.homepage.SelectedPayWay;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.ToastUtils;
import com.yunma.utils.ValueUtils;
import com.yunma.widget.MyListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToPayAdapter extends RecyclerView.Adapter<OrderWaitToPayAdapter.ViewHolder> {

    private Context mContext;
    private LayoutInflater inflater;
    private DelOrder delOrderClick;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> listBean;
    public OrderWaitToPayAdapter(Context mContext,DelOrder delOrderClick) {
        this.mContext = mContext;
        this.delOrderClick = delOrderClick;
        this.listBean = new ArrayList<>();
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return listBean.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(inflater.inflate(R.layout.item_wait_to_pay_list, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.tvTime.setText(DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        holder.tvTotalPrice.setText(ValueUtils.toTwoDecimal(listBean.get(position).getTotalcost()));
        int totalNums = 0 ;
        for(int i=0;i<listBean.get(position).getOrderdetails().size();i++){
            totalNums = totalNums + listBean.get(position).getOrderdetails().get(i).getNum();
        }
        holder.tvTotalNums.setText("共"+ totalNums +"件商品");
        holder.tvOrderId.setText(/*String.valueOf("Jh"+ DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyMMdd", Locale.CHINA)) + */listBean.get(position).getId() + "");
        OrderGoodsAdapter mAdapter = new OrderGoodsAdapter(mContext, listBean.get(position).getOrderdetails());
        holder.rvGoodsList.setAdapter(mAdapter);
        long orderTime = listBean.get(position).getDate();
        long systemTime = DateTimeUtils.getCurrentTimeInLong();
        long costTime = systemTime-orderTime;
        if(costTime/(1000*60) >30){
            holder.imgsOutTime.setVisibility(View.VISIBLE);
            holder.btnPay.setTextColor(Color.parseColor("#323232"));
            holder.btnPay.setBackground(
                    ContextCompat.getDrawable(mContext,R.drawable.bg_button_gray));
            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.showError(mContext,"订单已过期，无法支付");
                }
            });
        }else{
            holder.imgsOutTime.setVisibility(View.GONE);
            holder.btnPay.setTextColor(Color.parseColor("#f4bd39"));
            holder.btnPay.setBackground(
                    ContextCompat.getDrawable(mContext,R.drawable.bg_button_orange));
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, OrderDetialActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("orderDetial",listBean.get(holder.getAdapterPosition()));
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
            holder.btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext.getApplicationContext(),SelectedPayWay.class);
                    intent.putExtra("orderId",String.valueOf(listBean.get(holder.getAdapterPosition()).getId()));
                    intent.putExtra("totalPrice",listBean.get(holder.getAdapterPosition()).getTotalcost());
                    mContext.startActivity(intent);
                }
            });
        }
        holder.btnCance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=delOrderClick){
                    delOrderClick.delOrder(String.valueOf(
                            listBean.get(holder.getAdapterPosition()).getId()),holder.getAdapterPosition());
                }
            }
        });
    }

    public void refreshData(int delPos) {
        this.listBean.remove(delPos);
        notifyItemRemoved(delPos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvTime;
        TextView tvTotalNums;
        TextView tvTotalPrice;
        Button btnCance;
        Button btnPay;
        View layout;
        TextView tvOrderId;
        MyListView rvGoodsList;
        private ImageView imgsOutTime;
        public ViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTotalNums = itemView.findViewById(R.id.tvTotalNums);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            btnCance = itemView.findViewById(R.id.btnCance);
            btnPay = itemView.findViewById(R.id.btnPay);
            layout = itemView.findViewById(R.id.layout);
            imgsOutTime = itemView.findViewById(R.id.imgsOutTime);
            rvGoodsList = itemView.findViewById(R.id.rvGoodsList);
        }
    }

    public interface DelOrder{
        void delOrder(String ids,int delPos);
    }

    public List<OrderUnPayResultBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void addListBean(List<OrderUnPayResultBean.SuccessBean.ListBean> listBean) {
        this.listBean.addAll(listBean);
        notifyDataSetChanged();
    }

    public void setListBean(List<OrderUnPayResultBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
        notifyItemInserted(listBean.size());
    }
}
