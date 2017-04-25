package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class SaveOrderInterface {
    public interface SaveOrderModel{
        void saveOrder(Context mContext,SaveOrderBean saveOrderBean,
                       OnSaveOrderListener onSaveOrderListener);
    }
    public interface SaveOrderView{
        Context getContext();
        String getAddressId();
        SaveOrderBean getSaveOrderBean();
        void showSaveOrderInfos(SaveOrderResultBean resultBean,String msg);
    }
    public interface OnSaveOrderListener{
        void onListener(SaveOrderResultBean resultBean,String msg);
    }
}
