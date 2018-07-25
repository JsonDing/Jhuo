package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.InvoiceBean;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-03-20
 *
 * @author Json.
 */

public class InvoiceListAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater mInflater;
    private List<InvoiceBean.SuccessBean.ListBean> listBean;

    public InvoiceListAdapter(Context context, List<InvoiceBean.SuccessBean.ListBean> listBean) {
        this.mContext = context;
        this.listBean = listBean;
        mInflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.invoice_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvCompanyName.setText(listBean.get(position).getName());
        holder.tvRatepayer.setText(listBean.get(position).getNumber());
        holder.tvRegisterAddress.setText(listBean.get(position).getAddr());
        holder.tvRegisterPhone.setText(listBean.get(position).getTel());
        holder.tvRegisterBank.setText(listBean.get(position).getBank());
        holder.tvRegisterAccount.setText(listBean.get(position).getAccount());
        if(listBean.get(position).getAuth().equals("0")){
            holder.tvStatus.setText("待审核");
            holder.tvStatus.setTextColor(Color.parseColor("#f4bd39"));
        }else if(listBean.get(position).getAuth().equals("1")){
            holder.tvStatus.setText("已审核");
            holder.tvStatus.setTextColor(Color.parseColor("#019e1b"));
        }else{
            holder.tvStatus.setText("未通过");
            holder.tvStatus.setTextColor(Color.parseColor("#ebaf1d"));
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class ViewHolder {
        @BindView(R.id.tvCompanyName) TextView tvCompanyName;
        @BindView(R.id.tvStatus) TextView tvStatus;
        @BindView(R.id.textview) TextView textview;
        @BindView(R.id.tvRatepayer) TextView tvRatepayer;
        @BindView(R.id.tvRegisterAddress) TextView tvRegisterAddress;
        @BindView(R.id.textview2) TextView textview2;
        @BindView(R.id.tvRegisterPhone) TextView tvRegisterPhone;
        @BindView(R.id.textview3) TextView textview3;
        @BindView(R.id.tvRegisterBank) TextView tvRegisterBank;
        @BindView(R.id.textview4) TextView textview4;
        @BindView(R.id.tvRegisterAccount) TextView tvRegisterAccount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public List<InvoiceBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<InvoiceBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }
}
