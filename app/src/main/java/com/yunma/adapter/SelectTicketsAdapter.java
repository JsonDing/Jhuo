package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.InvoiceBean;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-03-29
 *
 * @author Json.
 */

public class SelectTicketsAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private OnSelectClick onSelectClick;
    private List<InvoiceBean.SuccessBean.ListBean> listBean;
    public SelectTicketsAdapter(Context context, List<InvoiceBean.SuccessBean.ListBean> listBean,
                                OnSelectClick onSelectClick) {
        this.mContext = context;
        this.listBean = listBean;
        this.onSelectClick = onSelectClick;
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
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.invoice_select_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvCompanyName.setText(listBean.get(position).getName());
        holder.tvRatepayer.setText(listBean.get(position).getNumber());
        holder.tvRegisterAddress.setText(listBean.get(position).getAddr());
        holder.tvRegisterPhone.setText(listBean.get(position).getTel());
        holder.tvRegisterBank.setText(listBean.get(position).getBank());
        holder.tvRegisterAccount.setText(listBean.get(position).getAccount());
        if(listBean.get(position).isSelect()==0){
            holder.checkbox.setChecked(false);
        }else if(listBean.get(position).isSelect()==1){
            holder.checkbox.setChecked(true);
        }else{
            holder.checkbox.setChecked(false);
        }

        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelectClick != null){
                    clearSelect();
                    onSelectClick.onSelectClick(position,listBean);
                }
            }
        });
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    public class ViewHolder {
        @BindView(R.id.tvCompanyName) TextView tvCompanyName;
        @BindView(R.id.textview) TextView textview;
        @BindView(R.id.tvRatepayer) TextView tvRatepayer;
        @BindView(R.id.tvRegisterAddress) TextView tvRegisterAddress;
        @BindView(R.id.tvRegisterPhone) TextView tvRegisterPhone;
        @BindView(R.id.tvRegisterBank) TextView tvRegisterBank;
        @BindView(R.id.tvRegisterAccount) TextView tvRegisterAccount;
        @BindView(R.id.checkbox) CheckBox checkbox;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public interface OnSelectClick{
        void onSelectClick(int position,  List<InvoiceBean.SuccessBean.ListBean> listBean);
    }

    public List<InvoiceBean.SuccessBean.ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<InvoiceBean.SuccessBean.ListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }

    public void clearSelect(){
        for(int i=0;i<listBean.size();i++){
            listBean.get(i).setSelect(0);
        }
    }
}
