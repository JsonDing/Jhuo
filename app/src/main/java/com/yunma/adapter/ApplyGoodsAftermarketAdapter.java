package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.OrderUnPayResultBean.SuccessBean.ListBean.OrderdetailsBean;
import com.yunma.utils.*;

import java.util.*;

import butterknife.*;

/**
 * Created on 2017-01-13
 *
 * @author Json.
 */
public class ApplyGoodsAftermarketAdapter extends BaseAdapter{

    private Context mContext;
    private LayoutInflater inflater;
    private List<OrderdetailsBean> orderdetailsBeanList;
    private List<Boolean> isCheck;
    public ApplyGoodsAftermarketAdapter(Context context, List<OrderdetailsBean> orderdetailsBeanList) {
        this.mContext = context;
        this.orderdetailsBeanList = orderdetailsBeanList;
        inflater = LayoutInflater.from(mContext);
        initCheck();
    }

    private void initCheck() {
        isCheck = new ArrayList<>();
        for(int i=0;i<orderdetailsBeanList.size();i++){
            isCheck.add(false);
        }
    }

    @Override
    public int getCount() {
        return orderdetailsBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderdetailsBeanList.get(position);
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
            view = inflater.inflate(R.layout.item_goods_aftermarket, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        holder.tvGoodsName.setText(orderdetailsBeanList.get(position).getInfo());
        holder.tvGoodsInfo.setText("颜色：如图 " + " 尺码：" + orderdetailsBeanList.get(position).getSize());
        holder.tvPrice.setText("￥" + orderdetailsBeanList.get(position).getUserprice());
        holder.tvGoodsNum.setText("x" + orderdetailsBeanList.get(position).getNum());
        if(orderdetailsBeanList.get(position).getRepoid() == 1){
            if(EmptyUtil.isNotEmpty(orderdetailsBeanList.get(position).getPic())){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.SElF_GOODS_IMAGE_URL +
                        orderdetailsBeanList.get(position).getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }else if(orderdetailsBeanList.get(position).getRepoid() == 2){
            if(EmptyUtil.isNotEmpty(orderdetailsBeanList.get(position).getPic())){
                GlideUtils.glidNormleFast(mContext,holder.imgGoods, ConUtils.GOODS_IMAGE_URL +
                        orderdetailsBeanList.get(position).getPic().split(",")[0]);
            }else{
                GlideUtils.glidLocalDrawable(mContext,holder.imgGoods,R.drawable.default_pic);
            }
        }
        holder.ckBtn.setChecked(isCheck.get(position));
        holder.ckBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheck.set(position,isChecked);
            }
        });

        return view;
    }

    class ViewHolder {
        @BindView(R.id.imgGoods) ImageView imgGoods;
        @BindView(R.id.tvGoodsName) TextView tvGoodsName;
        @BindView(R.id.tvGoodsInfo) TextView tvGoodsInfo;
        @BindView(R.id.tvPrice) TextView tvPrice;
        @BindView(R.id.tvGoodsNum) TextView tvGoodsNum;
        @BindView(R.id.ckBtn) CheckBox ckBtn;
        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    public List<OrderdetailsBean> getSelectGoods(){
        List<OrderdetailsBean>  detailsListBean = new ArrayList<>();
        for (int i=0;i<orderdetailsBeanList.size();i++){
            if(isCheck.get(i)){
                detailsListBean.add(orderdetailsBeanList.get(i));
            }
        }
        return detailsListBean;
    }
}
