package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.BuyRecordBean;
import com.yunma.jhuo.i.BuyRecordImpl;
import com.yunma.jhuo.m.BuyRecordInterface;

/**
 * Created on 2017-03-21
 *
 * @author Json.
 */

public class BuyRecordPre {
    private BuyRecordInterface.BuyRecordModel mModel;
    private BuyRecordInterface.BuyRecordView mView;

    public BuyRecordPre(BuyRecordInterface.BuyRecordView mView) {
        this.mView = mView;
        this.mModel = new BuyRecordImpl();
    }

    public void getBuyRecord(Context mContext, String page, String size){
        mModel.buyRecord(mContext, page, size, new BuyRecordInterface.OnBuyListener() {
            @Override
            public void onBuyListener(BuyRecordBean buyRecordBean, String msg) {
                mView.toShowBuyRecord(buyRecordBean,msg);
            }
        });
    }
}
