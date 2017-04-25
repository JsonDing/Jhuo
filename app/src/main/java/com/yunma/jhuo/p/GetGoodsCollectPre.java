package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.GetCollectResultBean;
import com.yunma.jhuo.i.GetGoodsCollectImpl;
import com.yunma.jhuo.m.GoodsCollectInterFace.*;

/**
 * Created on 2017-03-06
 *
 * @author Json.
 */

public class GetGoodsCollectPre {
    private GetGoodsCollectModel mModel;
    private GetGoodsCollectView mView;

    public GetGoodsCollectPre(GetGoodsCollectView mView) {
        this.mView = mView;
        this.mModel = new GetGoodsCollectImpl();
    }

    public void getGoodsCollect(Context mContext){
        mModel.GetCollect(mContext, new OnGetCollect() {
            @Override
            public void onGetCollectListener(GetCollectResultBean resultBean, String msg) {
                mView.onGetCollectShow(resultBean,msg);
            }
        });
    }
}
