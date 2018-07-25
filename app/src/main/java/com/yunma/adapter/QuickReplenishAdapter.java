package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.BuyRecordBean.SuccessBean.ListBean;
import com.yunma.utils.*;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-01-16
 *
 * @author Json.
 */
public class QuickReplenishAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<ListBean> listBean;
    private OnbuyClick onbuyClick;
    private Animation push_left_in,push_right_in;
    public QuickReplenishAdapter(Context context, List<ListBean> listBean,OnbuyClick onbuyClick) {
        this.mContext = context;
        this.listBean = listBean;
        this.onbuyClick = onbuyClick;
        inflater = LayoutInflater.from(mContext);
        push_left_in= AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
        push_right_in=AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
    }

    @Override
    public int getCount() {
        return listBean.size();
    }

    @Override
    public Object getItem(int position) {
        return listBean.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_quick_replenish, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if (position % 2 == 0) {
            push_left_in.setDuration(500);
            view.setAnimation(push_left_in);
        } else {
            push_right_in.setDuration(500);
            view.setAnimation(push_right_in);
        }
        if(listBean.get(position).getRepoid()==1){
            if(listBean.get(position).getPic()!=null){
                GlideUtils.glidNormle(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                listBean.get(position).getPic().split(",")[0] + "/min");
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }else{
            if (listBean.get(position).getPic()!=null){
                GlideUtils.glidNormle(mContext,holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        listBean.get(position).getPic().split(",")[0] + "/min");
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }
        holder.tvGoodsName.setText(listBean.get(position).getInfo());
        holder.tvGoodsSize.setText(String.valueOf("尺码：" + listBean.get(position).getSize()));
        holder.tvGoodsPrice.setText(String.valueOf("￥" + listBean.get(position).getUserprice()));
        if (onbuyClick != null) {
            holder.btnQuickReplenish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onbuyClick.onBuyClick(listBean.get(position).getGid());
                }
            });
            holder.viewItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onbuyClick.onLookGoodsDetial(listBean.get(position).getGid());
                }
            });
        }
        return view;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.imgGoods)
        ImageView imgGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.textview)
        TextView textview;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.btnQuickReplenish)
        Button btnQuickReplenish;
        @BindView(R.id.tvGoodsSize)
        TextView tvGoodsSize;
        private View viewItem;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
            this.viewItem = view;
        }
    }

    public interface OnbuyClick{
        void onBuyClick(String gId);
        void onLookGoodsDetial(String gId);
    }
    public void setListBean(List<ListBean> listBean) {
        this.listBean = listBean;
        notifyDataSetChanged();
    }
}
