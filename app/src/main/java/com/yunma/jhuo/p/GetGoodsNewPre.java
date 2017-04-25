package com.yunma.jhuo.p;

import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.i.GetNewGoodsImpl;
import com.yunma.jhuo.m.GetGoodsInterface.*;

/**
 * Created on 2017-03-02
 *
 * @author Json.
 */

public class GetGoodsNewPre {
    private GetGoodsNewModel mModel;
    private GetGoodsNewView mView;

    public GetGoodsNewPre(GetGoodsNewView mView) {
        this.mView = mView;
        this.mModel = new GetNewGoodsImpl();
    }

    public void getNewGoods(String size,int nextPage){
        mModel.getNewGoods(mView.getContext(), size, nextPage, new OnGetGoodsNewListener() {
            @Override
            public void onListener(GoodsInfoResultBean resultBean, String msg) {
                mView.showNewGoodsInfos(resultBean,msg);
            }
        });
    }
}
