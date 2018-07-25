package com.yunma.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v4.content.ContextCompat;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.RecipientManageBean;
import com.yunma.jhuo.m.RecipientManageInterface.OnModifyRecipient;
import com.yunma.utils.ValueUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-01-16
 *
 * @author Json.
 */
public class RecipientManagementAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private OnModifyRecipient onClick;
    private List<RecipientManageBean.SuccessBean.ListBean> addressList;
    public RecipientManagementAdapter(Context mContext, List<RecipientManageBean.SuccessBean.ListBean> addressList,
                                      OnModifyRecipient onClick) {
        this.mContext = mContext;
        this.addressList = addressList;
        this.onClick = onClick;
        inflater = LayoutInflater.from(mContext);
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_receiver_managment, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvReceiver.setText(addressList.get(position).getName());
        holder.tvReceiverPhone.setText(ValueUtils.hideNumber(
                addressList.get(position).getTel()));
        if(addressList.get(position).getUsed()==1){
            String strLocation = "[默认]" + addressList.get(position).getRegoin()
                    + addressList.get(position).getAddr();
            final ColorStateList mColor = ColorStateList.valueOf(
                    ContextCompat.getColor(mContext,R.color.b3));
            SpannableStringBuilder spanBuilder
                    = new SpannableStringBuilder(strLocation);
            spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 0, mColor, null)
                    , 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvAddress.setText(spanBuilder);
        }else{
            holder.tvAddress.setText(String.valueOf(addressList.get(position).getRegoin()
                    + addressList.get(position).getAddr()));
        }

        holder.layoutEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onClick!=null){
                    onClick.onModifyListener(position,addressList.get(position));
                }
            }
        });

        return view;
    }

    class ViewHolder {
        @BindView(R.id.tvReceiver) TextView tvReceiver;
        @BindView(R.id.tvReceiverPhone) TextView tvReceiverPhone;
        @BindView(R.id.tvAddress) TextView tvAddress;
        @BindView(R.id.layoutEdit) LinearLayout layoutEdit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setAddressList(List<RecipientManageBean.SuccessBean.ListBean> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }
}
