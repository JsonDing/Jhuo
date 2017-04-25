package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;

import butterknife.*;

/**
 * Created on 2017-01-10
 *
 * @author Json.
 */
public class MyWalletPayListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;

    public MyWalletPayListAdapter(Context mContext) {
        this.mContext = mContext;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return 4;
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_my_wallet_pay_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }


        return view;
    }

    class ViewHolder {
        @BindView(R.id.tvYear)
        TextView tvYear;
        @BindView(R.id.tvMonth)
        TextView tvMonth;
        @BindView(R.id.imgPayLogo)
        ImageView imgPayLogo;
        @BindView(R.id.tvHasPayed)
        TextView tvHasPayed;
        @BindView(R.id.tvPayDescribe)
        TextView tvPayDescribe;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
