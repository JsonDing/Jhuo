package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.dao.HistroySearch;
import com.yunma.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-04-18
 *
 * @author Json.
 */

public class HistroySearchAdapter extends BaseAdapter {

    private List<HistroySearch> searchHistory;
    private LayoutInflater inflater;
    private OnClickListener onClickListener;
    private Context context;
    public HistroySearchAdapter(Context context,OnClickListener onClickListener) {
        this.context = context;
        this.onClickListener = onClickListener;
        this.searchHistory = new ArrayList<>();
        inflater = LayoutInflater.from(context);
    }

    public void setSearchHistory(List<HistroySearch> searchHistory) {
        this.searchHistory = searchHistory;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return searchHistory.size();
    }

    @Override
    public Object getItem(int position) {
        return searchHistory.get(position);
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
            convertView = inflater.inflate(R.layout.item_history_search, parent, false);
            viewHolder.tvSearchNumber = convertView.findViewById(R.id.tvSearchNumber);
            viewHolder.imgPic = convertView.findViewById(R.id.imgPic);
            viewHolder.itemView = convertView.findViewById(R.id.itemView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String imgUrl = searchHistory.get(position).getGoodsImg();
        if (imgUrl != null){
            GlideUtils.glidNormleFast(context, viewHolder.imgPic,searchHistory.get(position).getGoodsImg());
        } else {
            GlideUtils.glidLocalDrawable(context, viewHolder.imgPic,R.drawable.logo);
        }

        viewHolder.tvSearchNumber.setText(searchHistory.get(position).getSearchNumber());
        if(null != onClickListener){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickListener.onSearchClick(searchHistory.get(position).getSearchNumber());
                }
            });
        }
        return convertView;
    }

    public class ViewHolder{
        private ImageView imgPic;
        private TextView tvSearchNumber;
        private View itemView;
    }

    public interface OnClickListener{
        void onSearchClick(String searchNumber);
    }
}
