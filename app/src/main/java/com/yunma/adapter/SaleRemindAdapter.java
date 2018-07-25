package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.jhuo.activity.mine.SaleRemindActivity;
import com.yunma.bean.GoodsRemindResultBean;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DensityUtils;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-20
 *
 * @author Json.
 */

public class SaleRemindAdapter extends RecyclerView.Adapter<SaleRemindAdapter.ViewHolder> {
    private Context mContext;
    private SaleRemindActivity mActivity;
    private Map<Integer, Boolean> map = new HashMap<>();
    private List<GoodsRemindResultBean.SuccessBean.NewRemindsBean> beanList;
    public SaleRemindAdapter(Context mContext,SaleRemindActivity mActivity) {
        this.mContext = mContext;
        this.mActivity = mActivity;
        this.beanList = new ArrayList<>();
        initMap();
    }

    //初始化map集合,默认为不选中
    private void initMap() {
        for (int i = 0; i < beanList.size(); i++) {
            map.put(i, false);
        }
    }

    @Override
    public int getItemCount() {
        return beanList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_sale_remind, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        GlideUtils.glidNormle(mContext,holder.imgsGoods,
                ConUtils.SElF_GOODS_IMAGE_URL + beanList.get(position).getGoodsPic().split(",")[0]);

        holder.tvGoodsName.setText(beanList.get(position).getGoodsName());
        holder.tvRemain.setText(String.valueOf(beanList.get(position).getNumber()));
        if(beanList.get(position).getNumber()==0){
            holder.tvTime.setTextColor(Color.parseColor("#a0a0a0"));
            holder.tvTime.setText("抢光啦");
        }else{
            holder.tvTime.setTextColor(Color.parseColor("#f4bd39"));
            holder.tvTime.setText("热卖中");
        }
        if (map.get(position) == null) {
            map.put(position, false);
        }
        holder.checkBox.setChecked(map.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                map.put(holder.getAdapterPosition(), isChecked);
                mActivity.test();
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.imgsGoods) ImageView imgsGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvTime) TextView tvTime;
        @BindView(R.id.textView) TextView textView;
        @BindView(R.id.tvRemain) TextView tvRemain;
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,5))/2;
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(width,width);
            imgsGoods.setLayoutParams(params);
        }
    }

    public void setNewRemindsBeanList(
            List<GoodsRemindResultBean.SuccessBean.NewRemindsBean> newRemindsBeanList) {
        this.beanList = newRemindsBeanList;
        initMap();
        notifyDataSetChanged();
    }

    public  List<String> getList() {
        List<String> list  = new ArrayList<>();
        for(int i=0;i<map.size();i++){
            if(map.get(i)){
                list.add(String.valueOf(beanList.get(i).getId()));
            }
        }
        return list;
    }
}
