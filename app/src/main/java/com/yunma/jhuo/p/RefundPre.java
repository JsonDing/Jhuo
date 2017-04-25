package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.DelRefundImpl;
import com.yunma.jhuo.m.GoodsRefundInterface;

/**
 * Created on 2017-03-14
 *
 * @author Json.
 */

public class RefundPre {
    private GoodsRefundInterface.DelRefundModel mModel;
    private GoodsRefundInterface.DelRefundView mView;

    public RefundPre(GoodsRefundInterface.DelRefundView mView) {
        this.mView = mView;
        this.mModel = new DelRefundImpl();
    }

    public void delRefund(Context mContext,String ids){
        mModel.delRefund(mContext, ids, new GoodsRefundInterface.OndelListener() {
            @Override
            public void onDelInfo(SuccessResultBean resultBean, String msg) {
                mView.showDelInfo(resultBean,msg);
            }
        });
    }

}
