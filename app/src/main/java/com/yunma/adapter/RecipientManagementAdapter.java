package com.yunma.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.*;
import android.text.style.TextAppearanceSpan;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.RecipientManageBean;
import com.yunma.jhuo.m.RecipientManageInterface.*;
import com.yunma.utils.ValueUtils;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-01-16
 *
 * @author Json.
 */
public class RecipientManagementAdapter extends BaseAdapter {
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
                    mContext.getResources().getColor(R.color.color_b3));
            SpannableStringBuilder spanBuilder
                    = new SpannableStringBuilder(strLocation);
            //style 为0 即是正常的，还有Typeface.BOLD(粗体) Typeface.ITALIC(斜体)等
            //size  为0 即采用原始的正常的 size大小
            spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 0, mColor, null)
                    , 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.tvAddress.setText(spanBuilder);
        }else{
            holder.tvAddress.setText(addressList.get(position).getRegoin()
                    + addressList.get(position).getAddr());
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
        @BindView(R.id.tvReceiver)
        TextView tvReceiver;
        @BindView(R.id.tvReceiverPhone)
        TextView tvReceiverPhone;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.layoutEdit)
        LinearLayout layoutEdit;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public List<RecipientManageBean.SuccessBean.ListBean> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<RecipientManageBean.SuccessBean.ListBean> addressList) {
        this.addressList = addressList;
        notifyDataSetChanged();
    }
}
