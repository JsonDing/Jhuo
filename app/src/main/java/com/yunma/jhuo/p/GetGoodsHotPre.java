package com.yunma.jhuo.p;

import com.yunma.bean.GoodsInfoResultBean;
import com.yunma.jhuo.i.GetGoodsHotImpl;
import com.yunma.jhuo.m.GetGoodsInterface.*;

/**
 * Created on 2017-03-02
 *
 * @author Json.
 */

public class GetGoodsHotPre {
    private GetGoodsHotView mView;
    private GetGoodsHotModel mModel;

    public GetGoodsHotPre(GetGoodsHotView mView) {
        this.mView = mView;
        this.mModel = new GetGoodsHotImpl();
    }
    public void getHotGoods(String size,int nextPage){
        mModel.getHotGoods(mView.getContext(),size,nextPage, new OnGetGoodsHotListener() {
            @Override
            public void onListener(GoodsInfoResultBean resultBean, String msg) {
                mView.showHotGoodsInfos(resultBean,msg);
            }
        });
    }
}
