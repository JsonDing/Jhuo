package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.view.animation.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.ExpressTracesBean;

import java.util.List;

import butterknife.*;

/**
 * Created on 2017-01-12
 *
 * @author Json.
 */
public class ExpressTraceRecordAdapter extends BaseAdapter {
   // 物流状态:0-无轨迹,1-已揽收, 2-在途中 201-到达派件城市，3-签收,4-问题件
    private Context mContext;
    private LayoutInflater inflater;
    private Animation push_left_in,push_right_in;
    private ExpressTracesBean.SuccessBean success;
    private List<ExpressTracesBean.SuccessBean.TracesBean> listBean;
    public ExpressTraceRecordAdapter(Context mContext, List<ExpressTracesBean.SuccessBean.TracesBean> listBean,
                                     ExpressTracesBean.SuccessBean success) {
        this.mContext = mContext;
        this.listBean = listBean;
        this.success = success;
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
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder;
        if (view != null) {
            holder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_express_record, parent, false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }
        if (position % 2 == 0) {
            push_left_in.setDuration(800);
            view.setAnimation(push_left_in);
        } else {
            push_right_in.setDuration(800);
            view.setAnimation(push_right_in);
        }
        holder.tvExpressStatus.setText(listBean.get(position).getAcceptStation());
        holder.tvFeedBackTime.setText(listBean.get(position).getAcceptTime());
        if(success.getState().equals("0")){
            holder.lineTop.setVisibility(View.INVISIBLE);
            holder.lineBottom.setVisibility(View.INVISIBLE);
        }else{
            if(position==0 && success.getState().equals("3")){
                holder.lineTop.setVisibility(View.INVISIBLE);
                holder.lineBottom.setVisibility(View.VISIBLE);
                holder.circlePoint.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.shape_yellow));
            }else if(position==0){
                holder.lineTop.setVisibility(View.INVISIBLE);
                holder.lineBottom.setVisibility(View.VISIBLE);
                holder.circlePoint.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.circle_gray));
            }else if(position == listBean.size()-1&&listBean.size()>1){
                holder.lineTop.setVisibility(View.VISIBLE);
                holder.lineBottom.setVisibility(View.INVISIBLE);
                holder.circlePoint.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.circle_gray));
            }else {
                holder.lineTop.setVisibility(View.VISIBLE);
                holder.lineBottom.setVisibility(View.VISIBLE);
                holder.circlePoint.setImageDrawable(
                        mContext.getResources().getDrawable(R.drawable.circle_gray));
            }

        }

        return view;
    }

    static class ViewHolder {
        @BindView(R.id.lineTop)
        View lineTop;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom)
        View lineBottom;
        @BindView(R.id.tvExpressStatus)
        TextView tvExpressStatus;
        @BindView(R.id.tvFeedBackTime)
        TextView tvFeedBackTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
