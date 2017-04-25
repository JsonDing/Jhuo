package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.BannerResultBean;
import com.yunma.jhuo.i.GetBannerModelImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-04-19
 *
 * @author Json.
 */

public class BannerPre {
    private SelfGoodsInterFace.GetBannerModel mModel;
    private SelfGoodsInterFace.GetBannerView mView;

    public BannerPre(SelfGoodsInterFace.GetBannerView mView) {
        this.mView = mView;
        this.mModel = new GetBannerModelImpl();
    }

    public void getNewsBanner(Context mContext){

        mModel.getNewsBanner(mContext, new SelfGoodsInterFace.OnBananerListener() {
            @Override
            public void onNewsBannerListener(BannerResultBean t, String msg) {
                mView.showNewsBanner(t,msg);
            }
        });
    }
}
