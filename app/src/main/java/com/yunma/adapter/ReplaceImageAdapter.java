package com.yunma.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.utils.*;
import com.yunma.publish.MyItemTouchCallback.ItemTouchAdapter;

import java.util.*;

/**
 * Created on 2017-04-12
 *
 * @author Json.
 */

public class ReplaceImageAdapter  extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchAdapter{
    private static final int ITEM_TYPE_CONTENT = 1;
    private static final int ITEM_TYPE_BOTTOM = 2;
    private static final int mBottomCount = 1;//底部View个数
    private Context context;
    private List<String> imgsUrl;
    private LayoutInflater inflater;
    private ImagePickClick imagePickClick;
    public ReplaceImageAdapter(Context context, ImagePickClick imagePickClick) {
        this.context = context;
        this.imgsUrl = new ArrayList<>();
        this.imagePickClick = imagePickClick;
        inflater = LayoutInflater.from(context);
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
            GlideUtils.glidNormleFast(context,((BodyViewHolder) holder).imgsGooods,
                    ConUtils.SElF_GOODS_IMAGE_URL + imgsUrl.get(position));
            ((BodyViewHolder) holder).imgsGooods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imagePickClick!=null){
                        imagePickClick.addImageClick(position);
                    }
                }
            });
            ((BodyViewHolder) holder).imgsRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imagePickClick!=null){
                        imagePickClick.removeImageClick(position,imgsUrl);
                    }
                }
            });
        }else if (holder instanceof BottomViewHolder) {
            ((BottomViewHolder)holder).imgsAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(imagePickClick!=null){
                        imagePickClick.addImageClick(position);
                    }
                }
            });
        }
    }

    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition==imgsUrl.size() || toPosition==imgsUrl.size()){
            return;
        }
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(imgsUrl, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(imgsUrl, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        imgsUrl.remove(position);
        notifyItemRemoved(position);
    }

    public interface ImagePickClick{
        void addImageClick(int position);
        void removeImageClick(int position,List<String> imgsUrl);
    }

    private class BodyViewHolder  extends RecyclerView.ViewHolder {
        private ImageView imgsGooods;
        private ImageView imgsRemove;
        BodyViewHolder(View view) {
            super(view);
            imgsGooods = (ImageView) view.findViewById(R.id.imgsAdd);
            imgsRemove = (ImageView) view.findViewById(R.id.imgsRemove);
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,20))/3;
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
            int width = (ScreenUtils.getScreenWidth(context)- DensityUtils.dp2px(context,20))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            imgsAdd.setLayoutParams(params);
        }
    }

    public void setImgsUrl(List<String> imgsUrl) {
        this.imgsUrl = imgsUrl;
        notifyItemInserted(this.imgsUrl.size());
    }
}
