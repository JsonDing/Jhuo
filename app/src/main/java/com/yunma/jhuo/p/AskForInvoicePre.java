package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.AskForInvoiceImpl;
import com.yunma.jhuo.m.AskForInvoiceInterFace;

/**
 * Created on 2017-03-29
 *
 * @author Json.
 */

public class AskForInvoicePre {
    private AskForInvoiceInterFace.AskForInvoiceModel mModel;
    private AskForInvoiceInterFace.AskForInvoiceView mView;

    public AskForInvoicePre(AskForInvoiceInterFace.AskForInvoiceView mView) {
        this.mView = mView;
        this.mModel = new AskForInvoiceImpl();
    }

    public void askForInvoice(Context mContext, AskForInvoiceBean askForInvoiceBean){
        mModel.askForInvoice(mContext, askForInvoiceBean, new AskForInvoiceInterFace.OnAskForInvoice() {
            @Override
            public void onAskListener(SuccessResultBean resultBean, String msg) {
                mView.showAskInfos(resultBean,msg);
            }
        });
    }
}
