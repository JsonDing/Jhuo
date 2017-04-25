package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.GetSelfGoodsResultBean;
import com.yunma.jhuo.i.GetSelfGoodsImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-04-17
 *
 * @author Json.
 */

public class SelfIssueGoodsPre {
    private SelfGoodsInterFace.GetSelfGoodsModel mModel;
    private SelfGoodsInterFace.GetIssueView mView;

    public SelfIssueGoodsPre(SelfGoodsInterFace.GetIssueView mView) {
        this.mView = mView;
        this.mModel = new GetSelfGoodsImpl();
    }

    public void getSpecialOfferGoods(Context mContext, String type, String size, int nextPage){
        mModel.getSpecialOfferGoods(mContext,type,size ,nextPage,
                new SelfGoodsInterFace.OnGetSelfGoodsListener() {
                    @Override
                    public void onGetGoodsListener(GetSelfGoodsResultBean t, String msg) {
                        mView.showIssueGoods(t,msg);
                    }
                });
    }
}
