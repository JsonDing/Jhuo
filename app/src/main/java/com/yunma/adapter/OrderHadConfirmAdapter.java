package com.yunma.adapter;

import android.content.*;
import android.view.*;
import android.widget.*;

import com.yunma.jhuo.activity.homepage.OrderDetial;
import com.yunma.jhuo.m.OrderHadConfirmInterFace.*;
import com.yunma.R;
import com.yunma.widget.MyListView;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class OrderHadConfirmAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private OrderGoodsAdapter mAdapter;
    private OnAdapterClick onAdapterClick;
    public OrderHadConfirmAdapter(Context mContext,OnAdapterClick onAdapterClick) {
        this.mContext = mContext;
        this.onAdapterClick = onAdapterClick;
        inflater = LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_has_confirm, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        /*mAdapter = new OrderGoodsAdapter(mContext, position);
        holder.rvGoodsList.setAdapter(mAdapter);*/

        holder.btnDeteleOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != onAdapterClick){
                 /*   onAdapterClick.onDeleteClickListener(position,
                            String.valueOf(list));*/
                }
            }
        });
        holder.btnApplyHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(null != onAdapterClick){
                    onAdapterClick.onApplyClickListener(position, listBean.get(position).getOrderdetails());
                }*/
            }
        });
        holder.btnOrderTrace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(null != onAdapterClick){
                    onAdapterClick.onTraceClickListener(position);
                }*/
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, OrderDetial.class);
                mContext.startActivity(intent);
            }
        });
        return view;
    }

    public class ViewHolder {
        TextView tvTime;
        TextView tvTotalNums;
        TextView tvTotalPrice;
        Button btnDeteleOrder;
        Button btnApplyHelp;
        Button btnOrderTrace;
        MyListView rvGoodsList;
        View layout;
        public ViewHolder(View itemView) {
            tvTime = (TextView)itemView.findViewById(R.id.tvTime);
            tvTotalNums = (TextView)itemView.findViewById(R.id.tvTotalNums);
            tvTotalPrice = (TextView)itemView.findViewById(R.id.tvTotalPrice);
            btnDeteleOrder = (Button)itemView.findViewById(R.id.btnDeteleOrder);
            btnApplyHelp = (Button)itemView.findViewById(R.id.btnApplyHelp);
            btnOrderTrace = (Button)itemView.findViewById(R.id.btnOrderTrace);
            rvGoodsList = (MyListView)itemView.findViewById(R.id.rvGoodsList);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
