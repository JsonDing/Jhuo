package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.AdInfoResultBean;
import com.yunma.jhuo.i.GetAdsModelImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-04-19
 *
 * @author Json.
 */

public class AdsPre {
    private SelfGoodsInterFace.GetAdsView mView;
    private SelfGoodsInterFace.GetAdsModel mModel;

    public AdsPre(SelfGoodsInterFace.GetAdsView mView) {
        this.mView = mView;
        this.mModel = new GetAdsModelImpl();
    }

    public void getAdInfo(Context mContext){
        mModel.getAd(mContext, new SelfGoodsInterFace.OnGetAdListener() {
            @Override
            public void onGetAdListener(AdInfoResultBean resultBean, String msg) {
                mView.showAdsInfo(resultBean,msg);
            }
        });
    }
}
