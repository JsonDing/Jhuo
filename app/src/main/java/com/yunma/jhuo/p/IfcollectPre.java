package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.AddGoodsCollectBean;
import com.yunma.jhuo.i.IfcollectImpl;
import com.yunma.jhuo.m.GoodsCollectInterFace;

/**
 * Created on 2017-03-11
 *
 * @author Json.
 */

public class IfcollectPre {
    private GoodsCollectInterFace.IfcollectModel mModel;
    private GoodsCollectInterFace.IfcollectView mView;

    public IfcollectPre(GoodsCollectInterFace.IfcollectView mView) {
        this.mView = mView;
        this.mModel = new IfcollectImpl();
    }

    public void ifCollect(Context mContext, String goodsId){
        mModel.ifCollect(mContext,goodsId,
                new GoodsCollectInterFace.IfcollectListener() {
            @Override
            public void onListener(AddGoodsCollectBean resultBean, String msg) {
             mView.showCollectInfos(resultBean,msg);
            }
        });
    }
}
