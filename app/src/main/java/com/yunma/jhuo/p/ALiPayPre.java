package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.ALiPayImpl;
import com.yunma.jhuo.m.PayInterface.ALiPayModel;
import com.yunma.jhuo.m.PayInterface.ALiPayView;
import com.yunma.jhuo.m.PayInterface.OnALiPayListener;

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

    public void getALiPayInfos(Context mContext,String orderId){
        mModel.getPayInfos(mContext, orderId, new OnALiPayListener() {
            @Override
            public void onListener(SuccessResultBean successResultBean, String msg) {
                mView.showALiPayInfos(successResultBean,msg);
            }
        });
    }
}
