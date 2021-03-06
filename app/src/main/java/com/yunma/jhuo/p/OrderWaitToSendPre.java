package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.jhuo.i.OrderWaitToSendImpl;
import com.yunma.jhuo.m.OrderWaitToSendInterface.*;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToSendPre {
    private OrderWaitToSendView mView;
    private OrderWaitToSendModel mModel;

    public OrderWaitToSendPre(OrderWaitToSendView mView) {
        this.mView = mView;
        this.mModel = new OrderWaitToSendImpl();
    }

    public void getUnSendOrders(Context context,String nums, String page){
        mModel.orderUnSend(context,nums,page, new OrderWaitToSendListener() {
            @Override
            public void orderWaitToSendListener(OrderUnPayResultBean resultBean, String msg) {
                mView.showOrderInfos(resultBean,msg);
            }
        });
    }
}
