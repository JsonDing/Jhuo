package com.yunma.adapter;

import android.content.*;
import android.os.Bundle;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.jhuo.activity.homepage.OrderDetial;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean;
import com.yunma.utils.*;
import com.yunma.widget.MyListView;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderWaitToSendAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<ListBean> listBean;
    private Animation push_left_in,push_right_in;
    private OrderGoodsAdapter mAdapter;
    public OrderWaitToSendAdapter(Context mContext, List<ListBean> listBean) {
        this.mContext = mContext;
        this.listBean = listBean;
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
            view = inflater.inflate(R.layout.item_wait_to_send, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if (position % 2 == 0) {
            push_right_in.setDuration(600);
            view.setAnimation(push_left_in);
        } else {
            push_left_in.setDuration(600);
            view.setAnimation(push_right_in);
        }
        holder.tvTime.setText(DateTimeUtils.getTime(listBean.get(position).getDate(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA)));
        holder.tvTotalPrice.setText("￥" + listBean.get(position).getTotalcost());
        int totalNums = 0 ;
        for(int i=0;i<listBean.get(position).getOrderdetails().size();i++){
            totalNums = totalNums + listBean.get(position).getOrderdetails().get(i).getNum();
        }
        holder.tvTotalNums.setText("共"+ totalNums +"件商品");
        holder.tvOrderId.setText("订单号:" + listBean.get(position).getId());
        mAdapter = new OrderGoodsAdapter(mContext, listBean.get(position).getOrderdetails());
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
        holder.btnNotSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showSuccess(mContext,"催单消息已发出" + "\n" +
                        "请耐心等待...");
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tvTime;
        TextView tvTotalNums;
        TextView tvTotalPrice,tvOrderId;
        Button btnNotSend;
        View layout;
        MyListView rvGoodsList;
        public ViewHolder(View itemView) {
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
            tvTotalNums = (TextView)itemView.findViewById(R.id.tvTotalNums);
            tvTotalPrice = (TextView)itemView.findViewById(R.id.tvTotalPrice);
            tvOrderId =  (TextView)itemView.findViewById(R.id.tvOrderId);
            btnNotSend = (Button)itemView.findViewById(R.id.btnNotSend);
            rvGoodsList = (MyListView)itemView.findViewById(R.id.rvGoodsList);
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
