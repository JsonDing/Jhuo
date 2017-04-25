package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.*;
import android.widget.*;

import com.yunma.R;

import java.util.List;

/**
 * Created on 2017-03-13
 *
 * @author Json.
 */

public class GoodsTagsAdapter extends BaseAdapter {

    private List<String> selectedTags;
    private Context mContext;
    private LayoutInflater layoutInflater;
    private TagsOnClick tagsOnClick;
    private int type;
    public GoodsTagsAdapter(Context context, List<String> selectedTags,TagsOnClick tagsOnClick,int type) {
        this.mContext = context;
        this.selectedTags = selectedTags;
        this.tagsOnClick = tagsOnClick;
        this.type = type;
        layoutInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void onlyAddAll(List<String> selectedTags) {
        this.selectedTags = selectedTags;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if(type==0){
            return selectedTags.size() + 1;
        }else{
            return selectedTags.size();
        }
    }

    @Override
    public String getItem(int position) {
        return selectedTags.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        private TextView tvTags;
        private View layoutAddTags;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.goods_tags_item, parent,false);
            viewHolder.tvTags = (TextView) convertView.findViewById(R.id.tvTags);
            viewHolder.layoutAddTags = convertView.findViewById(R.id.layoutAddTags);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if(position>=selectedTags.size()){
            viewHolder.layoutAddTags.setVisibility(View.VISIBLE);
            viewHolder.tvTags.setVisibility(View.GONE);
            viewHolder.layoutAddTags.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tagsOnClick!=null){
                        tagsOnClick.addTags();
                    }
                }
            });

        }else{
            if(type==0){
                viewHolder.tvTags.setBackground(mContext
                        .getResources().getDrawable(R.drawable.fillet_light_orange));
                viewHolder.tvTags.setTextColor(Color.parseColor("#ffffff"));
            }
            viewHolder.layoutAddTags.setVisibility(View.GONE);
            viewHolder.tvTags.setVisibility(View.VISIBLE);
            viewHolder.tvTags.setText(String.valueOf(selectedTags.get(position)));
        }

        return convertView;
    }

    public interface TagsOnClick{
        void addTags();
    }
}
