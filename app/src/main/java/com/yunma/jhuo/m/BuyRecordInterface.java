package com.yunma.jhuo.m;

import android.content.Context;

import com.yunma.bean.BuyRecordBean;

/**
 * Created on 2017-03-21
 *
 * @author Json.
 */

public class BuyRecordInterface {
    public interface BuyRecordModel{
        void buyRecord(Context mContext,String page,String size,OnBuyListener onBuyListener);
    }

    public interface BuyRecordView{
        void toShowBuyRecord(BuyRecordBean buyRecordBean,String msg);
    }

    public interface OnBuyListener{
        void onBuyListener(BuyRecordBean buyRecordBean, String msg);
    }
}
