package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.OrderUnPayResultBean;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToReceiveInterface {

    public interface OrderWaitToReceiveModel {
        void orderUnReceive(Context mContext, OrderWaitToReceiveListener orderWaitToReceiveListener);
    }

    public interface OrderWaitToReceiveView{
        Context getContext();
        void showOrderInfos(OrderUnPayResultBean resultBean, String msg);
    }

    public interface OrderWaitToReceiveListener{
        void orderWaitToReceiveListener(OrderUnPayResultBean resultBean, String msg);
    }

}
