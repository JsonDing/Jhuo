package com.yunma.adapter;

import android.app.Activity;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.utils.*;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-04-18
 *
 * @author Json.
 */

public class SearchNullRecommendAdapter extends BaseAdapter {
    private Activity mActivity;
    private LayoutInflater inflater;
    private List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen = null;

    public SearchNullRecommendAdapter(Activity mActivity) {
        this.mActivity = mActivity;
        this.goodsListBeen = new ArrayList<>();
        this.inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return goodsListBeen.size();
    }

    @Override
    public Object getItem(int position) {
        return goodsListBeen.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search_intro, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GlideUtils.glidNormle(mActivity,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
        goodsListBeen.get(position).getPic().split(",")[0]);
        holder.tvGoodsName.setText(goodsListBeen.get(position).getName());
        holder.tvGoodsPrice.setText(String.valueOf("￥" + ValueUtils.toTwoDecimal(
                goodsListBeen.get(position).getYmprice())));
        holder.tvSaleNums.setText(String.valueOf("浏览：" + 120));
        return convertView;
    }


    public void setGoodsListBeen(List<GetSelfGoodsResultBean.SuccessBean.ListBean> goodsListBeen) {
        this.goodsListBeen = goodsListBeen;
        notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.imgGoods)
        ImageView imgGoods;
        @BindView(R.id.tvGoodsName)
        TextView tvGoodsName;
        @BindView(R.id.tvGoodsPrice)
        TextView tvGoodsPrice;
        @BindView(R.id.tvSaleNums)
        TextView tvSaleNums;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
              int width = (ScreenUtils.getScreenWidth(mActivity)- DensityUtils.dp2px(mActivity,25))/2;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,width);
            imgGoods.setLayoutParams(params);
        }
    }
}
