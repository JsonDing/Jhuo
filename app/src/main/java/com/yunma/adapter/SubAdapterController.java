package com.yunma.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yunma.R;
import com.yunma.bean.SelfGoodsListBean;
import com.yunma.jhuo.activity.homepage.GoodsDetialsActivity;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ScreenUtils;

/**
 * Created by santa on 16/6/27.
 */
public class SubAdapterController {
    private Context mContext;
    private SubAdapter mAdapter;
    private String[] items;
    private SelfGoodsListBean listBean;
    SubAdapterController(String[] items, Context mContext,
                         SelfGoodsListBean listBean) {
        this.mContext = mContext;
        this.items = items;
        this.listBean = listBean;
    }

    public SubAdapter getAdapter(){
        if (items.length!=0) {
            if(null == mAdapter){
                mAdapter = new SubAdapter(mContext);
            }
            return mAdapter;
        } else {
            return null;
        }

    }

    class SubAdapter extends RecyclerView.Adapter<SubAdapter.SubViewHolder> {
        private LayoutInflater mInflater;
        public SubAdapter(Context context) {
            mInflater = LayoutInflater.from(context);

        }
        @Override
        public SubViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.subitem, parent, false);
            return new SubViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SubViewHolder holder, final int position) {
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/3;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            holder.image.setLayoutParams(params);
            String url = items[position];
            final String picUrl;
            if(listBean.getRepoid()==1){
                picUrl = ConUtils.SElF_GOODS_IMAGE_URL;
            }else{
                picUrl = ConUtils.GOODS_IMAGE_URL;
            }
            GlideUtils.glidNormle(mContext,holder.image, picUrl + url + "/min");
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, GoodsDetialsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("isToEnd","no");
                    bundle.putInt("goodId", listBean.getId());
                    bundle.putSerializable("goodsDetials", listBean);
                    intent.putExtras(bundle);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.length;
        }

        class SubViewHolder extends RecyclerView.ViewHolder {

            public ImageView image;

            SubViewHolder(View view) {
                super(view);
                image = view.findViewById(R.id.image);
               /* view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showSuccess(mContext,"click");
                        //启动一个ACTIVITY
                    }
                });*/

            }
        }
    }
}
