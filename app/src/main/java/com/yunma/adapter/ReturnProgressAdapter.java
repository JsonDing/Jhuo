package com.yunma.adapter;

import android.content.Context;
import android.view.*;
import android.widget.*;

import com.yunma.R;
import com.yunma.bean.ServiceResultBean;
import com.yunma.jhuo.m.GoodsRefundInterface.OnGoodsRefundProgressAdapterClick;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 2017-01-11
 *
 * @author Json.
 */
public class ReturnProgressAdapter extends BaseAdapter{
    private LayoutInflater inflater;
    private ServiceResultBean.SuccessBean.ListBean listBean;
    private OnGoodsRefundProgressAdapterClick onClick;
    public ReturnProgressAdapter(Context mContext, ServiceResultBean.SuccessBean.ListBean listBean,
                                 OnGoodsRefundProgressAdapterClick onClick) {
        this.onClick = onClick;
        this.listBean = listBean;
        this.inflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        if(listBean.getRefund()==1){
            return 3;
        }else if(listBean.getRefund()==2){
            return 4;
        }else if(listBean.getRefund()==3){
            return 5;
        }else{
            return 1;
        }
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position == 1) {
            if(listBean.getRefund()==1||listBean.getRefund()==2||listBean.getRefund()==3){//同意
                return 1;
            }else if(listBean.getRefund()==-1){//拒绝
                return 2;
            }else{
                return -1;
            }
        } else if (position == 2) {
            return 3;
        } else if (position == 3) {
            return 4;
        } else{
            return 5;
        }

    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        ViewHolder3 holder3 = null;
        ViewHolder4 holder4 = null;
        ViewHolder5 holder5 = null;
        ViewHolder6 holder6 = null;
        int type = getItemViewType(position);
        if (view == null) {
            switch (type) {
                case 0:
                    view = inflater.inflate(R.layout.item_return_progress_1, parent, false);
                    holder1 = new ViewHolder1(view);
                    view.setTag(holder1);
                    break;
                case 1:
                    view = inflater.inflate(R.layout.item_return_progress_2, parent, false);
                    holder2 = new ViewHolder2(view);
                    view.setTag(holder2);
                    break;
                case 2:
                    view = inflater.inflate(R.layout.item_return_progress_3, parent, false);
                    holder3 = new ViewHolder3(view);
                    view.setTag(holder3);
                    break;
                case 3:
                    view = inflater.inflate(R.layout.item_return_progress_4, parent, false);
                    holder4 = new ViewHolder4(view);
                    view.setTag(holder4);
                    break;
                case 4:
                    view = inflater.inflate(R.layout.item_return_progress_5, parent, false);
                    holder5 = new ViewHolder5(view);
                    view.setTag(holder5);
                    break;
                case 5:
                    view = inflater.inflate(R.layout.item_return_progress_6, parent, false);
                    holder6 = new ViewHolder6(view);
                    view.setTag(holder6);
                    break;

            }
        } else {
            switch (type) {
                case 0:
                    holder1 = (ViewHolder1) view.getTag();
                    break;
                case 1:
                    holder2 = (ViewHolder2) view.getTag();
                    break;
                case 2:
                    holder3 = (ViewHolder3) view.getTag();
                    break;
                case 3:
                    holder4 = (ViewHolder4) view.getTag();
                    break;
                case 4:
                    holder5 = (ViewHolder5) view.getTag();
                    break;
                case 5:
                    holder6 = (ViewHolder6) view.getTag();
                    break;
            }
        }

