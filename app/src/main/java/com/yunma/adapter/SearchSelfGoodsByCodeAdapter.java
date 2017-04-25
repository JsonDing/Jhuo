package com.yunma.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.*;
import android.text.style.ForegroundColorSpan;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.GetSelfGoodsResultBean.SuccessBean.ListBean;
import com.yunma.utils.*;

import java.util.List;

/**
 * Created on 2017-03-11
 *
 * @author Json.
 */

public class SearchSelfGoodsByCodeAdapter extends BaseAdapter {
    private String nums;
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ListBean> listBean;
    public SearchSelfGoodsByCodeAdapter(Context mContext, List<ListBean> listBean, String nums) {
        this.nums = nums;
        this.mContext = mContext;
        this.listBean = listBean;
        this.mInflater = LayoutInflater.from(mContext);
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
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = mInflater.inflate(R.layout.item_code_list, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        if(listBean.get(position).getPic()!=null){
            GlideUtils.glidNormleFast(mContext,holder.imgsPre, ConUtils.SElF_GOODS_IMAGE_URL +
            listBean.get(position).getPic().split(",")[0]);
        }else{
            GlideUtils.glidLocalDrawable(mContext,holder.imgsPre,R.drawable.default_pic);
        }
        String str = listBean.get(position).getNumber();
        int fstart = str.indexOf(nums);
        int fend = fstart + nums.length();
        SpannableStringBuilder style = new SpannableStringBuilder(str);
        style.setSpan(new ForegroundColorSpan(Color.RED),fstart,fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
        holder.tvGoodsCode.setText(style);
        return view;
    }

    private class ViewHolder {
        TextView tvGoodsCode;
        ImageView imgsPre;
        ViewHolder(View view) {
            tvGoodsCode = (TextView) view.findViewById(R.id.tvGoodsCode);
            imgsPre = (ImageView)view.findViewById(R.id.imgsPre);
            imgsPre.setVisibility(View.VISIBLE);
        }
    }

    public List<ListBean> getListBean() {
        return listBean;
    }

    public void setListBean(List<ListBean> listBean) {
        this.listBean = listBean;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }


}
