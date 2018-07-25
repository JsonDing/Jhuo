package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;
import com.yunma.R;
import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by Json on 2017/1/18.
 */
public class InvoiceGoodsAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;

    public InvoiceGoodsAdapter(Context context) {
        this.mContext = context;
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
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_invoice_goods, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }


        return view;
    }


    static class ViewHolder {
        @BindView(R.id.rbButton)
        CheckBox rbButton;
        @BindView(R.id.imgGoods)
        ImageView imgGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvOrderTime)
        TextView tvOrderTime;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.tvGoodsNums)
        TextView tvGoodsNums;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
