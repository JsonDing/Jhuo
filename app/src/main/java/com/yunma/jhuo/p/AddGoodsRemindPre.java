package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.AddGoodsRemindImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-04-19
 *
 * @author Json.
 */

public class AddGoodsRemindPre {
    private SelfGoodsInterFace.AddSelfGoodsRemindModel mModel;
    private SelfGoodsInterFace.AddSelfGoodsRemindView mView;

    public AddGoodsRemindPre(SelfGoodsInterFace.AddSelfGoodsRemindView mView) {
        this.mView = mView;
        this.mModel = new AddGoodsRemindImpl();
    }

    public void addGoodsRemind(Context mContext, String goodsNumbers){
        mModel.addSelfGoodsRemind(mContext, goodsNumbers,
                new SelfGoodsInterFace.AddSelfGoodsRemindListener() {
            @Override
            public void addSelfGoodsRemindListener(SuccessResultBean resultBean, String msg) {
                mView.showSelfGoodsRemindInfos(resultBean,msg);
            }
        });
    }
}
