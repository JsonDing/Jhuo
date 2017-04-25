package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created by Json on 2017/1/10.
 */

public class GoodsRefundInterface {

    public interface OnUnHandleClick{
        void onLookDetial(ServiceResultBean.SuccessBean.ListBean listBean);
        void onCancleRefund(int position,String ids);
    }

    public interface OnHandleClick{
        void onLookDetial(ServiceResultBean.SuccessBean.ListBean listBean);
        void onRefundStatus(ServiceResultBean.SuccessBean.ListBean listBean);
    }

    public interface OnCompleteClick{
        void onLookDetial(ServiceResultBean.SuccessBean.ListBean listBean);
        void onRefundStatus(ServiceResultBean.SuccessBean.ListBean listBean);
        void onLookMoney(ServiceResultBean.SuccessBean.ListBean listBean);
    }

    public interface OnRejeckClick{
        void onLookDetial(ServiceResultBean.SuccessBean.ListBean listBean);
        void onRejectReason();
    }

    public interface OnGoodsRefundProgressAdapterClick{
        void onLookDetialClickListener(ServiceResultBean.SuccessBean.ListBean listBean);
        void onWriteExpressClickListener(String serviceId);
    }

    public interface DelRefundModel{
        void delRefund(Context mContext,String id,OndelListener ondelListener);
    }

    public interface DelRefundView{
        void showDelInfo(SuccessResultBean resultBean, String msg);
    }

    public interface OndelListener{
        void onDelInfo(SuccessResultBean resultBean, String msg);
    }

    public interface WriteExpressModel{
        void toWriteExpress(Context mContext,String serviceId,String expressName,String expressCode,OnWriteExpressListener listener);
    }

    public interface WriteExpressView{
        void toShowExpressInfos(WriteExpressResultBean resultBean,String msg);
    }

    public interface OnWriteExpressListener{
        void onListener(WriteExpressResultBean resultBean,String msg);
    }
}
