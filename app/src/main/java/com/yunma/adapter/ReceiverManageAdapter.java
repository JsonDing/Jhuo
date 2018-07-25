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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.dao.ConsigneeAddress;
import com.yunma.dao.GreenDaoManager;
import com.yunma.greendao.ConsigneeAddressDao;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Json on 2016/12/31.
 */
public class ReceiverManageAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private OnAddressClick onAddressClick;
    private Context mContext;
    private List<ConsigneeAddress> addrList;
    public ReceiverManageAdapter(Context context, OnAddressClick onAddressClick) {
        this.mContext = context;
        this.onAddressClick = onAddressClick;
        this.addrList = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return addrList.size();
    }

    @Override
    public Object getItem(int position) {
        return addrList.get(position);
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
        holder.tvNumber.setText(addrList.get(pos).getTelePhone());
        holder.tvReceiver.setText(addrList.get(pos).getConsignee());
        if(addrList.get(pos).getIsHideDetial()){
            GlideUtils.glidLocalDrawable(mContext,holder.imgIsShow,R.drawable.addr_hide);
            holder.tvAddress.setVisibility(View.GONE);
        }else {
            GlideUtils.glidLocalDrawable(mContext,holder.imgIsShow,R.drawable.addr_show);
            holder.tvAddress.setVisibility(View.VISIBLE);
            if(addrList.get(pos).getIsDefault().equals("1")){//默认
                String strLocation = "[默认]" + addrList.get(pos).getRegoin() + addrList.get(pos).getAddress();
                ColorStateList mColor = ColorStateList.valueOf(
                        ContextCompat.getColor(mContext,R.color.b3));
                SpannableStringBuilder spanBuilder
                        = new SpannableStringBuilder(strLocation);
                spanBuilder.setSpan(new TextAppearanceSpan(null, 0, 0, mColor, null)
                        , 0, 4, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
                holder.tvAddress.setText(spanBuilder);
            }else{
                holder.tvAddress.setText(String.valueOf(addrList.get(pos).getRegoin() + addrList.get(pos).getAddress()));
            }
        }
        holder.layoutSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddressClick!=null && !addrList.get(pos).getIsDefault().equals("1")){
                    onAddressClick.onSelectedClick(pos,addrList.get(pos));
                }else{
                    ToastUtils.showWarning(mContext,"已选择");
                }
            }
        });
        if(addrList.get(pos).getIsDefault().equals("1")){
            holder.cbSelect.setChecked(true);
        }else{
            holder.cbSelect.setChecked(false);
        }
        holder.layoutModifyAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddressClick!=null){
                    onAddressClick.onModifyClick(pos,addrList.get(pos));
                }
            }
        });
        holder.imgIsShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onAddressClick!=null){
                    onAddressClick.isShowAddr(addrList.get(pos));
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onAddressClick!=null){
                    onAddressClick.onItemClick(pos,addrList.get(pos));
                }
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
       super.notifyDataSetChanged();
    }

    public void setRecipientManageBean(List<ConsigneeAddress> listBeen) {
        this.addrList = listBeen;
        notifyDataSetChanged();
    }

    public void changeDefault(String addressId){
        List<ConsigneeAddress> tempList = getAddressDao().loadAll();
        for(int i=0;i<tempList.size();i++){
            if(addressId.equals(tempList.get(i).getAddressId())){
                tempList.get(i).setIsDefault("1");
            }else{
                tempList.get(i).setIsDefault("0");
            }
        }
        notifyDataSetChanged();
        saveNLists(tempList);
    }

    public void clearDatas() {
        addrList.clear();
        notifyDataSetChanged();
    }

    public void refresh() {
        notifyDataSetChanged();
    }

    private class ViewHolder {
        View layoutModifyAddress;
        View layoutSelected;
        TextView tvReceiver;
        TextView tvNumber;
        TextView tvAddress;
        CheckBox cbSelect;
        ImageView imgIsShow;
        LinearLayout itemView;
        ViewHolder(View view) {
            itemView = view.findViewById(R.id.itemView);
            layoutModifyAddress =  view.findViewById(R.id.layoutModifyAddress);
            layoutSelected =  view.findViewById(R.id.layoutSelected);
            tvReceiver = view.findViewById(R.id.tvReceiver);
            tvNumber = view.findViewById(R.id.tvNumber);
            tvAddress = view.findViewById(R.id.tvAddress);
            cbSelect = view.findViewById(R.id.cbSelect);
            imgIsShow = view.findViewById(R.id.imgIsShow);
        }
    }

    public interface OnAddressClick{
        void onSelectedClick(int position, ConsigneeAddress addressBean);
        void onModifyClick(int position,ConsigneeAddress mBean);
        void isShowAddr(ConsigneeAddress consigneeAddress);
        void onItemClick(int position, ConsigneeAddress addressBean);
    }

    private ConsigneeAddressDao getAddressDao() {
        return GreenDaoManager.getInstance().getSession().getConsigneeAddressDao();
    }

    /**
     * 批量插入或修改用户信息
     * @param list      用户信息列表
     */
    private void saveNLists(final List<ConsigneeAddress> list){
        if(list == null || list.isEmpty()){
            return;
        }
        getAddressDao().getSession().runInTx(new Runnable() {
            @Override
            public void run() {
                for(int i=0; i<list.size(); i++){
                    ConsigneeAddress user = list.get(i);
                    getAddressDao().insertOrReplace(user);
                }
            }
        });

    }


}
