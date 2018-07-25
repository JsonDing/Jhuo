package com.yunma.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.dao.SystemNotices;
import com.yunma.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-01-17
 *
 * @author Json.
 */
public class SystemNoticeAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private  List<SystemNotices> list;
    public SystemNoticeAdapter(Context context) {
        this.list = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_system_notice, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if(position==list.size()-1){
            holder.layoutFoot.setVisibility(View.VISIBLE);
        }else{
            holder.layoutFoot.setVisibility(View.GONE);
        }
        holder.tvTittle.setText(Html.fromHtml(list.get(position).getTittle()));
        holder.tvNoticeContent.setText(Html.fromHtml(list.get(position).getContent()));
        holder.tvPublishTime.setText(DateTimeUtils.getTime(list.get(position).getTime(),
                new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA)));
        if(list.get(position).getIsRead().equals("yes")){
            holder.imgRemind.setVisibility(View.INVISIBLE);
        }else{
            holder.imgRemind.setVisibility(View.VISIBLE);
        }
        return view;
    }

    class ViewHolder {
        @BindView(R.id.imgRemind)
        ImageView imgRemind;
        @BindView(R.id.tvTittle)
        TextView tvTittle;
        @BindView(R.id.tvNoticeContent)
        TextView tvNoticeContent;
        @BindView(R.id.tvPublishTime)
        TextView tvPublishTime;
        @BindView(R.id.layoutFoot)
        View layoutFoot;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public void setList( List<SystemNotices> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
