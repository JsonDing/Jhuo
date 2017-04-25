package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.OrderUnPayResultBean;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToPayInterface {
    public interface OrderWaitToPayModel {
        void orderUnPay(Context mContext,String size,String page, OrderWaitToPayListener orderWaitToPayListener);
    }

    public interface OrderWaitToPayView{
        void showOrderInfos(OrderUnPayResultBean resultBean, String msg);
    }

    public interface OrderWaitToPayListener{
        void orderWaitToPayListener(OrderUnPayResultBean resultBean, String msg);
    }

    public interface CheckOrderModel {
        void checkOrder(Context mContext,String size,String page, CheckOrderListener orderListener);
    }

    public interface CheckOrderView{
        void checkOrderInfos(OrderUnPayResultBean resultBean, String msg);
    }

    public interface CheckOrderListener{
        void checkOrderListener(OrderUnPayResultBean resultBean, String msg);
    }
}
