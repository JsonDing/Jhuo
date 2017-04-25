package com.yunma.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.*;
import android.text.style.TextAppearanceSpan;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.RecipientManageBean;
import com.yunma.jhuo.m.RecipientManageInterface.OnModifyRecipient;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created by Json on 2016/12/31.
 */
public class ReceiverManageAdapter extends BaseAdapter {
    private List<RecipientManageBean.SuccessBean.ListBean> addressList;
    private LayoutInflater inflater;
    private OnModifyRecipient onModifyClick;
    private OnSelected onSelected;
    private Context mContext;
    public ReceiverManageAdapter(Context context, List<RecipientManageBean.SuccessBean.ListBean> addressList,
                                 OnModifyRecipient onModifyClick, OnSelected onSelected) {
        this.mContext = context;
        this.addressList = addressList;
        this.onSelected = onSelected;
        this.onModifyClick = onModifyClick;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Object getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        final ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_receiver_manage, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvNumber.setText(addressList.get(pos).getTel());
        holder.tvReceiver.setText(addressList.get(pos).getName());
        if(addressList.get(pos).getUsed()==1){//默认
            String strLocation = "[默认]" + addressList.get(pos).getRegoin()
                    + addressList.get(pos).getAddr();
            ColorStateList mColor = ColorStateList.valueOf(
                    mContext.getResources().getColor(R.color.color_b3));
            SpannableStringBuilder spanBuilder
                    = new SpannableStringBuilder(strLocation);
            spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 0, mColor, null)
                    , 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvAddress.setText(spanBuilder);
        }else{
            holder.tvAddress.setText(addressList.get(pos).getRegoin()
                    + addressList.get(pos).getAddr());
        }
        holder.layoutSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onSelected!=null&&
                        addressList.get(pos).getUsed()!=1){
                    onSelected.selected(pos,addressList);
                }else{
                    ToastUtils.showWarning(mContext,"已选择");
                }
            }
        });
        if(addressList.get(pos).getUsed()==1){
            holder.cbSelect.setChecked(true);
        }else{
            holder.cbSelect.setChecked(false);
        }
        holder.layoutModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onModifyClick!=null){
                    onModifyClick.onModifyListener(pos,addressList.get(pos));
                }
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
       super.notifyDataSetChanged();
    }

    public void setRecipientManageBean(List<RecipientManageBean.SuccessBean.ListBean> listBeen) {
        this.addressList = listBeen;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        View layoutModifyAddress;
        View layoutSelected;
        TextView tvReceiver;
        TextView tvNumber;
        TextView tvAddress;
        CheckBox cbSelect;

        ViewHolder(View view) {
            layoutModifyAddress =  view.findViewById(R.id.layoutModifyAddress);
            layoutSelected =  view.findViewById(R.id.layoutSelected);
            tvReceiver = (TextView) view.findViewById(R.id.tvReceiver);
            tvNumber = (TextView) view.findViewById(R.id.tvNumber);
            tvAddress = (TextView) view.findViewById(R.id.tvAddress);
            cbSelect = (CheckBox)view.findViewById(R.id.cbSelect);
        }
    }

    public interface OnSelected{
        void selected(int position, List<RecipientManageBean.SuccessBean.ListBean> addressList);
    }

}
