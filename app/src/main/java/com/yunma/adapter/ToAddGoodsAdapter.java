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
 * Created by Json on 2017/3/6.
 */
public class ToAddGoodsAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private int mBottomCount=1;//底部View个数
    private Context context;
    private List<String> imgsUrl;
    private OnAddPicClick addPicClick;
    private LayoutInflater inflater;
    public ToAddGoodsAdapter(Context context, List<String> imgsUrl, OnAddPicClick addPicClick) {
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
        if (position < 11 && position >=  dataItemCount) {
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
            return  new BodyViewHolder(inflater.inflate(R.layout.item_select_goods, parent, false));
        } else if (viewType == ITEM_TYPE_BOTTOM) {
            return new BottomViewHolder(inflater.inflate(R.layout.item_select_add, parent, false));
        }else{
            return null;
        }
    }

    @Override
    public int getItemCount() {
      return getBodyItemCount() + mBottomCount;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof BodyViewHolder) {
            Glide.with(context)
                    .load(imgsUrl.get(position))
                    .centerCrop()
                    .into(((BodyViewHolder) holder).imgsGooods);
            ((BodyViewHolder) holder).imgsGooods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addPicClick!=null){
                        addPicClick.prePicClick(position);
                    }
                }
            });
            ((BodyViewHolder) holder).imgsRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(addPicClick!=null){
                        addPicClick.removePicClick(position);
                    }
                }
            });
        }else if (holder instanceof BottomViewHolder) {
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
        void removePicClick(int position);
        void prePicClick(int position);
    }

    private class BodyViewHolder  extends RecyclerView.ViewHolder {
        private ImageView imgsGooods;
        private ImageView imgsRemove;
        BodyViewHolder(View view) {
            super(view);
            imgsGooods = (ImageView) view.findViewById(R.id.imgsAdd);
            imgsRemove = (ImageView) view.findViewById(R.id.imgsRemove);
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,35))/4;
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width,width);
            imgsGooods.setLayoutParams(params);
        }
    }

    //底部 ViewHolder
    private class BottomViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgsAdd;
        BottomViewHolder(View itemView) {
            super(itemView);
            imgsAdd = (ImageView) itemView.findViewById(R.id.imgsAdd);
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,35))/4;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            imgsAdd.setLayoutParams(params);
        }
    }

    public List<String> getImgsUrl() {
        return imgsUrl;
    }

    public void setImgsUrl(List<String> imgsUrl) {
        this.imgsUrl = imgsUrl;
    }
}
