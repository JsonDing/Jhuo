package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.AddToCartsRequestBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.AddToCartsImpl;
import com.yunma.jhuo.m.AddToCartsInterface;

/**
 * Created on 2017-04-24
 *
 * @author Json.
 */

public class AddToCartsPre {
    private AddToCartsInterface.AddToCartsModel mModel;
    private AddToCartsInterface.AddToCartsView mView;

    public AddToCartsPre(AddToCartsInterface.AddToCartsView mView) {
        this.mView = mView;
        this.mModel = new AddToCartsImpl();
    }

    public void addToBasket(Context mContext,AddToCartsRequestBean addToCartsRequestBean){
        mModel.addToCarts(mContext, addToCartsRequestBean, new AddToCartsInterface.OnAddToCartsListener() {
            @Override
            public void onAddListener(SuccessResultBean resultBean, String msg) {
                mView.showAddCartsInfos(resultBean,msg);
            }
        });
    }
}
