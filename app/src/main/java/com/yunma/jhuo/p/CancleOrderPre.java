package com.yunma.jhuo.p;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.CancleOrderImpl;
import com.yunma.jhuo.m.CancleOrderInterface;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class CancleOrderPre {
    private CancleOrderInterface.CancleOrderModel mModel;
    private CancleOrderInterface.CancleOrderView mView;

    public CancleOrderPre(CancleOrderInterface.CancleOrderView mView) {
        this.mView = mView;
        this.mModel = new CancleOrderImpl();
    }

    public void cancleOrder(String id){
        mModel.cancleOrder(mView.getContext(), id, new CancleOrderInterface.CancleOrderListener() {
            @Override
            public void cancleOrderListener(SuccessResultBean resultBean, String msg) {
                mView.showCancleInfos(resultBean,msg);
            }
        });
    }
}
