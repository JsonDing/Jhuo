package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.WriteExpressResultBean;
import com.yunma.jhuo.i.WriteExpressModelImpl;
import com.yunma.jhuo.m.GoodsRefundInterface;

/**
 * Created by Json on 2017/3/20.
 */

public class WriteExpressPre {
    private GoodsRefundInterface.WriteExpressModel mModel;
    private  GoodsRefundInterface.WriteExpressView mView;

    public WriteExpressPre(GoodsRefundInterface.WriteExpressView mView) {
        this.mView = mView;
        this.mModel = new WriteExpressModelImpl();
    }

    public void toSubmitExpressInfo(Context mContext, String serviceId, String expressName,
                                    String expressCode){
        mModel.toWriteExpress(mContext, serviceId, expressName, expressCode, new GoodsRefundInterface.OnWriteExpressListener() {
            @Override
            public void onListener(WriteExpressResultBean resultBean,String msg) {
                mView.toShowExpressInfos(resultBean,msg);
            }
        });
    }
}
