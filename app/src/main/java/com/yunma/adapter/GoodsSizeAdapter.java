package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;

import java.util.*;

/**
 * Created on 2016-12-30
 *
 * @author Json.
 */
public class GoodsSizeAdapter extends BaseAdapter{
    private List<String> mSize = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnAddBasketCarts onAddBasketCarts;
    public GoodsSizeAdapter(Context mContext,OnAddBasketCarts onAddBasketCarts) {
        this.onAddBasketCarts = onAddBasketCarts;
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mSize.size();
    }

    @Override
    public Object getItem(int position) {
        return mSize.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.size_tag_item, parent, false);
            viewHolder.tvName = convertView.findViewById(R.id.tv_tag);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(String.valueOf(mSize.get(position)));
        viewHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAddBasketCarts!=null){
                    onAddBasketCarts.onAddBasketCarts(String.valueOf(mSize.get(position)));
                }
            }
        });
        return convertView;
    }


    public void onlyAddAll(List<String> mSizes) {
        this.mSize = mSizes;
        notifyDataSetChanged();
    }

    public interface OnAddBasketCarts{
        void onAddBasketCarts(String size);
    }

    private class ViewHolder {
        private TextView tvName;
    }
}
