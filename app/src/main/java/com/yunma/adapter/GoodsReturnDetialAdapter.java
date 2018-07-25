package com.yunma.adapter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunma.R;
import com.yunma.bean.LocalImagePathBean;
import com.yunma.bean.PathBean;
import com.yunma.bean.ServiceResultBean.SuccessBean.ListBean.ServiceDetailsBean;
import com.yunma.jhuo.activity.PicViewerActivity;
import com.yunma.utils.ConUtils;
import com.yunma.utils.DateTimeUtils;
import com.yunma.utils.EmptyUtil;
import com.yunma.utils.GlideUtils;
import com.yunma.utils.ValueUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017-03-15
 *
 * @author Json.
 */

public class GoodsReturnDetialAdapter extends RecyclerView.Adapter<GoodsReturnDetialAdapter.ViewHolder>{

    private Context mContext;
    private long date;
    private int lastAnimatedPosition=-1;
    private boolean animationsLocked = false;
    private boolean delayEnterAnimation = true;
    private LayoutInflater mInflater;
    private List<ServiceDetailsBean> serviceDetails;
    public GoodsReturnDetialAdapter(Context mContext, List<ServiceDetailsBean> serviceDetails,long date) {
        this.serviceDetails = serviceDetails;
        this.mContext = mContext;
        this.date = date;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_refund_detials, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return serviceDetails.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        runEnterAnimation(holder.itemView,position);
        holder.tvType.setText(serviceDetails.get(position).getReason());
        holder.tvReturnMoney.setText("￥" + ValueUtils.toTwoDecimal(
                serviceDetails.get(position).getRefundMoney()));
        holder.tvRefundCode.setText(serviceDetails.get(position).getServiceId());
        holder.tvApplyTime.setText(DateTimeUtils.getTime(date,DateTimeUtils.DEFAULT_DATE_FORMAT));
        if(EmptyUtil.isEmpty(serviceDetails.get(position).getRemark())){
            holder.tvReason.setText("");
        }else{
            holder.tvReason.setText(serviceDetails.get(position).getRemark());
        }
        if(serviceDetails.get(position).getOrderdetail().getRepoid()==1){
            GlideUtils.glidNormleFast(mContext,holder.imgsGoods,ConUtils.SElF_GOODS_IMAGE_URL +
                    serviceDetails.get(position).getOrderdetail().getPic().split(",")[0]);
        }else{
            GlideUtils.glidNormleFast(mContext,holder.imgsGoods,ConUtils.GOODS_IMAGE_URL +
                    serviceDetails.get(position).getOrderdetail().getPic().split(",")[0]);
        }

        if(EmptyUtil.isNotEmpty(serviceDetails.get(position).getPic())){
            String s[] = serviceDetails.get(position).getPic().split(",");
            if(s.length==1){
                holder.imageV1.setVisibility(View.VISIBLE);
                holder.imageV2.setVisibility(View.INVISIBLE);
                holder.imageV3.setVisibility(View.INVISIBLE);
                GlideUtils.glidNormleFast(mContext,holder.imageV1,ConUtils.SERVICE_IMAGE_URL + s[0]);
            }else if (s.length==2){
                holder.imageV1.setVisibility(View.VISIBLE);
                holder.imageV2.setVisibility(View.VISIBLE);
                holder.imageV3.setVisibility(View.INVISIBLE);
                GlideUtils.glidNormleFast(mContext,holder.imageV1,ConUtils.SERVICE_IMAGE_URL + s[0]);
                GlideUtils.glidNormleFast(mContext,holder.imageV2,ConUtils.SERVICE_IMAGE_URL + s[1]);
            }else if(s.length==3){
                holder.imageV1.setVisibility(View.VISIBLE);
                holder.imageV2.setVisibility(View.VISIBLE);
                holder.imageV3.setVisibility(View.VISIBLE);
                GlideUtils.glidNormleFast(mContext,holder.imageV1,ConUtils.SERVICE_IMAGE_URL + s[0]);
                GlideUtils.glidNormleFast(mContext,holder.imageV2,ConUtils.SERVICE_IMAGE_URL + s[1]);
                GlideUtils.glidNormleFast(mContext,holder.imageV3,ConUtils.SERVICE_IMAGE_URL + s[2]);
            }else{
                holder.imageV1.setVisibility(View.INVISIBLE);
                holder.imageV2.setVisibility(View.INVISIBLE);
                holder.imageV3.setVisibility(View.INVISIBLE);
                GlideUtils.glidLocalDrawable(mContext,holder.imageV1,R.drawable.default_pic);
            }
            final LocalImagePathBean preBean = new LocalImagePathBean();
            List<PathBean> pathBeenList = new ArrayList<>();
            for (int i=0;i<s.length;i++){
                PathBean pathBean = new PathBean();
                pathBean.setImgsPath(ConUtils.SERVICE_IMAGE_URL + s[i]);
                pathBeenList.add(pathBean);
            }
            preBean.setPathBeen(pathBeenList);
                holder.imageV1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,PicViewerActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("path",preBean);
                        bundle.putInt("pos", 0);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                    }
                });

        }else{
            holder.imageV1.setVisibility(View.INVISIBLE);
            holder.imageV2.setVisibility(View.INVISIBLE);
            holder.imageV3.setVisibility(View.INVISIBLE);
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvType,tvReturnMoney,tvReason,tvRefundCode,tvApplyTime;
        ImageView imageV1,imageV2,imageV3,imgsGoods;

        public ViewHolder(View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tvType);
            tvReturnMoney = itemView.findViewById(R.id.tvReturnMoney);
            tvReason = itemView.findViewById(R.id.tvReason);
            tvRefundCode = itemView.findViewById(R.id.tvRefundCode);
            tvApplyTime = itemView.findViewById(R.id.tvApplyTime);
            imageV1 = itemView.findViewById(R.id.imageV1);
            imageV2 = itemView.findViewById(R.id.imageV2);
            imageV3 = itemView.findViewById(R.id.imageV3);
            imgsGoods = itemView.findViewById(R.id.imgsGoods);
        }
    }

    private void runEnterAnimation(View view, int position) {

        if (animationsLocked) return;//animationsLocked是布尔类型变量，一开始为false，确保仅屏幕一开始能够显示的item项才开启动画

        if (position > lastAnimatedPosition) {//lastAnimatedPosition是int类型变量，一开始为-1，这两行代码确保了recycleview滚动式回收利用视图时不会出现不连续的效果
            lastAnimatedPosition = position;
            view.setTranslationY(600);//相对于原始位置下方400
            view.setAlpha(0.f);//完全透明
            //每个item项两个动画，从透明到不透明，从下方移动到原来的位置
            //并且根据item的位置设置延迟的时间，达到一个接着一个的效果
            view.animate()
                    .translationY(0).alpha(1.f)
                    .setStartDelay(delayEnterAnimation ? 20 * (position) : 0)//根据item的位置设置延迟时间，达到依次动画一个接一个进行的效果
                    .setInterpolator(new DecelerateInterpolator(0.5f))//设置动画效果为在动画开始的地方快然后慢
                    .setDuration(700)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animationsLocked = true;//确保仅屏幕一开始能够显示的item项才开启动画，也就是说屏幕下方还没有显示的item项滑动时是没有动画效果
                        }
                    })
                    .start();
        }
    }
}
