package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.IssueBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.ShelveSelfGoodsModelImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created by Json on 2017/4/10.
 */

public class ShelveSelfGoodsPre {
    private SelfGoodsInterFace.ShelveSelfGoodsModel mModel;
    private SelfGoodsInterFace.ShelveSelfGoodsView mView;

    public ShelveSelfGoodsPre(SelfGoodsInterFace.ShelveSelfGoodsView mView) {
        this.mView = mView;
        this.mModel = new ShelveSelfGoodsModelImpl();
    }

    public void shelveGoods(Context mContext,IssueBean issueBean){
        mModel.shelveSelfGoods(mContext, issueBean, new SelfGoodsInterFace.ShelveSelfGoodsListener() {
            @Override
            public void onShelveListener(SuccessResultBean resultBean, String msg) {
                mView.shelveSelfGoods(resultBean,msg);
            }
        });
    }
}
