package com.yunma.jhuo.p;

import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.i.GetGoodsRecommendImpl;
import com.yunma.jhuo.m.GetGoodsInterface.*;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class GetGoodsRecommendPre {
    private GetGoodsRecommendModel mModel;
    private GetGoodsRecommendView mView;

    public GetGoodsRecommendPre(GetGoodsRecommendView mView) {
        this.mView = mView;
        this.mModel = new GetGoodsRecommendImpl();
    }

    public void getRecommendGoods(String size,int nextPage){
        mModel.getRecommenGoods(mView.getContext(),size,nextPage, new OnGetGoodsRecommendListener() {
            @Override
            public void onListener(GoodsInfoResultBean resultBean, String msg) {
                mView.showRecommendGoodsInfos(resultBean,msg);
            }
        });
    }
}
