package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.widget.CircleImageView;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-03-04
 *
 * @author Json.
 */

public class CustomerAdapter extends BaseAdapter{
    private List<String> usernames;
    private LayoutInflater inflater;
    public CustomerAdapter(Context context, List<String> usernames) {
        this.usernames = usernames;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return usernames.size();
    }

    @Override
    public Object getItem(int position) {
        return usernames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(R.layout.item_customer, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }
        holder.name.setText(usernames.get(position));
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.imgsPhotos)
        CircleImageView imgsPhotos;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