        switch (type) {
            case 0:
                if (getCount() == 1) {
                    holder1.lineBottom1.setVisibility(View.INVISIBLE);
                } else {
                    holder1.lineBottom1.setVisibility(View.VISIBLE);
                }

                holder1.tvReturnType.setText("退货原因：" + listBean.getServiceDetails().get(0).getReason());
                holder1.tvReturnDescribe.setText("退货描述：" + listBean.getServiceDetails().get(0).getRemark());
                holder1.btnReturnDatial.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=onClick){
                            onClick.onLookDetialClickListener(listBean);
                        }
                    }
                });

                break;
            case 1:
                if (getCount() == 2) {
                    holder2.lineTop2.setVisibility(View.VISIBLE);
                    holder2.lineBottom2.setVisibility(View.INVISIBLE);
                } else {
                    holder2.lineTop2.setVisibility(View.VISIBLE);
                    holder2.lineBottom2.setVisibility(View.VISIBLE);
                }
                break;
            case 2:
                if (getCount() == 2) {
                    holder3.lineTop3.setVisibility(View.VISIBLE);
                    holder3.lineBottom3.setVisibility(View.INVISIBLE);
                } else {
                    holder3.lineTop3.setVisibility(View.VISIBLE);
                    holder3.lineBottom3.setVisibility(View.VISIBLE);
                }
                break;
            case 3:
                if (getCount() == 3) {
                    holder4.lineTop4.setVisibility(View.VISIBLE);
                    holder4.lineBottom4.setVisibility(View.INVISIBLE);
                } else {
                    holder4.lineTop4.setVisibility(View.VISIBLE);
                    holder4.lineBottom4.setVisibility(View.VISIBLE);
                }
                if(listBean.getRefund()==1){
                    holder4.btnWriteExpress.setVisibility(View.VISIBLE);
                }else{
                    holder4.btnWriteExpress.setVisibility(View.INVISIBLE);
                }
                holder4.btnWriteExpress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(null!=onClick){
                            onClick.onWriteExpressClickListener(listBean.getServiceDetails().get(0).getServiceId());
                        }
                    }
                });
                break;
            case 4:
                if (getCount() == 4) {
                    holder5.lineTop5.setVisibility(View.VISIBLE);
                    holder5.lineBottom5.setVisibility(View.INVISIBLE);
                } else {
                    holder5.lineTop5.setVisibility(View.VISIBLE);
                    holder5.lineBottom5.setVisibility(View.VISIBLE);
                }
                holder5.tvReturnExpress.setText("物流单号：" + listBean.getExpress()
                        + "  " + listBean.getExpressNumber());
                holder5.tvReturnTime.setText("申明：本公司将在收到退货，对退回物品进行验收无误后，" +
                        "3个工作日内，将退款原路返还您的账户，请注意查收.");
                break;
            case 5:
                holder6.lineBottom6.setVisibility(View.INVISIBLE);
                break;
        }

        return view;
    }

    class ViewHolder1 {
        @BindView(R.id.btnReturnDatial)
        Button btnReturnDatial;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom1)
        View lineBottom1;
        @BindView(R.id.tvWho)
        TextView tvWho;
        @BindView(R.id.tvReturnType)
        TextView tvReturnType;
        @BindView(R.id.tvReturnDescribe)
        TextView tvReturnDescribe;
        @BindView(R.id.tvReturnTime)
        TextView tvReturnTime;

        ViewHolder1(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolder2 {
        @BindView(R.id.lineTop2)
        View lineTop2;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom2)
        View lineBottom2;
        @BindView(R.id.tvWho)
        TextView tvWho;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvReturnTime)
        TextView tvReturnTime;

        ViewHolder2(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolder3 {
        @BindView(R.id.lineTop2)
        View lineTop3;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom2)
        View lineBottom3;
        @BindView(R.id.tvWho)
        TextView tvWho;
        @BindView(R.id.tvRejectReason)
        TextView tvRejectReason;
        @BindView(R.id.tvReturnTime)
        TextView tvReturnTime;

        ViewHolder3(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolder4 {
        @BindView(R.id.lineTop4)
        View lineTop4;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom4)
        View lineBottom4;
        @BindView(R.id.tvWho)
        TextView tvWho;
        @BindView(R.id.tvReturnTime)
        TextView tvReturnTime;
        @BindView(R.id.btnWriteExpress)
        Button btnWriteExpress;

        ViewHolder4(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolder5 {
        @BindView(R.id.lineTop5)
        View lineTop5;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom5)
        View lineBottom5;
        @BindView(R.id.tvWho)
        TextView tvWho;
        @BindView(R.id.tvReturnExpress)
        TextView tvReturnExpress;
        @BindView(R.id.tvReturnTime)
        TextView tvReturnTime;

        ViewHolder5(View view) {
            ButterKnife.bind(this, view);
        }
    }

    class ViewHolder6 {
        @BindView(R.id.lineTop6)
        View lineTop6;
        @BindView(R.id.circlePoint)
        ImageView circlePoint;
        @BindView(R.id.lineBottom6)
        View lineBottom6;
        @BindView(R.id.tvWho)
        TextView tvWho;
        @BindView(R.id.tvAddress)
        TextView tvAddress;
        @BindView(R.id.tvReturnTime)
        TextView tvReturnTime;

        ViewHolder6(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
