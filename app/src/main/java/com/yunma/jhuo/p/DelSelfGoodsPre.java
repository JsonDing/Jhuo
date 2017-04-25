package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.DelSelfGoodsModelImpl;
import com.yunma.jhuo.m.SelfGoodsInterFace;

/**
 * Created on 2017-04-06
 *
 * @author Json.
 */

public class DelSelfGoodsPre {
    private SelfGoodsInterFace.DelSelfGoodsModel delModel;
    private SelfGoodsInterFace.DelSelfGoodsView delView;

    public DelSelfGoodsPre(SelfGoodsInterFace.DelSelfGoodsView delView) {
        this.delView = delView;
        this.delModel = new DelSelfGoodsModelImpl();
    }

    public void delPublishGoods(Context mContext, String ids){
        delModel.delSelfGoods(mContext, ids, new SelfGoodsInterFace.DelSelfGoodsListener() {
            @Override
            public void delSelfGoodsListene(SuccessResultBean resultBean, String msg) {
                delView.showDelInfos(resultBean,msg);
            }
        });
    }
}
