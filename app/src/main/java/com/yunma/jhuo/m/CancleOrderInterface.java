package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.*;

/**
 * Created on 2017-03-03
 *
 * @author Json.
 */

public class CancleOrderInterface {
    public interface CancleOrderModel {
        void cancleOrder(Context mContext,String id, CancleOrderListener cancleOrderListener);
    }

    public interface CancleOrderView{
        void showCancleInfos(SuccessResultBean resultBean, String msg);
    }

    public interface CancleOrderListener{
        void cancleOrderListener(SuccessResultBean resultBean, String msg);
    }
}
