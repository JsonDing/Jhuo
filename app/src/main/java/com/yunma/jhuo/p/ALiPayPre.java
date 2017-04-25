package com.yunma.jhuo.p;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.ALiPayImpl;
import com.yunma.jhuo.m.ALiPayInterface.*;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class ALiPayPre {
    private ALiPayModel mModel;
    private ALiPayView mView;

    public ALiPayPre(ALiPayView mView) {
        this.mView = mView;
        this.mModel = new ALiPayImpl();
    }

    public void getALiPayInfos(){
        mModel.getPayInfos(mView.getContext(), mView.getOrderId(), new OnALiPayListener() {
            @Override
            public void onListener(SuccessResultBean successResultBean, String msg) {
                mView.showPayInfos(successResultBean,msg);
            }
        });
    }
}
