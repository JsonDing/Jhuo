package com.yunma.jhuo.p;

import com.yunma.bean.*;
import com.yunma.jhuo.i.SaveOrderImpl;
import com.yunma.jhuo.m.SaveOrderInterface.*;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class SaveOrderPre {
    private SaveOrderModel mModel;
    private SaveOrderView mView;

    public SaveOrderPre(SaveOrderView mView) {
        this.mView = mView;
        this.mModel = new SaveOrderImpl();
    }

    public void toSaveOrder(){
        mModel.saveOrder(mView.getContext(), mView.getSaveOrderBean(), new OnSaveOrderListener() {
            @Override
            public void onListener(SaveOrderResultBean resultBean, String msg) {
                mView.showSaveOrderInfos(resultBean,msg);
            }
        });
    }
}
