package com.yunma.jhuo.p;

import com.yunma.bean.AddGoodsCollectBean;
import com.yunma.jhuo.i.AddGoodsCollectImpl;
import com.yunma.jhuo.m.GoodsCollectInterFace.*;

/**
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class AddGoodsCollectPre  {
    private AddGoodsCollectModel mModel;
    private AddGoodsCollectView mView;

    public AddGoodsCollectPre(AddGoodsCollectView mView) {
        this.mView = mView;
        this.mModel = new AddGoodsCollectImpl();
    }

    public void onAddCollect(){
        mModel.addToCollect(mView.getContext(), mView.getGoodId(), new OnAddCollect() {
            @Override
            public void onAddCollectListener(AddGoodsCollectBean resultBean, String msg) {
                mView.onAddCollectShow(resultBean,msg);
            }
        });
    }
}
