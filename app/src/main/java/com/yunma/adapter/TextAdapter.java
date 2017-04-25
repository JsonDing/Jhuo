package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.yunma.R;
import com.yunma.utils.LogUtils;

import java.util.ArrayList;

/**
 * Created on 2017-03-17
 *
 * @author Json.
 */

public class TextAdapter extends BaseAdapter{
    Context context;
    //List<PathBean> path;
    ArrayList<String> path;
    private LayoutInflater inflater;
    public TextAdapter(Context context, ArrayList<String> path) {
        this.context = context;
        this.path = path;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return path.size();
    }

    @Override
    public Object getItem(int position) {
        return path.get(position);
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
            view = inflater.inflate(R.layout.item_tst, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
    //    GlideUtils.loadSDImgs(context,holder.image,path.get(position));
        LogUtils.log("holder.image---> " + path.get(position));
        Glide.with(context).load(path.get(position)).into(holder.image);
        return view;
    }

    class  ViewHolder{
        ImageView image;
        public ViewHolder(View view) {
            image = (ImageView) view.findViewById(R.id.image);
        }
    }

    public ArrayList<String> getPath() {
        return path;
    }

    public void setPath(ArrayList<String> path) {
        this.path = path;
    }
}
