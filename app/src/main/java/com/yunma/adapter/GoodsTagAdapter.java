package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;

import java.util.List;

/**
 * Created on 2017-03-02
 *
 * @author Json.
 */
public class GoodsTagAdapter extends BaseAdapter {
    private List<String> tag;
    private LayoutInflater layoutInflater;
    public GoodsTagAdapter(Context mContext) {
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tag.size();
    }

    @Override
    public Object getItem(int position) {
        return tag.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView tvLabel;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.tag_item, parent, false);
            viewHolder.tvLabel = (TextView) convertView.findViewById(R.id.tvLabel);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvLabel.setText(String.valueOf(tag.get(position)));
        return convertView;
    }

    public void onlyAddAll(List<String> tag) {
        this.tag = tag;
        notifyDataSetChanged();
    }

}
