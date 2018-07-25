package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.OrderUnPayResultBean;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToSendInterface {

    public interface OrderWaitToSendModel {
        void orderUnSend(Context mContext,String nums,String page, OrderWaitToSendListener orderWaitToPayListener);
    }

    public interface OrderWaitToSendView{
        void showOrderInfos(OrderUnPayResultBean resultBean, String msg);
    }

    public interface OrderWaitToSendListener{
        void orderWaitToSendListener(OrderUnPayResultBean resultBean, String msg);
    }
}
