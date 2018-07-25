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

}
