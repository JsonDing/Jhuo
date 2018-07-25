package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.jhuo.i.OrderWaitToReceiveImpl;
import com.yunma.jhuo.m.OrderWaitToReceiveInterface.*;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToReceivePre {
    private OrderWaitToReceiveView mView;
    private OrderWaitToReceiveModel mModel;

    public OrderWaitToReceivePre(OrderWaitToReceiveView mView) {
        this.mView = mView;
        this.mModel = new OrderWaitToReceiveImpl();
    }

    public void getUnReceiveOrders(Context context,String nums,String page){
        mModel.orderUnReceive(context, nums,page,new OrderWaitToReceiveListener() {
            @Override
            public void orderWaitToReceiveListener(OrderUnPayResultBean resultBean, String msg) {
                mView.showOrderInfos(resultBean,msg);
            }
        });
    }
}
