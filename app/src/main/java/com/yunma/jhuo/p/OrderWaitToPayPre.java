package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.OrderUnPayResultBean;
import com.yunma.jhuo.i.OrderWaitToPayImpl;
import com.yunma.jhuo.m.OrderWaitToPayInterface.*;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class OrderWaitToPayPre {
    private OrderWaitToPayView mView;
    private OrderWaitToPayModel mModel;

    public OrderWaitToPayPre(OrderWaitToPayView mView) {
        this.mView = mView;
        this.mModel = new OrderWaitToPayImpl();
    }

    public void getUnPayOrders(Context mContext,String size, String page){
        mModel.orderUnPay(mContext,size,page, new OrderWaitToPayListener() {
            @Override
            public void orderWaitToPayListener(OrderUnPayResultBean resultBean, String msg) {
                mView.showOrderInfos(resultBean,msg);
            }
        });
    }
}
