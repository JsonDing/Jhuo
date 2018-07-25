package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.DelGoodsCollectImpl;
import com.yunma.jhuo.m.GoodsCollectInterFace.*;

/**
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class DelGoodsCollectPre {
    private DelGoodsCollectModel mModel;
    private DelGoodsCollectView mView;

    public DelGoodsCollectPre(DelGoodsCollectView mView) {
        this.mView = mView;
        this.mModel = new DelGoodsCollectImpl();
    }

    public void onDelCollect(Context context,String goodId){
        mModel.delCollect(context, goodId, new OnDelCollect() {
            @Override
            public void onDelCollectListener(SuccessResultBean resultBean, String msg) {
                mView.onDelCollectShow(resultBean,msg);
            }
        });
    }
}
