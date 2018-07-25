package com.yunma.jhuo.m;

import com.yunma.bean.OrderUnPayResultBean.SuccessBean;

/**
 * Created on 2017-01-12
 *
 * @author Json.
 */

public class OrderHadConfirmInterFace {

    public interface OnAdapterClick{
        void onDeleteClickListener(int position,String id);
        void onApplyClickListener(int position,
                                  SuccessBean.ListBean orderdetails,int totalNums);
        void onTraceClickListener(int orderId ,String code,String name,String number);
        void onGetTicketListener(int position, SuccessBean.ListBean orderdetails);
    }
}
