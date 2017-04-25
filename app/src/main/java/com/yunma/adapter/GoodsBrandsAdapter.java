package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.jhuo.m.OnInitSelectedPosition;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Json on 2017/3/10.
 */

public class GoodsBrandsAdapter extends BaseAdapter implements OnInitSelectedPosition {
    private List<String> tag;
    private Context mContext;
    private LayoutInflater layoutInflater;

    public GoodsBrandsAdapter(Context context) {
        this.mContext = context;
        this.tag = new ArrayList<>();
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tag.size();
    }

    @Override
    public String getItem(int position) {
        return tag.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void onlyAddAll(List<String> goodsBrands) {
        this.tag = goodsBrands;
        notifyDataSetChanged();
    }

    public void clearAndAddAll(List<String> datas) {
        this.tag.clear();
        onlyAddAll(datas);
    }



    @Override
    public boolean isSelectedPosition(int position) {
        return position % 2 == 0;

    }

    private class ViewHolder {
        private TextView tvBrands;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.goods_brands_item, parent, false);
            viewHolder.tvBrands = (TextView) convertView.findViewById(R.id.tvBrands);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvBrands.setText(String.valueOf(tag.get(position)));

        return convertView;
    }
}
