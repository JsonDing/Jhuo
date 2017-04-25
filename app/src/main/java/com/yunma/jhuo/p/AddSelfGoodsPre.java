package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.*;
import com.yunma.jhuo.i.AddSelfGoodsImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-03-22
 *
 * @author Json.
 */

public class AddSelfGoodsPre {
    private SelfGoodsInterFace.AddSelfGoodsModel mModel;
    private SelfGoodsInterFace.AddSelfGoodsView mView;

    public AddSelfGoodsPre(SelfGoodsInterFace.AddSelfGoodsView mView) {
        this.mView = mView;
        this.mModel = new AddSelfGoodsImpl();
    }

    public void publishGoods(Context mContext, PublishGoodsBean yunmasBean){
        mModel.addSelfGoods(mContext, yunmasBean, new SelfGoodsInterFace.AddSelfGoodsListener() {
            @Override
            public void addSelfGoodsListener(YunmasBeanResult resultBean, String msg) {
                mView.showAddSelfGoods(resultBean,msg);
            }
        });
    }
}
