package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-02-27
 *
 * @author Json.
 */

public class ALiPayInterface {
    public interface ALiPayModel{
        void getPayInfos(Context mContext,String orderId,OnALiPayListener onALiPayListener);
    }
    public interface ALiPayView{
        Context getContext();
        String getOrderId();
        void showPayInfos(SuccessResultBean successResultBean, String msg);
    }
    public interface OnALiPayListener{
        void onListener(SuccessResultBean successResultBean,String msg);
    }
}
