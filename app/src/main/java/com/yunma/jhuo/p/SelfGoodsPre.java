package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.GetSelfGoodsImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace.*;

/**
 * Created on 2017-02-13
 *
 * @author Json.
 */

public class SelfGoodsPre {
    private GetSelfGoodsModel mModel;
    private GetSelfGoodsView mView;

    public SelfGoodsPre(GetSelfGoodsView mView) {
        this.mView = mView;
        this.mModel = new GetSelfGoodsImpl();
    }

    public void getSpecialOfferGoods(Context mContext,String type,String size, int nextPage){
        mModel.getSpecialOfferGoods(mContext,type,size ,nextPage,
                new OnGetSelfGoodsListener() {
            @Override
            public void onGetGoodsListener(GetSelfGoodsResultBean t, String msg) {
                mView.showSpecialOfferGoods(t,msg);
            }
        });
    }
}
