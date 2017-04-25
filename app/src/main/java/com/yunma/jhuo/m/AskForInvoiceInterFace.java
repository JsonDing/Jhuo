package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-03-29
 *
 * @author Json.
 */

public class AskForInvoiceInterFace {

    public interface AskForInvoiceModel{
        void askForInvoice(Context mContext,AskForInvoiceBean askForInvoiceBean,OnAskForInvoice onAskForInvoice);
    }

    public interface AskForInvoiceView{
        void showAskInfos(SuccessResultBean resultBean, String msg);
    }

    public interface OnAskForInvoice{
        void onAskListener(SuccessResultBean resultBean, String msg);
    }
}
