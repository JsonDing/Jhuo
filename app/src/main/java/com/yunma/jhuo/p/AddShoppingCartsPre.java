package com.yunma.jhuo.p;

import android.content.Context;

import com.yunma.bean.ShoppingCartsBean;
import com.yunma.bean.SuccessResultBean;
import com.yunma.jhuo.i.AddShoppingCartsImpl;
import com.yunma.jhuo.m.AddShoppingCartsInterface.AddShoppingCartsModel;
import com.yunma.jhuo.m.AddShoppingCartsInterface.AddShoppingCartsView;
import com.yunma.jhuo.m.AddShoppingCartsInterface.OnAddShoppingCartsListener;

/**
 * Created on 2017-02-20
 *
 * @author Json.
 */

public class AddShoppingCartsPre {
    private AddShoppingCartsModel mModel;
    private AddShoppingCartsView mView;

    public AddShoppingCartsPre(AddShoppingCartsView mView) {
        this.mView = mView;
        this.mModel = new AddShoppingCartsImpl();
    }
    public void addToBasket(Context context,ShoppingCartsBean shoppingCartsBean){
        mModel.addToBasket(context, shoppingCartsBean, new OnAddShoppingCartsListener() {
            @Override
            public void onListener(SuccessResultBean resultBean, String msg) {
                mView.showAddShoppingCartsInfos(resultBean,msg);
            }
        });
    }
}
