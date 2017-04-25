package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created by Json on 2017/1/4.
 */
public class CompletedPayMentAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<GoodsInfoResultBean.SuccessBean.ListBean> listBean;
    public CompletedPayMentAdapter(Context context,
                                   List<GoodsInfoResultBean.SuccessBean.ListBean> listBean) {
        this.mContext = context;
        this.listBean = listBean;
        inflater = LayoutInflater.from(mContext);
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
    public View getView(int position, View convertView, ViewGroup parent) {
       ViewHolder holder ;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_intro, parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if(EmptyUtil.isNotEmpty(listBean.get(position).getPic())){
            GlideUtils.glidNormleFast(mContext,holder.imgGoods,ConUtils.GOODS_IMAGE_URL +
            listBean.get(position).getPic());
        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
        }
        holder.tvGoodsName.setText(listBean.get(position).getName());
        holder.tvGoodsPrice.setText(String.valueOf(listBean.get(position).getUserPrice()));
        if(EmptyUtil.isNotEmpty(listBean.get(position).getSales())){
            holder.tvSaleNums.setText(listBean.get(position).getSales());
        }else {
            holder.tvSaleNums.setText("0");
        }
        if((position+1)%2==0){
            holder.view1.setVisibility(View.VISIBLE);
            holder.view2.setVisibility(View.GONE);
        }else{
            holder.view2.setVisibility(View.VISIBLE);
            holder.view1.setVisibility(View.GONE);
        }

        return convertView;
    }

    private class ViewHolder{
        ImageView imgGoods;
        TextView tvGoodsName;
        TextView tvGoodsPrice;
        TextView tvSaleNums;
        View view1,view2,view3;

        ViewHolder(View view) {
            imgGoods = (ImageView) view.findViewById(R.id.imgGoods);
            tvGoodsName = (TextView) view.findViewById(R.id.tvGoodsName);
            tvGoodsPrice = (TextView) view.findViewById(R.id.tvGoodsPrice);
            tvSaleNums = (TextView) view.findViewById(R.id.tvSaleNums);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view3 = view.findViewById(R.id.view3);
            int width = (ScreenUtils.getScreenWidth(mContext)- DensityUtils.dp2px(mContext,40))/2;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }

}
