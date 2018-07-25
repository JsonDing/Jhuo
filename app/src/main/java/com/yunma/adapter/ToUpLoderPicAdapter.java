package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.bumptech.glide.Glide;
import com.yunma.R;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class ToUpLoderPicAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
private static final int ITEM_TYPE_CONTENT = 1;
private static final int ITEM_TYPE_BOTTOM = 2;
private int mBottomCount=1;//底部View个数
private Context context;
private List<String> imgsUrl;
private OnAddPicClick addPicClick;
private LayoutInflater inflater;
public ToUpLoderPicAdapter(Context context, List<String> imgsUrl, OnAddPicClick addPicClick) {
        this.context = context;
        this.imgsUrl =imgsUrl;
        inflater = LayoutInflater.from(context);
        this.addPicClick = addPicClick;
        }

//内容长度
private int getBodyItemCount(){

        return imgsUrl.size();

        }

    //判断当前item类型
    @Override
    public int getItemViewType(int position) {
        int dataItemCount = getBodyItemCount();
        if (position < 10 && position >=  dataItemCount) {
            //底部View
            return ITEM_TYPE_BOTTOM;
        } else {
            //内容View
            return ITEM_TYPE_CONTENT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_CONTENT) {
            return  new BodyViewHolder(inflater.inflate(R.layout.item_add_goods, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(inflater.inflate(R.layout.item_add_goods, parent, false));
        }else{
            return null;
        }
    }

    @Override
    public int getItemCount() {
        if(getBodyItemCount()==3)
            return getBodyItemCount();
        else
            return getBodyItemCount() + mBottomCount;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof BodyViewHolder) {
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,30))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            ((BodyViewHolder) holder).imgsGooods.setLayoutParams(params);
            Glide.with(context)
                    .load(imgsUrl.get(position))
                    .centerCrop()
                    .into(((BodyViewHolder) holder).imgsGooods);
            ((BodyViewHolder) holder).imgsGooods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addPicClick!=null){
                        addPicClick.prePicClick(holder.getAdapterPosition());
                    }
                }
            });
        }else if (holder instanceof BottomViewHolder) {
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,30))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            ((BottomViewHolder) holder).imgsAdd.setLayoutParams(params);

            ((BottomViewHolder)holder).imgsAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addPicClick!=null){
                        addPicClick.addPicClick();
                    }
                }
            });
        }
    }

    public interface OnAddPicClick{
        void addPicClick();
        void prePicClick(int position);
    }

    private static class BodyViewHolder  extends RecyclerView.ViewHolder {
        private ImageView imgsGooods;

        BodyViewHolder(View view) {
            super(view);
            imgsGooods = view.findViewById(R.id.imgsAdd);
        }
    }

    //底部 ViewHolder
    private static class BottomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgsAdd;
        BottomViewHolder(View itemView) {
            super(itemView);
            imgsAdd = itemView.findViewById(R.id.imgsAdd);
        }
    }

    public List<String> getImgsUrl() {
        return imgsUrl;
    }

    public void setImgsUrl(List<String> imgsUrl) {
        this.imgsUrl = imgsUrl;
        notifyDataSetChanged();
    }

}
