package com.yunma.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GetShoppingListBean;
import com.yunma.utils.*;

/**
 * Created by Json on 2017/1/1.
 */
public class GoodsManifestAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private GetShoppingListBean resultBean;
    public GoodsManifestAdapter(Context mContext, GetShoppingListBean resultBean) {
        this.mContext = mContext;
        this.resultBean = resultBean;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return resultBean.getSuccess().size();
    }

    @Override
    public Object getItem(int position) {
        return resultBean.getSuccess().get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_goods_manifest, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        String s ="￥" +  ValueUtils.toTwoDecimal(
                resultBean.getSuccess().get(position).getYunmaprice());
        SpannableStringBuilder ss = ValueUtils.changeText(mContext,R.color.color_b4,s, Typeface.NORMAL,
                DensityUtils.sp2px(mContext,14),1,s.indexOf("."));
        holder.tvPrice.setText(ss);
        holder.tvGoodsName.setText(resultBean.getSuccess().get(position).getName());
        holder.tvGoodsInfo.setText("尺码：" + resultBean.getSuccess().get(position).getCartSize());
        holder.tvGoodsNum.setText("x " + resultBean.getSuccess().get(position).getCartNum());
        if(resultBean.getSuccess().get(position).getRepoid()==1){
            if(resultBean.getSuccess().get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.SElF_GOODS_IMAGE_URL+
                resultBean.getSuccess().get(position).getPic().split(",")[0]);
            }else{
                holder.imgGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
            }
        }else {
            if(resultBean.getSuccess().get(position).getPic()!=null){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.GOODS_IMAGE_URL +
                        resultBean.getSuccess().get(position).getPic());
            }else{
                holder.imgGoods.setImageDrawable(mContext.getResources().getDrawable(R.drawable.default_pic));
            }
        }
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvGoodsNum;
        TextView tvPrice;
        TextView tvGoodsInfo;

        ViewHolder(View view) {
            imgGoods = (ImageView) view.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
            tvGoodsNum = (TextView) view.findViewById(R.id.tvGoodsNum);
            tvPrice = (TextView) view.findViewById(R.id.tvPrice);
            tvGoodsInfo = (TextView) view.findViewById(R.id.tvGoodsInfo);
        }
    }
}
