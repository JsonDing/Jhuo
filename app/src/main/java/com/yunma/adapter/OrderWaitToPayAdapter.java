package com.yunma.adapter;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.*;
import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.utils.*;
import com.yunma.widget.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToPayAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater inflater;
    private DelOrder delOrderClick;
    private List<OrderUnPayResultBean.SuccessBean.ListBean> listBean;
    private OrderGoodsAdapter mAdapter;
    private Animation slide_top_to_bottom,slide_bottom_to_top;
    public OrderWaitToPayAdapter(Context mContext,DelOrder delOrderClick,
                                 List<OrderUnPayResultBean.SuccessBean.ListBean> listBean) {
        this.mContext = mContext;
        this.delOrderClick = delOrderClick;
        this.listBean = listBean;
        inflater = LayoutInflater.from(mContext);
        slide_top_to_bottom= AnimationUtils.loadAnimation(mContext, R.anim.slide_top_to_bottom);
        slide_bottom_to_top=AnimationUtils.loadAnimation(mContext, R.anim.slide_bottom_to_top);
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
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
            view = inflater.inflate(R.layout.item_wait_to_pay_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if(position==0){
            slide_top_to_bottom.setDuration(500);
            view.setAnimation(slide_top_to_bottom);
        }else{
            slide_bottom_to_top.setDuration(500);
            view.setAnimation(slide_bottom_to_top);
        }
        holder.tvTime.setText(DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        holder.tvTotalPrice.setText("￥" + listBean.get(position).getTotalcost());
        int totalNums = 0 ;
        for(int i=0;i<listBean.get(position).getOrderdetails().size();i++){
            totalNums = totalNums + listBean.get(position).getOrderdetails().get(i).getNum();
        }
        holder.tvTotalNums.setText("共"+ totalNums +"件商品");
        holder.tvOrderId.setText("订单号：" + listBean.get(position).getId());
        mAdapter = new OrderGoodsAdapter(mContext,listBean.get(position).getOrderdetails());
        holder.rvGoodsList.setAdapter(mAdapter);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetial.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("orderDetial",listBean.get(position));
                intent.putExtras(bundle);
                mContext.startActivity(intent);
            }
        });
        holder.btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext.getApplicationContext(),SelectedPayWay.class);
                intent.putExtra("orderId",String.valueOf(listBean.get(position).getId()));
                intent.putExtra("totalPrice",listBean.get(position).getTotalcost());
                mContext.startActivity(intent);
            }
        });
        holder.btnCance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null!=delOrderClick){
                    delOrderClick.delOrder(String.valueOf(listBean.get(position).getId()));
                }
            }
        });

        return view;
    }

    public class ViewHolder {
        TextView tvTime;
        TextView tvTotalNums;
        TextView tvTotalPrice;
        Button btnCance;
        Button btnPay;
        View layout;
        TextView tvOrderId;
        MyListView rvGoodsList;
        public ViewHolder(View itemView) {
            tvOrderId = (TextView)itemView.findViewById(R.id.tvOrderId);
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
            tvTotalNums = (TextView)itemView.findViewById(R.id.tvTotalNums);
            tvTotalPrice = (TextView)itemView.findViewById(R.id.tvTotalPrice);
            btnCance = (Button)itemView.findViewById(R.id.btnCance);
            btnPay = (Button)itemView.findViewById(R.id.btnPay);
            layout = itemView.findViewById(R.id.layout);
            rvGoodsList = (MyListView)itemView.findViewById(R.id.rvGoodsList);
        }
    }

    public interface DelOrder{
        void delOrder(String ids);
    }

    public List<OrderUnPayResultBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<OrderUnPayResultBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }
}
